package fr.mastersid.pic2.escapegame.utils

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.mastersid.pic2.escapegame.model.FirebaseCallback
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Singleton

const val TAG3 : String="FirebaseRepository"

class EGFirebase{
    private val database =FirebaseDatabase.getInstance("https://escapegamedatabase-default-rtdb.europe-west1.firebasedatabase.app/")

    private val usersReference = database.getReference("users")


    private val _usersItems: MutableStateFlow<List<UsersItem>> = MutableStateFlow(emptyList())
    val usersItems get ()= _usersItems

    init {

        try {
            usersReference
                .orderByChild("_rank")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        usersItems.value = snapshot.children.map { dataSnapshot ->
                            dataSnapshot.getValue(UsersItem::class.java)!!
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.w(ContentValues.TAG, "loadPost:onCancelled", error.toException())
                    }
                })

        }
        catch (e : Exception){
            Log.e("erreur",e.stackTraceToString())


        }
    }
/*
    suspend fun writeUser(userId: String, connected: Int) {
        val user = UsersItem(userId, connected)
        usersReference.child(userId).setValue(user)
            .addOnSuccessListener {
                // Write was successful!
                Log.d(TAG3, "Write user "+user.uid + " connected " + user.connected)
            }
            .addOnFailureListener {
                // Write failed
                Log.w(ContentValues.TAG, "Writer Listener Failed")
            }
    }
*/

    fun fetchUser(liveData: MutableLiveData<UsersItem>, childUser:String) {
        usersReference.child(childUser).addValueEventListener(
        object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                liveData.value= dataSnapshot.getValue<UsersItem>()
                Log.d(
                    TAG3, "child is "+childUser +" et id "+liveData.value!!.uid
                        + " with connected "+ liveData.value!!.connected)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "loadUsers:onCancelled", error.toException())
            }

        })
    }

    fun fetchItem(childItem:String, callback: FirebaseCallback) {

        var itemItem : ItemItem? = null

        database.getReference("items").child(childItem).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    itemItem = dataSnapshot.getValue<ItemItem>()
                    callback.onCallback(itemItem!!)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "loadItem:onCancelled", error.toException())
                }
            })
    }

}

data class UsersItem(
    var uid: String="",
    var connected: Boolean=false
)

data class ItemItem(
    var id: String="",
    var img: String="",
    var desc: String="empty desc"
)

@InstallIn(SingletonComponent::class)
@Module
object EGFirebaseModule {
    @Provides
    @Singleton
    fun provideEGFirebase(): EGFirebase {
        return EGFirebase()
    }
}