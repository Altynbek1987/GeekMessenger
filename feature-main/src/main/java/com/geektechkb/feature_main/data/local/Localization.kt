enum class Localization(
    val option1: String,
    val option2: String,
    val languageCode: String,
    val language: String
) {
    ENGLISH(
        "en",
        "US",
        "en-US",
        "en"
    ),
    RUSSIAN(
        "ru",
        "RU",
        "ru-Ru",
        "ru"
    ),
    KYRGYZ(
        "ky",
        "KY",
        "ky-KY",
        "ky"
    )
}