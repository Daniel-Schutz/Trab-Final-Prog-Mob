package com.progmob.android.friendkeeper.repository;

import com.progmob.android.friendkeeper.dao.ReminderDao;
import com.progmob.android.friendkeeper.entities.Reminder;

import java.util.List;

/**
 * A classe ReminderRepository é responsável por gerenciar as operações de banco de dados para a entidade Reminder.
 * Ela abstrai a interação com o banco de dados e fornece métodos para adicionar, editar, deletar e listar lembretes.
 */
public class ReminderRepository {
    // Data Access Object (DAO) para a entidade Reminder
    private final ReminderDao reminderDao;

    /**
     * Construtor da classe ReminderRepository.
     * Inicializa o DAO de lembretes.
     *
     * @param reminderDao O DAO de lembretes.
     */
    public ReminderRepository(ReminderDao reminderDao) {
        this.reminderDao = reminderDao;
    }

    /**
     * Adiciona um novo lembrete no banco de dados.
     *
     * @param contactId O ID do contato associado ao lembrete.
     * @param type O tipo do lembrete.
     * @param date A data do lembrete.
     * @param notificationMessage A mensagem de notificação do lembrete.
     */
    public void addReminder(int contactId, String type, String date, String notificationMessage) {
        Reminder reminder = new Reminder();
        reminder.contactId = contactId;
        reminder.type = type;
        reminder.date = date;
        reminder.notificationMessage = notificationMessage;
        reminderDao.insert(reminder);
    }

    /**
     * Edita um lembrete existente no banco de dados.
     *
     * @param reminderId O ID do lembrete a ser editado.
     * @param type O novo tipo do lembrete.
     * @param date A nova data do lembrete.
     * @param notificationMessage A nova mensagem de notificação do lembrete.
     */
    public void editReminder(int reminderId, String type, String date, String notificationMessage) {
        Reminder reminder = reminderDao.getReminderById(reminderId);
        if (reminder != null) {
            reminder.type = type;
            reminder.date = date;
            reminder.notificationMessage = notificationMessage;
            reminderDao.update(reminder);
        }
    }

    /**
     * Deleta um lembrete existente no banco de dados.
     *
     * @param reminderId O ID do lembrete a ser deletado.
     */
    public void deleteReminder(int reminderId) {
        Reminder reminder = reminderDao.getReminderById(reminderId);
        if (reminder != null) {
            reminderDao.delete(reminder);
        }
    }

    /**
     * Retorna a lista de lembretes associados a um contato específico.
     *
     * @param contactId O ID do contato cujos lembretes devem ser retornados.
     * @return A lista de lembretes do contato.
     */
    public List<Reminder> listReminders(int contactId) {
        return reminderDao.getRemindersByContactId(contactId);
    }
}

