package com.geektechkb.feature_main.presentation.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.geektechkb.common.constants.Constants.HOURS_MINUTES_DATE_FORMAT
import com.geektechkb.core.base.BaseRecyclerViewHolder
import com.geektechkb.core.extensions.formatCurrentUserTime
import com.geektechkb.core.extensions.loadImageWithGlide
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.*
import com.geektechkb.feature_main.domain.models.Message

class MessagesAdapter :
    ListAdapter<Message, BaseRecyclerViewHolder<ViewBinding, Message>>(diffUtil) {

    private var senderPhoneNumber: String? = null
    private var receiverPhoneNumber: String? = null

    fun setPhoneNumber(senderPhoneNumber: String, receiverPhoneNumber: String) {
        this.senderPhoneNumber = senderPhoneNumber
        this.receiverPhoneNumber = receiverPhoneNumber
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BaseRecyclerViewHolder<ViewBinding, Message> {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_sent_messages ->
                MessageSentViewHolder(
                    ItemSentMessagesBinding.inflate(
                        inflater,
                        parent,
                        false
                    )
                )

            R.layout.item_sent_image -> ImageSentViewHolder(
                ItemSentImageBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )
            R.layout.item_received_message ->
                MessageReceivedViewHolder(
                    ItemReceivedMessageBinding.inflate(
                        inflater, parent, false
                    )
                )
            R.layout.item_received_image -> ImageReceivedViewHolder(
                ItemReceivedImageBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )
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
            R.layout.item_sent_image -> getItem(position)?.let {
                (holder as ImageSentViewHolder).onBind(it)
            }
            R.layout.item_received_message -> getItem(position)?.let {
                (holder as MessageReceivedViewHolder).onBind(it)
            }
            R.layout.item_received_image -> getItem(position)?.let {
                (holder as ImageReceivedViewHolder).onBind(it)
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
            getItem(position)?.senderPhoneNumber?.equals(senderPhoneNumber) == true && getItem(
                position
            )?.mediaResource == "" -> {
                R.layout.item_sent_messages
            }
            getItem(position)?.senderPhoneNumber?.equals(senderPhoneNumber) == true && !getItem(
                position
            )?.mediaResource.isNullOrEmpty() -> R.layout.item_sent_image
            getItem(position)?.senderPhoneNumber?.equals(senderPhoneNumber) == false && getItem(
                position
            )?.mediaResource == "" -> {
                R.layout.item_received_message
            }
            getItem(position)?.senderPhoneNumber?.equals(senderPhoneNumber) == false && !getItem(
                position
            )?.mediaResource.isNullOrEmpty() -> R.layout.item_received_image
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
            binding.tvMessage.text = item.message
            Log.e("check", "sender ${item.message}")
        }
    }

    inner class ImageSentViewHolder(binding: ItemSentImageBinding) :
        BaseRecyclerViewHolder<ItemSentImageBinding, Message>(binding) {
        override fun onBind(item: Message) {
            binding.imSent.loadImageWithGlide(item.mediaResource)
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
            Log.e("check", "receiver ${item.message}")
        }
    }

    inner class ImageReceivedViewHolder(binding: ItemReceivedImageBinding) :
        BaseRecyclerViewHolder<ItemReceivedImageBinding, Message>(binding) {
        override fun onBind(item: Message) {
            binding.imReceived.loadImageWithGlide(item.mediaResource)
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
    }
}