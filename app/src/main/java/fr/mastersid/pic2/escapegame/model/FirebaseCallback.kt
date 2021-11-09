package fr.mastersid.pic2.escapegame.model

interface FirebaseCallback<T> {
    fun onCallback(value: T)
}