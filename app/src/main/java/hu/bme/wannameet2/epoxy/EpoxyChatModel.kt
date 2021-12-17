package hu.bme.wannameet2.epoxy

import com.google.firebase.firestore.DocumentReference

data class EpoxyChatModel(
    val name: String? = null,
    val nickname: String? = null,
    val docRef: String? = null
)