package com.progmob.android.friendkeeper.ui.fragments;

import android.content.Context;
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

public class ContactListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewContacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        int userId = getArguments() != null ? getArguments().getInt("user_id") : -1;
        if (userId != -1) {
            AppDatabase db = AppDatabase.getDatabase(getContext());
            List<Contact> contacts = db.contactDao().getContactsByUserId(userId);
            ContactListAdapter adapter = new ContactListAdapter((Context) contacts);
            recyclerView.setAdapter(adapter);
        }

        return view;
    }

}
