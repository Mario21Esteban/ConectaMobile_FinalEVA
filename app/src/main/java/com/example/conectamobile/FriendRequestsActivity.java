package com.example.conectamobile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.conectamobile.adapters.FriendRequestAdapter;
import com.example.conectamobile.models.Contact;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FriendRequestsActivity extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_friend_requests; // Layout de solicitudes de amistad
    }

    @Override
    protected int getBottomNavigationMenuItemId() {
        return R.id.nav_requests; // Elemento seleccionado en el menú
    }

    private RecyclerView requestsRecyclerView;
    private FriendRequestAdapter requestAdapter;
    private List<Contact> requestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);

        // Inicializar vistas
        requestsRecyclerView = findViewById(R.id.requests_recycler_view);
        requestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar lista y adaptador
        requestList = new ArrayList<>();
        requestAdapter = new FriendRequestAdapter(requestList);
        requestsRecyclerView.setAdapter(requestAdapter);

        loadFriendRequests();
    }

    private void loadFriendRequests() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference("friend_requests");

        requestsRef.child(currentUserId).addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestList.clear();
                for (DataSnapshot requestSnapshot : snapshot.getChildren()) {
                    String senderId = requestSnapshot.getKey();
                    String status = requestSnapshot.getValue(String.class);

                    if ("pending".equals(status)) {
                        // Obtener datos del remitente
                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                        usersRef.child(senderId).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                Contact contact = userSnapshot.getValue(Contact.class);
                                if (contact != null) {
                                    requestList.add(contact);
                                    requestAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Manejar errores
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar errores
            }
        });
    }
}
