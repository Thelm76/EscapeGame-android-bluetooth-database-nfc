package fr.mastersid.pic2.escapegame.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import fr.mastersid.pic2.escapegame.R
import fr.mastersid.pic2.escapegame.MainActivity
import fr.mastersid.pic2.escapegame.databinding.FragmentItemsBinding
import fr.mastersid.pic2.escapegame.viewModel.ItemsViewModel

class ItemsFragment : Fragment() {
    private lateinit var _binding: FragmentItemsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemsBinding.inflate(inflater)

        (activity as MainActivity).supportActionBar?.title = "Items"
        return _binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: ItemsFragmentArgs by navArgs()
        val itemsViewModel: ItemsViewModel by hiltNavGraphViewModels(R.id.nav_graph)

        itemsViewModel.setPlayerNumber(args.playerNumber)

        //Needed to permit repository flow update...
        itemsViewModel.itemId.observe(this) {}

        itemsViewModel.item1.observe(this) { item ->
            _binding.itemDesc.text = item.desc
            item.img?.let {
                Glide.with(this).load(it).into(_binding.imageviewItem)
                Glide.with(this).load(it).into(_binding.imageviewItem1)
            }
        }

        itemsViewModel.item2.observe(this) { item ->
            _binding.itemDesc.text = item.desc
            item.img?.let {
                Glide.with(this).load(it).into(_binding.imageviewItem2)
            }
        }

        itemsViewModel.item3.observe(this) { item ->
            item.img?.let {
                Glide.with(this).load(it).into(_binding.imageviewItem3)
            }
        }

        _binding.buttonFusion.isVisible=(args.playerNumber==1)
        _binding.buttonFusion.setOnClickListener {
            itemsViewModel.sendRequestItem(2)
            _binding.buttonSuivant.isEnabled = true
        }

        _binding.buttonSuivant.setOnClickListener {
            itemsViewModel.updateRandomEnigma()
        }

        itemsViewModel.randomEnigma.observe(this) { value ->
            //Toast.makeText(context,"la question sera $value",5).show()
            if (value != -1) {
                val action = ItemsFragmentDirections.actionNfcFragmentToEnigmaFragment(value)
                findNavController().navigate(action)
            }
        }

        itemsViewModel.messageBT.observe(this) { value ->
            when {
                value == "fetch_item" -> {
                    Log.d(
                        "ItemView",
                        "fetch item detected, sending item ${itemsViewModel.itemId.value}"
                    )
                    itemsViewModel.sendItemAs(itemsViewModel.itemId.value ?: "", args.playerNumber)
                }
                value.startsWith("item2:") -> {
                    itemsViewModel.updateItem(2, value.substring(6))
                }
                value.startsWith("item3:") -> {
                    itemsViewModel.updateItem(3, value.substring(6))
                }
            }
        }
    }
}