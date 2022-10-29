package com.geektechkb.feature_main.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.geektechkb.core.base.BaseDiffUtil
import com.geektechkb.core.extensions.loadImageAndSetInitialsIfFailed
import com.geektechkb.feature_main.databinding.ItemUserBinding
import com.geektechkb.feature_main.domain.models.User

class UsersAdapter(val onItemClick: (phoneNumber: String?) -> Unit) :
    PagingDataAdapter<User, UsersAdapter.UsersViewHolder>(BaseDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        UsersViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        getItem(position)?.let { holder.onBind(it) }
    }

    inner class UsersViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(user: User) = with(user) {
            binding.apply {
                tvUsername.text = name.toString()
                avProfile.loadImageAndSetInitialsIfFailed(profileImage, name, cpiUserAvatar)
//                Toast.makeText(itemView.context, name.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick(getItem(absoluteAdapterPosition)?.phoneNumber)
            }
        }
    }
}