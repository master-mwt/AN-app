package it.univaq.disim.mwt.android_native_app;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthSignUpActivity extends AppCompatActivity {

    private TextInputEditText email;
    private TextInputEditText password;
    private MaterialButton signUpButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email_field);

        password = findViewById(R.id.password_field);

        signUpButton = findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();

                if( (!"".equals(emailText)) && (!"".equals(passwordText)) ){
                    signUpUser(emailText, passwordText);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null)
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            System.out.println("Logged in user email: " + currentUser.getEmail());
            signUpButton.setEnabled(false);
        } else {
            System.out.println("User not logged in");
        }
    }

    private void signUpUser(String emailText, String passwordText){
        mAuth.createUserWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();
                    System.out.println("Sign up success: "  + user.getEmail());
                    onBackPressed();
                } else {
                    // If sign in fails, display a message to the user.
                    System.out.println("Sign up failed: " + task.getException().getMessage());
                }
            }
        });
    }
}
