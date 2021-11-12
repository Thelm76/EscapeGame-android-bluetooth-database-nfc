package fr.mastersid.pic2.escapegame.model

import fr.mastersid.pic2.escapegame.utils.EGFirebase
import javax.inject.Inject

class LobbyRepository @Inject constructor(
      private val escapeGameFirebase: EGFirebase,
//    private val escapeGameBluetooth : EGBluetooth
) {
    fun getBluetoothMacAddress(): String {
        return "ok"//escapeGameBluetooth.getBluetoothMacAddress()
    }
}