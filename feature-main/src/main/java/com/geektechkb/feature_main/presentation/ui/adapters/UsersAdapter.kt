package com.geektechkb.feature_main.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.android.highlighting.toSpannedString
import com.geektechkb.core.base.BaseDiffUtil
import com.geektechkb.core.extensions.loadImageAndSetInitialsIfFailed
import com.geektechkb.feature_main.databinding.ItemUserBinding
import com.geektechkb.feature_main.domain.models.User

class UsersAdapter(private val onItemClick: (phoneNumber: String?) -> Unit) :
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
                tvUsername.text = highlightedName?.toSpannedString() ?: name

                lastName?.let { nonNullLastName ->
                    avProfile.loadImageAndSetInitialsIfFailed(
                        profileImage,
                        name,
                        nonNullLastName,
                        cpiUserAvatar
                    )
                } ?: avProfile.loadImageAndSetInitialsIfFailed(
                    profileImage,
                    name,
                    cpiUserAvatar
                )
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick(getItem(absoluteAdapterPosition)?.phoneNumber)
            }
        }
    }
}