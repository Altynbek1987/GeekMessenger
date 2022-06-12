package com.geektechkb.feature_main.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.geektechkb.core.base.BaseDiffUtil
import com.geektechkb.feature_main.databinding.ItemReceivedMessageBinding
import com.geektechkb.feature_main.databinding.ItemSentMessageBinding
import com.geektechkb.feature_main.domain.models.Message

class MessagesAdapter :
    PagingDataAdapter<Message, RecyclerView.ViewHolder>(BaseDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            MESSAGE_SENT -> {
                MessageSentViewHolder(
                    ItemSentMessageBinding.inflate(
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


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == MESSAGE_SENT)
            getItem(position)?.let { (holder as MessageSentViewHolder).onBind(it) }
        else
            getItem(position)?.let { (holder as MessageReceivedViewHolder).onBind(it) }


    }

    inner class MessageSentViewHolder(private val binding: ItemSentMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(message: Message) {
            binding.tvMessage.text = message.message
            binding.tvTimeMessageWasSent.text = message.timeMessageWasSent.toString()
        }
    }

    inner class MessageReceivedViewHolder(private val binding: ItemReceivedMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(message: Message) {
            binding.tvMessage.text = message.message
            binding.tvTimeMessageWasSent.text = message.timeMessageWasSent.toString()
        }

    }

    companion object {
        private const val MESSAGE_SENT = 0
    }
}