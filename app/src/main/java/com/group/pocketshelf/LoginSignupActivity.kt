package com.group.pocketshelf

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.group.pocketshelf.databinding.ActivityLoginSignupBinding
import android.view.View

class LoginSignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginSignupBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = FirebaseAuth.getInstance()

        // computer security is my passion
        // XSVhqqutpvPIpScdLr0jkLBzGs42
        val email = "test@y.com"
        val password = "123456"
        binding.editPassword.setText(password)
        binding.editEmail.setText(email)

        // Navigate to Starting Page
        binding.buttonLogin.setOnClickListener {
            if (binding.editEmail.text.toString() == "" || binding.editPassword.text.toString() == "") {

                Toast.makeText(this, "Please enter an email and password",
                    Toast.LENGTH_SHORT).show()
            } else {
                loginUser(
                    binding.editEmail.text.toString(),
                    binding.editPassword.text.toString()
                )

            }

        }

        // Sign up
        binding.buttonSignup.setOnClickListener {
            if (binding.editEmail.text.toString() == "" || binding.editPassword.text.toString() == "") {

                Toast.makeText(this, "Please enter an email and password",
                    Toast.LENGTH_SHORT).show()
            } else {
                createNewUser(
                    binding.editEmail.text.toString(),
                    binding.editPassword.text.toString()
                )

            }
        }


    }




    fun createNewUser(email : String, password: String) {
        var auth = FirebaseAuth.getInstance()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "Welcome ${user?.email}",
                        Toast.LENGTH_SHORT).show()

                    startActivity(Intent(this, LibraryShelvesScreen::class.java))
                    overridePendingTransition(R.anim.scale_in, android.R.anim.fade_out)


                } else {

                    Toast.makeText(this, "Signup failed. Make sure that you provided a valid email and password.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }



    fun loginUser(email : String, password: String) {
        var auth = FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "Welcome ${user?.email}", Toast.LENGTH_SHORT).show()

                    startActivity(Intent(this, LibraryShelvesScreen::class.java))
                    overridePendingTransition(R.anim.scale_in, android.R.anim.fade_out)

                } else {

                    Toast.makeText(this, "Login failed. Did you provide the right credentials?",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}
