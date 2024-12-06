package com.example.conectamobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailInput, passwordInput;
    private Button registerButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        registerButton = findViewById(R.id.registerButton);

        auth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear usuario con Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Obtener usuario registrado
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            // Llamar a la función para guardar datos del usuario en Firebase Database
                            saveUserData(user.getUid(), email);
                        }

                        // Redirigir al login después del registro exitoso
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        // Mostrar error en caso de que falle el registro
                        Toast.makeText(this, "Registro fallido: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveUserData(String userId, String email) {
        // Obtener referencia a la base de datos
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users"); // Crea o usa el nodo "users"

        // Datos del usuario que queremos guardar
        HashMap<String, Object> userData = new HashMap<>();
        userData.put("name", "Nuevo Usuario"); // Nombre inicial por defecto
        userData.put("email", email);
        userData.put("profilePicture", ""); // Vacío inicialmente

        // Guardar datos en Firebase bajo el UID del usuario
        usersRef.child(userId).setValue(userData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Firebase", "Datos del usuario guardados correctamente");
                    } else {
                        Log.e("Firebase", "Error al guardar datos", task.getException());
                    }
                });
    }
}
