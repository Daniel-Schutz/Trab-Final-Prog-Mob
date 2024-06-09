package com.progmob.android.friendkeeper.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.progmob.android.friendkeeper.ui.fragments.ContactListFragment;

import java.util.Locale;
import java.util.Objects;

public class LoggedActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "settings_prefs";
    private static final String NIGHT_MODE_KEY = "night_mode";
    private static final String LANGUAGE_KEY = "language";
    private static final String TAG = "LoggedActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        if (!isLoggedIn()) {
            redirectToLogin();
            Log.d(TAG, "onCreate: Usuário não está logado, redirecionando para login");
        }
    }

    private boolean isLoggedIn() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return preferences.getBoolean("is_logged_in", false);
    }

    private int getLoggedInUserId() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return preferences.getInt("user_id", -1);
    }

    private void redirectToLogin() {
        Intent intent = new Intent(LoggedActivity.this, OutLoggedActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Início");
        int userId = getLoggedInUserId();
        if (userId == -1) {
            redirectToLogin();
            Log.d(TAG, "onResume: Usuário não está logado, redirecionando para login");
        } else {
            loadContactListFragment();
            Log.d(TAG, "onResume: Carregar fragmento de lista de contatos");
        }
    }

    private void loadContactListFragment() {
        int userId = getLoggedInUserId();
        ContactListFragment fragment = new ContactListFragment();
        Bundle args = new Bundle();
        args.putInt("user_id", userId);
        fragment.setArguments(args);

        loadFragment(fragment);
        Log.d(TAG, "loadContactListFragment: Fragmento de lista de contatos carregado");
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_activity_logged, fragment);
        transaction.commit();
        Log.d(TAG, "loadFragment: Transação de fragmento cometida");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        Log.d(TAG, "onCreateOptionsMenu: Menu inflado");

        MenuItem nightModeItem = menu.findItem(R.id.action_night_mode);
        nightModeItem.setActionView(R.layout.menu_item_switch);

        SwitchCompat nightModeSwitch = Objects.requireNonNull(nightModeItem.getActionView()).findViewById(R.id.switch_night_mode);
        if (nightModeSwitch != null) {
            nightModeSwitch.setChecked(isNightModeEnabled());

            nightModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                saveNightModeState(isChecked);
            });
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_language_english) {
            setLocale("en");
            recreate();
            return true;
        } else if (id == R.id.action_language_portuguese) {
            setLocale("pt");
            recreate();
            return true;
        } else if (id == R.id.action_language_spanish) {
            setLocale("es");
            recreate();
            return true;
        } else if (id == R.id.action_language_french) {
            setLocale("fr");
            recreate();
            return true;
        } else if (id == R.id.action_night_mode) {
            SwitchCompat nightModeSwitch = (SwitchCompat) item.getActionView().findViewById(R.id.switch_night_mode);
            if (nightModeSwitch != null) {
                nightModeSwitch.setChecked(!nightModeSwitch.isChecked());
                if (nightModeSwitch.isChecked()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                saveNightModeState(nightModeSwitch.isChecked());
                recreate();
            }
            return true;
        } else if (id == R.id.action_logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private boolean isNightModeEnabled() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return preferences.getBoolean(NIGHT_MODE_KEY, false);
    }

    private void saveNightModeState(boolean isEnabled) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(NIGHT_MODE_KEY, isEnabled);
        editor.apply();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Context context = getBaseContext();
        android.content.res.Configuration config = context.getResources().getConfiguration();

        config.setLocale(locale);
        context.createConfigurationContext(config);

        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(LANGUAGE_KEY, lang);
        editor.apply();

        if (!locale.getLanguage().equals(Locale.getDefault().getLanguage())) {
            recreate();
        }
    }

    private void applySavedLanguage() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String lang = preferences.getString(LANGUAGE_KEY, "pt");
        setLocale(lang);
    }

    private void logout() {
        SharedPreferences.Editor editor = getSharedPreferences("user_prefs", MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();

        redirectToLogin();
    }
}
