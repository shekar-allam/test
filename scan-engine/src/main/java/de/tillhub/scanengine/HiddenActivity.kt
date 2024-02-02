package de.tillhub.scanengine

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import de.tillhub.scanengine.google.ui.GoogleScanningActivity
import de.tillhub.scanengine.helper.SingletonHolder
import timber.log.Timber

class HiddenActivity : ComponentActivity() {

    private var cameraScannerResult: ActivityResultLauncher<Intent>? =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            ScanEngine.getInstance(applicationContext).scanner.also { scanner ->
                result.data?.extras?.let {
                    scanner.evaluateScanResult(it)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            cameraScannerResult?.launch(Intent(this, GoogleScanningActivity::class.java))
        } catch (e: Exception) {
            Timber.w(e, "camera scanner could not be started.")
        }
    }
}
