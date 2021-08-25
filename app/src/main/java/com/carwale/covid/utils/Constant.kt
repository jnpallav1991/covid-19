package com.carwale.covid.utils


import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog


object Constant {
    const val NO_INTERNET = "No Internet, Please check your network connection."
    var IS_NETWORK_AVAILABLE: Boolean = true
    const val SERVER_NOT_RESPONDING = "Server not responding"

    object SubUrl {

        const val SUMMARY = "summary"

    }

    /**
     * This method is for showing Alert OR Error Dialog message of API Response
     *
     * @param context
     * @param title
     * @param msg
     */
    fun alertDialog(context: Context?, title: String, msg: String) {
        try {
            val dialogBuilder = AlertDialog.Builder(context!!)
            //val inflater = context.layoutInflater
            dialogBuilder.setCancelable(false)
            //val dialogView = inflater.inflate(R.layout.custom_alert_dialog, null)
            //dialogBuilder.setView(dialogView)

            //dialogBuilder.setTitle("Custom dialog")
            dialogBuilder.setMessage(msg)
            dialogBuilder.setPositiveButton("Ok") { dialog, _ ->
                dialog.dismiss()
            }

            val b = dialogBuilder.create()
            b.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun openKeyBoard(context: Context,view: View)
    {
        val imm: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    fun hideKeyBoard(context: Context,view: View)
    {
        val imm: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}