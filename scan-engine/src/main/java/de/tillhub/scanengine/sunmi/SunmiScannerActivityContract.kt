package de.tillhub.scanengine.sunmi

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import de.tillhub.scanengine.ScannedDataResult
import de.tillhub.scanengine.common.safeLet
import de.tillhub.scanengine.default.ui.GoogleScanningActivity
import timber.log.Timber

class SunmiScannerActivityContract : ActivityResultContract<Intent, ScannedDataResult>() {

    override fun createIntent(context: Context, input: Intent) = input
    override fun parseResult(resultCode: Int, intent: Intent?): ScannedDataResult =
        intent.takeIf { resultCode == Activity.RESULT_OK }?.extras?.let {
            evaluateScanResult(it)
        } ?: ScannedDataResult.Canceled


    private fun evaluateScanResult(extras: Bundle): ScannedDataResult {
        @Suppress("UNCHECKED_CAST")
        val rawCodes = extras.getSerializable("data") as List<Map<String, String>>
        val scanKey = extras.getString("scan_key")
        val result = parseScanResults(rawCodes)
        return result.firstOrNull()?.let {
            ScannedDataResult.ScannedData(
                it.content,
                scanKey
            )
        } ?: ScannedDataResult.Canceled
    }

    private fun parseScanResults(rawCodes: List<Map<String, String>>): List<ScanCode> {
        return rawCodes.mapNotNull {
            safeLet(
                it[SunmiScanner.RESPONSE_TYPE],
                it[SunmiScanner.RESPONSE_VALUE]
            ) { type, value ->
                ScanCode(
                    type.toScanCodeType(),
                    value
                )
            }
        }
    }

    private fun String?.toScanCodeType() = when (this) {
        null -> SunmiScanner.ScanCodeType.Unknown
        else -> SunmiScanner.ScanCodeType.Type(this)
    }

    private data class ScanCode(
        val codeType: SunmiScanner.ScanCodeType,
        val content: String,
    )

}
