package fr.mastersid.pic2.escapegame.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import fr.mastersid.pic2.escapegame.databinding.FragmentNfcBinding
import fr.mastersid.pic2.escapegame.viewModel.NfcViewModel

/**
 *Created by Bryan BARRE on 15/10/2021.
 */
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

        val nfcViewModel: NfcViewModel by viewModels()

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
    }

}






