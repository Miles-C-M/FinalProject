package com.example.finalproject

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import androidx.window.layout.WindowMetricsCalculator
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private val TAG = "RegisterActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = if (compactScreen())
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT else
            ActivityInfo.SCREEN_ORIENTATION_FULL_USER

        setContentView(R.layout.profile_activity)

        // Get instance of the FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser
        val fStore = FirebaseFirestore.getInstance()

        // If currentUser is not null, we have a user and go back to the MainActivity
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            // Make sure to call finish(), otherwise the user would be able to go back to the RegisterActivity
            finish()
        } else {
            // create a new ActivityResultLauncher to launch the sign-in activity and handle the result
            // When the result is returned, the result parameter will contain the data and resultCode (e.g., OK, Cancelled etc.).
            val signActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // The user has successfully signed in or he/she is a new user

                    val user = FirebaseAuth.getInstance().currentUser
                    Log.d(TAG, "onActivityResult: $user")

                    //Checking for User (New/Old) (optional--you do not have to show these toast messages)
                    if (user?.metadata?.creationTimestamp == user?.metadata?.lastSignInTimestamp) {
                        // This is a New User
                        val userId = user?.uid
                        val userData = hashMapOf(
                            "uid" to userId,
                            "email" to user?.email,
                            "displayName" to user?.displayName
                        )

                        if (userId != null) {
                            fStore.collection("users").document(userId)
                                .set(userData)
                                .addOnSuccessListener {
                                    Log.d(TAG, "User document created successfully in Firestore.")
                                }
                                .addOnFailureListener { e ->
                                    Log.e(TAG, "Error writing user document", e)
                                }
                        }

                        Toast.makeText(this, "Welcome New User!", Toast.LENGTH_SHORT).show()
                    }

                    // Since the user signed in, the user can go back to main activity
                    startActivity(Intent(this, MainActivity::class.java))
                    // Make sure to call finish(), otherwise the user would be able to go back to the RegisterActivity
                    finish()

                } else {
                    // Sign in failed. If response is null the user canceled the
                    // sign-in flow using the back button. Otherwise check
                    // response.getError().getErrorCode() and handle the error.
                    val response = IdpResponse.fromResultIntent(result.data)
                    if (response == null) {
                        Log.d(TAG, "onActivityResult: the user has cancelled the sign in request")
                    } else {
                        Log.e(TAG, "onActivityResult: ${response.error?.errorCode}")
                    }
                }
            }

            // Login Button
            findViewById<Button>(R.id.login_button).setOnClickListener {
                // Choose authentication providers -- make sure enable them on your firebase account first
                val providers = arrayListOf(
                    AuthUI.IdpConfig.GoogleBuilder().build()
                )

                // Create  sign-in intent
                val signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setTheme(R.style.LoginTheme)
                    .setLogo(R.drawable.app_logo)
                    .setAlwaysShowSignInMethodScreen(false) // use this if you have only one provider and really want the see the signin page
                    .setIsSmartLockEnabled(false)
                    .build()

                // Launch sign-in Activity with the sign-in intent above
                signActivityLauncher.launch(signInIntent)
            }
        }
    }
    /** Determines whether the device has a compact screen. **/
    private fun compactScreen() : Boolean {
        val metrics = WindowMetricsCalculator.getOrCreate().computeMaximumWindowMetrics(this)
        val width = metrics.bounds.width()
        val height = metrics.bounds.height()
        val density = resources.displayMetrics.density
        val windowSizeClass = WindowSizeClass.compute(width/density, height/density)

        return windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT ||
                windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT
    }


}