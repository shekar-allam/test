package de.tillhub.poslite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import de.tillhub.poslite.ui.theme.PosliteTheme
import de.tillhub.scanengine.ScanEngine
import de.tillhub.scanengine.ScanEvent
import de.tillhub.scanengine.ScannedDataResult
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textEmpty = mutableStateOf("")

        lifecycleScope.launch {
            ScanEngine.getInstance(this@MainActivity).scanner.observeScannerResults()
                .collect {
                    val t =
                        ((it as ScanEvent.Success).content as? ScannedDataResult.ScannedData)?.value.orEmpty()
                    textEmpty.value = t
                }
        }
        setContent {
            PosliteTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val value by textEmpty
                    Greeting(value, Modifier) {
                        startActivity(Intent(this, SecondActivity::class.java))
                    }
                }
            }
        }
        ScanEngine.getInstance(this)
    }
}

@Composable
fun Greeting(value: String, modifier: Modifier = Modifier, startCamaraScanner: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            modifier = modifier
        )
        Spacer(modifier = Modifier.height(36.dp))
        Button(onClick = { startCamaraScanner() }
        ) {
            Text(text = "Start camera scanner")
        }
    }
}