package com.progmob.android.friendkeeper.ui.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import com.progmob.android.friendkeeper.R;
import com.progmob.android.friendkeeper.database.AppDatabase;
import com.progmob.android.friendkeeper.ui.fragments.LoginFragment;
import com.progmob.android.friendkeeper.ui.fragments.RegisterFragment;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener {

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "friendkeeper-db")
                .allowMainThreadQueries()
                .build();

        if (savedInstanceState == null) {
            loadFragment(new LoginFragment());
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    public void onLogin(String email, String password) {
        boolean isLoggedIn = db.userDao().verifyUserLogin(email, password);
        if (isLoggedIn) {
            Toast.makeText(this, "Login bem-sucedido", Toast.LENGTH_SHORT).show();
            // Continue para a próxima Activity ou fragment
        } else {
            Toast.makeText(this, "Falha no login", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRegisterClicked() {
        loadFragment(new RegisterFragment());
    }

    @Override
    public void onRegister(String name, String email, String password) {
        db.userDao().createUser(name, email, password);
        Toast.makeText(this, "Usuário registrado com sucesso", Toast.LENGTH_SHORT).show();
        loadFragment(new LoginFragment());
    }

    @Override
    public void onLoginClicked() {
        loadFragment(new LoginFragment());
    }
}
