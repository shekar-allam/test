package de.tillhub.scanengine.google

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.VisibleForTesting
import androidx.core.content.ContextCompat.startActivity
import de.tillhub.scanengine.HiddenActivity
import de.tillhub.scanengine.ScanEvent
import de.tillhub.scanengine.ScanEventProvider
import de.tillhub.scanengine.ScannedData
import de.tillhub.scanengine.Scanner
import de.tillhub.scanengine.ScannerConnection
import de.tillhub.scanengine.google.ui.GoogleScanningActivity
import kotlinx.coroutines.flow.Flow
import timber.log.Timber


class GoogleScanner(
    private val appContext: Context
) : Scanner {
    @VisibleForTesting
    val scanEventProvider = ScanEventProvider()
    @VisibleForTesting
    var activeScannerConnection: GoogleScannerConnection? = null

    private var nextScanKey: String? = null

    override fun connect(activity: Activity): ScannerConnection? =
        when (activity) {
            is ComponentActivity -> GoogleScannerConnection(activity, appContext, scanEventProvider).apply {
                activeScannerConnection = this
            }
            else -> null
        }

    override fun disconnect(connection: ScannerConnection) {
        if (activeScannerConnection == connection) {
            activeScannerConnection = null
        }
        connection.disconnect()
    }

    override fun observeScannerResults(): Flow<ScanEvent> = scanEventProvider.scanEvents

    override fun scanCameraCode() {
        activeScannerConnection?.scanCameraCode(nextScanKey)
    }

    override fun scanNextWithKey(scanKey: String) {
        nextScanKey = scanKey
    }

    override fun clearScanKey() {
        nextScanKey = null
    }
}

class GoogleScannerConnection(
    activity: ComponentActivity,
    private val appContext: Context,
    private val scanEventProvider: ScanEventProvider,
) : ScannerConnection() {

    private var scanKey: String? = null

    private var cameraScannerResult: ActivityResultLauncher<Intent>? =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.extras?.let {
                evaluateScanResult(it)
            }
        }

    @Suppress("TooGenericExceptionCaught")
    internal fun scanCameraCode(scanKey: String?) {
        this.scanKey = scanKey
        try {
            startActivity(appContext, Intent(appContext, HiddenActivity::class.java), null)
        } catch (e: Exception) {
            Timber.w(e, "camera scanner could not be started.")
        }
    }

    override fun disconnect() {
        cameraScannerResult = null
    }

    private fun evaluateScanResult(extras: Bundle) {
        try {
            val result = extras.getString(GoogleScanningActivity.DATA_KEY).orEmpty()

            if (result.isNotEmpty()) {
                scanEventProvider.addScanResult(ScannedData(result, scanKey))
            }
            scanKey = null
        } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
            // nothing to do
        }
    }

    companion object {
        private fun scanIntent(context: Context): Intent =
            Intent(context, GoogleScanningActivity::class.java)
    }
}
