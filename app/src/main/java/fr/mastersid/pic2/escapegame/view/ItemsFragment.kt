package fr.mastersid.pic2.escapegame.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.navArgs
import fr.mastersid.pic2.escapegame.R
import fr.mastersid.pic2.escapegame.databinding.FragmentItemsBinding
import fr.mastersid.pic2.escapegame.viewModel.ItemsViewModel

/**
 *Created by Bryan BARRE on 15/10/2021.
 */
//TODO Add Fetch and Fuse buttons and send enigma code to next view
class ItemsFragment : Fragment() {
    private lateinit var _binding: FragmentItemsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemsBinding.inflate(inflater)
        return _binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: ItemsFragmentArgs by navArgs()
        val itemsViewModel: ItemsViewModel by hiltNavGraphViewModels(R.id.nav_graph)

        itemsViewModel.itemNFC.observe(this){ item ->
            itemsViewModel.updateItem()
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


        itemsViewModel.itemDesc.observe(this){ desc ->
            _binding.itemName.text=desc
        }
    }

}






