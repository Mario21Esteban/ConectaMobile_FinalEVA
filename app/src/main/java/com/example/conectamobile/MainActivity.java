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
    private Button logoutButton, goToContactsButton, goToRequestsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Verificar si el usuario está autenticado
        FirebaseUser currentUser = mAuth.getCurrentUser();


        // Vincular vistas
        logoutButton = findViewById(R.id.logout_button);
        goToContactsButton = findViewById(R.id.go_to_contacts_button);
        goToRequestsButton = findViewById(R.id.go_to_requests_button);

        // Configurar botón de cerrar sesión
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Finalizar MainActivity
        });

        // Configurar botón para ir a la lista de contactos
        goToContactsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ContactListActivity.class);
            startActivity(intent);
        });

        // Configurar botón para ir a la pantalla de solicitudes pendientes
        goToRequestsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FriendRequestsActivity.class);
            startActivity(intent);
        });
    }
}





