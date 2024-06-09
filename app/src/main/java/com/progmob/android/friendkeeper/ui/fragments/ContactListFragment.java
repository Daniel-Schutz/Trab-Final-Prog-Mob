package com.progmob.android.friendkeeper.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.progmob.android.friendkeeper.R;
import com.progmob.android.friendkeeper.database.AppDatabase;
import com.progmob.android.friendkeeper.entities.Contact;
import com.progmob.android.friendkeeper.ui.adapters.ContactListAdapter;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ContactListFragment extends Fragment {
    private RecyclerView recyclerViewContacts;
    private ContactListAdapter adapter;
    private int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        recyclerViewContacts = view.findViewById(R.id.recyclerViewContacts);
        recyclerViewContacts.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ContactListAdapter(getActivity());
        recyclerViewContacts.setAdapter(adapter);

        if (getArguments() != null) {
            userId = getArguments().getInt("user_id");
        }

        loadContacts();

        return view;
    }

    private void loadContacts() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(getContext());
            List<Contact> contacts = db.contactDao().getContactsByUserId(userId);
            if (isAdded()) {
                requireActivity().runOnUiThread(() -> adapter.setContacts(contacts));
            }
        });
    }
}
