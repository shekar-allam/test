package de.tillhub.scanengine

import android.app.Activity
import kotlinx.coroutines.flow.Flow

/**
 * Used for connecting and disconnecting a scanner, issue scan commands and observing scanned codes.
 */
interface Scanner {

    /**
     * Connects the scanner to the given [Activity]. When the connection was successful a [ScannerConnection]
     * is returned. This connection must be used to disconnect when the activity is destroyed using [disconnect].
     * When a [ScannerConnection] is obtained it will be used internally to make any further processing of
     * the scanner hardware. Any additional calls to [connect] will result in a new [ScannerConnection] which
     * is then used instead of the old one. Caution: the old [ScannerConnection] will not be disconnected
     * automatically. This needs to be implemented by the caller.
     *
     * The scanner results can be obtained via [observeScannerResults].
     */
    fun connect(activity: Activity): ScannerConnection?

    /**
     * Disconnects the [ScannerConnection]. If another [ScannerConnection] is currently being used
     * internally (i.e. another activity was started before the old one was destroyed) it will not be affected and
     * work as intended. However the passed [ScannerConnection] will be disconnected in any case to prevent
     * memory leaks.
     */
    fun disconnect(connection: ScannerConnection)

    /**
     * Can be used to observe any scanned code.
     */
    fun observeScannerResults(): Flow<ScanEvent>

    /**
     * Start the camera based scanner.
     */
    fun scanCameraCode()

    /**
     * Set the scanKey that will be used with next scanning event
     */
    fun scanNextWithKey(scanKey: String)

    /**
     * Clear the scanKey that is set to be used with next scanning event
     */
    fun clearScanKey()
}

/**
 * Represents a connection to a scanner. When the scanner is not used anymore it must be disconnected using
 * [Scanner.disconnect].
 */
@Suppress("UnnecessaryAbstractClass")
abstract class ScannerConnection {
    internal abstract fun disconnect()
}
