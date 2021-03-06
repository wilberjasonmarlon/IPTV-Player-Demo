package cu.wilb3r.iptvplayerdemo.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.logging.ANDROID
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging

fun initKtorClient() = HttpClient(Android) {
    install(Logging) {
        logger = Logger.ANDROID
        level = LogLevel.ALL
    }
}

fun snack(message : String, view: View) {
    Snackbar.make(view ,message, Snackbar.LENGTH_LONG).show()
}

fun Context.toast(message : String) {
    Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}