package fr.mastersid.pic2.escapegame.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fr.mastersid.pic2.escapegame.databinding.StartFragmentBinding

/**
 *Created by Bryan BARRE on 15/10/2021.
 */
class FragmentStart: Fragment() {
    private lateinit var _binding :StartFragmentBinding
    override fun onCreateView (
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ): View {
        _binding = StartFragmentBinding.inflate(inflater)
        return _binding.root
    }


    override fun onViewCreated (view : View, savedInstanceState : Bundle?) {
        super.onViewCreated (view , savedInstanceState )

    }
}