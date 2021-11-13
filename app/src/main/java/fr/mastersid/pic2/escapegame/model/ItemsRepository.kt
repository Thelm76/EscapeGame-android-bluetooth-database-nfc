package fr.mastersid.pic2.escapegame.model

import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import fr.mastersid.pic2.escapegame.utils.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ItemsRepository @Inject constructor(
    private val escapeGameFirebase: EGFirebase,
    escapeGameNfc: EGNFC,
    private val escapeGameBluetooth : EGBluetooth
    )
{
    private val _item1: MutableStateFlow<Item> = MutableStateFlow(Item())
    val item1 get ()= _item1

    private val _item2: MutableStateFlow<Item> = MutableStateFlow(Item())
    val item2 get ()= _item2

    private val _item3: MutableStateFlow<Item> = MutableStateFlow(Item())
    val item3 get ()= _item3

    private val _messageBT: MutableStateFlow<String> = escapeGameBluetooth.message
    val messageBT get() = _messageBT

    private val _itemId: MutableStateFlow<String> = escapeGameNfc.lastScan
    val itemId get ()= _itemId.onEach { value->
        if (value.isNotBlank())
        escapeGameFirebase.fetchFrom(
            EGFirebase.DB.ITEMS,
            value,
            object: FirebaseCallback<EGFirebase.ItemItem> {
                override fun onCallback(value: EGFirebase.ItemItem) {
                    FirebaseStorage.getInstance()
                        .getReferenceFromUrl(value.img)
                        .getBytes(Long.MAX_VALUE).addOnCompleteListener{
                            _item1.value = Item(value.id,value.desc,it.result)
                        }
                }
            }
        )
    }

    private var _randomEnigma: MutableStateFlow<Int> = MutableStateFlow(-1)
    val randomEnigma get() = _randomEnigma

    fun randomizeEnigma(){
        _randomEnigma.value=((1..6).random())
    }

    fun sendRequestItem(user: String) {
        Log.d("hello","repository item")
        escapeGameFirebase.fetchFrom(EGFirebase.DB.USERS, user, object: FirebaseCallback<EGFirebase.UsersItem> {
            override fun onCallback(value: EGFirebase.UsersItem) {
                escapeGameBluetooth.writeTo(value.mac, "fetch_item")
            }
        })
    }

    fun respondItem(user: Int, item: String){
        escapeGameFirebase.fetchFrom(EGFirebase.DB.USERS, "master", object: FirebaseCallback<EGFirebase.UsersItem> {
            override fun onCallback(value: EGFirebase.UsersItem) {
                escapeGameBluetooth.respond("item$user:$item")
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
                                    2->_item2.value = Item(value.id,value.desc,it.result)
                                    3->_item3.value = Item(value.id,value.desc,it.result)
                                }
                            }
                    }
                }
            )
    }

    data class Item(
        val id: String="",
        var desc: String="no item",
        val img: ByteArray?=null
    )
}