package com.geektechkb.feature_main.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.geektechkb.core.extensions.loadImageAndSetInitialsIfFailed
import com.geektechkb.feature_main.databinding.ItemUserListBinding
import com.geektechkb.feature_main.domain.models.Group

class GroupListAdapter(val onItemClick: (groups: Group) -> Unit) :
	ListAdapter<Group, GroupListAdapter.GroupUsersViewHolder>(diffUtil) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = GroupUsersViewHolder(
		ItemUserListBinding.inflate(
			LayoutInflater.from(parent.context), parent, false
		)
	)

	override fun onBindViewHolder(holder: GroupUsersViewHolder, position: Int) {
		getItem(position)?.let { holder.onBind(it) }
	}

	inner class GroupUsersViewHolder(private var binding: ItemUserListBinding) :
		RecyclerView.ViewHolder(binding.root) {
		fun onBind(item: Group) = with(item) {
			binding.apply {
				tvtUsername.text = groupName.toString()
				profileImage.loadImageAndSetInitialsIfFailed(
					groupPhoto,
					groupName,
					progressBar = cpiUserAvatar
				)
			}
//			item.groupMembers?.forEach {
//				if (it.phoneNumber == item.userNumber.toString()){
//					binding.root.isVisible = true
//				}else{
//					binding.root.isVisible = false
//				}
//			}

		}
//		if (preferences.currentUserPhoneNumber == preferences.currentUserPhoneNumber) {
//			binding.recyclerviewGroupList.isVisible = false
//		}else
//		{
//			binding.recyclerviewGroupList.isVisible = true
//		}

		init {
			binding.root.setOnClickListener {
				getItem(absoluteAdapterPosition)?.let { groupName ->
					onItemClick(groupName)
				}
			}
		}
	}


	companion object {
		val diffUtil = object : DiffUtil.ItemCallback<Group>() {
			override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
				return oldItem == newItem
			}

			override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
				return oldItem.groupId == newItem.groupId
			}
		}

	}
}