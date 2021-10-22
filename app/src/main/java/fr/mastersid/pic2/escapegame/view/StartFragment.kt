package fr.mastersid.pic2.escapegame.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fr.mastersid.pic2.escapegame.databinding.FragmentStartBinding

/**
 *Created by Bryan BARRE on 15/10/2021.
 */
class StartFragment: Fragment() {
    private lateinit var _binding :FragmentStartBinding
    override fun onCreateView (
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater)
        return _binding.root
    }


    override fun onViewCreated (view : View, savedInstanceState : Bundle?) {
        super.onViewCreated (view , savedInstanceState )

    }
}