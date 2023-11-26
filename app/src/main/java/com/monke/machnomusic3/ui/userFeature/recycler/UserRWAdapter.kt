package com.monke.machnomusic3.ui.userFeature.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.monke.machnomusic3.databinding.ItemTrackBinding
import com.monke.machnomusic3.databinding.ItemUserBinding
import com.monke.machnomusic3.ui.recyclerViewUtils.DiffUtilCallback
import com.monke.machnomusic3.ui.uiModels.TrackItem
import com.monke.machnomusic3.ui.uiModels.UserItem

class UserRWAdapter(
    private val onItemClicked: (Int) -> Unit
): RecyclerView.Adapter<UserRWAdapter.UserViewHolder>() {

    var usersList: List<UserItem> = ArrayList()
    set(value) {
        val diffCallback = DiffUtilCallback(
            oldList = field,
            newList = value,
            areContentsSame = { oldItem, newItem -> oldItem.user.id == newItem.user.id },
            areItemsSame = { oldItem, newItem -> oldItem == newItem },
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        field = value
        diffResult.dispatchUpdatesTo(this)
    }


    class UserViewHolder(
        private val binding: ItemUserBinding,
        private val onItemClicked: (Int) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(userItem: UserItem) {
            binding.txtLogin.text = userItem.user.login
            binding.txtUsername.text = userItem.user.username
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UserViewHolder(binding, onItemClicked)
    }

    override fun getItemCount(): Int = usersList.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(usersList[position])
    }
}