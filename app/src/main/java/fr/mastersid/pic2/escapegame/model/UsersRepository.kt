package fr.mastersid.pic2.escapegame.model

import android.content.ContentValues
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import fr.mastersid.pic2.escapegame.utils.EGFirebase
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class UsersRepository @Inject constructor(
    escapeGameFirebase: EGFirebase
) {
    private val _usersItems: MutableStateFlow<List<EGFirebase.UsersItem>> = MutableStateFlow(emptyList())
    val usersItems get ()= _usersItems


    init {
        escapeGameFirebase.addDBListener(EGFirebase.DB.USERS, object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _usersItems.value = snapshot.children.map { dataSnapshot ->
                    dataSnapshot.getValue(EGFirebase.UsersItem::class.java)!!
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "loadPost:onCancelled", error.toException())
            }
        })
    }
}