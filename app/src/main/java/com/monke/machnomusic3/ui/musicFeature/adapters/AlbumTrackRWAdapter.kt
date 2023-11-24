package com.monke.machnomusic3.ui.musicFeature.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.monke.machnomusic3.R
import com.monke.machnomusic3.databinding.ItemAlbumTrackBinding
import com.monke.machnomusic3.databinding.ItemTrackBinding
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.ui.recyclerViewUtils.DiffUtilCallback
import com.monke.machnomusic3.ui.uiModels.TrackItem

class AlbumTrackRWAdapter(
    private val onItemClicked: (Int) -> Unit
): RecyclerView.Adapter<AlbumTrackRWAdapter.TrackViewHolder>() {

    var tracksList: List<Track> = ArrayList()
        set(value) {
            val diffCallback = DiffUtilCallback(
                oldList = field,
                newList = value,
                areContentsSame = { oldItem, newItem -> oldItem.id == newItem.id },
                areItemsSame = { oldItem, newItem -> oldItem == newItem },
            )
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = value
            diffResult.dispatchUpdatesTo(this)
        }


    class TrackViewHolder(
        private val binding: ItemAlbumTrackBinding,
        private val onItemClicked: (Int) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(trackItem: Track, index: Int) {
            binding.txtTitle.text = trackItem.title
            binding.txtAuthor.text = trackItem.author.username
            binding.txtNumber.text = "${index + 1}"
            binding.root.setOnClickListener { onItemClicked(index) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = ItemAlbumTrackBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TrackViewHolder(binding, onItemClicked)
    }

    override fun getItemCount(): Int = tracksList.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracksList[position], position)
    }
}