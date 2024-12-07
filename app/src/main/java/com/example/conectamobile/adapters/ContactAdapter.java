package com.example.conectamobile.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.conectamobile.R;
import com.example.conectamobile.models.Contact;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<Contact> contactList;

    public ContactAdapter(List<Contact> contactList) {
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);

        // Validar que los datos del contacto no sean nulos
        if (contact == null || contact.getUserId() == null || contact.getUserId().isEmpty()) {
            Log.e("ContactAdapter", "El contacto o su userId es nulo o vacío.");
            return;
        }

        // Validar que el usuario actual esté autenticado
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : null;

        if (currentUserId == null || currentUserId.isEmpty()) {
            Log.e("ContactAdapter", "El ID del usuario actual es nulo o vacío.");
            return;
        }

        // Mostrar información del contacto
        holder.contactName.setText(contact.getName());
        holder.contactEmail.setText(contact.getEmail());

        // Referencia al nodo de solicitudes de amistad
        DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference("friend_requests");

        // Verificar el estado de la solicitud
        requestsRef.child(contact.getUserId()).child(currentUserId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && "pending".equals(snapshot.getValue(String.class))) {
                            holder.addFriendButton.setText("Pendiente");
                            holder.addFriendButton.setEnabled(false);
                        } else {
                            holder.addFriendButton.setText("Agregar");
                            holder.addFriendButton.setEnabled(true);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("ContactAdapter", "Error al verificar solicitud: " + error.getMessage());
                    }
                });

        // Configurar el botón "Agregar"
        holder.addFriendButton.setOnClickListener(v -> {
            requestsRef.child(contact.getUserId()).child(currentUserId)
                    .setValue("pending")
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            holder.addFriendButton.setText("Pendiente");
                            holder.addFriendButton.setEnabled(false);
                            Toast.makeText(v.getContext(), "Solicitud enviada", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(v.getContext(), "Error al enviar solicitud", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }



    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<Contact> newList) {
        contactList = newList;
        notifyDataSetChanged();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView contactName, contactEmail;
        Button addFriendButton;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contact_name);
            contactEmail = itemView.findViewById(R.id.contact_email);
            addFriendButton = itemView.findViewById(R.id.add_friend_button);
        }
    }
}
