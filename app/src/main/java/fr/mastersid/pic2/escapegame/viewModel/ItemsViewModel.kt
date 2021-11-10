package fr.mastersid.pic2.escapegame.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.mastersid.pic2.escapegame.model.ItemsRepository
import javax.inject.Inject

@HiltViewModel
class ItemsViewModel @Inject constructor(
    itemsRepository: ItemsRepository,
    private val firebaseRepository: ItemsRepository
): ViewModel() {
    private val _itemNFC: LiveData<String> = itemsRepository.lastScan.asLiveData()
    val itemNFC get() = _itemNFC

    private val _itemDesc: LiveData<String> = firebaseRepository.itemDesc.asLiveData()
    val itemDesc get() = _itemDesc

    private val _itemImg: LiveData<ByteArray?> = firebaseRepository.itemImg.asLiveData()
    val itemImg get() = _itemImg

    fun updateItem() {
        if (_itemNFC.value.toString().isNotEmpty())
            firebaseRepository.fetchItem(_itemNFC.value.toString())
    }
}