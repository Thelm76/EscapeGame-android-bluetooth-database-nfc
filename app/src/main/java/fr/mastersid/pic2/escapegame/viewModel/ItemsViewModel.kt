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
    ): ViewModel()
{
    private val _itemId: LiveData<String> = itemsRepository.itemId.asLiveData()
    val itemId get() = _itemId

    private val _messageBT: LiveData<String> = itemsRepository.messageBT.asLiveData()
    val messageBT get() = _messageBT

    private val _itemFinal: LiveData<ItemsRepository.Item> = itemsRepository.itemFinal.asLiveData()
    val itemFinal get() = _itemFinal

    private val _item1: LiveData<ItemsRepository.Item> = itemsRepository.item1.asLiveData()
    val item1 get() = _item1

    private val _item2: LiveData<ItemsRepository.Item> = itemsRepository.item2.asLiveData()
    val item2 get() = _item2

    private val _item3: LiveData<ItemsRepository.Item> = itemsRepository.item3.asLiveData()
    val item3 get() = _item3

    private val _randomEnigma:LiveData<Int> = itemsRepository.randomEnigma.asLiveData()
    val randomEnigma get() = _randomEnigma

    private val _mergeable:LiveData<Boolean> = itemsRepository.mergeable.asLiveData()
    val mergeable:LiveData<Boolean> get()= _mergeable

    private var _playerNumber = -1

    fun setPlayerNumber(playerNumber: Int) {
        _playerNumber = playerNumber
    }

    fun sendRequestItem(destPlayer:Int) {
        when (destPlayer){
            2-> itemsRepository.sendRequestItem("player2")
            3-> itemsRepository.sendRequestItem("player3")
        }
    }

    fun sendItemAs(item : String, playerNumber: Int ){
        when (playerNumber){
            1->{
                //itemsRepository.sendItem(2,item)
                //itemsRepository.sendItem(3,item)
            }
            2->{
                //itemsRepository.sendItem(3,item)
                itemsRepository.respondItem(2,item)
            }
            3->{
                //itemsRepository.sendItem(2,item)
                itemsRepository.respondItem(3,item)
            }
        }
    }

    fun updateItem(player: Int, item: String) {
        if (_itemFinal.value!!.id.isEmpty())
            if (player == 1) itemsRepository.fetchItem(item, _playerNumber)
            else itemsRepository.fetchItem(item, player)
    }

    fun updateRandomEnigma(){
        itemsRepository.randomizeEnigma()
    }

    fun cycleItem() {
        itemsRepository.setItem(
            when(_itemId.value) {
                "obj1" -> "obj2"
                "obj2" -> "obj3"
                "obj3" -> ""
                else -> "obj1"
            }
        )
    }
}