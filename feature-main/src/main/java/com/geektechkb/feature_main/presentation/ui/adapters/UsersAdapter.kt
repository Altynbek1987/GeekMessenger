package com.geektechkb.feature_main.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.geektechkb.core.base.BaseDiffUtil
import com.geektechkb.core.extensions.loadImageWithGlide
import com.geektechkb.feature_main.databinding.ItemUserBinding
import com.geektechkb.feature_main.domain.models.User

class UsersAdapter(private val onItemClick: ((phoneNumber: String?) -> Unit)? = null) :
    PagingDataAdapter<User, UsersAdapter.UsersViewHolder>(BaseDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        return UsersViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        getItem(position)?.let { holder.onBind(it) }
    }

    inner class UsersViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(user: User) {
            binding.tvUsername.text = user.name
            binding.imProfile.loadImageWithGlide(user.profileImage)
            binding.root.setOnClickListener {
                onItemClick?.let { it1 -> it1(user.id) }
            }

        }
    }
}