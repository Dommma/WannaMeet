package hu.bme.wannameet2.models

import com.google.firebase.firestore.DocumentReference

data class UserModel(
    val chats: Map<String, String>? = null,
    val email: String? = null,
    val name: String? = null,
    val nickname: String? = null
)