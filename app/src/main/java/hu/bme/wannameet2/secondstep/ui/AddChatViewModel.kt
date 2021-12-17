package hu.bme.wannameet2.secondstep.ui

import android.util.Log
import android.widget.Toast
import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import hu.bme.wannameet2.epoxy.EpoxyUserModel
import hu.bme.wannameet2.models.UserModel
import hu.bme.wannameet2.secondstep.state.AddChatState
import java.util.*

class AddChatViewModel(
    initialState: AddChatState
):BaseMvRxViewModel<AddChatState>(initialState, debugMode = true) {
    companion object {
        val TAG = "AddChatVieModel"
    }

    private val dbRef = Firebase.firestore.collection("users")

    init {
        setState {
            copy(users = Loading())
        }

        dbRef.addSnapshotListener{ snapshot, e ->
            if(e!=null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            if(snapshot != null) {
                val userCurrentChats = mutableListOf<EpoxyUserModel>()
                for(user in snapshot.documents) {
                    if(user.id != Firebase.auth.currentUser?.uid) {
                        userCurrentChats.add(EpoxyUserModel(user.id, user["name"].toString(), user["nickname"].toString()))
                    }
                }
                setState { copy(users = Success(userCurrentChats)) }
            }
            else {
                setState { copy(users = Success(emptyList())) }
            }
        }
    }
}