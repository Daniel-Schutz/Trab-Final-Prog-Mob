package com.progmob.android.friendkeeper.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.progmob.android.friendkeeper.entities.Contact;

import java.util.List;

/**
 * Interface ContactDao é um DAO (Data Access Object) para a entidade Contact.
 * Fornece métodos para realizar operações CRUD (Create, Read, Update, Delete) no banco de dados.
 */
@Dao
public interface ContactDao {

    /**
     * Insere um novo contato no banco de dados.
     *
     * @param contact A entidade Contact a ser inserida.
     */
    @Insert
    void insert(Contact contact);

    /**
     * Atualiza um contato existente no banco de dados.
     *
     * @param contact A entidade Contact com as novas informações.
     */
    @Update
    void update(Contact contact);

    /**
     * Deleta um contato do banco de dados.
     *
     * @param contact A entidade Contact a ser deletada.
     */
    @Delete
    void delete(Contact contact);

    /**
     * Busca um contato pelo seu ID.
     *
     * @param id O ID do contato.
     * @return A entidade Contact correspondente ao ID fornecido.
     */
    @Query("SELECT * FROM Contact WHERE id = :id")
    Contact getContactById(int id);

    /**
     * Busca todos os contatos associados a um determinado usuário pelo ID do usuário.
     *
     * @param userId O ID do usuário.
     * @return Uma lista de entidades Contact associadas ao ID do usuário fornecido.
     */
    @Query("SELECT * FROM Contact WHERE userId = :userId")
    List<Contact> getContactsByUserId(int userId);

    /**
     * Busca todos os contatos no banco de dados.
     *
     * @return Um LiveData contendo uma lista de todas as entidades Contact no banco de dados.
     */
    @Query("SELECT * FROM Contact")
    LiveData<List<Contact>> getAllContacts();


    /**
     * Busca todos os contatos pelo atributo birthdate usando uma busca parcial.
     *
     * @param partialBirthdate Parte da data de nascimento (formato "dd/MM").
     * @return Uma lista de entidades Contact cuja data de nascimento contém a parte fornecida.
     */
    @Query("SELECT * FROM Contact WHERE birthdate LIKE '%' || :partialBirthdate || '%'")
    List<Contact> getContactsByPartialBirthdate(String partialBirthdate);
}

