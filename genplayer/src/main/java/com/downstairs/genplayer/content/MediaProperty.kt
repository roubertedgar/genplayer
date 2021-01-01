package com.downstairs.genplayer.content

enum class MediaProperty(val value: String) {
    TITLE("Title"),
    DESCRIPTION("Description"),
    ARTWORK_URL("ArtworkURL"),
    COOKIE("Cookie"),
    COOKIES("cookies"),
    UNKNOWN("Unknown");

    companion object {
        fun from(value: String): MediaProperty {
            return when (value) {
                TITLE.value -> TITLE
                DESCRIPTION.value -> DESCRIPTION
                ARTWORK_URL.value -> ARTWORK_URL
                COOKIE.value -> COOKIE
                COOKIES.value -> COOKIES
                else -> UNKNOWN
            }
        }
    }
}