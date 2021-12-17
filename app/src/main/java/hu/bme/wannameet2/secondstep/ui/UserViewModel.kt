package hu.bme.wannameet2.secondstep.ui

import android.util.Log
import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import hu.bme.wannameet2.models.UserModel
import hu.bme.wannameet2.secondstep.state.UserState

class UserViewModel(
    initialState: UserState
): BaseMvRxViewModel<UserState>(initialState, debugMode = true) {

    private val TAG = "ProfileViewModel"
    private val dbRef = Firebase.firestore.collection("users").document(Firebase.auth.currentUser?.uid!!)

    init {
        setState {
            copy(user = Loading())
        }
        dbRef.addSnapshotListener { snapshot, e ->
            if(e!=null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            if(snapshot != null && snapshot.exists()) {
                val tmp: UserModel? = snapshot.toObject<UserModel>()
                if (tmp != null) {
                    setState {
                        if(!(user.invoke()?.name == tmp.name &&
                                    user.invoke()?.nickname == tmp.nickname)) {
                            copy(user = Success(tmp))
                        }
                        else {copy()}
                    }
                }
            }
        }
    }
}