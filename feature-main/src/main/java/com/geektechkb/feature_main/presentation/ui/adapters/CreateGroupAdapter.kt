package com.geektechkb.feature_main.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.geektechkb.core.base.BaseDiffUtil
import com.geektechkb.core.extensions.loadImageAndSetInitialsIfFailed
import com.geektechkb.feature_main.databinding.ItemCreateGroupBinding
import com.geektechkb.feature_main.domain.models.User

class CreateGroupAdapter :
	PagingDataAdapter<User, CreateGroupAdapter.CreateGroupViewHolder>(BaseDiffUtil()) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		CreateGroupViewHolder(
			ItemCreateGroupBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		)

	override fun onBindViewHolder(holder: CreateGroupViewHolder, position: Int) {
		getItem(position)?.let { holder.onBind(it) }
	}

	inner class CreateGroupViewHolder(private var binding: ItemCreateGroupBinding) :
		RecyclerView.ViewHolder(binding.root) {
		fun onBind(user: User) = with(user) {
			binding.apply {
				tvUsername.text = name
				avProfile.loadImageAndSetInitialsIfFailed(profileImage, name, cpiUserAvatar)
			}
		}

//        init {
//            binding.root.setOnClickListener {
//                getItem(absoluteAdapterPosition)?.let { user ->
//                    onItemClick(user)
//                }
//            }
//        }
	}


}