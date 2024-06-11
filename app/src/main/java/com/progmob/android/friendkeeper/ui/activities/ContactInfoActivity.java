package com.progmob.android.friendkeeper.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.progmob.android.friendkeeper.R;

/**
 * ContactInfoActivity
 * <p>
 * Atividade responsável por exibir as informações de um contato.
 */
public class ContactInfoActivity extends AppCompatActivity {

    private ImageView contactImageView;
    private TextInputEditText editTextName;
    private TextInputEditText editTextPhone;
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextAddress;
    private TextInputEditText editTextBirthday;
    private Button openMapsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        contactImageView = findViewById(R.id.contactImageView);
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextBirthday = findViewById(R.id.editTextBirthday);
        openMapsButton = findViewById(R.id.button);

        // Obtenha os dados do contato da Intent
        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("contact_name");
            String phone = intent.getStringExtra("contact_phone");
            String email = intent.getStringExtra("contact_email");
            String address = intent.getStringExtra("contact_address");
            double latitude = intent.getDoubleExtra("contact_latitude", 0.0);
            double longitude = intent.getDoubleExtra("contact_longitude", 0.0);
            String birthday = intent.getStringExtra("contact_birthday");
            String photoUri = intent.getStringExtra("contact_photo_uri");

            // Defina os dados nos campos correspondentes
            System.out.println(name+phone+birthday);
            editTextName.setText(name);
            editTextPhone.setText(phone);
            editTextEmail.setText(email);
            editTextAddress.setText(address);
            editTextBirthday.setText(birthday);

            if (photoUri != null) {
                Glide.with(this).load(Uri.parse(photoUri)).placeholder(R.drawable.ic_person).into(contactImageView);
            } else {
                contactImageView.setImageResource(R.drawable.ic_person);
            }

            openMapsButton.setOnClickListener(v -> {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            });
        }
    }
}
