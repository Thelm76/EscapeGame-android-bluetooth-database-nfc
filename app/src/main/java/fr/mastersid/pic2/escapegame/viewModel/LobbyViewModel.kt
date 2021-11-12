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

    fun getMacAdress(){

    }

    fun setDisconnected (user: Int){
        usersRepository.setDisconnected(
            when (user) {
                1-> "master"
                2-> "player2"
                3-> "player3"
                else -> ""
                        }
            ,false)
    }
    private val _connected : LiveData<List<Boolean>> = usersRepository.usersConnected.asLiveData()
    val connected get() = _connected


}