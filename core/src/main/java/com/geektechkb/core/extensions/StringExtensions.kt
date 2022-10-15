package com.geektechkb.core.extensions

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.geektechkb.core.R

fun String.removeExtraSpaces() = replace(" ", "")
fun String?.takeFirstCharacterAndCapitalizeIt() =
    this?.first()?.uppercaseChar()?.toString().toString()

fun String.downloadAgreementByDownloadManager(
    context: Context, title: String, description: String
) {
    val request = DownloadManager.Request(Uri.parse(this))
    request.setTitle(title)
    request.setMimeType("application/pdf")
//    request.setDescription(description)
    request.setAllowedOverMetered(true)
    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
    request.setDestinationInExternalPublicDir(
        Environment.DIRECTORY_DOWNLOADS,
        "${context.getString(R.string.app_name)}/$title"
    )
    val downloadManager: DownloadManager =
        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    downloadManager.enqueue(request)

}