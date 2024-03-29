package com.geektechkb.feature_main.presentation.ui.adapters

import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
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
import com.geektechkb.feature_main.domain.models.GroupMessage
import java.util.concurrent.TimeUnit
import kotlin.math.ceil

class GroupMessagesAdapter(
    private val onItemPhotoClick: (image: String, timeMessageWasSent: String, photoCount: Int) -> Unit,
    private val onItemVideoClick: (video: String, timeMessageWasSent: String, videoCount: Int) -> Unit
) :
    ListAdapter<GroupMessage, BaseRecyclerViewHolder<ViewBinding, GroupMessage>>(Companion) {

    private var senderPhoneNumber: String? = null
    private var receiverPhoneNumber: String? = null
    private var photoCount = 0
    private var videoCount = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BaseRecyclerViewHolder<ViewBinding, GroupMessage> {
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
            R.layout.item_sent_voice_message -> VoiceMessageSentViewHolder(
                ItemSentVoiceMessageBinding.inflate(
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
            R.layout.item_received_voice_message -> VoiceMessageReceivedViewHolder(
                ItemReceivedVoiceMessageBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )
            R.layout.item_sent_image_with_message -> ImageSentWithMessageViewHolder(
                ItemSentImageWithMessageBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )
            R.layout.item_received_image_with_message -> ImageReceivedWithMessageViewHolder(
                ItemReceivedImageWithMessageBinding.inflate(
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
        holder: BaseRecyclerViewHolder<ViewBinding, GroupMessage>,
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
            R.layout.item_sent_voice_message -> getItem(position)?.let {
                (holder as VoiceMessageSentViewHolder).onBind(it)
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
            R.layout.item_received_voice_message -> getItem(position)?.let {
                (holder as VoiceMessageReceivedViewHolder).onBind(it)
            }
            R.layout.item_sent_image_with_message -> getItem(position)?.let {
                (holder as ImageSentWithMessageViewHolder).onBind(it)
            }
            R.layout.item_received_image_with_message -> getItem(position)?.let {
                (holder as ImageReceivedWithMessageViewHolder).onBind(it)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            getItem(position)?.senderPhoneNumber?.equals(senderPhoneNumber) == true && getItem(
                position
            )?.media == "null" -> R.layout.item_sent_messages

            getItem(position)?.senderPhoneNumber?.equals(senderPhoneNumber) == true && getItem(
                position
            )?.mediaType == "image" && getItem(
                position
            )?.media?.isNotEmpty() == true && getItem(position)?.message?.isEmpty() == true ->
                R.layout.item_sent_image

//            getItem(position)?.senderPhoneNumber?.equals(senderPhoneNumber) == true && getItem(
//                position
//            )?.mediaType == "video" && getItem(position)?.media?.isNotEmpty() == true ->
//                R.layout.item_sent_video
            getItem(position)?.senderPhoneNumber?.equals(senderPhoneNumber) == true && getItem(
                position
            )?.mediaType == "voiceMessage" && getItem(position)?.media?.isNotEmpty() == true ->
                R.layout.item_sent_voice_message

            getItem(position)?.senderPhoneNumber?.equals(senderPhoneNumber) == false && getItem(
                position
            )?.media == "null" ->
                R.layout.item_received_message

            getItem(position)?.senderPhoneNumber?.equals(senderPhoneNumber) == false && getItem(
                position
            )?.mediaType == "image" && getItem(
                position
            )?.media?.isNotEmpty() == true && getItem(position)?.message?.isEmpty() == true ->
                R.layout.item_received_image
            getItem(position)?.senderPhoneNumber?.equals(senderPhoneNumber) == false && getItem(
                position
            )?.mediaType == "image" && getItem(
                position
            )?.media?.isNotEmpty() == true && getItem(position).message?.isNotEmpty() == true ->
                R.layout.item_received_image_with_message

            getItem(position)?.senderPhoneNumber?.equals(senderPhoneNumber) == true && getItem(
                position
            )?.mediaType == "image" && getItem(
                position
            )?.media?.isNotEmpty() == true && getItem(position).message?.isNotEmpty() == true ->
                R.layout.item_sent_image_with_message

//            getItem(position)?.senderPhoneNumber?.equals(senderPhoneNumber) == false && getItem(
//                position
//            )?.mediaType == "video" && getItem(
//                position
//            )?.media?.isNotEmpty() == true ->
//                R.layout.item_received_video
            getItem(position)?.senderPhoneNumber?.equals(senderPhoneNumber) == false && getItem(
                position
            )?.mediaType == "voiceMessage" && getItem(
                position
            )?.media?.isNotEmpty() == true ->
                R.layout.item_received_voice_message
            else ->
                R.layout.item_sent_messages
        }
    }

    fun setPhoneNumber(senderPhoneNumber: String, receiverPhoneNumber: String) {
        this.senderPhoneNumber = senderPhoneNumber
        this.receiverPhoneNumber = receiverPhoneNumber
    }

    inner class MessageSentViewHolder(binding: ItemSentMessagesBinding) :
        BaseRecyclerViewHolder<ItemSentMessagesBinding, GroupMessage>(binding) {
        override fun onBind(item: GroupMessage) {
            binding.mcvMessage.setBackgroundResource(R.drawable.first_message_sent_cornered_background)
            binding.tvTimeMessageWasSent.text = parseToFormat(item.timeMessageWasSent.toString())
            binding.tvMessage.text = item.message
        }
    }

    inner class ImageSentViewHolder(binding: ItemSentImageBinding) :
        BaseRecyclerViewHolder<ItemSentImageBinding, GroupMessage>(binding) {
        override fun onBind(item: GroupMessage) {
            binding.imSent.setImage(item.media)
            binding.tvTimeSent.text = parseToFormat(item.timeMessageWasSent.toString())
            photoCount++
            binding.mcvSent.setOnClickListener {
                onItemPhotoClick(
                    item.media.toString(),
                    item.timeMessageWasSent.toString(),
                    photoCount
                )
                photoCount = 0
            }
        }
    }

    inner class ImageSentWithMessageViewHolder(binding: ItemSentImageWithMessageBinding) :
        BaseRecyclerViewHolder<ItemSentImageWithMessageBinding, GroupMessage>(binding) {
        override fun onBind(item: GroupMessage) {
            binding.imSent.setImage(item.media)
            binding.tvMessage.text = item.message
            binding.tvTimeSent.text = parseToFormat(item.timeMessageWasSent.toString())
            photoCount++
            binding.mcvSent.setOnClickListener {
                onItemPhotoClick(
                    item.media.toString(),
                    item.timeMessageWasSent.toString(),
                    photoCount
                )
                photoCount = 0
            }
        }
    }

    inner class VideoSentViewHolder(binding: ItemSentVideoBinding) :
        BaseRecyclerViewHolder<ItemSentVideoBinding, GroupMessage>(binding) {
        override fun onBind(item: GroupMessage) {
            binding.vvSent.apply {
//                setVideoURI(Uri.parse(item.mediaResource))
                keepScreenOn = true
                requestFocus()
                start()
            }
            binding.tvTimeSent.text = parseToFormat(item.timeMessageWasSent.toString())
//            binding.tvVideoDuration.text = item.videoDuration
            videoCount++
            binding.vvSent.setOnClickListener {
                onItemVideoClick(
                    item.media.toString(),
                    item.timeMessageWasSent.toString(),
                    videoCount
                )
                videoCount = 0
            }
        }
    }

    inner class VoiceMessageSentViewHolder(binding: ItemSentVoiceMessageBinding) :
        BaseRecyclerViewHolder<ItemSentVoiceMessageBinding, GroupMessage>(binding) {
        private lateinit var runnable: Runnable
        override fun onBind(item: GroupMessage) = with(binding) {
            tvTimeMessageWasSent.text = parseToFormat(item.timeMessageWasSent.toString())
            val mediaPlayer =
                MediaPlayer.create(binding.root.context, Uri.parse(item.media))
            mediaPlayer.seekTo(1)
            val duration = mediaPlayer.duration.toLong()
            val durationInSeconds =
                TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(duration)
                )
            val totalDuration = when (durationInSeconds.toString().length < 2) {
                true ->
                    String.format(
                        "%d:0%d ",
                        TimeUnit.MILLISECONDS.toMinutes(duration),
                        durationInSeconds
                    )
                false ->
                    String.format(
                        "%d:%d",
                        TimeUnit.MILLISECONDS.toMinutes(duration),
                        durationInSeconds
                    )
            }
            tvCurrentAudioPositionAndTotalDuration.text =
                tvCurrentAudioPositionAndTotalDuration.context.getString(
                    R.string.start_voice_message_time,
                    totalDuration
                )
            val handler = Looper.myLooper()?.let { Handler(it) }
            seekbar.max = mediaPlayer.duration
            ibPlayer.apply {
                setOnClickListener {
                    this.isSelected = !this.isSelected
                    when (isSelected) {
                        true -> {
                            mediaPlayer.start()
                        }
                        false -> {
                            mediaPlayer.pause()
                        }
                    }
                }
                runnable = Runnable {
                    seekbar.progress = mediaPlayer.currentPosition
                    handler?.postDelayed(runnable, 100L)
                }
                handler?.postDelayed(runnable, 100L)
                mediaPlayer.setOnCompletionListener {
                    mediaPlayer.pause()
                    mediaPlayer.seekTo(0)
                    seekbar.progress = mediaPlayer.currentPosition
                    isSelected = false
                }
            }
            seekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser)
                        mediaPlayer.seekTo(progress)
                    val currentSeconds = ceil((progress / 1000f).toDouble()).toInt()
                    when (currentSeconds > 10) {
                        false -> {
                            tvCurrentAudioPositionAndTotalDuration.text =
                                tvCurrentAudioPositionAndTotalDuration.context.getString(
                                    R.string.current_seconds_if_less_than_ten_plus_total_duration,
                                    currentSeconds.toString(),
                                    totalDuration
                                )
                        }
                        true -> {
                            tvCurrentAudioPositionAndTotalDuration.text =
                                tvCurrentAudioPositionAndTotalDuration.context.getString(
                                    R.string.current_seconds_if_more_than_ten_plus_total_duration,
                                    currentSeconds.toString(),
                                    totalDuration
                                )
                        }
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                }
            })
        }
    }

    inner class MessageReceivedViewHolder(binding: ItemReceivedMessageBinding) :
        BaseRecyclerViewHolder<ItemReceivedMessageBinding, GroupMessage>(binding) {
        override fun onBind(item: GroupMessage) {
            binding.tvMessage.text = item.message
            binding.tvTimeMessageWasSent.text = formatCurrentUserTime(HOURS_MINUTES_DATE_FORMAT)
            binding.userGroupName.text = item.senderPhoneNumber

        }
    }

    inner class ImageReceivedViewHolder(binding: ItemReceivedImageBinding) :
        BaseRecyclerViewHolder<ItemReceivedImageBinding, GroupMessage>(binding) {
        override fun onBind(item: GroupMessage) {
            binding.imReceived.loadImageWithGlide(item.media)
            binding.tvTimeSent.text = parseToFormat(item.timeMessageWasSent.toString())
            photoCount++
            binding.mcvReceived.setOnClickListener {
                onItemPhotoClick(
                    item.media.toString(),
                    item.timeMessageWasSent.toString(),
                    photoCount
                )
                photoCount = 0

            }
        }
    }

    inner class ImageReceivedWithMessageViewHolder(binding: ItemReceivedImageWithMessageBinding) :
        BaseRecyclerViewHolder<ItemReceivedImageWithMessageBinding, GroupMessage>(binding) {
        override fun onBind(item: GroupMessage) {
            binding.imReceived.loadImageWithGlide(item.media)
            binding.tvMessage.text = item.message
            binding.tvTimeSent.text = parseToFormat(item.timeMessageWasSent.toString())
            photoCount++
            binding.mcvReceived.setOnClickListener {
                onItemPhotoClick(
                    item.media.toString(),
                    item.timeMessageWasSent.toString(),
                    photoCount
                )
                photoCount = 0

            }
        }
    }

    inner class VideoReceivedViewHolder(binding: ItemReceivedVideoBinding) :
        BaseRecyclerViewHolder<ItemReceivedVideoBinding, GroupMessage>(binding) {
        override fun onBind(item: GroupMessage) {
//            binding.vvReceived.apply {
//                setVideoURI(Uri.parse(item.media))
//                keepScreenOn = true
//                requestFocus()
//                start()
//            }
//            binding.tvTimeSent.text = parseToFormat(item.timeMessageWasSent.toString())
//            binding.tvVideoDuration.text = item.videoDuration
//            videoCount++
//            binding.vvReceived.setOnClickListener {
//                onItemVideoClick(
//                    item.mediaResource.toString(),
//                    item.timeMessageWasSent.toString(),
//                    videoCount
//                )
//                videoCount = 0
//            }
        }
    }

    inner class VoiceMessageReceivedViewHolder(binding: ItemReceivedVoiceMessageBinding) :
        BaseRecyclerViewHolder<ItemReceivedVoiceMessageBinding, GroupMessage>(binding) {
        private lateinit var runnable: Runnable
        override fun onBind(item: GroupMessage) = with(binding) {
            tvTimeMessageWasSent.text = parseToFormat(item.timeMessageWasSent.toString())
            val mediaPlayer =
                MediaPlayer.create(binding.root.context, Uri.parse(item.media))
            mediaPlayer.seekTo(1)
            val duration = mediaPlayer.duration.toLong()
            val durationInSeconds =
                TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(duration)
                )
            val totalDuration = when (durationInSeconds.toString().length < 2) {
                true ->
                    String.format(
                        "%d:0%d ",
                        TimeUnit.MILLISECONDS.toMinutes(duration),
                        durationInSeconds
                    )
                false ->
                    String.format(
                        "%d:%d",
                        TimeUnit.MILLISECONDS.toMinutes(duration),
                        durationInSeconds
                    )
            }
            tvCurrentAudioPositionAndTotalDuration.text =
                tvCurrentAudioPositionAndTotalDuration.context.getString(
                    R.string.start_voice_message_time,
                    totalDuration
                )
            val handler = Looper.myLooper()?.let { Handler(it) }
            seekbar.max = mediaPlayer.duration
            ibPlayer.apply {
                setOnClickListener {
                    this.isSelected = !this.isSelected
                    when (isSelected) {
                        true -> {
                            mediaPlayer.start()
                        }
                        false -> {
                            mediaPlayer.pause()
                        }
                    }
                }
                runnable = Runnable {
                    seekbar.progress = mediaPlayer.currentPosition
                    handler?.postDelayed(runnable, 100L)
                }
                handler?.postDelayed(runnable, 100L)
                mediaPlayer.setOnCompletionListener {
                    mediaPlayer.pause()
                    mediaPlayer.seekTo(0)
                    seekbar.progress = mediaPlayer.currentPosition
                    isSelected = false
                }
            }
            seekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser)
                        mediaPlayer.seekTo(progress)
                    val currentSeconds = ceil((progress / 1000f).toDouble()).toInt()
                    when (currentSeconds > 10) {
                        false -> {
                            tvCurrentAudioPositionAndTotalDuration.text =
                                tvCurrentAudioPositionAndTotalDuration.context.getString(
                                    R.string.current_seconds_if_less_than_ten_plus_total_duration,
                                    currentSeconds.toString(),
                                    totalDuration
                                )
                        }
                        true -> {
                            tvCurrentAudioPositionAndTotalDuration.text =
                                tvCurrentAudioPositionAndTotalDuration.context.getString(
                                    R.string.current_seconds_if_more_than_ten_plus_total_duration,
                                    currentSeconds.toString(),
                                    totalDuration
                                )
                        }
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                }
            })
        }
    }

    companion object : DiffUtil.ItemCallback<GroupMessage>() {
        override fun areItemsTheSame(oldItem: GroupMessage, newItem: GroupMessage) =
            oldItem.messageId == newItem.messageId

        override fun areContentsTheSame(oldItem: GroupMessage, newItem: GroupMessage) =
            oldItem == newItem
    }
}