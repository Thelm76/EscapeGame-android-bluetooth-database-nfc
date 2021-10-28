package fr.mastersid.pic2.escapegame.model

import fr.mastersid.pic2.escapegame.utils.EGFirebase
import fr.mastersid.pic2.escapegame.utils.EGNFC
import fr.mastersid.pic2.escapegame.utils.UsersItem
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class UsersRepository @Inject constructor(
    private val escapeGameFirebase: EGFirebase
) {
    private val _usersItems: MutableStateFlow<List<UsersItem>> = escapeGameFirebase.usersItems
    val usersItems get ()= _usersItems
}