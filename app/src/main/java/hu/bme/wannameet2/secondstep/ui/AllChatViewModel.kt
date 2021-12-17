package hu.bme.wannameet2.secondstep.ui

import android.util.Log
import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import hu.bme.wannameet2.epoxy.EpoxyChatModel
import hu.bme.wannameet2.models.UserModel
import hu.bme.wannameet2.secondstep.state.AllChatState

class AllChatViewModel(
    initialState: AllChatState
): BaseMvRxViewModel<AllChatState>(initialState, debugMode = true) {

    companion object {
        val TAG = "AddChatVieModel"
    }

    val dbRef = Firebase.firestore.collection("users").document(Firebase.auth.currentUser?.uid!!)

    init {
        setState {
            copy(chats = Loading())
        }
        dbRef.addSnapshotListener{ snapshot, e ->
            if(e!=null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            if(snapshot != null) {
                val newList = mutableListOf<EpoxyChatModel>()
                val tmpUser = snapshot.toObject<UserModel>()
                if(!(tmpUser?.chats.isNullOrEmpty())) {
                    for(i in 0 until(tmpUser?.chats?.size!!)) {
                        val index = tmpUser.chats.keys.elementAt(i)
                        Firebase.firestore.collection("users").document(index).get().addOnSuccessListener { doc ->
                            newList.add(EpoxyChatModel(doc["name"].toString(), doc["nickname"].toString(), tmpUser.chats[index]))
                            if(newList.size == tmpUser.chats.size) {
                                setState { copy(chats = Success(newList)) }                        }
                        }
                    }

                }
                else {
                    setState { copy(chats = Success(emptyList())) }
                }
            }
            else {
                setState { copy(chats = Success(emptyList())) }
            }
        }
    }
}