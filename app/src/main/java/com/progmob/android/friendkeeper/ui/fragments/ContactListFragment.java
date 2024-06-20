package com.progmob.android.friendkeeper.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.progmob.android.friendkeeper.R;
import com.progmob.android.friendkeeper.database.AppDatabase;
import com.progmob.android.friendkeeper.entities.Address;
import com.progmob.android.friendkeeper.entities.Contact;
import com.progmob.android.friendkeeper.ui.activities.AddContactActivity;
import com.progmob.android.friendkeeper.ui.activities.ContactInfoActivity;
import com.progmob.android.friendkeeper.ui.adapters.ContactListAdapter;
import com.progmob.android.friendkeeper.utils.PreferencesManager;

import java.util.List;

public class ContactListFragment extends Fragment {

    private ContactListAdapter adapter;
    private int userId;

    private FloatingActionButton addContact;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);

        RecyclerView recyclerViewContacts = view.findViewById(R.id.recyclerViewContacts);
        recyclerViewContacts.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ContactListAdapter(getActivity());
        recyclerViewContacts.setAdapter(adapter);

        adapter.setOnItemClickListener(position -> {
            Contact contact = adapter.getContact(position);
            if (contact != null) {
                Address address = getAddressById(contact.getAddressId());
                if (address != null) {
                    Intent intent = new Intent(getActivity(), ContactInfoActivity.class);
                    intent.putExtra("contact_id", contact.getId());
                    intent.putExtra("contact_name", contact.getName());
                    intent.putExtra("contact_phone", contact.getPhoneNumber());
                    intent.putExtra("contact_email", contact.getEmail());
                    intent.putExtra("contact_address", address.getTitle());
                    intent.putExtra("contact_latitude", address.getLatitude());
                    intent.putExtra("contact_longitude", address.getLongitude());
                    intent.putExtra("contact_birthday", contact.getBirthdate());
                    intent.putExtra("contact_photo_uri", contact.getPhotoUri() != null ? contact.getPhotoUri().toString() : null);
                    startActivity(intent);
                } else {
                    Log.e("ContactListFragment", "Endereço não encontrado para o contato ID: " + contact.getId());
                }
            }
        });

        // Configurar o idioma com base na preferência salva
        PreferencesManager preferencesManager = PreferencesManager.getInstance(getActivity());
        String languageCode = preferencesManager.getLanguage();
        preferencesManager.setLocale(getActivity(), languageCode);

        userId = preferencesManager.getUserId();

        loadContacts();

        // Configure o botão de adicionar contato
        addContact = view.findViewById(R.id.floating_action_button);
        addContact.setOnClickListener(v -> openAddContactActivity());

        return view;
    }

    private Address getAddressById(int addressId) {
        Address[] address = new Address[1];
        Thread thread = new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(getContext());
            address[0] = db.addressDao().getAddressById(addressId);
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return address[0];
    }

    private void loadContacts() {
        new Thread(() -> {
            try {
                AppDatabase db = AppDatabase.getDatabase(getContext());
                List<Contact> contacts = db.contactDao().getContactsByUserId(userId);
                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> adapter.setContacts(contacts));
                }
            } catch (Exception e) {
                Log.e("ContactListFragment", "Erro ao carregar contatos", e);
            }
        }).start();
    }

    private void openAddContactActivity() {
        Intent intent = new Intent(getActivity(), AddContactActivity.class);
        startActivity(intent);
    }

    // Método para mudar o idioma quando necessário
    private void changeLanguage(String languageCode) {
        PreferencesManager preferencesManager = PreferencesManager.getInstance(getActivity());
        preferencesManager.updateLanguage(getActivity(), languageCode);
    }
}
