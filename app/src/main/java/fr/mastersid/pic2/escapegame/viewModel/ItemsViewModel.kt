package fr.mastersid.pic2.escapegame.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.mastersid.pic2.escapegame.model.ItemsRepository
import javax.inject.Inject

@HiltViewModel
class ItemsViewModel @Inject constructor(
    private val itemsRepository: ItemsRepository
    ): ViewModel() {
    private val _itemNFC: LiveData<String> = itemsRepository.lastScan.asLiveData()
    val itemNFC get() = _itemNFC

    private val _itemDesc: LiveData<String> = itemsRepository.itemDesc.asLiveData()
    val itemDesc get() = _itemDesc

    private val _itemImg: LiveData<ByteArray?> = itemsRepository.itemImg.asLiveData()
    val itemImg get() = _itemImg

    fun updateItem() {
        if (_itemNFC.value.toString().isNotEmpty())
            itemsRepository.fetchItem(_itemNFC.value.toString())
    }
}