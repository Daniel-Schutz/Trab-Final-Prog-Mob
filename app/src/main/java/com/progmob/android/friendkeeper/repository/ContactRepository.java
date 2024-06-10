package com.progmob.android.friendkeeper.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.progmob.android.friendkeeper.dao.ContactDao;
import com.progmob.android.friendkeeper.database.AppDatabase;
import com.progmob.android.friendkeeper.entities.Contact;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A classe ContactRepository é responsável por gerenciar as operações de banco de dados para a entidade Contact.
 * Ela utiliza o padrão Repository para abstrair a interação com o banco de dados e fornecer uma camada de acesso aos dados.
 */
public class ContactRepository {
    // Data Access Object (DAO) para a entidade Contact
    private final ContactDao contactDao;
    // LiveData que contém a lista de todos os contatos
    private final LiveData<List<Contact>> allContacts;
    // ExecutorService para executar operações de banco de dados em uma thread separada
    private final ExecutorService executorService;

    /**
     * Construtor da classe ContactRepository.
     * Inicializa o banco de dados, o DAO de contatos e o ExecutorService.
     *
     * @param application A aplicação que está utilizando o repositório.
     */
    public ContactRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        contactDao = db.contactDao();
        allContacts = contactDao.getAllContacts();
        executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Insere um novo contato no banco de dados.
     * Executa a operação em uma thread separada.
     *
     * @param cont O contato a ser inserido.
     */
    public void insert(Contact cont) {
        executorService.execute(() -> {
            Contact contact = new Contact();
            contact.userId = cont.userId;
            contact.name = cont.name;
            contact.phoneNumber = cont.phoneNumber;
            contact.email = cont.email;
            contact.addressId = cont.addressId;
            contact.birthdate = cont.birthdate;
            contact.photoUri = cont.photoUri;
            contactDao.insert(contact);
        });
    }

    /**
     * Atualiza um contato existente no banco de dados.
     * Executa a operação em uma thread separada.
     *
     * @param cont O contato com as novas informações a serem atualizadas.
     */
    public void update(Contact cont) {
        executorService.execute(() -> {
            Contact contact = contactDao.getContactById(cont.id);
            if (contact != null) {
                contact.name = cont.name;
                contact.phoneNumber = cont.phoneNumber;
                contact.email = cont.email;
                contact.addressId = cont.addressId;
                contact.birthdate = cont.birthdate;
                contact.photoUri = cont.photoUri;
                contactDao.update(contact);
            }
        });
    }

    /**
     * Deleta um contato existente no banco de dados.
     * Executa a operação em uma thread separada.
     *
     * @param cont O contato a ser deletado.
     */
    public void delete(Contact cont) {
        executorService.execute(() -> {
            Contact contact = contactDao.getContactById(cont.id);
            if (contact != null) {
                contactDao.delete(contact);
            }
        });
    }

    /**
     * Retorna a lista de contatos de um usuário específico.
     *
     * @param userId O ID do usuário cujos contatos devem ser retornados.
     * @return A lista de contatos do usuário.
     */
    public List<Contact> listContacts(int userId) {
        return contactDao.getContactsByUserId(userId);
    }

    /**
     * Retorna todos os contatos como um LiveData.
     *
     * @return Um LiveData contendo a lista de todos os contatos.
     */
    public LiveData<List<Contact>> getAllContacts() {
        return allContacts;
    }
}

