package com.monke.machnomusic3.ui.userFeature.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.monke.machnomusic3.data.extensions.formatDate
import com.monke.machnomusic3.databinding.ItemAlbumBinding
import com.monke.machnomusic3.databinding.ItemPostBinding
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.ui.musicFeature.adapters.TrackRWAdapter
import com.monke.machnomusic3.ui.recyclerViewUtils.DiffUtilCallback
import com.monke.machnomusic3.ui.uiModels.PostItem

class PostRWAdapter(
    private val onTrackClicked: (
        tracksList: List<Track>,
        index: Int) -> Unit,
): RecyclerView.Adapter<PostRWAdapter.PostViewHolder>() {

    var postsList: List<PostItem> = ArrayList()
    set(value) {
        val diffCallback = DiffUtilCallback(
            oldList = field,
            newList = value,
            areContentsSame = { oldItem, newItem -> oldItem.post.id == newItem.post.id },
            areItemsSame = { oldItem, newItem -> oldItem == newItem },
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        field = value
        diffResult.dispatchUpdatesTo(this)
    }


    class PostViewHolder(
        private val binding: ItemPostBinding,
        private val onTrackClicked: (tracksList: List<Track>, index: Int) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(postItem: PostItem, index: Int) {
            binding.txtLogin.text = postItem.post.author.login
            binding.txtName.text = postItem.post.author.username
            binding.txtPost.text = postItem.post.text
            binding.txtDate.text =
                postItem.post.creationDate.formatDate(itemView.resources.configuration.locales[0])

            postItem.userPictureUrl?.let { setupAuthorPic(it) }
            setupPhotoRecyclerList(postItem)
            setupTracksRecyclerList(postItem, onTrackClicked)
        }

        private fun setupAuthorPic(picUrl: String) {
            Glide
                .with(itemView)
                .load(picUrl)
                .circleCrop()
                .into(binding.picProfile)
        }

        private fun setupPhotoRecyclerList(postItem: PostItem) {
            val imageAdapter = PostImageRWAdapter(
                onItemClicked = {
                }
            )

            binding.recyclerImages.adapter = imageAdapter
            binding.recyclerImages.layoutManager = LinearLayoutManager(
                itemView.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            imageAdapter.imagesUrlsList = postItem.imagesUrlsList

        }

        private fun setupTracksRecyclerList(
            postItem: PostItem,
            onTrackClicked: (tracksList: List<Track>, index: Int) -> Unit
        ) {
            val imageAdapter = TrackRWAdapter(
                onItemClicked = { index ->
                    onTrackClicked(postItem.tracksList.map { it.track }, index)
                }
            )

            binding.recyclerTracks.adapter = imageAdapter
            binding.recyclerTracks.layoutManager = LinearLayoutManager(
                itemView.context,
                LinearLayoutManager.VERTICAL,
                false
            )
            imageAdapter.tracksList = postItem.tracksList

        }


    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PostViewHolder(binding, onTrackClicked)
    }

    override fun getItemCount(): Int = postsList.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(postsList[position], position)
    }
}