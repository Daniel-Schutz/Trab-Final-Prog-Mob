package com.progmob.android.friendkeeper.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.progmob.android.friendkeeper.R;
import com.progmob.android.friendkeeper.utils.PreferencesManager;
import com.progmob.android.friendkeeper.ui.fragments.ContactListFragment;

import java.util.Locale;
import java.util.Objects;

/**
 * LoggedActivity
 * <p>
 * Classe responsável por gerenciar a atividade principal do usuário logado.
 * Esta classe lida com a exibição de fragmentos, configuração de preferências
 * como modo noturno e idioma, e gerenciamento do estado de login.
 */
public class LoggedActivity extends AppCompatActivity {

    // TAG para logs
    private static final String TAG = "LoggedActivity";
    private PreferencesManager preferencesManager;

    /**
     * Método onCreate
     * <p>
     * Inicializa a atividade, configura a barra de ferramentas, carrega o fragmento
     * de lista de contatos e verifica o estado de login do usuário.
     *
     * @param savedInstanceState O estado anterior da instância, se disponível.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesManager = PreferencesManager.getInstance(this);
        applySavedLanguage();
        Log.d(TAG, "onCreate: Início");
        setContentView(R.layout.activity_logged);

        try {
            Toolbar toolbar = findViewById(R.id.barraFerramentas);
            setSupportActionBar(toolbar);
            Log.d(TAG, "onCreate: Toolbar configurada com sucesso");
        } catch (Exception e) {
            Log.e(TAG, "Erro ao configurar a toolbar: ", e);
        }

        if (savedInstanceState == null) {
            loadContactListFragment();
            Log.d(TAG, "onCreate: Carregar fragmento de lista de contatos");
        }

        if (notLoggedIn()) {
            redirectToLogin();
            Log.d(TAG, "onCreate: Usuário não está logado, redirecionando para login");
        }
    }

    /**
     * Verifica se o usuário está logado.
     *
     * @return true se o usuário estiver logado, caso contrário false.
     */
    private boolean notLoggedIn() {
        return preferencesManager.getUserId() == -1;
    }

    /**
     * Redireciona o usuário para a tela de login.
     */
    private void redirectToLogin() {
        Intent intent = new Intent(LoggedActivity.this, OutLoggedActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Método onResume
     * <p>
     * Verifica o estado de login do usuário e carrega o fragmento de lista de contatos se estiver logado.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Início");
        if (notLoggedIn()) {
            redirectToLogin();
            Log.d(TAG, "onResume: Usuário não está logado, redirecionando para login");
        } else {
            loadContactListFragment();
            Log.d(TAG, "onResume: Carregar fragmento de lista de contatos");
        }
    }

    /**
     * Carrega o fragmento de lista de contatos.
     */
    private void loadContactListFragment() {
        int userId = preferencesManager.getUserId();
        ContactListFragment fragment = new ContactListFragment();
        Bundle args = new Bundle();
        args.putInt("user_id", userId);
        fragment.setArguments(args);

        loadFragment(fragment);
        Log.d(TAG, "loadContactListFragment: Fragmento de lista de contatos carregado");
    }

    /**
     * Substitui o fragmento atual pelo fragmento fornecido.
     *
     * @param fragment O fragmento a ser carregado.
     */
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_activity_logged, fragment);
        transaction.commit();
        Log.d(TAG, "loadFragment: Transação de fragmento cometida");
    }

    /**
     * Método onCreateOptionsMenu
     * <p>
     * Infla o menu da barra de ferramentas e configura o interruptor de modo noturno.
     *
     * @param menu O menu a ser inflado.
     * @return true se o menu foi criado com sucesso.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        Log.d(TAG, "onCreateOptionsMenu: Menu inflado");

        MenuItem nightModeItem = menu.findItem(R.id.action_night_mode);
        nightModeItem.setActionView(R.layout.menu_item_switch);

        SwitchCompat nightModeSwitch = Objects.requireNonNull(nightModeItem.getActionView()).findViewById(R.id.switch_night_mode);
        if (nightModeSwitch != null) {
            nightModeSwitch.setChecked(preferencesManager.isNightModeEnabled());

            nightModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                preferencesManager.saveNightModeState(isChecked);
            });
        }

        return true;
    }

    /**
     * Método onOptionsItemSelected
     * <p>
     * Trata a seleção de itens no menu da barra de ferramentas, incluindo mudança de idioma e logout.
     *
     * @param item O item selecionado.
     * @return true se o item foi tratado com sucesso.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // TODO: Ao trocar os idiomas as strings não são atualizadas na hora. Diferente do modo noturno que é atualizado na hora.
        if (id == R.id.action_language_english) {
            setLocale("en");
            return true;

        } else if (id == R.id.action_language_portuguese) {
            setLocale("pt");
            return true;

        } else if (id == R.id.action_language_spanish) {
            setLocale("es");
            return true;

        } else if (id == R.id.action_language_french) {
            setLocale("fr");
            return true;

        } else if (id == R.id.action_night_mode) {
            SwitchCompat nightModeSwitch = (SwitchCompat) Objects.requireNonNull(item.getActionView()).findViewById(R.id.switch_night_mode);
            if (nightModeSwitch != null) {
                nightModeSwitch.setChecked(!nightModeSwitch.isChecked());
                if (nightModeSwitch.isChecked()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                preferencesManager.saveNightModeState(nightModeSwitch.isChecked());
                recreate();
            }
            return true;

        } else if (id == R.id.action_logout) {
            logout();
            return true;

        } else if (id == R.id.action_permission_settings) {
            Intent intent = new Intent(this, PermissionSettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Define o idioma do aplicativo e salva nas preferências.
     *
     * @param lang O código do idioma a ser definido.
     */
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Context context = getBaseContext();
        android.content.res.Configuration config = context.getResources().getConfiguration();

        config.setLocale(locale);
        context.createConfigurationContext(config);

        preferencesManager.saveLanguage(lang);

        if (!locale.getLanguage().equals(Locale.getDefault().getLanguage())) {
            recreate();
        }
    }

    /**
     * Aplica o idioma salvo nas preferências.
     */
    private void applySavedLanguage() {
        String lang = preferencesManager.getLanguage();
        setLocale(lang);
    }

    /**
     * Realiza o logout do usuário e redireciona para a tela de login.
     */
    private void logout() {
        preferencesManager.clearUserId();
        redirectToLogin();
    }
}
