package fr.mastersid.pic2.escapegame.model

import fr.mastersid.pic2.escapegame.utils.ItemItem


interface FirebaseCallback {
    fun onCallback(value: ItemItem)
}