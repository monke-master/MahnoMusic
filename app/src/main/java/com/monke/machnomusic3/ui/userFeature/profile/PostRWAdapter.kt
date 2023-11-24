package com.monke.machnomusic3.ui.userFeature.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.monke.machnomusic3.databinding.ItemAlbumBinding
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
        private val binding: ItemAlbumBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(postItem: PostItem, index: Int) {
//            Glide
//                .with(itemView)
//                .load(postItem.)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemAlbumBinding.inflate(
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