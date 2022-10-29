package com.geektechkb.feature_main.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.geektechkb.core.base.BaseDiffUtil
import com.geektechkb.core.extensions.loadImageAndSetInitialsIfFailed
import com.geektechkb.feature_main.databinding.ItemUserGroupBinding
import com.geektechkb.feature_main.domain.models.User

class UsersGroupAdapter(val onItemClick: (user: User, checked: Boolean) -> Unit) :
	PagingDataAdapter<User, UsersGroupAdapter.UsersGroupViewHolder>(BaseDiffUtil()) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		UsersGroupViewHolder(
			ItemUserGroupBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		)

	override fun onBindViewHolder(holder: UsersGroupViewHolder, position: Int) {
		getItem(position)?.let { holder.onBind(it) }
	}

	inner class UsersGroupViewHolder(private var binding: ItemUserGroupBinding) :
		RecyclerView.ViewHolder(binding.root) {
		fun onBind(user: User) = with(user) {
			binding.apply {
				tvUsername.text = name
				avProfile.loadImageAndSetInitialsIfFailed(profileImage, name, cpiUserAvatar)
			}
		}

		init {
			binding.root.setOnClickListener {
				getItem(absoluteAdapterPosition)?.let { user ->
					onItemClick(user, !binding.checkBox.isVisible)
				}
				binding.checkBox.isVisible = !binding.checkBox.isVisible
			}
		}
	}


}