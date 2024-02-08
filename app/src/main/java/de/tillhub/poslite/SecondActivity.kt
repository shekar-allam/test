package de.tillhub.poslite

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import de.tillhub.scanengine.ScanEngine
import de.tillhub.scanengine.ScanEvent
import de.tillhub.scanengine.ScannedDataResult
import kotlinx.coroutines.launch

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        onClickFirst()
        lifecycleScope.launch {
            ScanEngine.getInstance(this@SecondActivity).scanner.observeScannerResults()
                .collect {
                    val t = ((it as ScanEvent.Success).content as? ScannedDataResult.ScannedData)?.value.orEmpty()
                    Toast.makeText(this@SecondActivity, t, Toast.LENGTH_SHORT)
                }
        }
    }
    fun onClickFirst() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_container_view, FirstFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}