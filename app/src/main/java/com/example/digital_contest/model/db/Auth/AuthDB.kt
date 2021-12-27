package com.example.digital_contest.model.db.Auth

import android.net.Uri
import android.util.Log
import com.example.digital_contest.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class AuthDB : auth {
    val auth : FirebaseAuth = FirebaseAuth.getInstance()
    val db : FirebaseFirestore = FirebaseFirestore.getInstance()
    val storage : FirebaseStorage = FirebaseStorage.getInstance()
    val storageRef = storage.reference
    
    
    override suspend fun signUp(user: User, password: String, profileImg : Uri?): AuthResult {
        //회원가입 전체를 진행하는 함수, 회원가입 결과는 AuthResult에 정의된데로 반환

        if (profileImg != null){
            val saveProfileImgResult = saveProfileImg(profileImg, user.id) ?: return AuthResult.Fail
            user.profileImgUrl = saveProfileImgResult
        }   else{
            user.profileImgUrl = "https://firebasestorage.googleapis.com/v0/b/digital-contest-f689c.appspot.com/o/user%2Fdefault_profile_img.png?alt=media&token=b14de24b-b5e3-45eb-b871-0511286fc9e4"
        }

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
        //회원가입 하려는 userData와 비밀번호를 받아서 계정 생성을 진행한다. 회원가입 결과는 AuthResult에 정의된데로 반환

        var result = AuthResult.OK
        auth.createUserWithEmailAndPassword(user.email, password)
            .addOnFailureListener {
                result = AuthResult.Fail
            }
            .await()

        return result
    }

    override suspend fun saveProfileImg(img: Uri, userID: String): String? {
        var downloadUri : String? = null

        val imgRef = storageRef.child("user/${userID}.png")

        imgRef.putFile(img)
            .continueWithTask{
                return@continueWithTask imgRef.downloadUrl
            }
            .addOnSuccessListener {
                downloadUri = it.toString()
            }
            .await()

        return downloadUri
    }

    override suspend fun saveUserData(user: User): AuthResult {
        //userData를 받고 데이터를 저장한다.
        var result = AuthResult.Fail

        db.collection("user").document(user.id).set(user)
            .addOnSuccessListener {
                result = AuthResult.OK
            }
            .await()

        return result
    }

    override suspend fun idOverlapCheck(user: User) : AuthResult {
        //id중복 체크 기능 함수, 아직 제작 안함

        var result = AuthResult.IdOverlap
        db.collection("user").document(user.id).get()
            .addOnSuccessListener {
                result = if(it.data == null) AuthResult.IdOverlap else AuthResult.OK
            }
            .await()

        return result
    }

    override suspend fun getUserDataById(id: String): User? {
        //id중복 체크 기능 함수, 아직 제작 안함
        
        var user : User? = null
        db.collection("user").document(id).get()
            .addOnSuccessListener {
                user = it.toObject(User::class.java)//!! as User
            }
            .await()

        return user
    }

    override suspend fun getUserDataByEmail(email: String): User? {
        var resultUsserData : User? = null

        db.collection("user").whereEqualTo("email", email).get()
            .addOnSuccessListener { documents ->
                for(document in documents){
                    resultUsserData = document.toObject(User::class.java)
                }
            }.await()

        return resultUsserData
    }

    override suspend fun userTotalLikeCountUpdate(user: User): AuthResult {
        var result = AuthResult.Fail

        db.collection("user").document(user.id)
            .update("totalLikeCount", user.totalLikeCount)
            .addOnSuccessListener {
                result = AuthResult.OK
            }
            .await()

        return result
    }

    override suspend fun login(id: String, password: String): User? {
        //로그인을 전체적으로 진행하는 함수, id와 비밀번호를 입력받아서 로그인을 진행하고
        // 로그인에 성공했다면 id에 해당하는 유저의 데이터를, 실패했다면 null을 반환한다.

        var user : User? = getUserDataById(id)

        if(user == null){
            return null
        }

        try{
            auth.signInWithEmailAndPassword(user.email, password).await()
        }   catch (e : com.google.firebase.auth.FirebaseAuthInvalidCredentialsException){
            user = null
        }

        return user
    }
}