package br.com.programadorthi.facts.ui

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ActivityScenario.launch
import br.com.programadorthi.facts.fakes.FactsUseCaseFake
import br.com.programadorthi.facts.ui.viewmodel.SearchFactsViewModel
import com.google.android.material.chip.Chip
import io.reactivex.schedulers.Schedulers
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
import org.robolectric.annotation.Config
import kotlin.random.Random

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class SearchFactsActivityTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val random = Random.Default

    private lateinit var factsUseCase: FactsUseCaseFake

    private lateinit var searchFactsViewModel: SearchFactsViewModel

    @Before
    fun `before each test`() {
        factsUseCase = FactsUseCaseFake()

        searchFactsViewModel = SearchFactsViewModel(
            Schedulers.trampoline(),
            factsUseCase
        )

        startKoin {
            modules(
                module {
                    scope(named<SearchFactsActivity>()) {
                        viewModel { searchFactsViewModel }
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
    fun `should search term EditText is empty when activity start`() {
        launch(SearchFactsActivity::class.java).onActivity { activity ->
            val expected = ""

            assertThat(activity.searchFactsEditText.text?.toString()).isEqualTo(expected)
        }
    }

    @Test
    fun `should perform search when click keyboard enter`() {
        val expected = ""

        val scenario = launch(SearchFactsActivity::class.java)

        scenario.onActivity { activity ->
            activity.searchFactsEditText.onEditorAction(EditorInfo.IME_ACTION_SEARCH)
        }

        val resultCode = scenario.result.resultCode
        val resultData = scenario.result
            .resultData.getStringExtra(FactsActivity.SEARCH_RESULT_EXTRA_KEY)

        assertThat(resultCode).isEqualTo(Activity.RESULT_OK)
        assertThat(resultData).isEqualTo(expected)
    }

    @Test
    fun `should have no categories when activity start`() {
        launch(SearchFactsActivity::class.java).onActivity { activity ->
            val expectedChildCount = 0
            val expectedVisibility = View.VISIBLE

            assertThat(activity.searchFactsCategoriesTitleTextView.visibility)
                .isEqualTo(expectedVisibility)
            assertThat(activity.searchFactsCategoriesChipGroup.childCount)
                .isEqualTo(expectedChildCount)
        }
    }

    @Test
    fun `should have no categories when activity start and has error`() {

        factsUseCase.categoriesException = Exception("something is wrong")

        launch(SearchFactsActivity::class.java).onActivity { activity ->
            val expectedChildCount = 0
            val expectedVisibility = View.INVISIBLE

            assertThat(activity.searchFactsCategoriesTitleTextView.visibility)
                .isEqualTo(expectedVisibility)
            assertThat(activity.searchFactsCategoriesChipGroup.childCount)
                .isEqualTo(expectedChildCount)
        }
    }

    @Test
    fun `should have categories when activity start`() {

        val categories = listOf("fashion", "animal", "movies")

        factsUseCase.categories = categories

        launch(SearchFactsActivity::class.java).onActivity { activity ->
            val expectedChildCount = categories.size
            val expectedVisibility = View.VISIBLE

            val chipGroup = activity.searchFactsCategoriesChipGroup

            assertThat(activity.searchFactsCategoriesTitleTextView.visibility)
                .isEqualTo(expectedVisibility)
            assertThat(chipGroup.childCount).isEqualTo(expectedChildCount)

            for (index in 0 until chipGroup.childCount) {
                val chip = chipGroup.getChildAt(index) as Chip
                assertThat(chip.text).isEqualTo(categories[index])
            }
        }
    }

    @Test
    fun `should navigate back to facts activity when click in one category`() {

        val categories = listOf("fashion", "animal", "movies")

        factsUseCase.categories = categories

        val categoryToClick = random.nextInt(categories.size)

        val scenario = launch(SearchFactsActivity::class.java)

        scenario.onActivity { activity ->
            val chipGroup = activity.searchFactsCategoriesChipGroup
            chipGroup.getChildAt(categoryToClick).performClick()
        }

        val resultCode = scenario.result.resultCode
        val resultData = scenario.result
            .resultData.getStringExtra(FactsActivity.SEARCH_RESULT_EXTRA_KEY)

        assertThat(resultCode).isEqualTo(Activity.RESULT_OK)
        assertThat(resultData).isEqualTo(categories[categoryToClick])
    }

    @Test
    fun `should have no last searches when activity start`() {
        launch(SearchFactsActivity::class.java).onActivity { activity ->
            val expectedChildCount = 0
            val expectedVisibility = View.INVISIBLE

            assertThat(activity.searchFactsLastSearchesTitleTextView.visibility)
                .isEqualTo(expectedVisibility)
            assertThat(activity.searchFactsLastSearchesChipGroup.childCount)
                .isEqualTo(expectedChildCount)
        }
    }

    @Test
    fun `should have last searches when activity start`() {

        val lastSearches = listOf("fashion", "animal", "movies")

        factsUseCase.lastSearches = lastSearches

        launch(SearchFactsActivity::class.java).onActivity { activity ->
            val expectedChildCount = lastSearches.size
            val expectedVisibility = View.VISIBLE

            val chipGroup = activity.searchFactsLastSearchesChipGroup

            assertThat(activity.searchFactsLastSearchesTitleTextView.visibility)
                .isEqualTo(expectedVisibility)
            assertThat(chipGroup.childCount).isEqualTo(expectedChildCount)

            for (index in 0 until chipGroup.childCount) {
                val chip = chipGroup.getChildAt(index) as Chip
                assertThat(chip.text).isEqualTo(lastSearches[index])
            }
        }
    }

    @Test
    fun `should navigate back to facts activity when click in one last search`() {

        val lastSearches = listOf("fashion", "animal", "movies")

        factsUseCase.lastSearches = lastSearches

        val lastSearchToClick = random.nextInt(lastSearches.size)

        val scenario = launch(SearchFactsActivity::class.java)

        scenario.onActivity { activity ->
            val chipGroup = activity.searchFactsLastSearchesChipGroup
            chipGroup.getChildAt(lastSearchToClick).performClick()
        }

        val resultCode = scenario.result.resultCode
        val resultData = scenario.result
            .resultData.getStringExtra(FactsActivity.SEARCH_RESULT_EXTRA_KEY)

        assertThat(resultCode).isEqualTo(Activity.RESULT_OK)
        assertThat(resultData).isEqualTo(lastSearches[lastSearchToClick])
    }
}