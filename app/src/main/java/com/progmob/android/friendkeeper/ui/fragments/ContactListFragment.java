package com.progmob.android.friendkeeper.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.progmob.android.friendkeeper.R;
import com.progmob.android.friendkeeper.database.AppDatabase;
import com.progmob.android.friendkeeper.entities.Contact;
import com.progmob.android.friendkeeper.ui.activities.AddContactActivity;
import com.progmob.android.friendkeeper.ui.adapters.ContactListAdapter;
import com.progmob.android.friendkeeper.utils.PreferencesManager;

import java.util.List;

/**
 * A classe ContactListFragment é responsável por exibir a lista de contatos do usuário.
 * Ela inclui um RecyclerView para mostrar os contatos e um FloatingActionButton para
 * adicionar novos contatos. A lista de contatos é carregada do banco de dados e atualizada
 * na interface do usuário.
 */
public class ContactListFragment extends Fragment {

    private ContactListAdapter adapter;
    private int userId;

    /**
     * Método chamado para inflar a view do fragmento.
     *
     * @param inflater           O LayoutInflater que pode ser usado para inflar qualquer view no fragmento.
     * @param container          Se não-nulo, este é o pai da view que o fragmento deve inflar.
     * @param savedInstanceState Se não-nulo, este fragmento está sendo reconstruído a partir de um estado salvo anteriormente.
     * @return A View para o layout do fragmento.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);

        RecyclerView recyclerViewContacts = view.findViewById(R.id.recyclerViewContacts);
        recyclerViewContacts.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ContactListAdapter(getActivity());
        recyclerViewContacts.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.floating_action_button);
        fab.setOnClickListener(v -> openAddContactActivity());

        PreferencesManager preferencesManager = PreferencesManager.getInstance(getActivity());
        userId = preferencesManager.getUserId();

        loadContacts();

        return view;
    }

    /**
     * Carrega a lista de contatos do banco de dados em uma nova thread e atualiza o adaptador.
     */
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

    /**
     * Abre a atividade para adicionar um novo contato.
     */
    private void openAddContactActivity() {
        Intent intent = new Intent(getActivity(), AddContactActivity.class);
        startActivity(intent);
    }
}