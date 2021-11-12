package fr.mastersid.pic2.escapegame.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.mastersid.pic2.escapegame.model.LobbyRepository
import fr.mastersid.pic2.escapegame.model.UsersRepository
import javax.inject.Inject

@HiltViewModel
class LobbyViewModel @Inject constructor(
    private val lobbyRepository: LobbyRepository,
    private val usersRepository: UsersRepository,
) : ViewModel() {
    private val _usersConnected : LiveData<List<Boolean>> = usersRepository.usersConnected.asLiveData()
    val usersConnected get() = _usersConnected

    fun MacAddress() : String{
        val Mac = lobbyRepository.getBluetoothMacAddress()
        //Log.d("hello","ViewModel " + lobbyRepository.getBluetoothMacAddress())
        return Mac
    }

    fun SaveMacAddress(mac: String, playerNumber: Int){
        lobbyRepository.saveMacAddress(mac,
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