package com.geektechkb.feature_main.presentation.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.geektechkb.common.constants.Constants
import com.geektechkb.core.base.BaseRecyclerViewHolder
import com.geektechkb.core.extensions.formatCurrentUserTime
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.ItemReceivedMessageBinding
import com.geektechkb.feature_main.databinding.ItemReceivedVoiceMessageBinding
import com.geektechkb.feature_main.databinding.ItemSentMessagesBinding
import com.geektechkb.feature_main.databinding.ItemSentVoiceMessageBinding
import com.geektechkb.feature_main.domain.models.GroupMessage

class GroupMessagesAdapter :
	ListAdapter<GroupMessage, BaseRecyclerViewHolder<ViewBinding, GroupMessage>>(diffUtil) {

	private var senderPhoneNumber: String? = null
	private var receiverPhoneNumber: String? = null

	fun setPhoneNumber(senderPhoneNumber: String, receiverPhoneNumber: String) {
		this.senderPhoneNumber = senderPhoneNumber
		this.receiverPhoneNumber = receiverPhoneNumber
	}

	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int,
	): BaseRecyclerViewHolder<ViewBinding, GroupMessage> {
		return when (viewType) {
			R.layout.item_sent_messages -> {
				GroupMessageSentViewHolder(
					ItemSentMessagesBinding.inflate(
						LayoutInflater.from(parent.context), parent, false
					)
				)
			}
			R.layout.item_received_message -> {
				GroupMessageReceivedViewHolder(
					ItemReceivedMessageBinding.inflate(
						LayoutInflater.from(
							parent.context
						), parent, false
					)
				)
			}
			/*R.layout.item_received_voice_message -> {
				VoiceMessageReceivedViewHolder(
					ItemReceivedVoiceMessageBinding.inflate(
						LayoutInflater.from(parent.context), parent, false
					)
				)
			}
			R.layout.item_sent_voice_message -> {
				VoiceMessageSentViewHolder(
					ItemSentVoiceMessageBinding.inflate(
						LayoutInflater.from(parent.context), parent, false
					)
				)
			}*/

			else -> {
				throw IllegalArgumentException("Not found ViewHolder!")
			}
		}
	}


	override fun onBindViewHolder(
		holder: BaseRecyclerViewHolder<ViewBinding, GroupMessage>,
		position: Int,
	) {
		when (getItemViewType(position)) {
			R.layout.item_sent_messages -> getItem(position)?.let {
				(holder as GroupMessageSentViewHolder).onBind(it)
			}
			R.layout.item_received_message -> getItem(position)?.let {
				(holder as GroupMessageReceivedViewHolder).onBind(it)
			}
			/*R.layout.item_sent_voice_message -> getItem(position)?.let {
				(holder as VoiceMessageSentViewHolder).onBind(it)
			}
			R.layout.item_received_voice_message -> getItem(position)?.let {
				(holder as VoiceMessageReceivedViewHolder).onBind(it)
			}*/

		}
	}

	override fun getItemViewType(position: Int): Int {
		return when {
			getItem(position)?.senderPhoneNumber?.equals(senderPhoneNumber) == true -> {
				R.layout.item_sent_messages
			}
			else -> {
				R.layout.item_received_message
			}
		}
	}


	inner class GroupMessageSentViewHolder(binding: ItemSentMessagesBinding) :
		BaseRecyclerViewHolder<ItemSentMessagesBinding, GroupMessage>(binding) {
		override fun onBind(item: GroupMessage) {
			binding.mcvMessage.setBackgroundResource(R.drawable.first_message_sent_cornered_background)
			binding.tvTimeMessageWasSent.text =
				formatCurrentUserTime(Constants.HOURS_MINUTES_DATE_FORMAT)
			binding.tvMessage.text = item.message
			Log.e("check", "sender ${item.message}")
		}
	}


	inner class GroupVoiceMessageSentViewHolder(binding: ItemSentVoiceMessageBinding) :
		BaseRecyclerViewHolder<ItemSentVoiceMessageBinding, GroupMessage>(binding) {
		override fun onBind(item: GroupMessage) {

		}
	}

	inner class GroupMessageReceivedViewHolder(binding: ItemReceivedMessageBinding) :
		BaseRecyclerViewHolder<ItemReceivedMessageBinding, GroupMessage>(binding) {
		override fun onBind(item: GroupMessage) {
			binding.tvMessage.text = item.message
			binding.tvTimeMessageWasSent.text =
				formatCurrentUserTime(Constants.HOURS_MINUTES_DATE_FORMAT)
			binding.userGroupName.text = item.senderPhoneNumber
			Log.e("check", "receiver ${item.message}")
		}
	}

	inner class GroupVoiceMessageReceivedViewHolder(binding: ItemReceivedVoiceMessageBinding) :
		BaseRecyclerViewHolder<ItemReceivedVoiceMessageBinding, GroupMessage>(binding) {
		override fun onBind(item: GroupMessage) {
		}
	}

	companion object {

		val diffUtil = object : DiffUtil.ItemCallback<GroupMessage>() {
			override fun areItemsTheSame(oldItem: GroupMessage, newItem: GroupMessage): Boolean {
				return oldItem == newItem
				
			}
			override fun areContentsTheSame(oldItem: GroupMessage, newItem: GroupMessage): Boolean {
				return oldItem.timeMessageWasSent == newItem.timeMessageWasSent
			}
		}
	}
}