package com.monke.machnomusic3.ui.userFeature.post

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.monke.machnomusic3.databinding.ItemPostPhotoBinding
import com.monke.machnomusic3.ui.recyclerViewUtils.DiffUtilCallback

class PostingImageRWAdapter(
    private val onItemClicked: (Int) -> Unit
): RecyclerView.Adapter<PostingImageRWAdapter.ImageViewHolder>() {

    var imagesList: List<Uri> = ArrayList()
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

        fun bind(uri: Uri, index: Int) {
            binding.image.setImageURI(uri)
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

    override fun getItemCount(): Int = imagesList.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(imagesList[position], position)
    }
}