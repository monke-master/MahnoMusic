package com.monke.machnomusic3.ui.userFeature.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.monke.machnomusic3.databinding.ItemPostPhotoBinding
import com.monke.machnomusic3.ui.recyclerViewUtils.DiffUtilCallback

class PostImageRWAdapter(
    private val onItemClicked: (Int) -> Unit
): RecyclerView.Adapter<PostImageRWAdapter.ImageViewHolder>() {

    var imagesUrlsList: List<String> = ArrayList()
    set(value) {
        val diffCallback = DiffUtilCallback(
            oldList = field,
            newList = value,
            areContentsSame = { oldItem, newItem -> oldItem == newItem },
            areItemsSame = { oldItem, newItem -> oldItem == newItem },
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        field = value
        diffResult.dispatchUpdatesTo(this)
    }


    class ImageViewHolder(
        private val binding: ItemPostPhotoBinding,
        private val onItemClicked: (Int) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(url: String, index: Int) {
            Glide
                .with(itemView)
                .load(url)
                .into(binding.image)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemPostPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageViewHolder(binding, onItemClicked)
    }

    override fun getItemCount(): Int = imagesUrlsList.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(imagesUrlsList[position], position)
    }
}