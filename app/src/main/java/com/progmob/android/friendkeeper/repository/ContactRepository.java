package com.progmob.android.friendkeeper.repository;

import com.progmob.android.friendkeeper.dao.ContactDao;
import com.progmob.android.friendkeeper.entities.Contact;

import java.util.List;

public class ContactRepository {
    private final ContactDao contactDao;

    public ContactRepository(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    public void addContact(int userId, String name, String phoneNumber, String email, String address, String birthdate, String photoUri, String location) {
        Contact contact = new Contact();
        contact.userId = userId;
        contact.name = name;
        contact.phoneNumber = phoneNumber;
        contact.email = email;
        contact.address = address;
        contact.birthdate = birthdate;
        contact.photoUri = photoUri;
        contact.location = location;
        contactDao.insert(contact);
    }

    public void editContact(int contactId, String name, String phoneNumber, String email, String address, String birthdate, String photoUri, String location) {
        Contact contact = contactDao.getContactById(contactId);
        if (contact != null) {
            contact.name = name;
            contact.phoneNumber = phoneNumber;
            contact.email = email;
            contact.address = address;
            contact.birthdate = birthdate;
            contact.photoUri = photoUri;
            contact.location = location;
            contactDao.update(contact);
        }
    }

    public void deleteContact(int contactId) {
        Contact contact = contactDao.getContactById(contactId);
        if (contact != null) {
            contactDao.delete(contact);
        }
    }

    public List<Contact> listContacts(int userId) {
        return contactDao.getContactsByUserId(userId);
    }
}
