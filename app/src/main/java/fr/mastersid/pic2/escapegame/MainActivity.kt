package fr.mastersid.pic2.escapegame

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import fr.mastersid.pic2.escapegame.utils.EGNFC
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var nfcHandler: EGNFC

    private var pendingIntent: PendingIntent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )

        if (intent != null) {
            // Check if the app was started via an NDEF intent
            //logMessage("Found intent in onCreate", intent.action.toString())
            processIntent(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        nfcHandler.nfcAdapter?.enableForegroundDispatch(this, pendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        nfcHandler.nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) processIntent(intent)
    }

    private fun processIntent(checkIntent: Intent) {
        // Check if intent has the action of a discovered NFC tag
        // with NDEF formatted contents
        if (checkIntent.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {
            nfcHandler.processIntent(checkIntent)
        }
    }
}
