package com.geektechkb.feature_main.presentation.ui.fragments.profil

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.data.local.preferences.UserPreferencesHelper
import com.geektechkb.core.extensions.generateRandomId
import com.geektechkb.core.extensions.loadImageWithGlide
import com.geektechkb.core.extensions.stateBottomSheet
import com.geektechkb.core.extensions.toast
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentProfileBinding
import com.geektechkb.feature_main.presentation.ui.adapters.GalleryPicturesAdapter
import com.geektechkb.feature_main.presentation.ui.fragments.gallerydialogbotomsheet.GalleryBottomSheetViewModel
import com.geektechkb.feature_main.presentation.ui.models.GalleryPicture
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment :
    BaseFragment<FragmentProfileBinding, ProfileViewModel>(R.layout.fragment_profile) {

    override val binding by viewBinding(FragmentProfileBinding::bind)
    override val viewModel: ProfileViewModel by viewModels()
    private val galleryViewModel: GalleryBottomSheetViewModel by viewModels()
    private val args by navArgs<ProfileFragmentArgs>()
    private var username: String? = null
    private var savedUserStatus: String? = null
    private var bottomSheetBehavior: BottomSheetBehavior<MaterialCardView>? = null
    private val pictures = ArrayList<GalleryPicture>()
    private val adapter = GalleryPicturesAdapter(this::onSelect, pictures)

    override fun assembleViews() {
        binding.toolbarButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    @Inject
    lateinit var preferences: UserPreferencesHelper
    override fun initialize() {
        requestReadStoragePermission()
    }

    private fun requestReadStoragePermission() {
        val readStorage = Manifest.permission.READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                readStorage
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(readStorage), 3)
        } else initBottomSheetRecycler()
        setupBottomSheet()
    }

    override fun setupListeners() {
        binding.openBottomSheet.setOnClickListener {
            getData()
        }
        //menu navigation
        binding.menuToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.profile ->
                    toast("GeekTech")
                R.id.photo ->
                    getData()
                R.id.delete ->
                    toast("Ты Лузер Работай Хорошо, и НЕ Сдавайся")
            }
            true
        }
        binding.toolbarButton.setOnClickListener {
            findNavController().navigateUp()
        }
        languageСhange()
    }

    private fun languageСhange() {
        binding.tvLanguage.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_languagesFragment)
        }
    }

    private fun getData() {
        binding.openBottomSheet.isVisible = false
        binding.coordinatorGallery.isVisible = true
        stateBottomSheet(bottomSheetBehavior, BottomSheetBehavior.STATE_HALF_EXPANDED)
        bottomSheetBehavior?.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    showView(binding.galleryBottomSheet.appbarLayout, getActionBarSize())
                    binding.openBottomSheet.isVisible = false
                } else {
                    binding.openBottomSheet.isVisible = true
                    hideAppBar(binding.galleryBottomSheet.appbarLayout)
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    override fun establishRequest() {
        fetchUser()
    }

    private fun fetchUser() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.fetchUser(preferences.currentUserPhoneNumber)
        }
    }

    override fun launchObservers() {
        subscribeToUser()
    }

    private fun subscribeToUser() {
        viewModel.userState.spectateUiState(success = {
            savedUserStatus = it.lastSeen
            binding.tvNumber.text = (it.phoneNumber)
            if (args.croppedImage.isNullOrEmpty()) {
                binding.imImageProfile.loadImageWithGlide(it.profileImage)
            }
            binding.tvName.text = it.name
            binding.tvLastSeen.text = it.lastSeen
            username = it.name
        })
        Log.e("animee", viewModel.userState.toString())
    }

    private fun setupBottomSheet() {
        args.croppedImage?.let {
            val imageBytes = Base64.decode(it, 0)
            val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            val stream = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.PNG, 90, stream)
            lifecycleScope.launch {
                viewModel.updateUserProfileImage(generateRandomId(), stream.toByteArray())
                    ?.let { image ->
                        binding.imImageProfile.loadImageWithGlide(image)
                    }
            }
        }
        bottomSheetBehavior =
            BottomSheetBehavior.from(binding.galleryBottomSheet.galleryBottomSheetDialog)
    }

    private fun showView(view: View, size: Int) {
        val params = view.layoutParams
        params.height = size
        binding.galleryBottomSheet.appbarLayout.isVisible = true
        view.layoutParams = params
    }

    private fun hideAppBar(view: View) {
        val params = view.layoutParams
        params.height = 4
        binding.galleryBottomSheet.appbarLayout.isVisible = false
        view.layoutParams = params
    }

    private fun getActionBarSize(): Int {
        val array =
            requireContext().theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
        return array.getDimension(0, 0f).toInt()
    }

    private fun initBottomSheetRecycler() {
        binding.galleryBottomSheet.recyclerviewRating.adapter = adapter
        binding.galleryBottomSheet.recyclerviewRating.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                loadPictures()
            }
        })
        loadPictures()
    }

    private fun loadPictures() {
        galleryViewModel.getImagesFromGallery(context = requireContext(), pageSize = 10) {
            if (it.isNotEmpty()) {
                pictures.addAll(it)
                adapter.notifyItemRangeInserted(pictures.size, it.size)
            }
            Log.e("GalleryListSize", "${pictures.size}")
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            initBottomSheetRecycler()
    }

    private fun onSelect(uri: Uri) {
        findNavController().navigate(
            ProfileFragmentDirections.actionProfileFragmentToCropPhotoFragment(uri.toString())
        )
    }
}




