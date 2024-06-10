package com.progmob.android.friendkeeper.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * NotificationReceiver
 * <p>
 * Classe que herda de BroadcastReceiver para tratar o recebimento de notificações.
 * Este receptor é acionado quando uma intenção de notificação é enviada, exibindo
 * a notificação ao usuário.
 */
public class NotificationReceiver extends BroadcastReceiver {

    /**
     * Método onReceive
     * <p>
     * Este método é chamado quando o BroadcastReceiver recebe uma intenção (intent).
     * Ele extrai o título e a mensagem da notificação a partir da intenção e, em seguida,
     * utiliza a classe NotificationUtil para exibir a notificação.
     *
     * @param context O contexto da aplicação ou atividade.
     * @param intent A intenção recebida contendo os dados da notificação.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        // Extrai o título da notificação da intenção recebida
        String title = intent.getStringExtra("title");

        // Extrai a mensagem da notificação da intenção recebida
        String message = intent.getStringExtra("message");

        // Verifica se a versão do Android é igual ou superior a TIRAMISU (Android 13)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Utiliza a classe NotificationUtil para exibir a notificação
            NotificationUtil.showNotification(context, title, message);
        }
    }
}

