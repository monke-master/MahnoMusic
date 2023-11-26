package com.monke.machnomusic3.ui.musicFeature.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.monke.machnomusic3.R
import com.monke.machnomusic3.databinding.ItemSelectingTrackBinding
import com.monke.machnomusic3.databinding.ItemTrackBinding
import com.monke.machnomusic3.ui.recyclerViewUtils.DiffUtilCallback
import com.monke.machnomusic3.ui.uiModels.TrackItem

class SelectingTrackRWAdapter(
    private val onItemSelected: (trackId: String) -> Unit
) : RecyclerView.Adapter<SelectingTrackRWAdapter.TrackViewHolder>() {

    var tracksList: List<TrackItem> = ArrayList()
        set(value) {
            val diffCallback = DiffUtilCallback<TrackItem>(
                oldList = field,
                newList = value,
                areContentsSame = { oldItem, newItem -> oldItem.track.id == newItem.track.id },
                areItemsSame = { oldItem, newItem -> oldItem == newItem },
            )
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = value
            diffResult.dispatchUpdatesTo(this)
        }


    class TrackViewHolder(
        private val binding: ItemSelectingTrackBinding,
        private val onItemClicked: (trackId: String) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(trackItem: TrackItem, index: Int) {
            binding.txtTitle.text = trackItem.track.title
            binding.txtAuthor.text = trackItem.track.author.username
            Glide
                .with(binding.picCover)
                .load(trackItem.coverUrl)
                .placeholder(R.drawable.ic_track)
                .into(binding.picCover)
            binding.btnAdd.setOnClickListener { onItemClicked(trackItem.track.id) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = ItemSelectingTrackBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TrackViewHolder(binding, onItemSelected)
    }

    override fun getItemCount(): Int = tracksList.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracksList[position], position)
    }
}