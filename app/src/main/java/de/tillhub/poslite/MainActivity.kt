package de.tillhub.poslite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.lifecycle.lifecycleScope
import de.tillhub.poslite.ui.theme.PosliteTheme
import de.tillhub.scanengine.ScanEngine
import de.tillhub.scanengine.ScanEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textEmpty = mutableStateOf("")

        lifecycleScope.launch {
            ScanEngine.getInstance(this@MainActivity).scanner.observeScannerResults()
                .collectLatest {
                    textEmpty.value = (it as ScanEvent.Success).content.value
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
                        ScanEngine.getInstance(this@MainActivity).scanner.startCameraScanner()
                    }
                }
            }
        }
        ScanEngine.getInstance(this)
    }
}

@Composable
fun Greeting(value: String, modifier: Modifier = Modifier, startCamaraScanner: () -> Unit) {
    Column(modifier=Modifier.fillMaxSize(),
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