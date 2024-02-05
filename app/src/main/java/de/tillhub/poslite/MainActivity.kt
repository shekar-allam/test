package de.tillhub.poslite

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import de.tillhub.scanengine.ScanEngine
import de.tillhub.scanengine.ScanEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            ScanEngine.getInstance(this@MainActivity).scanner.observeScannerResults()
                .collectLatest {
                    Log.d("======","=========>${ (it as ScanEvent.Success).content.value}")
                }
        }
        ScanEngine.getInstance(this).scanner.scanCameraCode("key")
    }
}
