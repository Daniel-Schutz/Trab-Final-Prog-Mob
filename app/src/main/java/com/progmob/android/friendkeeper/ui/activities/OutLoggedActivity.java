package com.progmob.android.friendkeeper.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.progmob.android.friendkeeper.R;
import com.progmob.android.friendkeeper.database.AppDatabase;
import com.progmob.android.friendkeeper.entities.User;
import com.progmob.android.friendkeeper.ui.fragments.LoginFragment;
import com.progmob.android.friendkeeper.ui.fragments.RegisterFragment;
import com.progmob.android.friendkeeper.utils.PreferencesManager;

/**
 * OutLoggedActivity
 * <p>
 * Esta atividade gerencia a interface para usuários não logados. Permite aos usuários
 * fazer login ou registrar-se.
 */
public class OutLoggedActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener {

    private static final String TAG = "OutLoggedActivity";
    private AppDatabase db;
    private PreferencesManager preferencesManager;

    /**
     * Método onCreate
     * <p>
     * Inicializa a atividade, verifica o estado de login do usuário e carrega o fragmento de login se o usuário não estiver logado.
     *
     * @param savedInstanceState O estado anterior da instância, se disponível.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferencesManager = PreferencesManager.getInstance(this);

        // Verifica se o usuário já está logado
        if (isLoggedIn()) {
            redirectToLoggedActivity();
            return;
        }

        setContentView(R.layout.activity_logged_out);

        try {
            db = AppDatabase.getDatabase(getApplicationContext());

            if (savedInstanceState == null) {
                loadFragment(new LoginFragment());
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao inicializar o banco de dados ou carregar fragmento: ", e);
        }
    }

    /**
     * Verifica se o usuário está logado.
     *
     * @return true se o usuário estiver logado, false caso contrário.
     */
    private boolean isLoggedIn() {
        try {
            return preferencesManager.getUserId() != -1;
        } catch (Exception e) {
            Log.e(TAG, "Erro ao verificar se o usuário está logado: ", e);
            return false;
        }
    }

    /**
     * Redireciona para a atividade LoggedActivity se o usuário estiver logado.
     */
    private void redirectToLoggedActivity() {
        try {
            Intent intent = new Intent(OutLoggedActivity.this, LoggedActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Erro ao redirecionar para a atividade LoggedActivity: ", e);
        }
    }

    /**
     * Carrega o fragmento especificado na atividade.
     *
     * @param fragment O fragmento a ser carregado.
     */
    private void loadFragment(Fragment fragment) {
        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container_activity_logged_out, fragment);
            transaction.commit();
        } catch (Exception e) {
            Log.e(TAG, "Erro ao carregar o fragmento: ", e);
        }
    }

    @Override
    public void onLogin(String email, String password) {
        new Thread(() -> {
            try {
                AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
                boolean status = db.userDao().verifyUserLogin(email, password);
                User user = db.userDao().getUserByEmail(email);

                runOnUiThread(() -> {
                    if (status && user != null) {
                        preferencesManager.saveUserId(user.id);
                        redirectToLoggedActivity();
                    } else {
                        Toast.makeText(OutLoggedActivity.this, R.string.login_invalido, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Erro ao realizar login: ", e);
                runOnUiThread(() -> Toast.makeText(OutLoggedActivity.this, R.string.erro_login, Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    @Override
    public void onRegister(String name, String email, String password) {
        new Thread(() -> {
            try {
                boolean status = db.userDao().createUser(name, email, password);

                runOnUiThread(() -> {
                    if (status) {
                        Toast.makeText(this, R.string.usuario_registrado, Toast.LENGTH_SHORT).show();
                        loadFragment(new LoginFragment());
                    } else {
                        Toast.makeText(this, R.string.erro_registro_usuario, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Erro ao registrar usuário: ", e);
                runOnUiThread(() -> Toast.makeText(this, R.string.erro_registro_usuario, Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    @Override
    public void onRegisterClicked() {
        try {
            loadFragment(new RegisterFragment());
        } catch (Exception e) {
            Log.e(TAG, "Erro ao carregar fragmento de registro: ", e);
        }
    }

    @Override
    public void onLoginClicked() {
        try {
            loadFragment(new LoginFragment());
        } catch (Exception e) {
            Log.e(TAG, "Erro ao carregar fragmento de login: ", e);
        }
    }
}
