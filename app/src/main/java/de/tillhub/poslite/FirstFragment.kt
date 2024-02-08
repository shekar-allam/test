package de.tillhub.poslite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import de.tillhub.scanengine.ScanEngine
import de.tillhub.scanengine.ScanEvent
import de.tillhub.scanengine.ScannedDataResult
import kotlinx.coroutines.launch

class FirstFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.buttonNext).setOnClickListener {
            ScanEngine.getInstance(requireActivity()).scanner.startCameraScanner("key")
        }
        lifecycleScope.launch {
            ScanEngine.getInstance(requireActivity()).scanner.observeScannerResults()
                .collect {
//                    val t =
//                        ((it as ScanEvent.Success).content as? ScannedDataResult.ScannedData)?.value.orEmpty()
//                    view.findViewById<TextView>(R.id.text).text = t
                    if(it is ScanEvent.Success) {
                        onClickSecond()
                    }
                }
        }
    }

    fun onClickSecond() {
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_container_view, SecondFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}