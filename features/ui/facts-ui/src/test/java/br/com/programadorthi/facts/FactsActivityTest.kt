package br.com.programadorthi.facts

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Build
import android.widget.TextView
import android.widget.Toast
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import br.com.programadorthi.facts.adapter.FactsViewHolder
import br.com.programadorthi.facts.facts.FactsActivity
import br.com.programadorthi.facts.facts.FactsViewModel
import br.com.programadorthi.facts.fake.FactsUseCaseFake
import br.com.programadorthi.facts.search.SearchFactsActivity
import br.com.programadorthi.network.exception.NetworkingError
import kotlinx.android.synthetic.main.activity_facts.factsRecyclerView
import kotlinx.android.synthetic.main.item_fact.view.itemFactCategoryTextView
import kotlinx.android.synthetic.main.item_fact.view.itemFactContentTextView
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast
import com.google.android.material.R as materialR

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class FactsActivityTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var factsUseCase: FactsUseCaseFake

    private lateinit var factsViewModel: FactsViewModel

    @Before
    fun `before each test`() {
        factsUseCase = FactsUseCaseFake()

        factsViewModel = FactsViewModel(factsUseCase)

        startKoin {
            modules(
                module {
                    scope(named<FactsActivity>()) {
                        viewModel { factsViewModel }
                    }
                }
            )
        }
    }

    @After
    fun `after each test`() {
        stopKoin()
    }

    @Test
    fun `should have no facts when activity start`() {
        launch(FactsActivity::class.java).onActivity { activity ->
            val expected = 0
            assertThat(activity.factsRecyclerView.adapter?.itemCount).isEqualTo(expected)
        }
    }

    @Test
    fun `should navigate to search activity when click in the search menu`() {
        launch(FactsActivity::class.java).onActivity { activity ->
            val expected = Intent(activity, SearchFactsActivity::class.java)

            shadowOf(activity).clickMenuItem(R.id.menuSearch)

            val applicationContext = ApplicationProvider.getApplicationContext<Application>()

            val actual = shadowOf(applicationContext).nextStartedActivity

            assertThat(actual.component).isEqualTo(expected.component)
        }
    }

    @Test
    fun `should display the facts when search has results`() {
        launch(FactsActivity::class.java).onActivity { activity ->
            val facts = listOf(
                Fact(
                    categories = emptyList(),
                    url = "url",
                    value = "The Chuck Norris integration existed even before Slack existed",
                    id = "1"
                ),
                Fact(
                    categories = listOf("category"),
                    url = "url",
                    value = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque tristique turpis id convallis ornare. Proin eget enim vel libero bibendum tincidunt nec et massa. Vivamus sagittis tristique nisi, vitae molestie velit vestibulum sed. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Donec nulla justo, sollicitudin eget massa vitae, tempus rutrum tortor. Donec nec purus elit. Aenean felis quam, iaculis id pharetra sit amet.",
                    id = "2"
                )
            )

            val recyclerView = activity.factsRecyclerView

            val expectedBigTextView = TextView(activity).apply {
                setTextAppearance(materialR.style.TextAppearance_MaterialComponents_Headline4)
            }

            val expectedSmallTextView = TextView(activity).apply {
                setTextAppearance(materialR.style.TextAppearance_MaterialComponents_Subtitle1)
            }

            val expectedUncategorizedText = activity
                .getText(R.string.item_fact_view_holder_uncategorized_label)

            factsUseCase.searchResult = facts

            shadowOf(activity).clickMenuItem(R.id.menuSearch)

            activity.receiveResult("term")

            for (index in facts.indices) {
                val viewHolder = recyclerView
                    .findViewHolderForAdapterPosition(index) as FactsViewHolder

                val valueTextView = viewHolder.containerView.itemFactContentTextView
                val categoryTextView = viewHolder.containerView.itemFactCategoryTextView

                val fact = facts[index]

                val textSize = if (fact.value.length > 80) {
                    expectedSmallTextView.textSize
                } else {
                    expectedBigTextView.textSize
                }

                val categoryText = if (fact.categories.isEmpty()) {
                    expectedUncategorizedText
                } else {
                    fact.categories.first()
                }

                assertThat(valueTextView.textSize).isEqualTo(textSize)
                assertThat(valueTextView.text).isEqualTo(fact.value)
                assertThat(categoryTextView.text).isEqualTo(categoryText)
            }

            assertThat(recyclerView.adapter?.itemCount).isEqualTo(facts.size)
        }
    }

    @Test
    fun `should share a fact when click one of them`() {
        launch(FactsActivity::class.java).onActivity { activity ->
            val facts = listOf(
                Fact(
                    categories = emptyList(),
                    url = "url",
                    value = "The Chuck Norris integration existed even before Slack existed",
                    id = "1"
                )
            )

            val expected = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, facts.first().url)
                type = "text/plain"
            }

            factsUseCase.searchResult = facts

            shadowOf(activity).clickMenuItem(R.id.menuSearch)

            activity.receiveResult("term")

            activity.factsRecyclerView
                .findViewHolderForAdapterPosition(0)
                ?.itemView
                ?.performClick()

            val applicationContext = ApplicationProvider.getApplicationContext<Application>()

            val actual = shadowOf(applicationContext).nextStartedActivity

            assertThat(actual.component).isEqualTo(expected.component)
        }
    }

    @Test
    fun `should display a toast when search has no results`() {
        launch(FactsActivity::class.java).onActivity { activity ->
            val expectedText = activity.getText(R.string.activity_facts_empty_search_result)
            val expectedLength = Toast.LENGTH_LONG

            shadowOf(activity).clickMenuItem(R.id.menuSearch)

            activity.receiveResult("term")

            assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo(expectedText)

            assertThat(getToasts())
                .isNotEmpty
                .hasSize(1)
                .first()
                .matches { toast -> toast.duration == expectedLength }
        }
    }

    @Test
    fun `should display a toast when the search term is invalid`() {
        launch(FactsActivity::class.java).onActivity { activity ->
            val expectedText = activity.getText(R.string.activity_facts_empty_search_term)
            val expectedLength = Toast.LENGTH_LONG

            shadowOf(activity).clickMenuItem(R.id.menuSearch)

            factsUseCase.searchException = FactsBusiness.EmptySearch

            activity.receiveResult("")

            assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo(expectedText)

            assertThat(getToasts())
                .isNotEmpty
                .hasSize(1)
                .first()
                .matches { toast -> toast.duration == expectedLength }
        }
    }

    @Test
    fun `should display a toast when there is no internet connection`() {
        launch(FactsActivity::class.java).onActivity { activity ->
            val expectedText = activity.getText(R.string.activity_facts_no_internet_connection)
            val expectedLength = Toast.LENGTH_LONG

            factsUseCase.searchException = NetworkingError.NoInternetConnection

            activity.receiveResult("term")

            assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo(expectedText)

            assertThat(getToasts())
                .isNotEmpty
                .hasSize(1)
                .first()
                .matches { toast -> toast.duration == expectedLength }
        }
    }

    @Test
    fun `should display a toast when there is an unknown error`() {
        launch(FactsActivity::class.java).onActivity { activity ->
            val expectedText = activity.getText(R.string.activity_facts_something_wrong)
            val expectedLength = Toast.LENGTH_LONG

            shadowOf(activity).clickMenuItem(R.id.menuSearch)

            factsUseCase.searchException = Exception("something is wrong")

            activity.receiveResult("term")

            assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo(expectedText)

            assertThat(getToasts())
                .isNotEmpty
                .hasSize(1)
                .first()
                .matches { toast -> toast.duration == expectedLength }
        }
    }

    private fun Activity.requestIntent() = Intent(this, SearchFactsActivity::class.java)

    private fun getToasts(): List<Toast> {
        val applicationContext = ApplicationProvider.getApplicationContext<Application>()

        return shadowOf(applicationContext).shownToasts
    }

    private fun Activity.receiveResult(term: String) {
        val intent = Intent().apply {
            putExtra(FactsActivity.SEARCH_RESULT_EXTRA_KEY, term)
        }

        shadowOf(this).receiveResult(requestIntent(), Activity.RESULT_OK, intent)
    }

}