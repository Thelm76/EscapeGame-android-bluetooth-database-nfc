package fr.mastersid.pic2.escapegame.model

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.mastersid.pic2.escapegame.utils.EGNFC
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


class NfcRepository @Inject constructor(
    private val escapeGameNFC: EGNFC
){
    private val _lastScan = escapeGameNFC.lastScan
    val lastScan get() = _lastScan
}