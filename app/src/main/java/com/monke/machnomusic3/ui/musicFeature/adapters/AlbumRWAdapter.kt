package com.monke.machnomusic3.ui.musicFeature.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.monke.machnomusic3.R
import com.monke.machnomusic3.databinding.ItemAlbumBinding
import com.monke.machnomusic3.ui.components.DiffUtilCallback
import com.monke.machnomusic3.ui.uiModels.AlbumItem

class AlbumRWAdapter(
    private val onItemClicked: (Int) -> Unit
): RecyclerView.Adapter<AlbumRWAdapter.AlbumViewHolder>() {

    var albumsList: List<AlbumItem> = ArrayList()
        set(value) {
            val diffCallback = DiffUtilCallback<AlbumItem>(
                oldList = field,
                newList = value,
                areContentsSame = { oldItem, newItem -> oldItem.album.id == newItem.album.id },
                areItemsSame = { oldItem, newItem -> oldItem == newItem },
            )
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = value
            diffResult.dispatchUpdatesTo(this)
        }


    class AlbumViewHolder(
        private val binding: ItemAlbumBinding,
        private val onItemClicked: (Int) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(albumItem: AlbumItem, index: Int) {
            binding.txtTitle.text = albumItem.album.title
            binding.txtAuthor.text = albumItem.album.author.username
            Glide
                .with(binding.picCover)
                .load(albumItem.coverUrl)
                .placeholder(R.drawable.ic_track)
                .into(binding.picCover)
            binding.root.setOnClickListener { onItemClicked(index) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = ItemAlbumBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AlbumViewHolder(binding, onItemClicked)
    }

    override fun getItemCount(): Int = albumsList.size

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(albumsList[position], position)
    }
}