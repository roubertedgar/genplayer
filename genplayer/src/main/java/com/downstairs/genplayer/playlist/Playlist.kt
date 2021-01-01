package com.downstairs.genplayer.playlist

import com.downstairs.genplayer.content.Content

class Playlist {

    private var currentIndex = 0
    private val contentList = mutableListOf<Content>()
    private var iterator = contentList.listIterator()

    private val listeners = mutableListOf<ContentChangeListener>()

    fun addContentChangeListener(contentChangeListener: ContentChangeListener) {
        listeners.add(contentChangeListener)
        current()
    }

    fun set(content: Content) {
        contentList.clear()
        add(content)
        current()
    }

    private fun add(content: Content) {
        contentList.add(content)
        iterator = contentList.listIterator(currentIndex)
    }

    fun addInSequence(content: Content) {
        contentList.add(currentIndex + 1, content)
        iterator = contentList.listIterator(currentIndex)

        current()
    }

    fun next() {
        nextContent()
    }

    fun previous() {
        previousContent()
    }

    fun current() {
        currentContent()
    }

    private fun nextContent() {
        if (iterator.hasNext()) {
            currentIndex = iterator.nextIndex()
            notifyContentChanged(iterator.next())
        }
    }

    private fun previousContent() {
        if (iterator.hasPrevious()) {
            currentIndex = iterator.previousIndex()
            notifyContentChanged(iterator.previous())
        }
    }

    private fun currentContent() {
        if (contentList.isNotEmpty())
            notifyContentChanged(contentList[currentIndex])
    }

    private fun notifyContentChanged(changedContent: Content?) {
        changedContent?.also { content ->
            listeners.forEach {
                it.onContentChanged(content)
            }
        }
    }
}