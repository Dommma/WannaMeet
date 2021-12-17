package hu.bme.wannameet2.secondstep.ui

import android.util.Log
import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import hu.bme.wannameet2.epoxy.EpoxyMessageModel
import hu.bme.wannameet2.models.MessageModel
import hu.bme.wannameet2.secondstep.state.CurrentChatState
import java.util.*

class CurrentChatViewModel(
    initialState: CurrentChatState
) : BaseMvRxViewModel<CurrentChatState>(initialState, debugMode = true) {

    companion object {
        val TAG = "CurrentChatVieModel"
    }

    var dbRef: DocumentReference? = null

    init{
        setState {
            copy(messages = Loading())
        }
    }

    fun setDbRef(id: String) {
        dbRef = Firebase.firestore.collection("chats").document(id)

        dbRef?.addSnapshotListener { snapshot,e ->
            if(e!=null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            if(snapshot!=null) {
                val tmpList = mutableListOf<EpoxyMessageModel>()
                val dataList = snapshot.toObject<MessageModel>()
                if(!(dataList?.messages.isNullOrEmpty())) {
                    for(i in 0 until(dataList?.messages?.size!!)) {
                        val index = dataList.messages.keys.elementAt(i)
                        if(dataList.messages[index]?.substring(0,9).equals(Firebase.auth.currentUser?.uid?.substring(0,9))) {
                            tmpList.add(EpoxyMessageModel(dataList.messages[index]?.substring(9), "ME", index))
                        }
                        else {
                            tmpList.add(EpoxyMessageModel(dataList.messages[index]?.substring(9), "OTHER", index))
                        }
                    }
                    tmpList.sortBy { it.time }
                    setState { copy(messages = Success(tmpList)) }
                }
                else {
                    setState { copy(messages = Success(emptyList())) }
                }
            }
            else {
                setState { copy(messages = Success(emptyList())) }
            }
        }
    }

    fun newMessage(str: String) {
        val date = Calendar.getInstance().timeInMillis.toString()
        dbRef?.update(mapOf("messages."+date to Firebase.auth.currentUser?.uid?.substring(0,9)+str))
    }
}