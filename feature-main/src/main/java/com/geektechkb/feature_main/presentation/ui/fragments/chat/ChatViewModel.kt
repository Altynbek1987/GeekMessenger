package com.geektechkb.feature_main.presentation.ui.fragments.chat

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.geektechkb.core.base.BaseViewModel
import com.geektechkb.feature_main.domain.models.User
import com.geektechkb.feature_main.domain.useCases.*
import com.geektechkb.feature_main.presentation.ui.models.GalleryPicture
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val sendVoiceMessageUseCase: SendVoiceMessageUseCase,
    private val fetchPagedMessagesUseCase: FetchPagedMessagesUseCase,
    private val fetchUserUseCase: FetchUserUseCase,
    private val makeAVoiceCallUseCase: MakeAVoiceCallUseCase
) : BaseViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private var startingRow = 0
    private var rowsToLoad = 0
    private var allLoaded = false

    private val _userState = mutableUiStateFlow<User>()
    val userState = _userState.asStateFlow()

    fun sendMessage(
        id: String,
        receiverPhoneNumber: String,
        message: String,
        timeMessageWasSent: String,
        messageId: String,
    ) {
        viewModelScope.launch {
            sendMessageUseCase(id, receiverPhoneNumber, message, timeMessageWasSent, messageId )
        }
    }

    fun sendVoiceMessage(file: String, imageFileName: String) {
        viewModelScope.launch {
            sendVoiceMessageUseCase(file, imageFileName)
        }
    }


    fun getImagesFromGallery(
        context: Context,
        pageSize: Int?,
        list: (List<GalleryPicture>) -> Unit
    ) {
        compositeDisposable.add(
            Single.fromCallable {
                pageSize?.let { fetchGalleryImages(context, it) }
            }.subscribeOn(Schedulers.single()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    list(it)
                }, {
                    it.printStackTrace()
                })
        )
    }

    private fun fetchGalleryImages(context: Context, rowsPerLoad: Int): List<GalleryPicture> {
        val galleryImageUrls = LinkedList<GalleryPicture>()
        val cursor = getGalleryCursor(context)

        if (cursor != null && !allLoaded) {
            val totalRows = cursor.count

            allLoaded = rowsToLoad == totalRows
            if (rowsToLoad < rowsPerLoad) {
                rowsToLoad = rowsPerLoad
            }

            for (i in startingRow until rowsToLoad) {
                cursor.moveToPosition(i)
                val dataColumnIndex =
                    cursor.getColumnIndex(MediaStore.MediaColumns._ID) //get column index
                galleryImageUrls.add(GalleryPicture(getImageUri(cursor.getString(dataColumnIndex)).toString())) //get Image path from column index

            }
            Log.i("TotalGallerySize", "$totalRows")
            Log.i("GalleryStart", "$startingRow")
            Log.i("GalleryEnd", "$rowsToLoad")

            startingRow = rowsToLoad

            if (rowsPerLoad > totalRows || rowsToLoad >= totalRows)
                rowsToLoad = totalRows
            else {
                if (totalRows - rowsToLoad <= rowsPerLoad)
                    rowsToLoad = totalRows
                else
                    rowsToLoad += rowsPerLoad
            }

            cursor.close()
            Log.i("PartialGallerySize", " ${galleryImageUrls.size}")
        }
        return galleryImageUrls
    }

    private fun getGalleryCursor(context: Context): Cursor? {
        val externalUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val columns = arrayOf(MediaStore.MediaColumns._ID, MediaStore.MediaColumns.DATE_MODIFIED)
        val orderBy = MediaStore.MediaColumns.DATE_MODIFIED //order data by modified
        return context.contentResolver
            .query(
                externalUri,
                columns,
                null,
                null,
                "$orderBy DESC"
            )//get all data in Cursor by sorting in DESC order
    }

    private fun getImageUri(path: String) = ContentUris.withAppendedId(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        path.toLong()
    )

    fun fetchPagedMessages() = fetchPagedMessagesUseCase()

    suspend fun fetchUser(phoneNumber: String) =
        fetchUserUseCase(phoneNumber).gatherRequest(_userState)

    fun makeAVoiceCall(
        callerId: String,
        calleeId: String,
        actionOnCallCreatedSuccessfully: (() -> Unit)? = null,
        actionOnCallConnected: (() -> Unit)? = null,
        actionOnCallEnded: (() -> Unit)? = null
    ) = makeAVoiceCallUseCase(
        callerId,
        calleeId,
        actionOnCallCreatedSuccessfully,
        actionOnCallConnected,
        actionOnCallEnded
    )

    init {
        fetchPagedMessages()
    }
}