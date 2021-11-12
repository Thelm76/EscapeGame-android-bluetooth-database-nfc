package fr.mastersid.pic2.escapegame.model

import android.content.ContentValues
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import fr.mastersid.pic2.escapegame.utils.EGFirebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.forEach
import javax.inject.Inject

class UsersRepository @Inject constructor(
    private val escapeGameFirebase: EGFirebase
) {
    private val _usersItems: MutableStateFlow<List<EGFirebase.UsersItem>> = MutableStateFlow(emptyList())

    private val _usersConnected : MutableStateFlow<List<Boolean>> = MutableStateFlow(emptyList())
    val usersConnected get():StateFlow<List<Boolean>> = _usersConnected

    init {
        escapeGameFirebase.addDBListener(EGFirebase.DB.USERS, object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _usersItems.value = snapshot.children.map { dataSnapshot ->
                    dataSnapshot.getValue(EGFirebase.UsersItem::class.java)!!
                }
                val list = mutableListOf<Boolean>()
                _usersItems.value.forEach{
                    list.add(it.connected)
                }
                _usersConnected.value=list
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "loadPost:onCancelled", error.toException())
            }
        })
    }

        fun setConnected (user: String, connected : Boolean){
            escapeGameFirebase.writeAttribute(EGFirebase.DB.USERS, user, "connected", connected)
        }

        fun setDisconnected (user: String, connected : Boolean){
            escapeGameFirebase.writeAttribute(EGFirebase.DB.USERS, user, "connected", connected)
        }
}