package fr.mastersid.pic2.escapegame.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import fr.mastersid.pic2.escapegame.databinding.FragmentEnigmaBinding
import fr.mastersid.pic2.escapegame.databinding.FragmentLobyBinding

class EnigmaFragment : Fragment() {
    private lateinit var _binding: FragmentEnigmaBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnigmaBinding.inflate(inflater)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args:EnigmaFragmentArgs by navArgs()
        Toast.makeText(context,"get ready for enigme number "+args.enigma,5).show()
        //TODO db and args



    }
}