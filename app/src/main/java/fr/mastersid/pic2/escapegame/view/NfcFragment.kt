package fr.mastersid.pic2.escapegame.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import fr.mastersid.pic2.escapegame.databinding.FragmentNfcBinding
import fr.mastersid.pic2.escapegame.viewModel.NfcViewModel

//TODO Add Fetch and Fuse buttons and send enigma code to next view
@AndroidEntryPoint
class NfcFragment : Fragment() {
    private lateinit var _binding: FragmentNfcBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNfcBinding.inflate(inflater)
        return _binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: NfcFragmentArgs by navArgs()
        val nfcViewModel: NfcViewModel by viewModels()
        var enigma=0

        nfcViewModel.item.observe(this) { item ->
            try {
                _binding.imageviewItem.setImageDrawable(
                    getDrawable(
                        this.requireContext(),
                        resources.getIdentifier(item, "drawable", context?.packageName)
                    )
                )
            } catch (e: Exception) {
                _binding.imageviewItem.setImageDrawable(
                    getDrawable(
                        this.requireContext(),
                        resources.getIdentifier("no_item", "drawable", context?.packageName)
                    )
                )
            }
        }

        _binding.buttonFusion.setOnClickListener{
            enigma=1
            Toast.makeText(context,"la question sera "+enigma,5)
            _binding.buttonSuivant.isClickable = true
            _binding.buttonSuivant.isEnabled = true
        }

        _binding.buttonSuivant.setOnClickListener{
            val action = NfcFragmentDirections.actionNfcFragmentToEnigmaFragment(enigma)
            findNavController().navigate(action)
        }
    }
}






