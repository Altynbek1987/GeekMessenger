package com.geektechkb.feature_main.presentation.ui.fragments.gallerydialogbotomsheet

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import com.geektechkb.core.base.BaseViewModel
import com.geektechkb.feature_main.presentation.ui.models.GalleryPicture
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit


class GalleryBottomSheetViewModel : BaseViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private var startingRow = 0
    private var rowsToLoad = 0
    private var allLoaded = false
    private var shouldVideoBeShownInGallery = false

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
                    cursor.getColumnIndex(MediaStore.MediaColumns._ID)
                galleryImageUrls.add(
                    GalleryPicture(
                        getImageUri(cursor.getString(dataColumnIndex)).toString(),
                        false
                    )
                )

            }

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
        }
        if (shouldVideoBeShownInGallery) {
            val videoCursor = getVideoCursor(context)
            if (videoCursor != null && !allLoaded) {
                val totalRows = videoCursor.count

                allLoaded = rowsToLoad == totalRows
                if (rowsToLoad < rowsPerLoad) {
                    rowsToLoad = rowsPerLoad
                }

                for (i in startingRow until rowsToLoad) {
                    videoCursor.moveToPosition(i)
                    val dataColumnIndex =
                        videoCursor.getColumnIndex(MediaStore.MediaColumns._ID)
                    galleryImageUrls.add(
                        GalleryPicture(
                            getVideoUri(
                                videoCursor.getString(
                                    dataColumnIndex
                                )
                            ).toString(),
                            true,
                            getVideoDuration(
                                context,
                                getVideoUri(videoCursor.getString(dataColumnIndex))
                            )
                        )
                    )
                }

                startingRow = rowsToLoad

                if (rowsPerLoad > totalRows || rowsToLoad >= totalRows)
                    rowsToLoad = totalRows
                else {
                    if (totalRows - rowsToLoad <= rowsPerLoad)
                        rowsToLoad = totalRows
                    else
                        rowsToLoad += rowsPerLoad
                }

                videoCursor.close()
            }
        }

        return galleryImageUrls
    }

    private fun getGalleryCursor(context: Context): Cursor? {
        val externalUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val columns = arrayOf(MediaStore.MediaColumns._ID, MediaStore.MediaColumns.DATE_MODIFIED)
        val orderBy = MediaStore.MediaColumns.DATE_MODIFIED
        return context.contentResolver
            .query(
                externalUri,
                columns,
                null,
                null,
                "$orderBy DESC"
            )
    }

    private fun getVideoCursor(context: Context): Cursor? {
        val externalUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val columns = arrayOf(MediaStore.MediaColumns._ID, MediaStore.MediaColumns.DATE_MODIFIED)
        val orderBy = MediaStore.MediaColumns.DATE_MODIFIED
        return context.contentResolver
            .query(
                externalUri,
                columns,
                null,
                null,
                "$orderBy DESC"
            )
    }

    private fun getImageUri(path: String) = ContentUris.withAppendedId(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        path.toLong()
    )

    private fun getVideoUri(path: String) =
        ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, path.toLong())

    override fun onCleared() {
        compositeDisposable.clear()
    }

    fun shouldVideoBeShown(areVideoShown: Boolean) {
        shouldVideoBeShownInGallery = areVideoShown
    }

    private fun getVideoDuration(context: Context, uri: Uri): String {
        val mediaPlayer: MediaPlayer = MediaPlayer.create(context, uri)
        val duration = mediaPlayer.duration.toLong()
        val durationInSeconds =
            TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(duration)
            )
        mediaPlayer.release()
        return when (durationInSeconds.toString().length < 2) {
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
    }
}