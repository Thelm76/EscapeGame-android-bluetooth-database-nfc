package fr.mastersid.pic2.escapegame.model

import fr.mastersid.pic2.escapegame.utils.*
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class ItemsRepository @Inject constructor(
    private val escapeGameFirebase: EGFirebase,
    private val escapeGameNfc: EGNFC,
    private val escapeGameBluetooth : EGBluetooth
    ) {
    private val _lastScan: MutableStateFlow<String> = escapeGameNfc.lastScan
    val lastScan get ()= _lastScan

    private val _itemDesc: MutableStateFlow<String> = MutableStateFlow("")
    val itemDesc get ()= _itemDesc

    fun fetchItemDesc(item: String) {
        escapeGameFirebase.fetchItem(item, object : FirebaseCallback {
            override fun onCallback(value: ItemItem) {
                _itemDesc.value = value.desc
            }
        })
    }
}