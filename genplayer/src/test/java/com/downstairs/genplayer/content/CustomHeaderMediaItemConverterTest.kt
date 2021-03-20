package com.downstairs.genplayer.content

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class CustomHeaderMediaItemConverterTest {
    private val converter = CustomHeaderMediaItemConverter()

    @Test
    fun bla() {
        val content = Content(
            "title",
            "description",
            "source",
            "artwork",
            "type",
            mapOf("key1" to "valu1", "key2" to "value2"),
            0
        )

        val mediaQueueItem = converter.toMediaQueueItem(content.toMediaItem())
        val mediaItem = converter.toMediaItem(mediaQueueItem)

        println(mediaQueueItem)
        println(mediaItem)
    }
}