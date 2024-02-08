package de.tillhub.scanengine

import androidx.activity.ComponentActivity
import de.tillhub.scanengine.default.DefaultScanner
import de.tillhub.scanengine.helper.SingletonHolder
import de.tillhub.scanengine.sunmi.SunmiScanner
import java.lang.ref.WeakReference

class ScanEngine private constructor(activity: ComponentActivity) {


    val scanner: Scanner by lazy {
        when (ScannerManufacturer.get()) {
            ScannerManufacturer.SUNMI -> SunmiScanner(WeakReference(activity))
            ScannerManufacturer.OTHER -> DefaultScanner(activity.activityResultRegistry)
        }
    }
    init {
        activity.lifecycle.addObserver(scanner)
    }

    companion object : SingletonHolder<ScanEngine, ComponentActivity>(::ScanEngine)
}
