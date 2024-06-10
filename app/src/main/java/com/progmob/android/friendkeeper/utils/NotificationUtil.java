package com.progmob.android.friendkeeper.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.progmob.android.friendkeeper.R;

import java.util.Calendar;

/**
 * NotificationUtil
 * <p>
 * Classe utilitária para gerenciar notificações no aplicativo FriendKeeper.
 * Fornece métodos para criar canais de notificação, agendar notificações e
 * exibir notificações ao usuário.
 */
public class NotificationUtil {

    // ID do canal de notificação
    private static final String CHANNEL_ID = "FriendKeeperChannel";

    // Código de solicitação para a permissão de post de notificações
    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 1001;

    /**
     * Método createNotificationChannel
     * <p>
     * Cria um canal de notificação com as configurações especificadas.
     * Este canal é necessário para exibir notificações no Android 8.0 (API nível 26) e superior.
     *
     * @param context O contexto da aplicação ou atividade.
     */
    public static void createNotificationChannel(Context context) {
        CharSequence name = "FriendKeeperChannel";
        String description = "Channel for FriendKeeper";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        // Cria um novo canal de notificação com as configurações especificadas
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        // Obtém o gerenciador de notificações e cria o canal de notificação
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    /**
     * Método scheduleNotification
     * <p>
     * Agenda uma notificação para ser exibida em um momento específico.
     * Utiliza AlarmManager para agendar a notificação.
     *
     * @param context O contexto da aplicação ou atividade.
     * @param title O título da notificação.
     * @param message A mensagem da notificação.
     * @param calendar O calendário com a data e hora para exibir a notificação.
     */
    public static void scheduleNotification(Context context, String title, String message, Calendar calendar) {
        // Verifica se pode agendar alarmes exatos
        if (AlarmUtil.canScheduleExactAlarms(context)) {
            // Cria uma intenção para o NotificationReceiver com os dados da notificação
            Intent intent = new Intent(context, NotificationReceiver.class);
            intent.putExtra("title", title);
            intent.putExtra("message", message);

            // Cria um PendingIntent para ser acionado pelo AlarmManager
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            try {
                // Obtém o AlarmManager e agenda a notificação para o momento especificado
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } catch (SecurityException e) {
                // Trata a exceção caso a permissão não tenha sido concedida
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    AlarmUtil.requestExactAlarmPermission(context);
                }
            }
        } else {
            // Solicita a permissão para agendar alarmes exatos se necessário
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                AlarmUtil.requestExactAlarmPermission(context);
            }
        }
    }

    /**
     * Método showNotification
     * <p>
     * Exibe uma notificação ao usuário.
     * Este método requer a permissão POST_NOTIFICATIONS no Android 13 (API nível 33) e superior.
     *
     * @param context O contexto da aplicação ou atividade.
     * @param title O título da notificação.
     * @param message A mensagem da notificação.
     */
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static void showNotification(Context context, String title, String message) {
        // Cria um construtor de notificação com os dados fornecidos
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Obtém o gerenciador de notificações
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // Verifica se a permissão POST_NOTIFICATIONS foi concedida
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Solicita a permissão se não foi concedida
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_POST_NOTIFICATIONS);
            return;
        }

        // Exibe a notificação
        notificationManager.notify(1, builder.build());
    }
}