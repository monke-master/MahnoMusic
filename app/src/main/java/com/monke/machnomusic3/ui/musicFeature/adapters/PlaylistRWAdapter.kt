package com.monke.machnomusic3.ui.musicFeature.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.monke.machnomusic3.R
import com.monke.machnomusic3.databinding.ItemPlaylistBinding
import com.monke.machnomusic3.ui.recyclerViewUtils.DiffUtilCallback
import com.monke.machnomusic3.ui.uiModels.PlaylistItem

class PlaylistRWAdapter(
    private val onItemClicked: (String) -> Unit
): RecyclerView.Adapter<PlaylistRWAdapter.PlaylistViewHolder>() {

    var playlistItems: List<PlaylistItem> = ArrayList()
        set(value) {
            val diffCallback = DiffUtilCallback(
                oldList = field,
                newList = value,
                areContentsSame = { oldItem, newItem -> oldItem.playlist.id == newItem.playlist.id },
                areItemsSame = { oldItem, newItem -> oldItem == newItem },
            )
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = value
            diffResult.dispatchUpdatesTo(this)
        }


    class PlaylistViewHolder(
        private val binding: ItemPlaylistBinding,
        private val onItemClicked: (String) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(playlistItem: PlaylistItem) {
            binding.txtTitle.text = playlistItem.playlist.title

            Glide
                .with(itemView)
                .load(playlistItem.coverUrl)
                .placeholder(R.drawable.ic_track)
                .into(binding.imgCover)
            binding.root.setOnClickListener { onItemClicked(playlistItem.playlist.id) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = ItemPlaylistBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlaylistViewHolder(binding, onItemClicked)
    }

    override fun getItemCount(): Int = playlistItems.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlistItems[position])
    }
}