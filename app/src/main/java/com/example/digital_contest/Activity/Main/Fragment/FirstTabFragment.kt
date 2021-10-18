package com.example.digital_contest.Activity.Main.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.digital_contest.Activity.Login.LoginActivity
import com.example.digital_contest.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirstTabFragment:Fragment(){
    lateinit var auth : FirebaseAuth
    lateinit var db : FirebaseFirestore

    override fun onCreateView(//view를 넣어주는 역할을
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_first_tab,container,false)
    }
    fun newInstant() : FirstTabFragment
    {
        val args = Bundle()
        val frag = FirstTabFragment()
        frag.arguments = args
        return frag
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        class MainActivity : AppCompatActivity()

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val current_user = auth.currentUser


        val logOutBtn = view.findViewById<Button>(R.id.btn_firstTap_logout)



        logOutBtn.setOnClickListener {
            auth.signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)

            //프래그먼트에서의 finish() 와 같다함
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.remove(this)
                ?.commit()
        }

        db.collection("user")
            .whereEqualTo("email", current_user?.email)
            .get()
            .addOnSuccessListener { documents ->
                for(document in documents){
                    view.findViewById<TextView>(R.id.txt_firstTap_id).text = document.id.toString()
                    view.findViewById<TextView>(R.id.txt_firstTap_name).text = document["name"].toString()
                    view.findViewById<TextView>(R.id.txt_firstTap_email).text = current_user?.email
                }
            }




    }

}