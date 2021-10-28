package fr.mastersid.pic2.escapegame.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.mastersid.pic2.escapegame.model.NfcRepository
import javax.inject.Inject

@HiltViewModel
class NfcViewModel @Inject constructor(
    private val repository: NfcRepository
): ViewModel() {
    private val _item: LiveData<String> = repository.lastScan.asLiveData()
    val item get() = _item
}