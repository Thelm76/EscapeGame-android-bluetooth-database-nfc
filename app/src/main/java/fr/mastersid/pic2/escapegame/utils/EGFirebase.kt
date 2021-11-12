package fr.mastersid.pic2.escapegame.utils

import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

class EGFirebase{
    val TAG : String="FirebaseRepository"

    val database = FirebaseDatabase.getInstance("https://escapegamedatabase-default-rtdb.europe-west1.firebasedatabase.app/")


    fun writeAttribute( db : DB, child: String, attribute : String, value : Any){
        database.getReference((db.dbName)).child(child).child(attribute).setValue(value)
    }

    fun addDBListener(db: DB, listener: ValueEventListener) {
        try {
            database.getReference(db.dbName)
                .orderByChild("_rank")
                .addValueEventListener(listener)
        }
        catch (e : Exception){
            Log.e(TAG,"Failed adding listener : ${e.stackTraceToString()}")
        }
    }

    inline fun <reified dataType> fetchFrom(db: DB, child:String, callback: FirebaseCallback<dataType>) {
        var tItem: dataType?

        database.getReference(db.dbName).child(child).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    tItem = dataSnapshot.getValue<dataType>()
                    tItem?.let { callback.onCallback(it) }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "loadItem:onCancelled", error.toException())
                }
            })
    }

    enum class DB(val dbName:String) {
        USERS("users"),
        ITEMS("items"),
        ENIGMAS("enigmas")
    }

    data class UsersItem(
        var uid: String="",
        var connected: Boolean = false,
        var mac: String = ""
    )

    data class ItemItem(
        var id: String="no item",
        var img: String="",
        var desc: String="empty desc"
    )

    data class EnigmaItem(
        var qid: String="no question",
        var answer: List<EnigmaAnswer>
    )

    data class EnigmaAnswer(
        var aid: String="no answer"
    )
}

interface FirebaseCallback<T> {
    fun onCallback(value: T)
}

@InstallIn(SingletonComponent::class)
@Module
object EGFirebaseModule {
    @Provides
    @Singleton
    fun provideEGFirebase(): EGFirebase {
        return EGFirebase()
    }
}