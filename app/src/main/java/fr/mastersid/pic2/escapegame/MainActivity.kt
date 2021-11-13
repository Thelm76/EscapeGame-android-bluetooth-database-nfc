package fr.mastersid.pic2.escapegame

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import fr.mastersid.pic2.escapegame.databinding.ActivityMainBinding
import fr.mastersid.pic2.escapegame.utils.EGNFC
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

        pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        )
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
        val intent = Intent(this, PopUpWindow::class.java)
        when (_binding.toolbar.title) {
            "EscapeGame" -> {
                intent.putExtra(
                    "popuptext",
                    getString(R.string.notice_escape_game)
                )
            }
            "Lobby" -> {
                intent.putExtra(
                    "popuptext",
                    getString(R.string.notice_lobby)
                )
            }
            "Items" -> {
                intent.putExtra(
                    "popuptext",
                    getString(R.string.notice_items)
                )
            }
            "Enigma" -> {
                intent.putExtra(
                    "popuptext",
                    getString(R.string.notice_enigma)
                )
            }
        }
        startActivity(intent)
        return true
    }
}
