package fr.mastersid.pic2.escapegame.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.navArgs
import fr.mastersid.pic2.escapegame.R
import fr.mastersid.pic2.escapegame.databinding.FragmentLobbyBinding
import fr.mastersid.pic2.escapegame.viewModel.LobbyViewModel
import java.util.*

/**
 *Created by Bryan BARRE on 15/10/2021.
 */
class LobbyFragment : Fragment() {
    private lateinit var _binding: FragmentLobbyBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLobbyBinding.inflate(inflater)
        return _binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lobbyViewModel: LobbyViewModel by hiltNavGraphViewModels(R.id.nav_graph)
        val args: LobbyFragmentArgs by navArgs()


        lobbyViewModel.connected.observe(this) { value ->
            //TODO get connected players from DB & allow continuing party if everyone is connected
            //TODO lower priority, initialize bluetooth connection and share BT MAC adresses
            Log.d("ok", value.toString())

            if (value.isNotEmpty()){
                _binding.switch1.isChecked = value[0]
                _binding.player1ProgressBar.isVisible = !value[0]

                _binding.switch2.isChecked = value[1]
                _binding.player2ProgressBar.isVisible = !value[1]

                _binding.switch3.isChecked = value[2]
                _binding.player3ProgressBar.isVisible = !value[2]


                _binding.playButton.isEnabled = value[0] && value[1] && value[2]
                _binding.playButton.isVisible = args.playerNumber==1
            }


        }


        /*_binding.playButton.setOnClickListener {
            val playerNumbers = args.playerNumber
            val action = LobyFragmentDirections.actionLobyFragmentToNfcFragment(playerNumbers)
            findNavController().navigate(action)
        }*/
        /*_binding.playButton.setOnClickListener(){

            Log.d("Hello", lobyModel.getMacAddress())
        }*/

    }


}
