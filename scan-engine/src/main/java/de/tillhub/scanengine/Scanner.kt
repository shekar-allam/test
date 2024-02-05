package de.tillhub.scanengine

import android.os.Bundle
import kotlinx.coroutines.flow.Flow

/**
 * Used for connecting and disconnecting a scanner, issue scan commands and observing scanned codes.
 */
interface Scanner {
    /**
     * Can be used to observe any scanned code.
     */
    fun observeScannerResults(): Flow<ScanEvent>

    /**
     * Start the camera based scanner.
     */
    fun scanCameraCode(scanKey: String)
}

/**
 * Represents a connection to a scanner. When the scanner is not used anymore it must be disconnected using
 * [Scanner.disconnect].
 */
@Suppress("UnnecessaryAbstractClass")
abstract class ScannerConnection {
    internal abstract fun disconnect()
}
