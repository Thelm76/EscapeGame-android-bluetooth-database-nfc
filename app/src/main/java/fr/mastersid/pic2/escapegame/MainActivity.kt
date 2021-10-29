package fr.mastersid.pic2.escapegame

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import dagger.hilt.android.AndroidEntryPoint
import fr.mastersid.pic2.escapegame.databinding.ActivityMainBinding
import fr.mastersid.pic2.escapegame.databinding.FragmentLobyBinding
import fr.mastersid.pic2.escapegame.utils.EGNFC
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var nfcHandler: EGNFC
    private lateinit var _binding : ActivityMainBinding

    private var pendingIntent: PendingIntent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        setSupportActionBar(_binding.toolbar)

        pendingIntent = PendingIntent.getActivity(this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)

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
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return true
    }


    override fun onOptionsItemSelected(item:MenuItem):Boolean {
        Log.d("test","---------------------------------------")
        val intent = Intent(this, PopUpWindow::class.java)
        intent.putExtra("popuptitle", "Error")
        intent.putExtra("popuptext", "voici la fenetre qui servira a afficher les instructions pour chaque jeu\nvoici la fenetre qui servira a afficher les instructions pour chaque jeu\nvoici la fenetre qui servira a afficher les instructions pour chaque jeu\nvoici la fenetre qui servira a afficher les instructions pour chaque jeu\nvoici la fenetre qui servira a afficher les instructions pour chaque jeu\nvoici la fenetre qui servira a afficher les instructions pour chaque jeu\nvoici la fenetre qui servira a afficher les instructions pour chaque jeu\nvoici la fenetre qui servira a afficher les instructions pour chaque jeu\n")
        intent.putExtra("popupbtn", "OK")
        intent.putExtra("darkstatusbar", false)
        startActivity(intent)
        return true//item.onNavDestinationSelected(findNavController(R.id.navHostFragment))
    }
}
