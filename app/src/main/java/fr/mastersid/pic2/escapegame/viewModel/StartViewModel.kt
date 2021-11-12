package fr.mastersid.pic2.escapegame.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData

import dagger.hilt.android.lifecycle.HiltViewModel
import fr.mastersid.pic2.escapegame.model.UsersRepository
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val usersRepository : UsersRepository
    ) : ViewModel()
{

    private val _connected : LiveData<List<Boolean>> = usersRepository.usersConnected.asLiveData()
    val connected get() = _connected

    fun setConnected (user:Int, connected: Boolean){
        usersRepository.setConnected(
            when (user) {
                1-> "master"
                2-> "player2"
                3-> "player3"
                else -> ""
            },
            connected
        )
    }
}