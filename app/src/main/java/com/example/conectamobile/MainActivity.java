package com.example.conectamobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.core.view.View;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Verificar si el usuario está autenticado
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            // Si no hay usuario, redirigir al login
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else {
            // Si el usuario está autenticado, mostrar lista de contactos
            startActivity(new Intent(MainActivity.this, ContactListActivity.class));
            finish();
        }
    }

    public void logoutUser(@SuppressLint("RestrictedApi") View view) {
        // Cerrar sesión en Firebase
        mAuth.signOut();

        // Redirigir al login
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }


}
