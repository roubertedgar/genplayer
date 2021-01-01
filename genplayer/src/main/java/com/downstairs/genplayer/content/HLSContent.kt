package com.downstairs.genplayer.content

object HLSContentFactory {

    fun createContent(
        title: String,
        source: String,
        cookies: String = "",
        positionMs: Long = 0
    ): Content {
        return Content(
            title,
            "Descrição da arte caraio",
            source,
            "application/x-mpegURL",
            cookies,
            positionMs,
            "https://thumbs.dreamstime.com/z/vintage-decorative-element-engraving-baroque-ornament-pattern-skull-hand-drawn-vector-illustration-84014332.jpg"
        )
    }
}
