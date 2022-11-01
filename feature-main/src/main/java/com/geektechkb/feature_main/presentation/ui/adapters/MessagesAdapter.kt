package com.geektechkb.feature_main.presentation.ui.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.geektechkb.common.constants.Constants.HOURS_MINUTES_DATE_FORMAT
import com.geektechkb.core.base.BaseRecyclerViewHolder
import com.geektechkb.core.extensions.formatCurrentUserTime
import com.geektechkb.core.extensions.loadImageWithGlide
import com.geektechkb.core.extensions.parseToFormat
import com.geektechkb.core.extensions.setImage
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.*
import com.geektechkb.feature_main.domain.models.Message

class MessagesAdapter   (
    private val onItemPhotoClick: (image: String, timeMessageWasSent: String, photoCount: Int) -> Unit,
    private val onItemVideoClick: (video: String, timeMessageWasSent: String, videoCount: Int) -> Unit
) :
    ListAdapter<Message, BaseRecyclerViewHolder<ViewBinding, Message>>(Companion) {

    private var senderPhoneNumber: String? = null
    private var receiverPhoneNumber: String? = null
    private var photoCount = 0
    private var videoCount = 0

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
            R.layout.item_sent_video -> VideoSentViewHolder(
                ItemSentVideoBinding.inflate(
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
            R.layout.item_received_video -> VideoReceivedViewHolder(
                ItemReceivedVideoBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )
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
            R.layout.item_sent_video -> getItem(position)?.let {
                (holder as VideoSentViewHolder).onBind(it)
            }
            R.layout.item_received_message -> getItem(position)?.let {
                (holder as MessageReceivedViewHolder).onBind(it)
            }
            R.layout.item_received_image -> getItem(position)?.let {
                (holder as ImageReceivedViewHolder).onBind(it)
            }
            R.layout.item_received_video -> getItem(position)?.let {
                (holder as VideoReceivedViewHolder).onBind(it)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            getItem(position)?.senderPhoneNumber?.equals(senderPhoneNumber) == true && getItem(
                position
            )?.mediaResource == "null" -> R.layout.item_sent_messages

            getItem(position)?.senderPhoneNumber?.equals(senderPhoneNumber) == true && getItem(
                position
            )?.mediaType == "image" && getItem(
                position
            )?.mediaResource?.isNotEmpty() == true ->
                R.layout.item_sent_image

            getItem(position)?.senderPhoneNumber?.equals(senderPhoneNumber) == true && getItem(
                position
            )?.mediaType == "video" && getItem(position)?.mediaResource?.isNotEmpty() == true ->
                R.layout.item_sent_video

            getItem(position)?.senderPhoneNumber?.equals(senderPhoneNumber) == false && getItem(
                position
            )?.mediaResource == "null" ->
                R.layout.item_received_message

            getItem(position)?.senderPhoneNumber?.equals(senderPhoneNumber) == false && getItem(
                position
            )?.mediaType == "image" && getItem(
                position
            )?.mediaResource?.isNotEmpty() == true ->
                R.layout.item_received_image

            getItem(position)?.senderPhoneNumber?.equals(senderPhoneNumber) == false && getItem(
                position
            )?.mediaType == "video" && getItem(
                position
            )?.mediaResource?.isNotEmpty() == true ->
                R.layout.item_received_video
            else ->
                R.layout.item_sent_messages
        }
    }

    fun setPhoneNumber(senderPhoneNumber: String, receiverPhoneNumber: String) {
        this.senderPhoneNumber = senderPhoneNumber
        this.receiverPhoneNumber = receiverPhoneNumber
    }

    inner class MessageSentViewHolder(binding: ItemSentMessagesBinding) :
        BaseRecyclerViewHolder<ItemSentMessagesBinding, Message>(binding) {
        override fun onBind(item: Message) {
            binding.mcvMessage.setBackgroundResource(R.drawable.first_message_sent_cornered_background)
            binding.tvTimeMessageWasSent.text = parseToFormat(item.timeMessageWasSent.toString())
            binding.tvMessage.text = item.message
        }
    }

    inner class ImageSentViewHolder(binding: ItemSentImageBinding) :
        BaseRecyclerViewHolder<ItemSentImageBinding, Message>(binding) {
        override fun onBind(item: Message) {
            binding.imSent.setImage(item.mediaResource)
            binding.tvTimeSent.text = parseToFormat(item.timeMessageWasSent.toString())
            photoCount++
            binding.mcvSent.setOnClickListener {
                onItemPhotoClick(
                    item.mediaResource.toString(),
                    item.timeMessageWasSent.toString(),
                    photoCount
                )
                photoCount = 0
            }
        }
    }

    inner class VideoSentViewHolder(binding: ItemSentVideoBinding) :
        BaseRecyclerViewHolder<ItemSentVideoBinding, Message>(binding) {
        override fun onBind(item: Message) {
            binding.vvSent.apply {
                setVideoURI(Uri.parse(item.mediaResource))
                keepScreenOn = true
                requestFocus()
                start()

            }
            binding.tvTimeSent.text = parseToFormat(item.timeMessageWasSent.toString())
            binding.tvVideoDuration.text = item.videoDuration
            videoCount++
            binding.vvSent.setOnClickListener {
                onItemVideoClick(
                    item.mediaResource.toString(),
                    item.timeMessageWasSent.toString(),
                    videoCount
                )
                videoCount = 0
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

    inner class ImageReceivedViewHolder(binding: ItemReceivedImageBinding) :
        BaseRecyclerViewHolder<ItemReceivedImageBinding, Message>(binding) {
        override fun onBind(item: Message) {
            binding.imReceived.loadImageWithGlide(item.mediaResource)
            binding.tvTimeSent.text = parseToFormat(item.timeMessageWasSent.toString())
            photoCount++
            binding.mcvReceived.setOnClickListener {
                onItemPhotoClick(
                    item.mediaResource.toString(),
                    item.timeMessageWasSent.toString(),
                    photoCount
                )
                photoCount = 0

            }
        }
    }

    inner class VideoReceivedViewHolder(binding: ItemReceivedVideoBinding) :
        BaseRecyclerViewHolder<ItemReceivedVideoBinding, Message>(binding) {
        override fun onBind(item: Message) {
            binding.vvReceived.apply {
                setVideoURI(Uri.parse(item.mediaResource))
                keepScreenOn = true
                requestFocus()
                start()
            }
            binding.tvTimeSent.text = parseToFormat(item.timeMessageWasSent.toString())
            binding.tvVideoDuration.text = item.videoDuration
            videoCount++
            binding.vvReceived.setOnClickListener {
                onItemVideoClick(
                    item.mediaResource.toString(),
                    item.timeMessageWasSent.toString(),
                    videoCount
                )
                videoCount = 0
            }
        }
    }

    companion object : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message) =
            oldItem.messageId == newItem.messageId

        override fun areContentsTheSame(oldItem: Message, newItem: Message) =
            oldItem == newItem
    }
}