package fr.mastersid.pic2.escapegame.viewModel

import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.mastersid.pic2.escapegame.model.LobbyRepository
import javax.inject.Inject

@HiltViewModel
class LobbyViewModel @Inject constructor(
    lobbyRepository: LobbyRepository
) : ViewModel() {
    fun getMacAdress(){

    }

}