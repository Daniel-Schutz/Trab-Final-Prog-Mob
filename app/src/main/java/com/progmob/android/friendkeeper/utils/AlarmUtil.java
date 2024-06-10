package com.progmob.android.friendkeeper.utils;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.progmob.android.friendkeeper.R;

/**
 * AlarmUtil
 * <p>
 * Classe utilitária para gerenciar permissões e agendamentos de alarmes exatos no Android.
 * Esta classe fornece métodos para verificar se a permissão para agendar alarmes exatos está disponível
 * e para solicitar essa permissão ao usuário, se necessário.
 */
public class AlarmUtil {

    /**
     * Verifica se o aplicativo tem permissão para agendar alarmes exatos.
     *
     * @param context O contexto da aplicação ou atividade.
     * @return true se o aplicativo pode agendar alarmes exatos, false caso contrário.
     */
    public static boolean canScheduleExactAlarms(Context context) {
        // Obtém o serviço de gerenciamento de alarmes do sistema
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Verifica se a versão do Android é igual ou superior a S (Android 12)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return alarmManager.canScheduleExactAlarms();
        }

        return false;
    }

    /**
     * Solicita ao usuário a permissão para agendar alarmes exatos, se não já estiver concedida.
     *
     * @param context O contexto da aplicação ou atividade.
     */
    @RequiresApi(api = Build.VERSION_CODES.S)
    public static void requestExactAlarmPermission(Context context) {
        // Verifica se o aplicativo já tem permissão para agendar alarmes exatos
        if (!canScheduleExactAlarms(context)) {
            // Cria um diálogo de alerta para informar o usuário sobre a necessidade da permissão
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.perm_necessaria)
                    .setMessage(R.string.perg_perm_agendar_alarmes_exatos)
                    .setPositiveButton(R.string.sim, (dialog, which) -> {
                        // Cria uma intenção para abrir a tela de configurações para solicitar a permissão de alarme exato
                        Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                        context.startActivity(intent);
                    })
                    .setNegativeButton(R.string.nao, (dialog, which) -> {
                        // Fecha o diálogo se o usuário recusar a permissão
                        dialog.dismiss();
                    })
                    .show();
        }
    }
}
