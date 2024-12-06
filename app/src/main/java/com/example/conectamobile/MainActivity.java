package com.example.conectamobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private TextView welcomeMessage;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Vincular elementos del diseño
        welcomeMessage = findViewById(R.id.welcomeMessage);
        logoutButton = findViewById(R.id.logoutButton);

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Verificar si hay un usuario autenticado
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            // Redirigir a LoginActivity si no hay sesión
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else {
            // Mostrar mensaje de bienvenida
            welcomeMessage.setText("Bienvenido, " + user.getEmail());
        }

        // Cerrar sesión
        logoutButton.setOnClickListener(v -> {
            auth.signOut();
            Toast.makeText(MainActivity.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
    }
}
