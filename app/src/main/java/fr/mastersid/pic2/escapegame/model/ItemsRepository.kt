package fr.mastersid.pic2.escapegame.model

import fr.mastersid.pic2.escapegame.utils.EGFirebase
import fr.mastersid.pic2.escapegame.utils.ItemItem
import fr.mastersid.pic2.escapegame.utils.UsersItem
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class ItemsRepository @Inject constructor(
    private val escapeGameFirebase: EGFirebase
) {
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