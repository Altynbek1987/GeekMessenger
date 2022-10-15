package com.geektechkb.feature_main.presentation.ui.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.geektechkb.feature_main.databinding.ItemGalleryBinding
import com.geektechkb.feature_main.presentation.ui.models.GalleryPicture


class GalleryPicturesAdapter(
    private val onImageSelected: (uri: Uri) -> Unit,
    private val onVideoSelected: ((uri: Uri, videoDuration: String?) -> Unit)? = null
) : ListAdapter<GalleryPicture, GalleryPicturesAdapter.GalleryPicturesViewHolder>(Companion) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        GalleryPicturesViewHolder(
            ItemGalleryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: GalleryPicturesViewHolder, position: Int) {
        getItem(position)?.let { holder.onBind(it) }
    }

    inner class GalleryPicturesViewHolder(val binding: ItemGalleryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(galleryPicture: GalleryPicture) = with(binding) {
            when (galleryPicture.isVideo) {
                true -> {
                    tvVideoDuration.isGone = false
                    tvVideoDuration.text = galleryPicture.videoDuration
                }
                false -> {
                    tvVideoDuration.isGone = true
                    tvVideoDuration.text = ""
                }
            }
            Glide.with(ivImg).load(galleryPicture.path).into(ivImg)
            root.setOnClickListener {

                when (galleryPicture.isVideo) {
                    false ->
                        onImageSelected(
                            Uri.parse(galleryPicture.path)
                        )
                    true -> onVideoSelected?.invoke(
                        Uri.parse(galleryPicture.path),
                        galleryPicture.videoDuration
                    )
                }
            }
        }
    }

    companion object : DiffUtil.ItemCallback<GalleryPicture>() {
        override fun areItemsTheSame(oldItem: GalleryPicture, newItem: GalleryPicture) =
            oldItem.path == newItem.path

        override fun areContentsTheSame(oldItem: GalleryPicture, newItem: GalleryPicture) =
            oldItem == newItem
    }
}