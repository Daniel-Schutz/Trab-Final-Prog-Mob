package com.progmob.android.friendkeeper.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.progmob.android.friendkeeper.entities.Address;

/**
 * Interface AddressDao é um DAO (Data Access Object) para a entidade Address.
 * Fornece métodos para realizar operações CRUD (Create, Read, Update, Delete) no banco de dados.
 */
@Dao
public interface AddressDao {

    /**
     * Insere um novo endereço no banco de dados.
     *
     * @param address A entidade Address a ser inserida.
     * @return O ID da nova entrada inserida.
     */
    @Insert
    long insert(Address address);

    /**
     * Atualiza um endereço existente no banco de dados.
     *
     * @param address A entidade Address com as novas informações.
     */
    @Update
    void update(Address address);

    /**
     * Deleta um endereço do banco de dados.
     *
     * @param address A entidade Address a ser deletada.
     */
    @Delete
    void delete(Address address);

    /**
     * Busca um endereço pelo seu ID.
     *
     * @param id O ID do endereço.
     * @return A entidade Address correspondente ao ID fornecido.
     */
    @Query("SELECT * FROM Address WHERE id = :id")
    Address getAddressById(int id);
}

