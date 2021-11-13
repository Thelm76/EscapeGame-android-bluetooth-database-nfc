package fr.mastersid.pic2.escapegame.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import fr.mastersid.pic2.escapegame.MainActivity
import fr.mastersid.pic2.escapegame.databinding.FragmentStartBinding
import fr.mastersid.pic2.escapegame.viewModel.StartViewModel

@AndroidEntryPoint
class StartFragment : Fragment() {
    private lateinit var _binding: FragmentStartBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater)
        (activity as MainActivity).supportActionBar?.title = "EscapeGame"
        return _binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val startViewModel : StartViewModel by viewModels()

        startViewModel.connected.observe(this){
           value ->
            if (value.isNotEmpty()){
                _binding.button1.isEnabled=!value[0]
                _binding.button2.isEnabled=!value[1]
                _binding.button3.isEnabled=!value[2]
            }

        }

        //TODO: for debug purpose, please remove on final version
        _binding.imageView.setOnLongClickListener{
            startViewModel.setConnected(1,false)
            startViewModel.setConnected(2,false)
            startViewModel.setConnected(3,false)
            true
        }

        _binding.button1.setOnClickListener {
            val playerNumber = 1
            startViewModel.setConnected(playerNumber,true)
            val action = StartFragmentDirections.actionStartFragmentToLobbyFragment(playerNumber)
            findNavController().navigate(action)
        }

        _binding.button2.setOnClickListener {
            val playerNumber = 2
            startViewModel.setConnected(playerNumber,true)

            val action = StartFragmentDirections.actionStartFragmentToLobbyFragment(playerNumber)
            findNavController().navigate(action)
        }

        _binding.button3.setOnClickListener {
            val playerNumber = 3
            startViewModel.setConnected(playerNumber,true)
            val action = StartFragmentDirections.actionStartFragmentToLobbyFragment(playerNumber)
            findNavController().navigate(action)
        }
    }
}