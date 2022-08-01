package com.geektechkb.feature_main.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.geektechkb.common.constants.Constants.HOURS_MINUTES_DATE_FORMAT
import com.geektechkb.core.base.BaseRecyclerViewHolder
import com.geektechkb.core.extensions.formatCurrentUserTime
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.ItemReceivedMessageBinding
import com.geektechkb.feature_main.databinding.ItemReceivedVoiceMessageBinding
import com.geektechkb.feature_main.databinding.ItemSentMessagesBinding
import com.geektechkb.feature_main.databinding.ItemSentVoiceMessageBinding
import com.geektechkb.feature_main.domain.models.Message

class MessagesAdapter :
    ListAdapter<Message, BaseRecyclerViewHolder<ViewBinding, Message>>(diffUtil) {

    private var phoneNumber: String? = null

    fun setPhoneNumber(phoneNumber: String) {
        this.phoneNumber = phoneNumber
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BaseRecyclerViewHolder<ViewBinding, Message> {
        return when (viewType) {
            R.layout.item_sent_messages -> {
                MessageSentViewHolder(
                    ItemSentMessagesBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            R.layout.item_received_message -> {
                MessageReceivedViewHolder(
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
        holder: BaseRecyclerViewHolder<ViewBinding, Message>,
        position: Int,
    ) {
        when (getItemViewType(position)) {
            R.layout.item_sent_messages -> getItem(position)?.let {
                (holder as MessageSentViewHolder).onBind(it)
            }
            R.layout.item_received_message -> getItem(position)?.let {
                (holder as MessageReceivedViewHolder).onBind(it)
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
            getItem(position)?.senderPhoneNumber?.equals(phoneNumber) == false -> {
                R.layout.item_received_message
            }
            else -> {
                R.layout.item_sent_messages
            }
        }
    }


    inner class MessageSentViewHolder(binding: ItemSentMessagesBinding) :
        BaseRecyclerViewHolder<ItemSentMessagesBinding, Message>(binding) {
        override fun onBind(item: Message) {
            binding.mcvMessage.setBackgroundResource(R.drawable.first_message_sent_cornered_background)
            binding.tvTimeMessageWasSent.text = formatCurrentUserTime(HOURS_MINUTES_DATE_FORMAT)
//            when (itemViewType) {
//                FIRST_IN_POSITION ->
//                    binding.clMessage.background =
//                        itemView.context.getDrawable(R.drawable.first_message_sent_cornered_background)
//                MIDDLE_IN_POSITION -> binding.clMessage.background =
//                    itemView.context.getDrawable(R.drawable.middle_message_sent_cornered_background)
//                LAST_IN_POSITION -> binding.clMessage.background =
//                    itemView.context.getDrawable(R.drawable.last_message_sent_cornered_background)
//            }
            binding.tvMessage.text = item.message
        }
    }

    inner class VoiceMessageSentViewHolder(binding: ItemSentVoiceMessageBinding) :
        BaseRecyclerViewHolder<ItemSentVoiceMessageBinding, Message>(binding) {
        override fun onBind(item: Message) {

        }
    }


    inner class MessageReceivedViewHolder(binding: ItemReceivedMessageBinding) :
        BaseRecyclerViewHolder<ItemReceivedMessageBinding, Message>(binding) {
        override fun onBind(item: Message) {
            binding.tvMessage.text = item.message
            binding.tvTimeMessageWasSent.text = formatCurrentUserTime(HOURS_MINUTES_DATE_FORMAT)
        }
    }

    inner class VoiceMessageReceivedViewHolder(binding: ItemReceivedVoiceMessageBinding) :
        BaseRecyclerViewHolder<ItemReceivedVoiceMessageBinding, Message>(binding) {
        override fun onBind(item: Message) {
        }
    }


    companion object {

        val diffUtil = object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem.timeMessageWasSent == newItem.timeMessageWasSent
            }

        }

        private const val FIRST_IN_POSITION = -1
        private const val MIDDLE_IN_POSITION = 0
        private const val LAST_IN_POSITION = 1
    }
}