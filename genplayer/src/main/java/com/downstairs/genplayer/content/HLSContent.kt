package com.downstairs.genplayer.content

object HLSContentFactory {

    fun createContent(
        title: String,
        source: String,
        cookies: Map<String, String> = mapOf(),
        positionMs: Long = 0
    ): Content {
        return Content(
            title,
            "Descrição da arte caraio",
            source,
            "https://thumbs.dreamstime.com/z/vintage-decorative-element-engraving-baroque-ornament-pattern-skull-hand-drawn-vector-illustration-84014332.jpg",
            "application/x-mpegURL",
            cookies,
            positionMs
        )
    }
}
