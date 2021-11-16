package fr.mastersid.pic2.escapegame.model

import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import fr.mastersid.pic2.escapegame.utils.EGBluetooth
import fr.mastersid.pic2.escapegame.utils.EGFirebase
import fr.mastersid.pic2.escapegame.utils.EGNFC
import fr.mastersid.pic2.escapegame.utils.FirebaseCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ItemsRepository @Inject constructor(
    private val escapeGameFirebase: EGFirebase,
    escapeGameNfc: EGNFC,
    private val escapeGameBluetooth : EGBluetooth
    )
{
    private val _itemFinal: MutableStateFlow<Item> = MutableStateFlow(Item())
    val itemFinal get ()= _itemFinal

    private val _item1: MutableStateFlow<Item> = MutableStateFlow(Item())
    val item1 get ()= _item1

    private val _item2: MutableStateFlow<Item> = MutableStateFlow(Item())
    val item2 get ()= _item2

    private val _item3: MutableStateFlow<Item> = MutableStateFlow(Item())
    val item3 get ()= _item3

    private val _mergeable: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val mergeable get ()= _mergeable.onEach { value ->
        if (value) {
            fetchItem("obj1+2+3",0)
        } else {
            fetchItem("",0)
        }
    }

    private val _messageBT: MutableStateFlow<String> = escapeGameBluetooth.message
    val messageBT get() = _messageBT

    private val _itemId: MutableStateFlow<String> = escapeGameNfc.lastScan
    val itemId get ()= _itemId.onEach { value->
        fetchItem(value, 1)
    }

    private var _randomEnigma: MutableStateFlow<Int> = MutableStateFlow(-1)
    val randomEnigma get() = _randomEnigma

    fun randomizeEnigma(){
        _randomEnigma.value=((1..6).random())
    }

    fun sendItem(from: Int, user: String, item: String) {
        escapeGameFirebase.fetchFrom(EGFirebase.DB.USERS, user, object: FirebaseCallback<EGFirebase.UsersItem> {
            override fun onCallback(value: EGFirebase.UsersItem) {
                escapeGameBluetooth.writeTo(value.mac, "item$from:$item")
            }
        })
    }

    fun respondItem(from: Int, item: String){
        escapeGameFirebase.fetchFrom(EGFirebase.DB.USERS, "master", object: FirebaseCallback<EGFirebase.UsersItem> {
            override fun onCallback(value: EGFirebase.UsersItem) {
                escapeGameBluetooth.respond("item$from:$item")
            }
        })
    }

    fun fetchItem(item: String, itemNb: Int) {
        if (item.isNotBlank())
            escapeGameFirebase.fetchFrom(
                EGFirebase.DB.ITEMS,
                item,
                object: FirebaseCallback<EGFirebase.ItemItem> {
                    override fun onCallback(value: EGFirebase.ItemItem) {
                        FirebaseStorage.getInstance()
                            .getReferenceFromUrl(value.img)
                            .getBytes(Long.MAX_VALUE).addOnCompleteListener{
                                when (itemNb) {
                                    1->_item1.value = Item(value.id,value.desc,it.result)
                                    2->_item2.value = Item(value.id,value.desc,it.result)
                                    3->_item3.value = Item(value.id,value.desc,it.result)
                                    0->_itemFinal.value = Item(value.id,value.desc,it.result)
                                }
                                val i1 = _item1.value.id
                                val i2 = _item2.value.id
                                val i3 = _item3.value.id
                                _mergeable.value = (i1.isNotBlank()&&i2.isNotBlank()&&i3.isNotBlank()) && (i1!=i2 && i2!=i3 && i3!=i1)
                            }
                    }
                }
            )
        else
            when (itemNb) {
                1->_item1.value = Item()
                2->_item2.value = Item()
                3->_item3.value = Item()
                0->_itemFinal.value = Item()
            }
    }

    fun setItem(itemId: String) {
        _itemId.value=itemId
    }

    data class Item(
        val id: String="",
        var desc: String="no item",
        val img: ByteArray?=null
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Item

            if (id != other.id) return false

            return true
        }

        override fun hashCode(): Int {
            return id.hashCode()
        }
    }
}