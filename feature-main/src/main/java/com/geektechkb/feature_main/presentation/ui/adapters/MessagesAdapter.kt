package com.geektechkb.feature_main.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.viewbinding.ViewBinding
import com.geektechkb.common.constants.Constants.HOURS_MINUTES_DATE_FORMAT
import com.geektechkb.core.base.BaseDiffUtil
import com.geektechkb.core.base.BaseRecyclerViewHolder
import com.geektechkb.core.extensions.formatCurrentUserTime
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.ItemReceivedMessageBinding
import com.geektechkb.feature_main.databinding.ItemSentMessagesBinding
import com.geektechkb.feature_main.domain.models.Message

class MessagesAdapter :
    PagingDataAdapter<Message, BaseRecyclerViewHolder<ViewBinding, Message>>(BaseDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
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
            else -> {
                MessageReceivedViewHolder(
                    ItemReceivedMessageBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
        }

    }


    override fun onBindViewHolder(
        holder: BaseRecyclerViewHolder<ViewBinding, Message>,
        position: Int
    ) {
        when (getItemViewType(position)) {
            R.layout.item_sent_messages -> getItem(position)?.let {
                (holder as MessageSentViewHolder).onBind(
                    it
                )
            }

            else -> getItem(position)?.let { (holder as MessageReceivedViewHolder).onBind(it) }
        }


    }

    override fun getItemViewType(position: Int): Int {
        return when {
            getItem(position)?.phoneNumber.equals(getItem(position)?.phoneNumber) -> {
                R.layout.item_sent_messages
            }
            else -> {
                R.layout.item_received_message
            }
        }
    }


    inner class MessageSentViewHolder(binding: ItemSentMessagesBinding) :
        BaseRecyclerViewHolder<ItemSentMessagesBinding, Message>(binding) {
        override fun onBind(item: Message) {
            binding.tvMessage.text = item.message
            binding.tvTimeMessageWasSent.text = formatCurrentUserTime(HOURS_MINUTES_DATE_FORMAT)
        }
    }

    inner class MessageReceivedViewHolder(binding: ItemReceivedMessageBinding) :
        BaseRecyclerViewHolder<ItemReceivedMessageBinding, Message>(binding) {
        override fun onBind(item: Message) {
            binding.tvMessage.text = item.message
            binding.tvTimeMessageWasSent.text = formatCurrentUserTime(HOURS_MINUTES_DATE_FORMAT)

        }

    }


}