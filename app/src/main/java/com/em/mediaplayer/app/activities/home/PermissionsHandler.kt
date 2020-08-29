package com.em.mediaplayer.app.activities.home

import android.Manifest.permission.READ_PHONE_STATE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import com.em.mediaplayer.app.activities.home.PermissionsHandler.Permission.*

class PermissionsHandler(private val context: AppCompatActivity) : LifecycleObserver {


    val permissionAvailable = MutableLiveData<Permission>()

    init {
        check()
    }

    fun check(){
        val storageNotGranted = ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        val phoneStateNotGranted = ContextCompat.checkSelfPermission(context, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
        Log.d("PERMISSION", "$storageNotGranted : $phoneStateNotGranted")
        if (storageNotGranted || phoneStateNotGranted) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(context,
                            READ_PHONE_STATE)) {
                val requiredPersmissions = arrayListOf<String>()
                if(storageNotGranted){
                    requiredPersmissions.add(WRITE_EXTERNAL_STORAGE)
                }
                if(phoneStateNotGranted){
                    requiredPersmissions.add(READ_PHONE_STATE)
                }
                permissionAvailable.value = RequestPermission(requiredPersmissions)
            } else {
                ActivityCompat.requestPermissions(context,
                        arrayOf(WRITE_EXTERNAL_STORAGE, READ_PHONE_STATE),
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
        class RequestPermission(val permissions: List<String>) : Permission()
        object DeniedPermission : Permission()
        object Granted : Permission()
    }

    companion object {
        const val EXTERNAL_STORAGE_PERMISSION = 1
    }
}