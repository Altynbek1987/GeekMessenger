package com.geektechkb.feature_main.presentation.ui.adapters

import android.util.Log
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
    private var phoneNumber: String? = null


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerViewHolder<ViewBinding, Message> {
        lateinit var viewHolder: BaseRecyclerViewHolder<ViewBinding, Message>
        when (viewType) {
            R.layout.item_sent_messages -> {
                viewHolder =
                    MessageSentViewHolder(
                        ItemSentMessagesBinding.inflate(
                            LayoutInflater.from(parent.context),
                            parent,
                            false
                        )
                    )

                Log.e("gaypop", viewHolder.binding.toString())
            }
            R.layout.item_received_message -> {
                viewHolder =

                    MessageReceivedViewHolder(
                        ItemReceivedMessageBinding.inflate(
                            LayoutInflater.from(
                                parent.context
                            ), parent, false
                        )
                    )
                Log.e("gaypop2", viewHolder.binding.toString())
            }

        }
        return viewHolder

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
            R.layout.item_received_message -> getItem(position)?.let {
                (holder as MessageReceivedViewHolder).onBind(
                    it
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            getItem(position)?.phoneNumber?.equals(phoneNumber) == false -> {

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
            binding.tvMessage.text = item.message
            binding.tvTimeMessageWasSent.text = formatCurrentUserTime(HOURS_MINUTES_DATE_FORMAT)
            when(absoluteAdapterPosition) {
                FIRST_IN_POSITION -> {

                }

            }
        }
    }

    inner class MessageReceivedViewHolder(binding: ItemReceivedMessageBinding) :
        BaseRecyclerViewHolder<ItemReceivedMessageBinding, Message>(binding) {
        override fun onBind(item: Message) {
            binding.tvMessage.text = item.message
            binding.tvTimeMessageWasSent.text = formatCurrentUserTime(HOURS_MINUTES_DATE_FORMAT)
        }
    }

    companion object {
        private const val FIRST_IN_POSITION = -1
        private const val MIDDLE_IN_POSITION = 0
        private const val LAST_IN_POSITION = 1
    }
}