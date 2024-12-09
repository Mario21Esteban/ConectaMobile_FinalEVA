package com.example.conectamobile;




import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main; // Layout principal de esta actividad
    }

    @Override
    protected int getBottomNavigationMenuItemId() {
        return R.id.nav_home; // Elemento seleccionado en el menú
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                // Ya estamos en Home
                return true;
            } else if (id == R.id.nav_contacts) {
                // Abrir la lista de contactos
                startActivity(new Intent(MainActivity.this, ContactListActivity.class));
                return true;
            } else if (id == R.id.nav_requests) {
                // Abrir la lista de solicitudes
                startActivity(new Intent(MainActivity.this, FriendRequestsActivity.class));
                return true;
            } else {
                return false;
            }
        });

    }
}
