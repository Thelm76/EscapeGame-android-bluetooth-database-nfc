package fr.mastersid.pic2.escapegame.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import fr.mastersid.pic2.escapegame.MainActivity
import fr.mastersid.pic2.escapegame.databinding.FragmentEnigmaBinding
import fr.mastersid.pic2.escapegame.viewModel.EnigmaViewModel

@AndroidEntryPoint
class EnigmaFragment : Fragment() {
    private lateinit var _binding: FragmentEnigmaBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnigmaBinding.inflate(inflater)
        (activity as MainActivity).supportActionBar?.title = "Enigma"
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args:EnigmaFragmentArgs by navArgs()
        Toast.makeText(context,"get ready for enigma number "+args.enigma,5).show()
        val enigmaViewModel: EnigmaViewModel by viewModels()
        //TODO db and args
        enigmaViewModel.updateEnigma(args.enigma)

        enigmaViewModel.enigma.observe(this){enigma->
            _binding.question.text= enigma.question
            _binding.btnAnswer1.text = enigma.answers.aid1
            _binding.btnAnswer2.text = enigma.answers.aid2
            _binding.btnAnswer3.text = enigma.answers.aid3
            _binding.btnAnswer4.text = enigma.answers.aid4

            _binding.btnAnswer1.setOnClickListener{
                if (enigma.answer =="aid1"){
                    Toast.makeText(context,"gg",10).show()
                    _binding.imageView6.isVisible=true
                }
            }
            _binding.btnAnswer2.setOnClickListener{
                if (enigma.answer=="aid2"){
                    Toast.makeText(context,"gg",10).show()
                    _binding.imageView6.isVisible=true
                }
            }
            _binding.btnAnswer3.setOnClickListener{
                if (enigma.answer =="aid3"){
                    Toast.makeText(context,"gg",10).show()
                    _binding.imageView6.isVisible=true
                }
            }
            _binding.btnAnswer4.setOnClickListener{
                if (enigma.answer =="aid4"){
                    Toast.makeText(context,"gg",10).show()
                    _binding.imageView6.isVisible=true
                }
            }
        }
    }
}