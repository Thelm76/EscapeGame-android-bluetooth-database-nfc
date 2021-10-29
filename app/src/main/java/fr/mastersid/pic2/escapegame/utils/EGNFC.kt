package fr.mastersid.pic2.escapegame.utils

import android.content.Context
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.migration.DisableInstallInCheck
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Singleton

@Singleton
class EGNFC(context: Context) {

    // Pending intent for NFC intent foreground dispatch.
    // Used to read all NDEF tags while the app is running in the foreground.

    private var _nfcAdapter : NfcAdapter? = null
    val nfcAdapter: NfcAdapter?
        get() = _nfcAdapter

    private val _lastScan = MutableStateFlow("")
    val lastScan get() = _lastScan

    init {
        // Check if NFC is supported and enabled
        _nfcAdapter = NfcAdapter.getDefaultAdapter(context)
        Log.d("EGNFC","NFC supported : " + (_nfcAdapter != null).toString())
        Log.d("EGNFC","NFC enabled : " + (_nfcAdapter?.isEnabled).toString())
    }


    fun processIntent(intent: Intent){

        // Retrieve the raw NDEF message from the tag
        val rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)

        // Asssume we have 1x URI record
        if (rawMessages != null && rawMessages.isNotEmpty()) {
            val ndefMsg = rawMessages[0] as NdefMessage
            if (ndefMsg.records != null && ndefMsg.records.isNotEmpty()) {
                val ndefRecord = ndefMsg.records[0]
                if (ndefRecord.toUri() != null) {
                    Log.d("NFC","URI detected"+ndefRecord.toUri().toString())
                } else {
                    Log.d("NFC",String(ndefRecord.payload).substring(3))
                    _lastScan.value=(String(ndefRecord.payload).substring(3))
                }
            }
        }
    }
}


@InstallIn(SingletonComponent::class)
@Module
object EGNFCModule {
    @Provides
    @Singleton
    fun provideEGNFC(@ApplicationContext appContext: Context): EGNFC {
        return EGNFC(appContext)
    }
}