package fr.mastersid.pic2.escapegame.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import fr.mastersid.pic2.escapegame.databinding.FragmentLobyBinding

/**
 *Created by Bryan BARRE on 15/10/2021.
 */
class LobyFragment : Fragment() {
    private lateinit var _binding: FragmentLobyBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLobyBinding.inflate(inflater)
        return _binding.root
    }


    override fun onViewCreated (view : View, savedInstanceState : Bundle?) {
        super.onViewCreated (view , savedInstanceState )
        val args : LobyFragmentArgs by navArgs()

        if (args.playerNumber==1){
            _binding.switch1.isChecked=true
            _binding.player1ProgressBar.isVisible=false
        }
        if (args.playerNumber==2){
            _binding.switch2.isChecked=true
            _binding.player2ProgressBar.isVisible=false
        //////////////////////////////////////////////////for debug
            _binding.switch1.isChecked=true
            _binding.switch3.isChecked=true
        ////////////////////////////////////////////////////////////

        }
        if (args.playerNumber==3){
            _binding.switch3.isChecked=true
            _binding.player3ProgressBar.isVisible=false
        }
        if (_binding.switch1.isChecked && _binding.switch2.isChecked &&_binding.switch3.isChecked){
            _binding.playButton.isClickable=true
            _binding.playButton.isEnabled=true
        }

        _binding.playButton.setOnClickListener{
            val playerNumbers=args.playerNumber
            val action = LobyFragmentDirections.actionLobyFragmentToNfcFragment(playerNumbers)
            findNavController().navigate(action)
        }

    }
}