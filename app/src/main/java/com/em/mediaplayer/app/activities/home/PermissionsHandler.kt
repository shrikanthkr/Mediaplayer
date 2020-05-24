package com.em.mediaplayer.app.activities.home

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import com.em.mediaplayer.app.activities.home.PermissionsHandler.Permission.*

class PermissionsHandler(context: AppCompatActivity) : LifecycleObserver {


    val permissionAvailable = MutableLiveData<Permission>()

    init {
        if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionAvailable.value = RequestPermission
            } else {
                ActivityCompat.requestPermissions(context,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        EXTERNAL_STORAGE_PERMISSION)
            }
        } else {
            permissionAvailable.value = Granted
        }
    }


    fun handleResults(requestCode: Int, grantResults: IntArray) {
        when (requestCode) {
            EXTERNAL_STORAGE_PERMISSION -> {
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionAvailable.value = Granted
                } else {
                    permissionAvailable.value = DeniedPermission
                }
            }
        }
    }

    sealed class Permission {
        object RequestPermission : Permission()
        object DeniedPermission : Permission()
        object Granted : Permission()
    }

    companion object {
        const val EXTERNAL_STORAGE_PERMISSION = 1
    }
}