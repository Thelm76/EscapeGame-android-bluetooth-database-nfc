package fr.mastersid.pic2.escapegame

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import fr.mastersid.pic2.escapegame.databinding.ActivityMainBinding
import fr.mastersid.pic2.escapegame.utils.EGNFC
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_items.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var nfcHandler: EGNFC
    private lateinit var _binding: ActivityMainBinding

    private var pendingIntent: PendingIntent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        setSupportActionBar(_binding.toolbar)
        //this.supportActionBar?.title = "Start"
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    //TODO allow text to be different depending on the current view, and move to strings.xml
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //item.title=_binding.toolbar.title
        val intent = Intent(this, PopUpWindow::class.java)
        when (_binding.toolbar.title) {
            "EscapeGame" -> {
                intent.putExtra(
                    "popuptext",
                    "notice frag EscapeGame"
                )
            }
            "Lobby" -> {
                intent.putExtra(
                    "popuptext",
                    "notice frag Lobby"
                )
            }
            "Items" -> {
                intent.putExtra(
                    "popuptext",
                    "notice frag Items"
                )
            }
            "Enigma" -> {
                intent.putExtra(
                    "popuptext",
                    "notice frag Enigma"
                )
            }
        }

        startActivity(intent)
        return true
    }


}
