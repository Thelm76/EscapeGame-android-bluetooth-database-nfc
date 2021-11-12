package fr.mastersid.pic2.escapegame.model

import fr.mastersid.pic2.escapegame.utils.EGBluetooth
import fr.mastersid.pic2.escapegame.utils.EGFirebase
import fr.mastersid.pic2.escapegame.utils.EGNFC
import javax.inject.Inject

class LobbyRepository @Inject constructor(
    private val escapeGameFirebase: EGFirebase,
    private val escapeGameBluetooth : EGBluetooth,
) {
    fun getBluetoothMacAddress(): String {
        return escapeGameBluetooth.getBluetoothMacAddress()
    }

    fun saveMacAddress(mac: String, user: String){
        if (user.isNotBlank()){
            escapeGameFirebase.writeAttribute(EGFirebase.DB.USERS, user, "mac", mac)
        }
    }

    fun sendItem(mac: String, item: String){
        escapeGameBluetooth.writeTo(mac,item);
    }
}