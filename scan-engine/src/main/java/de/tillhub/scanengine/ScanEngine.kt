package de.tillhub.scanengine

import android.content.Context
import de.tillhub.scanengine.google.GoogleScanner
import de.tillhub.scanengine.helper.SingletonHolder
import de.tillhub.scanengine.sunmi.SunmiScanner

class ScanEngine private constructor(context: Context) {


    val scanner: Scanner by lazy {
        GoogleScanner(context)
    }

    companion object : SingletonHolder<ScanEngine, Context>(::ScanEngine)
}
