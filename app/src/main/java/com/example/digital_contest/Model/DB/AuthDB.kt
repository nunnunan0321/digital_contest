package com.example.digital_contest.Model.DB

import com.example.digital_contest.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthDB : auth{
    val auth : FirebaseAuth = FirebaseAuth.getInstance()
    val db : FirebaseFirestore = FirebaseFirestore.getInstance()
    
    
    override suspend fun signUp(user: User, password: String): AuthResult {
        val saveUserDataResult = saveUserData(user)
        if(saveUserDataResult == AuthResult.Fail){
            //유저 정보 저장이 실패한 경우
            return AuthResult.Fail
        }

        val createAccountResult = createAccount(user, password)
        if(createAccountResult == AuthResult.Fail){
            //계정 생성이 실패한 경우 이저에 저장한 유저 정보를 삭제한다.
            db.collection("user").document(user.id).delete()
            return AuthResult.Fail
        }

        return AuthResult.OK
    }
    
    override suspend fun createAccount(user: User, password: String): AuthResult {
        var result = AuthResult.OK
        auth.createUserWithEmailAndPassword(user.email, password)
            .addOnFailureListener {
                result = AuthResult.Fail
            }
            .await()

        return result
    }

    override suspend fun saveUserData(user: User): AuthResult {
        var result = AuthResult.OK

        db.collection("user").document(user.id).set(user)
            .addOnFailureListener {
                result = AuthResult.Fail
            }
            .await()

        return result
    }

    override suspend fun idOverlapCheck(user: User) : AuthResult {
        var result = AuthResult.IdOverlap
        db.collection("user").document(user.id).get()
            .addOnSuccessListener {
                result = if(it.data == null) AuthResult.IdOverlap else AuthResult.OK
            }
            .await()

        return result
    }
}