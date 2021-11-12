package fr.mastersid.pic2.escapegame.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.mastersid.pic2.escapegame.model.EnigmaRepository
import fr.mastersid.pic2.escapegame.utils.EGFirebase
import javax.inject.Inject

@HiltViewModel
class EnigmaViewModel @Inject constructor(
    private val enigmaRepository: EnigmaRepository
): ViewModel() {
    private val _enigma: LiveData<EGFirebase.EnigmaItem> = enigmaRepository.enigma.asLiveData()
    val enigma get() = _enigma

    fun updateEnigma(enigma: Int) {
        Log.d("-*-*-*-*-*-*-","88888888888")
        enigmaRepository.fetchEnigma("question$enigma")
    }
}