package util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtils {

    private const val PERMISSION_REQUEST_CODE = 1001

    fun requestPermissions(
        activity: Activity,
        permissions: Array<String>,
//        permissionListener: PermissionListener
    ) {
//        val shouldShowRequestPermissionRationale =
//            permissions.any { shouldShowRationale(it, activity) }

//        if (permissions.all { isPermissionGranted(it, activity) }) {
//            permissionListener.onPermissionGranted()
//        } else {
//            if (shouldShowRequestPermissionRationale) {
                // Show explanation to user and request again.
                // This could be a dialog or Toast message.
                // permissionListener.onPermissionRationaleNeeded(permissions)
//            } else {
                ActivityCompat.requestPermissions(
                    activity,
                    permissions,
                    PERMISSION_REQUEST_CODE
                )
//            }
//        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
        permissionListener: PermissionListener
    ) {
//        println("拒绝的权限回调${requestCode} ${grantResults.map { it.toString() }}")
//        permissions.forEach { println("列表权限${it}") }
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                permissionListener.onPermissionGranted()
            } else {
                permissionListener.onPermissionDenied()
            }
        }
    }

    fun isPermissionGranted(permission: String, context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun isPermissionGranted(permissions: Array<String>, context: Context): Boolean {
        return permissions.all { isPermissionGranted(it, context) }
    }

    fun shouldShowRationale(permission: String, activity: Activity): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }

    fun shouldShowRationale(permissions: Array<String>, activity: Activity): Boolean {
        return permissions.all { shouldShowRationale(it, activity) }
    }
    interface PermissionListener {
        fun onPermissionGranted()
        fun onPermissionDenied()
        // Optionally:
        // fun onPermissionRationaleNeeded(permissions: Array<String>)
    }
}