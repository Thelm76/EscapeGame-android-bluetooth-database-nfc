package fr.mastersid.pic2.escapegame.model

import android.content.ContentValues
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import fr.mastersid.pic2.escapegame.utils.EGFirebase
import fr.mastersid.pic2.escapegame.utils.EGNFC
import fr.mastersid.pic2.escapegame.utils.ItemItem
import fr.mastersid.pic2.escapegame.utils.UsersItem
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class ItemsRepository @Inject constructor(
    private val escapeGameFirebase: EGFirebase,
    escapeGameNfc: EGNFC
    ) {

    private val _lastScan: MutableStateFlow<String> = escapeGameNfc.lastScan
    val lastScan get ()= _lastScan

    private val _itemDesc: MutableStateFlow<String> = MutableStateFlow("")
    val itemDesc get ()= _itemDesc

    fun fetchItem(itemName: String) {
        escapeGameFirebase.fetchFrom("items", itemName, object: FirebaseCallback<ItemItem> {
            override fun onCallback(value: ItemItem) {
                _itemDesc.value = value.desc
            }
        })
    }
}