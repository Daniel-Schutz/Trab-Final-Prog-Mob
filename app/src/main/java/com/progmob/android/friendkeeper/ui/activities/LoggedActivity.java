package com.progmob.android.friendkeeper.ui.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.progmob.android.friendkeeper.dao.ContactDao;
import com.progmob.android.friendkeeper.database.AppDatabase;
import com.progmob.android.friendkeeper.entities.Contact;
import com.progmob.android.friendkeeper.ui.activities.NotificationActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.progmob.android.friendkeeper.R;
import com.progmob.android.friendkeeper.utils.PreferencesManager;
import com.progmob.android.friendkeeper.ui.fragments.ContactListFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


import static android.Manifest.permission.POST_NOTIFICATIONS;
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
    private static final String CHANNEL_ID = "com.progmob.android.friendkeeper";
    private static final int PERMISSION_REQUEST_CODE = 200;



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


        createNotificationChannel(); // Crie o canal de notificação
        verifyNotifyPermission(); // Verifique a permissão de notificação

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
        } else {
            checkForBirthdays();
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


    /**
     * Verifica e solicita a permissão de notificação, se necessário.
     */
    private void verifyNotifyPermission() {
        if (!checkPermission()) {
            requestPermissions();
        }
    }

    /**
     * Verifica se a permissão de notificação foi concedida.
     *
     * @return true se a permissão foi concedida, caso contrário false.
     */
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), POST_NOTIFICATIONS);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Solicita a permissão de notificação.
     */
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
    }

    /**
     * Verifica se há aniversários hoje e exibe uma notificação, se houver.
     */
    private void checkForBirthdays() {
        new Thread(() -> {
            String currentDate = new SimpleDateFormat("dd/MM", Locale.getDefault()).format(new Date());
            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
            ContactDao contactDao = db.contactDao();
            List<Contact> contacts = contactDao.getContactsByPartialBirthdate(currentDate);

            for (Contact contact : contacts) {
                showBirthdayNotification(contact.getName());
            }
        }).start();
    }

    /**
     * Exibe uma notificação para um aniversário.
     *
     * @param contactName O nome do contato que faz aniversário.
     */
    private void showBirthdayNotification(String contactName) {
        Intent intent = new Intent(this, LoggedActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setContentTitle("Aniversário Hoje")
                .setContentText("Hoje é o aniversário de " + contactName + "!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            return;
        }

        notificationManager.notify(contactName.hashCode(), builder.build());
        //Toast.makeText(this, "Notificação de aniversário emitida", Toast.LENGTH_LONG).show();
    }

    /**
     * Cria o canal de notificação (necessário para Android 8.0 e superior).
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Birthday Notifications";
            String description = "Notificações para aniversários de contatos";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }







}

