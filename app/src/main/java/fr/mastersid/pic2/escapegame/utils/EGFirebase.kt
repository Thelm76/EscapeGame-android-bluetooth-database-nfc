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
    val database = FirebaseDatabase.getInstance("https://escapegamedatabase-default-rtdb.europe-west1.firebasedatabase.app/")

    /*suspend fun writeUser(userId: String, connected: Int) {
        val user = UsersItem(userId, connected)
        database.getReference("users")
            .child(userId).setValue(user)
            .addOnSuccessListener {
                // Write was successful!
                Log.d(TAG3, "Write user "+user.uid + " connected " + user.connected)
            }
            .addOnFailureListener {
                // Write failed
                Log.w(ContentValues.TAG, "Writer Listener Failed")
            }
    }*/

    fun addDBListener(db: String, listener: ValueEventListener) {
        try {
            database.getReference(db)
                .orderByChild("_rank")
                .addValueEventListener(listener)
        }
        catch (e : Exception){
            Log.e("erreur",e.stackTraceToString())
        }
    }

    inline fun <reified dataType> fetchFrom(db: String, child:String, callback: FirebaseCallback<dataType>) {
        var tItem: dataType?

        database.getReference(db).child(child).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    tItem = dataSnapshot.getValue<dataType>()
                    callback.onCallback(tItem!!)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "loadItem:onCancelled", error.toException())
                }
            })
    }

}

data class UsersItem(
    var uid: String="",
    var connected: Int=0
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