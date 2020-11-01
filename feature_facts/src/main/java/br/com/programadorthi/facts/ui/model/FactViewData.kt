package br.com.programadorthi.facts.ui.model

data class FactViewData(
    val category: String,
    val url: String,
    val value: String
) {
    fun applySmallFont(): Boolean = value.length > HIGH_FONT_CHARACTERS_LIMIT

    private companion object {
        private const val HIGH_FONT_CHARACTERS_LIMIT = 80
    }
}
