package fr.mastersid.pic2.escapegame.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.mastersid.pic2.escapegame.model.ItemsRepository
import javax.inject.Inject

@HiltViewModel
class ItemsViewModel @Inject constructor(
    private val itemsRepository: ItemsRepository,
    private val firebaseRepository: ItemsRepository
): ViewModel() {
    private val _itemNFC: LiveData<String> = itemsRepository.lastScan.asLiveData()
    val itemNFC get() = _itemNFC

    private val _itemDesc: LiveData<String> = firebaseRepository.itemDesc.asLiveData()
    val itemDesc get() = _itemDesc


    protected val _randomEnigma:LiveData<Int> = itemsRepository.randomEnigma.asLiveData()
    val randomEnigma get() = _randomEnigma

    fun updateRandomEnigma(){
        itemsRepository.randomizeEnigma()
    }

    fun updateItem() {
        firebaseRepository.fetchItem(_itemNFC.value.toString())
    }
}