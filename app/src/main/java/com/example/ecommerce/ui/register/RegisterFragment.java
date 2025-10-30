package com.example.ecommerce.ui.register;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ecommerce.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterFragment extends Fragment {

    private EditText emailInput, passwordInput, confirmPasswordInput;
    private Button registerButton;
    private TextView verifyText;
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        emailInput = view.findViewById(R.id.Email);
        passwordInput = view.findViewById(R.id.password1);
        confirmPasswordInput = view.findViewById(R.id.password);
        registerButton = view.findViewById(R.id.register);
        verifyText = view.findViewById(R.id.textVerify);

        auth = FirebaseAuth.getInstance();
        verifyText.setVisibility(View.GONE); // hide text

        // Validate input
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = emailInput.getText().toString().trim();
                String pass1 = passwordInput.getText().toString().trim();
                String pass2 = confirmPasswordInput.getText().toString().trim();
                registerButton.setEnabled(!email.isEmpty() && !pass1.isEmpty() && pass1.equals(pass2));
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };

        emailInput.addTextChangedListener(watcher);
        passwordInput.addTextChangedListener(watcher);
        confirmPasswordInput.addTextChangedListener(watcher);

        registerButton.setOnClickListener(v -> registerUser());

        return view;
    }

    private void registerUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            user.sendEmailVerification().addOnCompleteListener(verifyTask -> {
                                if (verifyTask.isSuccessful()) {
                                    Toast.makeText(getContext(), "Verification email sent. Please check your inbox.", Toast.LENGTH_LONG).show();
                                    verifyText.setVisibility(View.VISIBLE);
                                    registerButton.setEnabled(false);
                                } else {
                                    Toast.makeText(getContext(), "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(getContext(), "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
