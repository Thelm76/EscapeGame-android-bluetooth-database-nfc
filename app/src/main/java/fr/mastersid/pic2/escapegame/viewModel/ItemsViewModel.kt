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
    private val itemsRepository: ItemsRepository
    ): ViewModel() {
    private val _itemNFC: LiveData<String> = itemsRepository.lastScan.asLiveData()
    val itemNFC get() = _itemNFC

    private val _message = itemsRepository.message.asLiveData()
    val message get() = _message


    private var _playerNumber = -1


    private val _itemDesc: LiveData<String> = itemsRepository.itemDesc.asLiveData()
    val itemDesc get() = _itemDesc

    private val _itemImg: LiveData<ByteArray?> = itemsRepository.itemImg.asLiveData()
    val itemImg get() = _itemImg

    fun updateItem() {
        if (_itemNFC.value.toString().isNotEmpty())
            itemsRepository.fetchItem(_itemNFC.value.toString())
    }

    fun setPlayerNumber(playerNumber: Int) {
        _playerNumber = playerNumber
    }

    fun sendRequestItem() {
        Log.d("hello","item viewModel")

 //       itemsRepository.sendRequestItem("player2")
        itemsRepository.sendRequestItem("player3")
    }

    fun sendItem(item : String, playerNumber: Int ){
        when (playerNumber){
            2,3->itemsRepository.sendItem("master",item)
        }
    }
}