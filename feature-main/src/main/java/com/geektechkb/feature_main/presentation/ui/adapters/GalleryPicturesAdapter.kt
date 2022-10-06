package com.geektechkb.feature_main.presentation.ui.adapters

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.ItemGalleryBinding
import com.geektechkb.feature_main.presentation.ui.models.GalleryPicture


class GalleryPicturesAdapter(
    val onSelect: (uri: Uri) -> Unit,
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
        Log.e("gaypopViewType", getItemViewType(position).toString())
        when (getItemViewType(position)) {

            1 -> {
//                holder.binding.mcvGalleryImage.setCardBackgroundColor(R.drawable.image_from_gallery_left_top_corner_background)
                holder.binding.mcvGalleryImage.background =
                    R.drawable.image_from_gallery_left_top_corner_background.toDrawable()
                holder.binding.mcvGalleryImage.setBackgroundResource(R.drawable.image_from_gallery_left_top_corner_background)
            }
            3 -> {
//                holder.binding.mcvGalleryImage.setCardBackgroundColor(R.drawable.image_from_gallery_right_top_corner_background)
                holder.binding.mcvGalleryImage.background =
                    R.drawable.image_from_gallery_right_top_corner_background.toDrawable()
                holder.binding.mcvGalleryImage.setBackgroundResource(R.drawable.image_from_gallery_right_top_corner_background)
            }
            else -> {
//                holder.binding.mcvGalleryImage.setCardBackgroundColor(R.drawable.image_from_gallery_no_corners_background)
                holder.binding.mcvGalleryImage.background =
                    R.drawable.image_from_gallery_no_corners_background.toDrawable()
                holder.binding.mcvGalleryImage.setBackgroundResource(R.drawable.image_from_gallery_no_corners_background)
            }
        }
    }


    override fun getItemViewType(position: Int) = when (position) {
        0 -> 1
        2 -> 3
        else -> 2
    }

    inner class GalleryPicturesViewHolder(val binding: ItemGalleryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(galleryPicture: GalleryPicture) {
            Glide.with(binding.ivImg).load(galleryPicture.path).into(binding.ivImg)
            binding.root.setOnClickListener {
                onSelect(Uri.parse(galleryPicture.path))
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