package com.progmob.android.friendkeeper.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.progmob.android.friendkeeper.R;
import com.progmob.android.friendkeeper.database.AppDatabase;
import com.progmob.android.friendkeeper.entities.Address;
import com.progmob.android.friendkeeper.entities.Contact;
import com.progmob.android.friendkeeper.utils.ImageUtil;
import com.progmob.android.friendkeeper.utils.PermissionManager;
import com.progmob.android.friendkeeper.utils.PreferencesManager;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * AddContactActivity é uma Activity responsável por fornecer uma interface de usuário para adicionar novos contatos
 * ao aplicativo FriendKeeper. Ele permite que os usuários insiram informações do contato, capturem ou selecionem uma
 * foto, e associem um endereço ao contato. As permissões de armazenamento são gerenciadas de acordo com a versão do
 * Android em execução.
 */
public class AddContactActivity extends AppCompatActivity {

    private int userId;

    // Permissões necessárias para dispositivos com Android abaixo da versão 33
    private static final String[] REQUIRED_PERMISSIONS_BELOW_33 = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    // Permissões necessárias para dispositivos com Android 33 e acima
    @SuppressLint("InlinedApi")
    private static final String[] REQUIRED_PERMISSIONS_33_AND_ABOVE = new String[]{
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.CAMERA
    };

    // Campos de entrada para os detalhes do contato
    private TextInputEditText editTextName, editTextPhone, editTextEmail, editTextAddress, editTextBirthday;

    // ImageView para exibir a foto do contato
    private ImageView contactImageView;

    // ID do endereço selecionado
    private int addressId = -1;

    // URI da foto do contato selecionada ou tirada
    private Uri contactPhotoUri;

    // Tag para logs
    private static final String TAG = "AddContactActivity";

    // Gerenciador de permissões
    private PermissionManager permissionManager;


    // Variáveis para armazenar a latitude e longitude do endereço
    private double latitude;
    private double longitude;

