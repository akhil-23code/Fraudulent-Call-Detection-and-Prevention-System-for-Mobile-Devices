package com.ibrahim.scamdetect.util

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionHelper {
    private const val REQUEST_STORAGE = 101

    /** Returns true if permission already granted, otherwise requests it and returns false */
    fun checkStoragePermission(activity: Activity): Boolean {
        val perm = android.Manifest.permission.READ_EXTERNAL_STORAGE
        return if (ContextCompat.checkSelfPermission(activity, perm)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                activity,
                arrayOf(perm),
                REQUEST_STORAGE
            )
            false
        } else {
            true
        }
    }

    /** Call from your Activity’s onRequestPermissionsResult */
    fun handleRequestResult(
        requestCode: Int,
        grantResults: IntArray,
        onGranted: () -> Unit,
        onDenied: () -> Unit
    ) {
        if (requestCode == REQUEST_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onGranted()
            } else {
                onDenied()
            }
        }
    }
}
