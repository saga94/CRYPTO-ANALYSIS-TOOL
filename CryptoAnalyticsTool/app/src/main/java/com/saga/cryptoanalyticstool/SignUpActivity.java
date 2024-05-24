package com.saga.cryptoanalyticstool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputEditText editText;
    private TextInputEditText editText2;
    private TextInputEditText editText3;
    private ProgressBar progressBar;
    private Button signUp;
    private TextView textView;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signUp = findViewById(R.id.login_button);

        textView = findViewById(R.id.textView);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar = findViewById(R.id.progress_bar);
                progressBar.setVisibility(View.VISIBLE);
                editText = findViewById(R.id.email);
                editText2 = findViewById(R.id.password);
                editText3 = findViewById((R.id.confirm_password));
                mAuth = FirebaseAuth.getInstance();
                if(null != editText.getText().toString() && null != editText2.getText().toString() && null != editText3.getText().toString()) {
                    String email = editText.getText().toString();
                    String password = editText2.getText().toString();
                    String cnfPassword = editText3.getText().toString();
                    if(password.equals(cnfPassword)) {
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseAuth.getInstance().signOut();
                                    updateProfile(email);
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(SignUpActivity.this, "Account created.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    return;
                                }
                            }
                        });
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUpActivity.this, "Passwords are not matching", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignUpActivity.this, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private String updateProfile(String email) {
        RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);
        TextInputEditText firstName = findViewById(R.id.first_name);
        TextInputEditText lastName = findViewById(R.id.last_name);
        TextInputEditText phone = findViewById(R.id.phone_number);
        String url = "http://192.168.29.38:8443/profile";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> Toast.makeText(SignUpActivity.this, "Successfully added profile to DB", Toast.LENGTH_SHORT).show(),
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());
                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("firstName", firstName.getText().toString());
                params.put("lastName", lastName.getText().toString());
                params.put("email", email);
                params.put("phoneNumber", phone.getText().toString());
                return params;
            }
        };
        queue.add(stringRequest);
        return "OK";
    }
}