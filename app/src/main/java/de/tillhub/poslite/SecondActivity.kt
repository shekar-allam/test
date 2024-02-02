package de.tillhub.poslite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import de.tillhub.poslite.ui.theme.PosliteTheme
import de.tillhub.scanengine.ScanEngine
import de.tillhub.scanengine.google.ui.GoogleScanningActivity

class SecondActivity : ComponentActivity() {
    private var cameraScannerResult: ActivityResultLauncher<Intent>? =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            ScanEngine.getInstance(this).scanner.also { scanner ->
                launch()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launch()
        extracted()
    }
    private fun extracted() {
        setContent {
            PosliteTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                }
            }
        }
    }
    private fun launch() {
        try {
            cameraScannerResult?.launch(Intent(this, GoogleScanningActivity::class.java))
        } catch (e: Exception) {
            Log.d("==", "camera scanner could not be started.")
        }
    }

}