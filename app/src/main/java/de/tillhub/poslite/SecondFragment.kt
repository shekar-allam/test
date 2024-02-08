package de.tillhub.poslite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import de.tillhub.scanengine.ScanEngine
import de.tillhub.scanengine.ScanEvent
import de.tillhub.scanengine.ScannedDataResult
import kotlinx.coroutines.launch

class SecondFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.buttonScan).setOnClickListener {
            ScanEngine.getInstance(requireActivity()).scanner.startCameraScanner("key")
        }

    }
}
