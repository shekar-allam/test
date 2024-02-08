package de.tillhub.scanengine.sunmi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import de.tillhub.scanengine.ScanEvent
import de.tillhub.scanengine.ScanEventProvider
import de.tillhub.scanengine.ScannedDataResult
import de.tillhub.scanengine.Scanner
import de.tillhub.scanengine.Scanner.Companion.CAMERA_SCANNER_KEY
import de.tillhub.scanengine.common.safeLet
import de.tillhub.scanengine.default.ui.GoogleScanningActivity
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import java.lang.ref.WeakReference

class SunmiScanner(
    private val registry: ActivityResultRegistry,
    private val context: Context
) : Scanner {

    private val scanEventProvider = ScanEventProvider()

    private var scanKey: String? = null

    private lateinit var sunmiScannerLauncher: ActivityResultLauncher<Intent>

    private val broadcastReceiver = SunmiBarcodeScannerBroadcastReceiver(scanEventProvider)

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        sunmiScannerLauncher =
            registry.register(
                CAMERA_SCANNER_KEY,
                owner,
                SunmiScannerActivityContract()
            ) { result ->
                scanEventProvider.addScanResult(result)
            }
        ContextCompat.registerReceiver(
            context,
            broadcastReceiver,
            createIntentFilter(),
            ContextCompat.RECEIVER_EXPORTED
        )
    }

    override fun observeScannerResults(): Flow<ScanEvent> = scanEventProvider.scanEvents
    override fun startCameraScanner(scanKey: String?) {
        this.scanKey = scanKey
        sunmiScannerLauncher.launch(scanIntent(scanKey = scanKey))
    }

    override fun scanNextWithKey(scanKey: String) {
        this.scanKey = scanKey
    }

    sealed class ScanCodeType {
        data class Type(val code: String) : ScanCodeType()
        object Unknown : ScanCodeType()
    }

    private class SunmiBarcodeScannerBroadcastReceiver(private val scanEventProvider: ScanEventProvider) :
        BroadcastReceiver() {
        var scanKey: String? = null

        override fun onReceive(context: Context, intent: Intent) {
            val code = intent.getStringExtra(DATA)
            if (!code.isNullOrEmpty()) {
                Timber.v("scanned code: %s", code)
                scanEventProvider.addScanResult(ScannedDataResult.ScannedData(code, scanKey))
            }
        }

        companion object {
            private const val DATA = "data"
        }
    }

    companion object {
        const val RESPONSE_TYPE = "TYPE"
        const val RESPONSE_VALUE = "VALUE"

        private fun scanIntent(scanMultipleCodes: Boolean = false, scanKey: String?): Intent =
            Intent("com.summi.scan").apply {
                setPackage("com.sunmi.sunmiqrcodescanner")

                // Additional intent options:
                //
                // The current preview resolution ,PPI_1920_1080 = 0X0001;PPI_1280_720 = 0X0002;PPI_BEST = 0X0003;
                // putExtra("CURRENT_PPI", 0X0003)
                //
                // Whether to identify inverse code
                // putExtra("IDENTIFY_INVERSE_QR_CODE", true)
                //
                // Vibrate after scanning, default false, only support M1 right now.
                // putExtra("PLAY_VIBRATE", false)

                // Prompt tone after scanning (default true)
                putExtra("PLAY_SOUND", true)

                // Whether to display the settings button at the top-right corner (default true)
                putExtra("IS_SHOW_SETTING", true)

                // Whether to display the album button (default true)
                putExtra("IS_SHOW_ALBUM", false)

                // Whether to identify several codes at once (default false)
                putExtra("IDENTIFY_MORE_CODE", scanMultipleCodes)

                // Scan key
                putExtra(GoogleScanningActivity.SCAN_KEY, scanKey)
            }

        private const val ACTION_DATA_CODE_RECEIVED = "com.sunmi.scanner.ACTION_DATA_CODE_RECEIVED"

        private fun createIntentFilter(): IntentFilter = IntentFilter().apply {
            addAction(ACTION_DATA_CODE_RECEIVED)
        }
    }
}
