package com.progmob.android.friendkeeper.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.progmob.android.friendkeeper.dao.ContactDao;
import com.progmob.android.friendkeeper.entities.Contact;
import com.progmob.android.friendkeeper.repository.ContactRepository;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {

    private final ContactRepository mRepository;
    private final LiveData<List<Contact>> mAllContacts;

    public ContactViewModel(Application application) {
        super(application);
        mRepository = new ContactRepository(application);
        mAllContacts = mRepository.getAllContacts();
    }

    public LiveData<List<Contact>> getAllContacts() {
        return mAllContacts;
    }

    public void insert(Contact contact) {
        mRepository.insert(contact);
    }

    public void delete(Contact contact) {
        mRepository.delete(contact);
    }

    public void update(Contact contact) {
        mRepository.update(contact);
    }
}
