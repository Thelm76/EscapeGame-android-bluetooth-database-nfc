package fr.mastersid.pic2.escapegame.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import fr.mastersid.pic2.escapegame.MainActivity
import fr.mastersid.pic2.escapegame.R
import fr.mastersid.pic2.escapegame.databinding.FragmentLobbyBinding
import fr.mastersid.pic2.escapegame.viewModel.LobbyViewModel

class LobbyFragment : Fragment() {
    private lateinit var _binding: FragmentLobbyBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLobbyBinding.inflate(inflater)
        (activity as MainActivity).supportActionBar?.title = "Lobby"
        return _binding.root
    }

    //TODO initialize bluetooth connection and share BT MAC addresses
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lobbyViewModel: LobbyViewModel by hiltNavGraphViewModels(R.id.nav_graph)
        val args: LobbyFragmentArgs by navArgs()

        val playerNumber = args.playerNumber
        val mac = lobbyViewModel.MacAddress()
        lobbyViewModel.SaveMacAddress(mac, playerNumber)

        lobbyViewModel.usersConnected.observe(this) { value ->
            if (value.isNotEmpty()){
                _binding.switch1.isChecked = value[0]
                _binding.player1ProgressBar.isVisible = !value[0]

                _binding.switch2.isChecked = value[1]
                _binding.player2ProgressBar.isVisible = !value[1]

                _binding.switch3.isChecked = value[2]
                _binding.player3ProgressBar.isVisible = !value[2]

                _binding.playButton.isEnabled = value[0] && value[1] && value[2]
            }
        }

        _binding.playButton.setOnClickListener {
            val action = LobbyFragmentDirections.actionLobbyFragmentToItemsFragment(playerNumber)
            findNavController().navigate(action)
        }

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val action =  LobbyFragmentDirections.actionLobbyFragmentToStartFragment(playerNumber)
                lobbyViewModel.setDisconnected(playerNumber)
                findNavController().navigate(action)
            }
        })
    }
}