    /**
     * Lançador para solicitar permissões múltiplas.
     */
    private final ActivityResultLauncher<String[]> requestPermissionsLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                boolean allGranted = true;
                for (Boolean granted : result.values()) {
                    if (!granted) {
                        allGranted = false;
                        break;
                    }
                }
                if (allGranted) {
                    Log.d(TAG, "Todas as permissões foram concedidas");
                    showPhotoDialog();
                } else {
                    Log.d(TAG, "Nem todas as permissões foram concedidas");
                    Toast.makeText(this, R.string.verif_perm_n_concedidas, Toast.LENGTH_SHORT).show();
                }
            });

    /**
     * Lançador para tirar uma foto com a câmera.
     */
    private final ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    assert extras != null;
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    contactImageView.setImageBitmap(imageBitmap);
                    contactPhotoUri = ImageUtil.saveImageToExternalStorage(this, imageBitmap);
                    if (contactPhotoUri != null) {
                        Toast.makeText(this, "Imagem salva com sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Erro ao salvar a imagem", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    /**
     * Lançador para selecionar uma foto da galeria.
     */
    private final ActivityResultLauncher<Intent> selectPhotoLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    contactPhotoUri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contactPhotoUri);
                        contactImageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        Log.d(TAG, "onActivityResult: " + e);
                    }
                }
            });


    /**
     * Chamado quando a atividade é criada pela primeira vez.
     * @param savedInstanceState Se a atividade está sendo reinicializada após ter sido encerrada, este parâmetro conterá os dados mais recentemente fornecidos.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        // Obtém o userId a partir de PreferencesManager
        PreferencesManager preferencesManager = PreferencesManager.getInstance(this);
        String languageCode = preferencesManager.getLanguage();
        preferencesManager.setLocale(this, languageCode);
        userId = preferencesManager.getUserId();

        contactImageView = findViewById(R.id.contactImageView);
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextBirthday = findViewById(R.id.editTextBirthday);

        permissionManager = new PermissionManager(this, REQUIRED_PERMISSIONS_BELOW_33, REQUIRED_PERMISSIONS_33_AND_ABOVE);

        Button buttonAddPhoto = findViewById(R.id.buttonAddPhoto);
        Button buttonSaveContact = findViewById(R.id.buttonSaveContact);

        buttonAddPhoto.setOnClickListener(v -> {
            try {
                if (permissionManager.allPermissionsGranted()) {
                    Log.d(TAG, "Permissões já concedidas");
                    showPhotoDialog();
                } else {
                    Log.d(TAG, "Solicitando permissões necessárias");
                    permissionManager.requestPermissions(requestPermissionsLauncher);
                }
            } catch (Exception e) {
                Log.e(TAG, "Erro ao verificar permissões ou exibir diálogo de fotos: ", e);
            }
        });

        buttonSaveContact.setOnClickListener(v -> saveContact());

        if (savedInstanceState != null) {
            editTextName.setText(savedInstanceState.getString("name"));
            editTextPhone.setText(savedInstanceState.getString("phone"));
            editTextEmail.setText(savedInstanceState.getString("email"));
            editTextAddress.setText(savedInstanceState.getString("address"));
            editTextBirthday.setText(savedInstanceState.getString("birthday"));
            addressId = savedInstanceState.getInt("addressId", -1);

            contactPhotoUri = savedInstanceState.getParcelable("contactPhotoUri");
            if (contactPhotoUri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contactPhotoUri);
                    contactImageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    Log.e(TAG, "Erro ao restaurar a imagem do contato: ", e);
                }
            }
        }

    }

    /**
     * Mostra um diálogo para o usuário escolher entre tirar uma nova foto ou selecionar uma existente.
     */
    private void showPhotoDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.add_foto)
                .setItems( new String[] {
                        getString(R.string.add_ct_add_foto_tirar_foto),
                        getString(R.string.add_ct_add_foto_selecionar_foto)
                }, (dialog, which) -> {
                    try {
                        if (which == 0) {
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                takePictureLauncher.launch(takePictureIntent);
                            }
                        } else {
                            Intent selectPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            selectPhotoLauncher.launch(selectPhotoIntent);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Erro ao iniciar intenção para captura ou seleção de foto: ", e);
                    }
                })
                .show();
    }

    /**
     * Salva o contato inserido pelo usuário no banco de dados, incluindo a geocodificação e salvamento do endereço.
     */
    private void saveContact() {
        String name = Objects.requireNonNull(editTextName.getText()).toString();
        String phone = Objects.requireNonNull(editTextPhone.getText()).toString();
        String email = Objects.requireNonNull(editTextEmail.getText()).toString();
        String address = Objects.requireNonNull(editTextAddress.getText()).toString();
        String birthday = Objects.requireNonNull(editTextBirthday.getText()).toString();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty() || birthday.isEmpty()) {
            Toast.makeText(this, R.string.verif_todos_campos, Toast.LENGTH_SHORT).show();
            return;
        }

        geocodeAddress(address, name, phone, email, birthday);
    }

    /**
     * Geocodifica o endereço inserido pelo usuário para obter as coordenadas de latitude e longitude e salva o endereço no banco de dados.
     *
     * @param address  O endereço inserido pelo usuário.
     * @param name     O nome do contato.
     * @param phone    O telefone do contato.
     * @param email    O email do contato.
     * @param birthday O aniversário do contato.
     */
    private void geocodeAddress(String address, String name, String phone, String email, String birthday) {
        if (address.isEmpty()) {
            Toast.makeText(this, R.string.verif_inserir_endereco, Toast.LENGTH_SHORT).show();
            return;
        }

        Geocoder geocoder = new Geocoder(this);
        try {
            List<android.location.Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses != null && !addresses.isEmpty()) {
                android.location.Address location = addresses.get(0);
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                System.out.println("lat long:"+latitude+ longitude);
                saveAddressAndContact(name, phone, email, address, birthday);
            } else {
                Toast.makeText(this, R.string.verif_endereco_n_encontrado, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Log.d(TAG, "geocodeAddress: " + e);
            Toast.makeText(this, R.string.verif_erro_geocod_endereco, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Salva o endereço e o contato no banco de dados.
     *
     * @param name     O nome do contato.
     * @param phone    O telefone do contato.
     * @param email    O email do contato.
     * @param address  O endereço do contato.
     * @param birthday O aniversário do contato.
     */
    private void saveAddressAndContact(String name, String phone, String email, String address, String birthday) {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
            Address addressEntity = new Address();
            addressEntity.title = address;
            addressEntity.latitude = latitude;
            addressEntity.longitude = longitude;
            long addressId = db.addressDao().insert(addressEntity);

            Contact contact = new Contact();
            contact.userId = userId;
            contact.name = name;
            contact.phoneNumber = phone;
            contact.email = email;
            contact.addressId = (int) addressId;
            contact.birthdate = birthday;
            if (contactPhotoUri != null) {
                contact.photoUri = contactPhotoUri.toString();
            }

            db.contactDao().insert(contact);

            runOnUiThread(() -> {
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            });
        }).start();
    }


    /**
     * Salva o estado atual da atividade para restaurá-lo em uma recriação.
     * @param outState O objeto Bundle no qual salvar o estado da atividade.
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name", Objects.requireNonNull(editTextName.getText()).toString());
        outState.putString("phone", Objects.requireNonNull(editTextPhone.getText()).toString());
        outState.putString("email", Objects.requireNonNull(editTextEmail.getText()).toString());
        outState.putString("address", Objects.requireNonNull(editTextAddress.getText()).toString());
        outState.putString("birthday", Objects.requireNonNull(editTextBirthday.getText()).toString());
        outState.putInt("addressId", addressId);
        outState.putParcelable("contactPhotoUri", contactPhotoUri);
    }
}
