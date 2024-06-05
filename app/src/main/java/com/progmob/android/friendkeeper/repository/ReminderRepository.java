package com.progmob.android.friendkeeper.repository;

import com.progmob.android.friendkeeper.dao.ReminderDao;
import com.progmob.android.friendkeeper.entities.Reminder;

import java.util.List;

public class ReminderRepository {
    private final ReminderDao reminderDao;

    public ReminderRepository(ReminderDao reminderDao) {
        this.reminderDao = reminderDao;
    }

    public void addReminder(int contactId, String type, String date, String notificationMessage) {
        Reminder reminder = new Reminder();
        reminder.contactId = contactId;
        reminder.type = type;
        reminder.date = date;
        reminder.notificationMessage = notificationMessage;
        reminderDao.insert(reminder);
    }

    public void editReminder(int reminderId, String type, String date, String notificationMessage) {
        Reminder reminder = reminderDao.getReminderById(reminderId);
        if (reminder != null) {
            reminder.type = type;
            reminder.date = date;
            reminder.notificationMessage = notificationMessage;
            reminderDao.update(reminder);
        }
    }

    public void deleteReminder(int reminderId) {
        Reminder reminder = reminderDao.getReminderById(reminderId);
        if (reminder != null) {
            reminderDao.delete(reminder);
        }
    }

    public List<Reminder> listReminders(int contactId) {
        return reminderDao.getRemindersByContactId(contactId);
    }
}
