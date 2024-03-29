package fr.mastersid.pic2.escapegame.model

import fr.mastersid.pic2.escapegame.utils.EGFirebase
import fr.mastersid.pic2.escapegame.utils.FirebaseCallback
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


class EnigmaRepository @Inject constructor(
    private val escapeGameFirebase: EGFirebase
    ){

    private var _enigma: MutableStateFlow<EGFirebase.EnigmaItem> = MutableStateFlow(EGFirebase.EnigmaItem())
    val enigma get() = _enigma

    fun fetchEnigma(enigmaName: String) {
        escapeGameFirebase.fetchFrom(EGFirebase.DB.ENIGMAS, enigmaName, object:
            FirebaseCallback<EGFirebase.EnigmaItem> {
            override fun onCallback(value: EGFirebase.EnigmaItem) {
                _enigma.value = value
            }
        })
    }
}



