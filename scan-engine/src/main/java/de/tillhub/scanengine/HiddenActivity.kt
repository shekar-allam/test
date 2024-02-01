package de.tillhub.scanengine

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity

import androidx.appcompat.app.AppCompatActivity

class HiddenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("==========","============")
    }
}
