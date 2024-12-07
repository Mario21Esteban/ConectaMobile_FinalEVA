package com.example.conectamobile;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.conectamobile.adapters.ContactAdapter;
import com.example.conectamobile.models.Contact;

import java.util.ArrayList;
import java.util.List;

public class FriendRequestsActivity extends AppCompatActivity {

    private RecyclerView requestsRecyclerView;
    private ContactAdapter contactAdapter;
    private List<Contact> requestList;
    private DatabaseReference requestsRef;
    private FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);

        requestsRecyclerView = findViewById(R.id.requests_recycler_view);
        requestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        requestList = new ArrayList<>();
        contactAdapter = new ContactAdapter(requestList);
        requestsRecyclerView.setAdapter(contactAdapter);

        auth = FirebaseAuth.getInstance();
        requestsRef = FirebaseDatabase.getInstance().getReference("friend_requests");

        loadFriendRequests();
    }

    private void loadFriendRequests() {
        String currentUserId = auth.getCurrentUser().getUid();
        requestsRef.child(currentUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        requestList.clear();
                        for (DataSnapshot requestSnapshot : snapshot.getChildren()) {
                            String senderId = requestSnapshot.getKey();
                            // Obtener datos del usuario que envió la solicitud
                            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                            usersRef.child(senderId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                    Contact contact = userSnapshot.getValue(Contact.class);
                                    if (contact != null) {
                                        requestList.add(contact);
                                        contactAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Manejar error si es necesario
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Manejar error si es necesario
                    }
                });
    }
}

