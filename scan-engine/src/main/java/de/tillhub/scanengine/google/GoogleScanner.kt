package de.tillhub.scanengine.google

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.core.content.ContextCompat.startActivity
import de.tillhub.scanengine.HiddenActivity
import de.tillhub.scanengine.ScanEvent
import de.tillhub.scanengine.ScanEventProvider
import de.tillhub.scanengine.ScannedData
import de.tillhub.scanengine.Scanner
import de.tillhub.scanengine.google.ui.GoogleScanningActivity
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class GoogleScanner(
    private val appContext: Context
) : Scanner {
    @VisibleForTesting
    val scanEventProvider = ScanEventProvider()

    private var nextScanKey: String? = null


    override fun observeScannerResults(): Flow<ScanEvent> = scanEventProvider.scanEvents

    override fun scanCameraCode() {
        try {
            startActivity(appContext, Intent(appContext, HiddenActivity::class.java), null)
        } catch (e: Exception) {
            Timber.w(e, "camera scanner could not be started.")
        }
    }

    override fun scanNextWithKey(scanKey: String) {
        nextScanKey = scanKey
    }

    override fun clearScanKey() {
        nextScanKey = null
    }

    override fun evaluateScanResult(extras: Bundle) {
        try {
            val result = extras.getString(GoogleScanningActivity.DATA_KEY).orEmpty()

            if (result.isNotEmpty()) {
                scanEventProvider.addScanResult(ScannedData(result, nextScanKey))
            }
            nextScanKey = null
        } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
            // nothing to do
        }
    }
}
