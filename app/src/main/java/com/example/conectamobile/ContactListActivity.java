package com.example.conectamobile;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.conectamobile.adapters.ContactAdapter;
import com.example.conectamobile.models.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactListActivity extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_contact_list; // Layout de la lista de contactos
    }

    @Override
    protected int getBottomNavigationMenuItemId() {
        return R.id.nav_contacts; // Elemento seleccionado en el menú
    }

    private EditText searchField;
    private RecyclerView contactsRecyclerView;
    private ContactAdapter contactAdapter;
    private List<Contact> userList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        // Inicializar vistas
        searchField = findViewById(R.id.search_field);
        contactsRecyclerView = findViewById(R.id.contacts_recycler_view);
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();
        contactAdapter = new ContactAdapter(userList);
        contactsRecyclerView.setAdapter(contactAdapter);

        // Conectar con Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Cargar lista de usuarios
        loadUsers();

        // Filtrar lista de contactos
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadUsers() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    Contact contact = userSnapshot.getValue(Contact.class);
                    if (contact != null) {
                        userList.add(contact);
                    }
                }
                contactAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar errores si es necesario
            }
        });
    }

    private void filterUsers(String query) {
        List<Contact> filteredList = new ArrayList<>();
        for (Contact contact : userList) {
            if (contact.getName().toLowerCase().contains(query.toLowerCase()) ||
                    contact.getEmail().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(contact);
            }
        }
        contactAdapter.updateList(filteredList);
    }
}
