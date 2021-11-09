package fr.mastersid.pic2.escapegame.model

import fr.mastersid.pic2.escapegame.utils.EGNFC
import javax.inject.Inject


class NfcRepository @Inject constructor(
    private val escapeGameNFC: EGNFC
) {
    private val _lastScan = escapeGameNFC.lastScan
    val lastScan get() = _lastScan
}