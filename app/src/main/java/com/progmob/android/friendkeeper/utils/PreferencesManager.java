package com.progmob.android.friendkeeper.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * PreferencesManager
 * <p>
 * Classe responsável por gerenciar as preferências compartilhadas (SharedPreferences)
 * do aplicativo, incluindo o ID do usuário, idioma e estado do modo noturno.
 */
public class PreferencesManager {

    private static final String PREFS_NAME = "user_prefs";
    private static final String USER_ID_KEY = "user_id";
    private static final String LANGUAGE_KEY = "language";
    private static final String NIGHT_MODE_KEY = "night_mode";
    private static PreferencesManager instance;
    private static final String TAG = "PreferencesManager";
    private final SharedPreferences sharedPreferences;

    /**
     * Construtor privado para evitar instâncias externas.
     *
     * @param context O contexto da aplicação.
     */
    private PreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Obtém a instância singleton de PreferencesManager.
     *
     * @param context O contexto da aplicação.
     * @return A instância de PreferencesManager.
     */
    public static PreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new PreferencesManager(context);
        }
        return instance;
    }

    /**
     * Salva o ID do usuário nas preferências compartilhadas.
     *
     * @param userId O ID do usuário.
     */
    public void saveUserId(int userId) {
        try {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(USER_ID_KEY, userId);
            editor.apply();
        } catch (Exception e) {
            Log.e(TAG, "Erro ao salvar userId", e);
        }
    }

    /**
     * Obtém o ID do usuário das preferências compartilhadas.
     *
     * @return O ID do usuário, ou -1 se não estiver definido.
     */
    public int getUserId() {
        return sharedPreferences.getInt(USER_ID_KEY, -1);
    }

    /**
     * Limpa o ID do usuário das preferências compartilhadas.
     */
    public void clearUserId() {
        try {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(USER_ID_KEY);
            editor.apply();
        } catch (Exception e) {
            Log.e(TAG, "Erro ao limpar userId", e);
        }
    }

    /**
     * Salva o idioma nas preferências compartilhadas.
     *
     * @param language O código do idioma a ser salvo.
     */
    public void saveLanguage(String language) {
        try {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(LANGUAGE_KEY, language);
            editor.apply();
        } catch (Exception e) {
            Log.e(TAG, "Erro ao salvar language", e);
        }
    }

    /**
     * Obtém o idioma das preferências compartilhadas.
     *
     * @return O código do idioma salvo, ou "pt" se não estiver definido.
     */
    public String getLanguage() {
        return sharedPreferences.getString(LANGUAGE_KEY, "pt");
    }

    /**
     * Salva o estado do modo noturno nas preferências compartilhadas.
     *
     * @param isEnabled true para ativar o modo noturno, false para desativar.
     */
    public void saveNightModeState(boolean isEnabled) {
        try {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(NIGHT_MODE_KEY, isEnabled);
            editor.apply();
        } catch (Exception e) {
            Log.e(TAG, "Erro ao salvar night mode", e);
        }
    }

    /**
     * Verifica se o modo noturno está ativado nas preferências compartilhadas.
     *
     * @return true se o modo noturno estiver ativado, caso contrário false.
     */
    public boolean isNightModeEnabled() {
        return sharedPreferences.getBoolean(NIGHT_MODE_KEY, false);
    }
}
