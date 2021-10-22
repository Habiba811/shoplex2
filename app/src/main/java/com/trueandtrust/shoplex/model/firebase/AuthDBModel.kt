package com.trueandtrust.shoplex.model.firebase

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.trueandtrust.shoplex.R
import com.trueandtrust.shoplex.model.extra.FirebaseReferences
import com.trueandtrust.shoplex.model.extra.StoreInfo
import com.trueandtrust.shoplex.model.interfaces.AuthListener
import com.trueandtrust.shoplex.model.pojo.Store

class AuthDBModel(val listener: AuthListener, val context: Context) {
    private lateinit var imgTask: StorageTask<UploadTask.TaskSnapshot>
    fun login(email: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loginWithEmail(email)
                } else {
                    listener.onLoginFailed()
                }
            }
    }

    fun createAccount(store: Store, password: String) {
        val ref: DocumentReference = FirebaseReferences.pendingSellersRef.document()
        addImage(Uri.parse(store.image), ref.id)
        Firebase.auth.fetchSignInMethodsForEmail(store.email).addOnCompleteListener {
            if (it.isSuccessful && it.result?.signInMethods?.size == 0) {
                Firebase.auth.createUserWithEmailAndPassword(store.email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            addNewStore(store, ref)
                        } else {
                            imgTask.cancel()
                            FirebaseReferences.imagesStoreRef.child(store.storeID).delete()
                            Toast.makeText(context, R.string.authentication_fail, Toast.LENGTH_SHORT).show()
                            listener.onAddStoreFailed()
                        }
                    }
            } else {
                imgTask.cancel()
                FirebaseReferences.imagesStoreRef.child(store.storeID).delete()
                listener.onUserExists(context)
            }
        }
    }

    private fun addNewStore(store: Store, ref: DocumentReference) {
//        val image = store.image
        store.image = ""
        store.storeID = ref.id
        ref.set(store).addOnSuccessListener {
            FirebaseReferences.imagesStoreRef.child(store.storeID).downloadUrl.addOnSuccessListener { uri ->
                if(uri != null)
                FirebaseReferences.pendingSellersRef.document(store.storeID)
                    .update("image", uri.toString())
            }

            StoreInfo.storeID = store.storeID
            StoreInfo.updateTokenID()
            Toast.makeText(context, R.string.success_create_account, Toast.LENGTH_SHORT).show()
            (context as AppCompatActivity).finish()
        }.addOnFailureListener {
            //listener.onAddNewStore(null)
            Toast.makeText(context, R.string.failed, Toast.LENGTH_SHORT).show()
            imgTask.cancel()
            FirebaseReferences.imagesStoreRef.child(store.storeID).delete()
            listener.onAddStoreFailed()
        }

    }

    private fun addImage(uri: Uri, storeId: String) {
        val imageRef = FirebaseReferences.imagesStoreRef.child(storeId)
        imgTask = imageRef.putFile(uri).addOnSuccessListener { _ ->
            imageRef.downloadUrl.addOnSuccessListener {
                FirebaseReferences.pendingSellersRef.document(storeId)
                    .update("image", it.toString())
                //update profile
                val profileUpdates = userProfileChangeRequest {
                    photoUri = it
                }
                Firebase.auth.currentUser?.updateProfile(profileUpdates)
            }
        }
    }

    private fun loginWithEmail(userEmail: String) {
        FirebaseReferences.storeRef.whereEqualTo("email", userEmail).get()
            .addOnSuccessListener {
                val store: Store?
                when {
                    it.documents.count() > 0 -> {
                        store = it.documents[0].toObject()!!
                        listener.onLoginSuccess(context, store)
                    }
                    else -> {
                        // listener.onLoginFailed()
                        checkPendingStore(userEmail)
                    }
                }
            }.addOnFailureListener {
                listener.onLoginFailed()
            }
    }

    private fun checkPendingStore(userEmail: String) {
        FirebaseReferences.pendingSellersRef.whereEqualTo("email", userEmail).get()
            .addOnSuccessListener {
                // var store: Store?
                when {
                    it.documents.count() > 0 -> {
                        // store = it.documents[0].toObject()!!
                        listener.onPendingStore()
                    }
                    else -> {
                        listener.onLoginFailed()
                    }
                }
            }.addOnFailureListener {
                listener.onLoginFailed()
            }
    }
}