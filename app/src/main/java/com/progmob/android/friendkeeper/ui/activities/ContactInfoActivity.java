package com.progmob.android.friendkeeper.ui.activities;



import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.progmob.android.friendkeeper.R;
import com.progmob.android.friendkeeper.database.AppDatabase;
import com.progmob.android.friendkeeper.entities.Address;
import com.progmob.android.friendkeeper.entities.Contact;
import com.progmob.android.friendkeeper.utils.ImageUtil;
import com.progmob.android.friendkeeper.utils.PermissionManager;
import com.progmob.android.friendkeeper.utils.PreferencesManager;

import java.io.IOException;
import java.util.Objects;

/**
 * ContactInfoActivity
 * <p>
 * Atividade responsável por exibir as informações de um contato.
 */
public class ContactInfoActivity extends AppCompatActivity {

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
    private ImageView contactImageView;
    private TextInputEditText editTextName;
    private TextInputEditText editTextPhone;
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextAddress;
    private TextInputEditText editTextBirthday;
    private Button openMapsButton;

    private Button editPhoto;
    private Button deleteContact;
    private Button saveContact;

    private PermissionManager permissionManager;
    private int contactId; // Adicionado para armazenar o ID do contato
    private Uri contactPhotoUri; // URI da foto do contato

    private static final String TAG = "ContactInfoActivity";

    private static final String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        permissionManager = new PermissionManager(this, REQUIRED_PERMISSIONS_BELOW_33, REQUIRED_PERMISSIONS_33_AND_ABOVE);
        PreferencesManager preferencesManager = PreferencesManager.getInstance(this);
        String languageCode = preferencesManager.getLanguage();
        preferencesManager.setLocale(this, languageCode);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        contactImageView = findViewById(R.id.contactImageView);
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextBirthday = findViewById(R.id.editTextBirthday);
        openMapsButton = findViewById(R.id.button);
        editPhoto = findViewById(R.id.buttonAddPhoto); // Referência ao botão de editar foto
        deleteContact = findViewById(R.id.buttonEditContact);
        saveContact = findViewById(R.id.buttonSaveContact);


        // Obtenha os dados do contato da Intent
        Intent intent = getIntent();
        if (intent != null) {
            contactId = intent.getIntExtra("contact_id", -1);
            String name = intent.getStringExtra("contact_name");
            String phone = intent.getStringExtra("contact_phone");
            String email = intent.getStringExtra("contact_email");
            String address = intent.getStringExtra("contact_address");
            double latitude = intent.getDoubleExtra("contact_latitude", 0.0);
            double longitude = intent.getDoubleExtra("contact_longitude", 0.0);
            String birthday = intent.getStringExtra("contact_birthday");
            String photoUri = intent.getStringExtra("contact_photo_uri");

            // Defina os dados nos campos correspondentes
            editTextName.setText(name);
            editTextPhone.setText(phone);
            editTextEmail.setText(email);
            editTextAddress.setText(address);
            editTextBirthday.setText(birthday);

            if (photoUri != null) {
                contactPhotoUri = Uri.parse(photoUri); // Armazenar a URI da foto do contato
                Glide.with(this).load(contactPhotoUri).placeholder(R.drawable.ic_person).into(contactImageView);
            } else {
                contactImageView.setImageResource(R.drawable.ic_person);
            }

            openMapsButton.setOnClickListener(v -> {
                int zoomLevel = 15; // Ajuste o nível de zoom conforme necessário
                String uri = String.format("geo:%s,%s?q=%s,%s&z=%d", latitude, longitude, latitude, longitude, zoomLevel);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                mapIntent.setPackage("com.google.android.apps.maps");

                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    // Tenta abrir no Maps se a aplicação não estiver instalada
                    Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    try {
                        startActivity(unrestrictedIntent);
                    } catch (ActivityNotFoundException innerEx) {
                        Toast.makeText(this, "Nenhuma aplicação de mapas encontrada", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            deleteContact.setOnClickListener(v -> deleteContact());
            saveContact.setOnClickListener(v -> saveContactChanges());
            editPhoto.setOnClickListener(v -> handleEditPhoto()); // Configurar o clique do botão de editar foto
        }
    }

    private void handleEditPhoto() {
        if (permissionManager.allPermissionsGranted()) {
            showPhotoDialog();
        } else {
            requestPermissionsLauncher.launch(REQUIRED_PERMISSIONS);
        }
    }

    private void showPhotoDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.add_foto)
                .setItems(new String[]{
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
     * Método para deletar o contato.
     */
    private void deleteContact() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());

            // Obter o contato pelo ID
            Contact contact = db.contactDao().getContactById(contactId);

            // Verificar se o contato existe
            if (contact != null) {
                // Obter o endereço associado ao contato
                Address address = db.addressDao().getAddressById(contact.addressId);

                // Deletar o endereço, se existir
                if (address != null) {
                    db.addressDao().delete(address);
                }

                // Deletar o contato
                db.contactDao().delete(contact);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Contato deletado com sucesso", Toast.LENGTH_SHORT).show();
                    finish();
                });
            } else {
                runOnUiThread(() -> Toast.makeText(this, "Erro ao deletar o contato", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    /**
     * Método para salvar as alterações feitas no contato.
     */
    private void saveContactChanges() {
        String name = Objects.requireNonNull(editTextName.getText()).toString();
        String phone = Objects.requireNonNull(editTextPhone.getText()).toString();
        String email = Objects.requireNonNull(editTextEmail.getText()).toString();
        String address = Objects.requireNonNull(editTextAddress.getText()).toString();
        String birthday = Objects.requireNonNull(editTextBirthday.getText()).toString();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty() || birthday.isEmpty()) {
            Toast.makeText(this, R.string.verif_todos_campos, Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
            Contact contact = db.contactDao().getContactById(contactId);
            if (contact != null) {
                contact.name = name;
                contact.phoneNumber = phone;
                contact.email = email;
                contact.birthdate = birthday;
                if (contactPhotoUri != null) {
                    contact.photoUri = contactPhotoUri.toString();
                }
                db.contactDao().update(contact);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Contato atualizado com sucesso", Toast.LENGTH_SHORT).show();
                    finish();
                });
            } else {
                runOnUiThread(() -> Toast.makeText(this, "Erro ao atualizar o contato", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
