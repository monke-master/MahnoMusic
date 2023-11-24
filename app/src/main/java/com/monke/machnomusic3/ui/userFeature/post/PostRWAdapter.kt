package com.monke.machnomusic3.ui.userFeature.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.monke.machnomusic3.databinding.ItemAlbumBinding
import com.monke.machnomusic3.databinding.ItemPostBinding
import com.monke.machnomusic3.ui.recyclerViewUtils.DiffUtilCallback
import com.monke.machnomusic3.ui.uiModels.PostItem

class PostRWAdapter: RecyclerView.Adapter<PostRWAdapter.PostViewHolder>() {

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
        private val binding: ItemPostBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(postItem: PostItem, index: Int) {
            binding.txtLogin.text = postItem.post.author.login
            binding.txtName.text = postItem.post.author.username
            binding.txtDate.text = postItem.post.text
            setupPhotoRecyclerList(postItem)
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

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PostViewHolder(binding)
    }

    override fun getItemCount(): Int = postsList.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(postsList[position], position)
    }
}