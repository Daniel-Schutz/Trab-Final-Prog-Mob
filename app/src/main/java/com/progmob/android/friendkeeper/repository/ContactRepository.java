package com.progmob.android.friendkeeper.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.progmob.android.friendkeeper.dao.ContactDao;
import com.progmob.android.friendkeeper.database.AppDatabase;
import com.progmob.android.friendkeeper.entities.Contact;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContactRepository {
    private final ContactDao contactDao;
    private final LiveData<List<Contact>> allContacts;
    private final ExecutorService executorService;

    public ContactRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        contactDao = db.contactDao();
        allContacts = contactDao.getAllContacts();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Contact cont) {
        executorService.execute(() -> {
            Contact contact = new Contact();
            contact.userId = cont.userId;
            contact.name = cont.name;
            contact.phoneNumber = cont.phoneNumber;
            contact.email = cont.email;
            contact.address = cont.address;
            contact.birthdate = cont.birthdate;
            contact.photoUri = cont.photoUri;
            contact.location = cont.location;
            contactDao.insert(contact);
        });
    }

    public void update(Contact cont) {
        executorService.execute(() -> {
            Contact contact = contactDao.getContactById(cont.id);
            if (contact != null) {
                contact.name = cont.name;
                contact.phoneNumber = cont.phoneNumber;
                contact.email = cont.email;
                contact.address = cont.address;
                contact.birthdate = cont.birthdate;
                contact.photoUri = cont.photoUri;
                contact.location = cont.location;
                contactDao.update(contact);
            }
        });
    }

    public void delete(Contact cont) {
        executorService.execute(() -> {
            Contact contact = contactDao.getContactById(cont.id);
            if (contact != null) {
                contactDao.delete(contact);
            }
        });
    }

    public List<Contact> listContacts(int userId) {
        return contactDao.getContactsByUserId(userId);
    }

    public LiveData<List<Contact>> getAllContacts() {
        return allContacts;
    }
}
