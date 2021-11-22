package fr.mastersid.pic2.escapegame.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.mastersid.pic2.escapegame.model.UsersRepository
import javax.inject.Inject

@HiltViewModel
class LobbyViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
) : ViewModel() {
    private val _usersConnected : LiveData<List<Boolean>> = usersRepository.usersConnected.asLiveData()
    val usersConnected get() = _usersConnected

    fun macAddress(): String {
        //Log.d("hello","ViewModel " + lobbyRepository.getBluetoothMacAddress())
        return usersRepository.getBluetoothMacAddress()
    }

    fun saveMacAddress(mac: String, playerNumber: Int){
        usersRepository.saveMacAddress(mac,
            when (playerNumber){
                1 -> "master"
                2 -> "player2"
                3 -> "player3"
                else -> ""
            })
    }

    fun setDisconnected (user: Int){
        usersRepository.setConnected(
            when (user) {
                1-> "master"
                2-> "player2"
                3-> "player3"
                else -> ""
            },
            false
        )
    }
}