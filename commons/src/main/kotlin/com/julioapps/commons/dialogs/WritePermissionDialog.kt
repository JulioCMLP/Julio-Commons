package com.julioapps.commons.dialogs

import android.app.Activity
import android.support.v7.app.AlertDialog
import com.julioapps.commons.R
import com.julioapps.commons.extensions.setupDialogStuff

/**
 * A dialog for displaying the steps needed to confirm SD card write access on Android 5+
 *
 * @param activity has to be activity to avoid some Theme.AppCompat issues
 * @param callback an anonymous function
 *
 */
class WritePermissionDialog(activity: Activity, val callback: () -> Unit) {
    var dialog: AlertDialog

    init {
        val view = activity.layoutInflater.inflate(R.layout.dialog_write_permission, null)

        dialog = AlertDialog.Builder(activity)
                .setPositiveButton(R.string.ok, { dialog, which -> dialogConfirmed() })
                .setOnCancelListener { com.julioapps.commons.activities.BaseSimpleActivity.funAfterSAFPermission = null }
                .create().apply {
            activity.setupDialogStuff(view, this, R.string.confirm_storage_access_title)
        }
    }

    private fun dialogConfirmed() {
        dialog.dismiss()
        callback()
    }
}
