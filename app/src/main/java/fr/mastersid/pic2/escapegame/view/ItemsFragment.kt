package fr.mastersid.pic2.escapegame.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
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

        itemsViewModel.setPlayerNumber(args.playerNumber)

        itemsViewModel.itemNFC.observe(this){ item ->
            itemsViewModel.updateItem()
        }


        itemsViewModel.itemDesc.observe(this){ desc ->
            _binding.itemName.text=desc
        }



        _binding.buttonFusion.setOnClickListener(){
            Log.d("hello","item fragment")
            itemsViewModel.sendRequestItem()

        }

        itemsViewModel.itemImg.observe(this){img ->
            img?.let {
                Glide.with(this).load(it).into(_binding.imageviewItem)
            }
        }
    }
}