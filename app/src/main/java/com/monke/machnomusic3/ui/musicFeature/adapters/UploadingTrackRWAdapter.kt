package com.monke.machnomusic3.ui.musicFeature.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.monke.machnomusic3.data.extensions.formatDuration
import com.monke.machnomusic3.databinding.ItemUploadingTrackBinding
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.model.UploadingTrack
import com.monke.machnomusic3.ui.components.DiffUtilCallback

class UploadingTrackRWAdapter(
    private val onItemClicked: (Int) -> Unit
): RecyclerView.Adapter<UploadingTrackRWAdapter.TrackViewHolder>() {

    var tracksList: List<UploadingTrack> = ArrayList()
    set(value) {
        val diffCallback = DiffUtilCallback<UploadingTrack>(
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
        private val binding: ItemUploadingTrackBinding,
        private val onItemClicked: (Int) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(track: UploadingTrack, index: Int) {
            binding.txtTitle.text = track.title
            binding.txtNumber.text = "${index + 1}"
            binding.txtDuration.text = track.duration.formatDuration()

            binding.root.setOnClickListener { onItemClicked(index) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = ItemUploadingTrackBinding.inflate(
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