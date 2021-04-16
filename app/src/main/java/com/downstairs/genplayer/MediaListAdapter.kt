package com.downstairs.genplayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.downstairs.genplayer.content.Content
import kotlinx.android.synthetic.main.media_list_item.view.*

class MediaListAdapter : ListAdapter<Content, MediaListAdapter.MediaViewHolder>(diffItemCallback) {

    private var onItemClicked: (Content) -> Unit = {}

    fun setOnItemClickListener(onItemClicked: (Content) -> Unit) {
        this.onItemClicked = onItemClicked
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        return MediaViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.media_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        holder.bindItem(getItem(position))
    }

    companion object {
        val diffItemCallback = object : DiffUtil.ItemCallback<Content>() {
            override fun areItemsTheSame(oldItem: Content, newItem: Content): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Content, newItem: Content): Boolean {
                return oldItem.source == newItem.source
            }
        }
    }

    inner class MediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                onItemClicked(getItem(adapterPosition))
            }
        }

        fun bindItem(content: Content) {
            itemView.titleText.text = content.title
            itemView.descriptionText.text = content.description
        }
    }
}