package fr.mastersid.pic2.escapegame.model

import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import fr.mastersid.pic2.escapegame.utils.*
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

class ItemsRepository @Inject constructor(
    private val escapeGameFirebase: EGFirebase,
    escapeGameNfc: EGNFC,
    private val escapeGameBluetooth : EGBluetooth
    ) {

    private val _lastScan: MutableStateFlow<String> = escapeGameNfc.lastScan
    val lastScan get ()= _lastScan

    private val _itemDesc: MutableStateFlow<String> = MutableStateFlow("")
    val itemDesc get ()= _itemDesc

    private val _itemImg: MutableStateFlow<ByteArray?> = MutableStateFlow(null)
    val itemImg get ()= _itemImg

    fun fetchItem(itemName: String) {
        escapeGameFirebase.fetchFrom(EGFirebase.DB.ITEMS, itemName, object: FirebaseCallback<EGFirebase.ItemItem> {
            override fun onCallback(value: EGFirebase.ItemItem) {
                _itemDesc.value = value.desc
                val storage = FirebaseStorage.getInstance()
                val storageRef = storage.getReferenceFromUrl(value.img)

                storageRef.getBytes(Long.MAX_VALUE).addOnCompleteListener {
                    _itemImg.value = it.result
                }
            }
        })
    }

    fun sendRequestItem(user: String) {
        Log.d("hello","repository item")
        escapeGameFirebase.fetchFrom(EGFirebase.DB.USERS, user, object: FirebaseCallback<EGFirebase.UsersItem> {
            override fun onCallback(value: EGFirebase.UsersItem) {

                escapeGameBluetooth.writeTo(value.mac, "fetch_item")
                Log.d("hello", value.mac)
                Log.d("hello","fetc item")
                Log.d("hello","repository 2 item")
            }
        })
    }
}