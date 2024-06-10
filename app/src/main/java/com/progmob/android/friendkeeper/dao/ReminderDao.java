package com.progmob.android.friendkeeper.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.progmob.android.friendkeeper.entities.Reminder;

import java.util.List;

/**
 * Interface ReminderDao é um DAO (Data Access Object) para a entidade Reminder.
 * Fornece métodos para realizar operações CRUD (Create, Read, Update, Delete) no banco de dados.
 */
@Dao
public interface ReminderDao {

    /**
     * Insere um novo lembrete no banco de dados.
     *
     * @param reminder A entidade Reminder a ser inserida.
     */
    @Insert
    void insert(Reminder reminder);

    /**
     * Atualiza um lembrete existente no banco de dados.
     *
     * @param reminder A entidade Reminder com as novas informações.
     */
    @Update
    void update(Reminder reminder);

    /**
     * Deleta um lembrete do banco de dados.
     *
     * @param reminder A entidade Reminder a ser deletada.
     */
    @Delete
    void delete(Reminder reminder);

    /**
     * Busca todos os lembretes associados a um determinado contato pelo ID do contato.
     *
     * @param contactId O ID do contato.
     * @return Uma lista de entidades Reminder associadas ao ID do contato fornecido.
     */
    @Query("SELECT * FROM Reminder WHERE contactId = :contactId")
    List<Reminder> getRemindersByContactId(int contactId);

    /**
     * Busca um lembrete pelo seu ID.
     *
     * @param id O ID do lembrete.
     * @return A entidade Reminder correspondente ao ID fornecido.
     */
    @Query("SELECT * FROM Reminder WHERE id = :id")
    Reminder getReminderById(int id);
}
