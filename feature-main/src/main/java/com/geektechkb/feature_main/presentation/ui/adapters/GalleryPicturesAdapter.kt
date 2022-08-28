package com.geektechkb.feature_main.presentation.ui.adapters

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.geektechkb.feature_main.databinding.ItemGalleryBinding
import com.geektechkb.feature_main.presentation.ui.models.GalleryPicture


class GalleryPicturesAdapter(
    val onSelect: (uri: Uri) -> Unit,
    private val list: ArrayList<GalleryPicture>
) : RecyclerView.Adapter<GalleryPicturesAdapter.GalleryPicturesViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, position: Int): GalleryPicturesViewHolder {
        return GalleryPicturesViewHolder(
            ItemGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }


    override fun onBindViewHolder(holder: GalleryPicturesViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    inner class GalleryPicturesViewHolder(private var binding: ItemGalleryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(galleryPicture: GalleryPicture) {
            Glide.with(binding.ivImg).load(galleryPicture.path).into(binding.ivImg)
            Log.e("olo", galleryPicture.path.toString())
            binding.root.setOnClickListener {
                onSelect(Uri.parse(galleryPicture.path))
            }
        }
    }

    override fun getItemCount() = list.size

}