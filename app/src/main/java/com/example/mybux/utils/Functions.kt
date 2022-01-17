package com.example.mybux.utils

import android.app.AlertDialog
import android.content.Context
import android.content.res.Configuration
import android.provider.Settings.System.getConfiguration
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.airbnb.lottie.animation.content.Content
import com.example.domain.language.Language
import com.example.domain.language.LocaleManager
import com.example.mybux.R
import com.example.mybux.databinding.*
import com.example.mybux.theme.MyAppTheme

fun View.errorDialog(context:Context,strError:String,button:String,myAppTheme: MyAppTheme){
    val builder = AlertDialog.Builder(context, R.style.AppBottomSheetDialogTheme)
    val create = builder.create()
    val errorDialogBinding = DialogErrorBinding.inflate(LayoutInflater.from(context), null, false)
    create.setView(errorDialogBinding.root)
    errorDialogBinding.clearDialogText.setTextColor(myAppTheme.textColor(context))
    errorDialogBinding.clearDialogText.text = strError
    errorDialogBinding.textError1.setTextColor(myAppTheme.textColor(context))
    errorDialogBinding.dialogCons.setBackgroundColor(myAppTheme.ActivityBackgroundColor(context))
    errorDialogBinding.textError1.text = button
    errorDialogBinding.okBtn.setOnClickListener {
        create.dismiss()
    }
    create.setCancelable(false)
    create.show()
}


fun View.errorInternetDialog(context:Context,internetStr:String,button:String){
    val builder = AlertDialog.Builder(context, R.style.AppBottomSheetDialogTheme)
    val create = builder.create()
    val errorDialogBinding = DialogNointernetBinding.inflate(LayoutInflater.from(context), null, false)
    create.setView(errorDialogBinding.root)
    errorDialogBinding.clearDialogText.text = internetStr
    errorDialogBinding.textError1.text = button
    errorDialogBinding.okBtn.setOnClickListener {
        create.dismiss()
    }
    create.setCancelable(false)
    create.show()
}

