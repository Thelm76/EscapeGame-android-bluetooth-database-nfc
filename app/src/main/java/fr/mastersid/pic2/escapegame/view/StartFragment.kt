package fr.mastersid.pic2.escapegame.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import fr.mastersid.pic2.escapegame.databinding.FragmentStartBinding

/**
 *Created by Bryan BARRE on 15/10/2021.
 */
//TODO synchronize connected users to DB. If user already connected disable button
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
        _binding.button1.setOnClickListener {
            val playerNumber = 1
            val action = StartFragmentDirections.actionStartFragmentToLobyFragment(playerNumber)
            findNavController().navigate(action)
        }

        _binding.button2.setOnClickListener {
            val playerNumber = 2
            val action = StartFragmentDirections.actionStartFragmentToLobyFragment(playerNumber)
            findNavController().navigate(action)
        }

        _binding.button3.setOnClickListener {
            val playerNumber = 3
            val action = StartFragmentDirections.actionStartFragmentToLobyFragment(playerNumber)
            findNavController().navigate(action)
        }
    }
}