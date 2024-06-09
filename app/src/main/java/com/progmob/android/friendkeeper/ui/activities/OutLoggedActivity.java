package com.progmob.android.friendkeeper.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.progmob.android.friendkeeper.R;
import com.progmob.android.friendkeeper.database.AppDatabase;
import com.progmob.android.friendkeeper.entities.User;
import com.progmob.android.friendkeeper.ui.fragments.LoginFragment;
import com.progmob.android.friendkeeper.ui.fragments.RegisterFragment;

public class OutLoggedActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener {

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verifica se o usuário já está logado
        if (isLoggedIn()) {
            redirectToLoggedActivity();
            return;
        }

        setContentView(R.layout.activity_logged_out);

        db = AppDatabase.getDatabase(getApplicationContext());

        if (savedInstanceState == null) {
            loadFragment(new LoginFragment());
        }
    }

    private boolean isLoggedIn() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return preferences.getBoolean("is_logged_in", false);
    }

    private void redirectToLoggedActivity() {
        Intent intent = new Intent(OutLoggedActivity.this, LoggedActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_activity_logged_out, fragment);
        transaction.commit();
    }

    @Override
    public void onLogin(String email, String password) {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
            boolean status = db.userDao().verifyUserLogin(email, password);
            User user = db.userDao().getUserByEmail(email);

            runOnUiThread(() -> {
                if (status && user != null) {
                    saveLoginState(user.id);
                    redirectToLoggedActivity();
                } else {
                    Toast.makeText(OutLoggedActivity.this, R.string.login_invalido, Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void saveLoginState(int userId) {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("is_logged_in", true);
        editor.putInt("user_id", userId);
        editor.apply();
    }


    @Override
    public void onRegister(String name, String email, String password) {
        new Thread(() -> {
            boolean status = db.userDao().createUser(name, email, password);

            runOnUiThread(() -> {
                if (status) {
                    Toast.makeText(this, R.string.usuario_registrado, Toast.LENGTH_SHORT).show();
                    loadFragment(new LoginFragment());
                } else {
                    Toast.makeText(this, R.string.erro_registro_usuario, Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    @Override
    public void onRegisterClicked() {
        loadFragment(new RegisterFragment());
    }

    @Override
    public void onLoginClicked() {
        loadFragment(new LoginFragment());
    }
}
