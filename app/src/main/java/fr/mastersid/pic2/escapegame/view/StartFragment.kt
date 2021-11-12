package fr.mastersid.pic2.escapegame.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import fr.mastersid.pic2.escapegame.databinding.FragmentStartBinding
import fr.mastersid.pic2.escapegame.viewModel.StartViewModel

/**
 *Created by Bryan BARRE on 15/10/2021.
 */
//TODO synchronize connected users to DB. If user already connected disable button
@AndroidEntryPoint
class StartFragment : Fragment() {
    private lateinit var _binding: FragmentStartBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater)
        return _binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val startViewModel : StartViewModel by viewModels()

        startViewModel.connected.observe(this){
           value ->
            if (value.isNotEmpty()){
                _binding.button1.isEnabled=!value.get(0)
                _binding.button2.isEnabled=!value.get(1)
                _binding.button3.isEnabled=!value.get(2)
            }

        }

        _binding.button1.setOnClickListener {
            val playerNumber = 1
            startViewModel.setConnected(playerNumber)
            val action = StartFragmentDirections.actionStartFragmentToLobbyFragment(playerNumber)
            findNavController().navigate(action)



        }

        _binding.button2.setOnClickListener {
            val playerNumber = 2
            startViewModel.setConnected(playerNumber)

            val action = StartFragmentDirections.actionStartFragmentToLobbyFragment(playerNumber)
            findNavController().navigate(action)
        }

        _binding.button3.setOnClickListener {
            val playerNumber = 3
            startViewModel.setConnected(playerNumber)
            val action = StartFragmentDirections.actionStartFragmentToLobbyFragment(playerNumber)
            findNavController().navigate(action)
        }
    }
}