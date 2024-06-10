package com.progmob.android.friendkeeper.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.progmob.android.friendkeeper.dao.AddressDao;
import com.progmob.android.friendkeeper.dao.ContactDao;
import com.progmob.android.friendkeeper.dao.ReminderDao;
import com.progmob.android.friendkeeper.dao.UserDao;
import com.progmob.android.friendkeeper.entities.Address;
import com.progmob.android.friendkeeper.entities.Contact;
import com.progmob.android.friendkeeper.entities.Reminder;
import com.progmob.android.friendkeeper.entities.User;


/**
 * AppDatabase é a classe principal do Room Database para o aplicativo FriendKeeper.
 * Define a configuração do banco de dados, as entidades que ele gerencia e fornece métodos abstratos
 * para acessar os DAOs (Data Access Objects) correspondentes.
 */
@Database(entities = {User.class, Contact.class, Reminder.class, Address.class}, version = 1, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {

    /**
     * @return o DAO para a entidade Contact.
     */
    public abstract ContactDao contactDao();

    /**
     * @return o DAO para a entidade User.
     */
    public abstract UserDao userDao();

    /**
     * @return o DAO para a entidade Reminder.
     */
    public abstract ReminderDao reminderDao();

    /**
     * @return o DAO para a entidade Address.
     */
    public abstract AddressDao addressDao();


    /**
     * Instância única da base de dados.
     * A anotação volatile garante que a variável é visível em todos os threads.
     */
    private static volatile AppDatabase INSTANCE;

    /**
     * Método para obter a instância do banco de dados.
     * Cria a base de dados se ela ainda não existir.
     *
     * @param context O contexto da aplicação.
     * @return A instância do banco de dados.
     */
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "friendkeeper_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
