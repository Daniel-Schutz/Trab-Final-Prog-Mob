package com.progmob.android.friendkeeper.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.ContextCompat;

/**
 * PermissionManager é uma classe responsável por gerenciar as permissões necessárias para a aplicação.
 * Ele facilita a verificação e a solicitação de permissões com base na versão do Android.
 */
public class PermissionManager {

    // A atividade atual
    private final Activity activity;

    // Permissões necessárias para dispositivos com Android abaixo da versão 33
    private final String[] requiredPermissions;

    // Permissões necessárias para dispositivos com Android 33 e acima
    private final String[] requiredPermissions33AndAbove;

    // Tag para logs
    private static final String TAG = "PermissionManager";

    /**
     * Construtor da classe PermissionManager.
     *
     * @param activity A atividade atual.
     * @param requiredPermissions As permissões necessárias para Android abaixo da versão 33.
     * @param requiredPermissions33AndAbove As permissões necessárias para Android 33 e acima.
     */
    public PermissionManager(Activity activity, String[] requiredPermissions, String[] requiredPermissions33AndAbove) {
        this.activity = activity;
        this.requiredPermissions = requiredPermissions;
        this.requiredPermissions33AndAbove = requiredPermissions33AndAbove;
    }

    /**
     * Obtém as permissões necessárias com base na versão do Android.
     *
     * @return Um array de strings com as permissões necessárias.
     */
    public String[] getRequiredPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return requiredPermissions33AndAbove;
        } else {
            return requiredPermissions;
        }
    }

    /**
     * Verifica se todas as permissões necessárias foram concedidas.
     *
     * @return True se todas as permissões foram concedidas, false caso contrário.
     */
    public boolean allPermissionsGranted() {
        for (String permission : getRequiredPermissions()) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Solicita as permissões necessárias usando o launcher fornecido.
     *
     * @param launcher O ActivityResultLauncher usado para solicitar permissões.
     */
    public void requestPermissions(ActivityResultLauncher<String[]> launcher) {
        launcher.launch(getRequiredPermissions());
    }
}