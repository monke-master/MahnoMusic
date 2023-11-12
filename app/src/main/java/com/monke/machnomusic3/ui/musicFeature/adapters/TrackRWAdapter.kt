package com.monke.machnomusic3.ui.musicFeature.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.monke.machnomusic3.databinding.ItemTrackBinding
import com.monke.machnomusic3.domain.model.Track

class TrackRWAdapter(
    private val onItemClicked: (Int) -> Unit
): RecyclerView.Adapter<TrackRWAdapter.TrackViewHolder>() {

    var tracks: List<Track> = ArrayList()

    class TrackViewHolder(
        private val binding: ItemTrackBinding,
        private val onItemClicked: (Int) -> Unit
    ): ViewHolder(binding.root) {

        fun bind(track: Track, index: Int) {
            binding.txtTitle.text = track.title
            binding.txtAuthor.text = track.author.username
            binding.root.setOnClickListener { onItemClicked(index) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = ItemTrackBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TrackViewHolder(binding, onItemClicked)
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position], position)
    }
}