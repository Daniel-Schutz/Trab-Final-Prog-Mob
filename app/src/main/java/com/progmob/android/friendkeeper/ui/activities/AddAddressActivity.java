package com.progmob.android.friendkeeper.ui.activities;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.progmob.android.friendkeeper.R;
import com.progmob.android.friendkeeper.database.AppDatabase;
import com.progmob.android.friendkeeper.entities.Address;

import java.io.IOException;
import java.util.List;

/**
 * AddAddressActivity
 * <p>
 * Classe responsável por permitir ao usuário adicionar um endereço com um título,
 * geocodificar o endereço para obter coordenadas de latitude e longitude,
 * e salvar o endereço no banco de dados.
 */
public class AddAddressActivity extends AppCompatActivity implements OnMapReadyCallback {

    // EditText para o título do endereço
    private EditText editTextTitle;

    // EditText para o endereço
    private EditText editTextAddress;

    // Variáveis para armazenar a latitude e longitude do endereço
    private double latitude;
    private double longitude;

    private String titulo = "";

    // GoogleMap para exibir o mapa e o marcador do endereço
    private GoogleMap mMap;

    // TAG para logs
    private static final String TAG = "AddAddressActivity";

    /**
     * Método onCreate
     * <p>
     * Inicializa a atividade, configura a visualização e os componentes,
     * e define os listeners para os botões de geocodificação e salvar endereço.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        editTextTitle = findViewById(R.id.editTextAddressTitle);
        editTextAddress = findViewById(R.id.editTextAddress);
        Button buttonSaveAddress = findViewById(R.id.buttonSaveAddress);
        Button buttonGeocodeAddress = findViewById(R.id.buttonGeocode);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        buttonGeocodeAddress.setOnClickListener(v -> geocodeAddress());
        buttonSaveAddress.setOnClickListener(v -> saveAddress());
    }

    /**
     * Método onMapReady
     * <p>
     * Callback chamado quando o mapa está pronto para uso.
     * Inicializa o objeto GoogleMap.
     *
     * @param googleMap O GoogleMap que foi instanciado.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        updateMap();
    }

    /**
     * Método geocodeAddress
     * <p>
     * Geocodifica o endereço inserido pelo usuário para obter as coordenadas
     * de latitude e longitude. Exibe um marcador no mapa na posição do endereço.
     */
    private void geocodeAddress() {
        String address = editTextAddress.getText().toString();
        titulo = editTextTitle.getText().toString();

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
                // TODO: Mapa não atualiza
                runOnUiThread(this::updateMap); // Atualiza o mapa na thread principal
            } else {
                Toast.makeText(this, R.string.verif_endereco_n_encontrado, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Log.d(TAG, "geocodeAddress: " + e);
            Toast.makeText(this, R.string.verif_erro_geocod_endereco, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Método saveAddress
     * <p>
     * Salva o endereço inserido pelo usuário no banco de dados, incluindo
     * o título, latitude e longitude. Retorna o resultado para a atividade chamadora.
     */
    private void saveAddress() {
        String title = editTextTitle.getText().toString();
        if (title.isEmpty() || latitude == 0 || longitude == 0) {
            Toast.makeText(this, R.string.verif_todos_campos, Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
            Address address = new Address();
            address.title = title;
            address.latitude = latitude;
            address.longitude = longitude;
            long addressId = db.addressDao().insert(address);

            runOnUiThread(() -> {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("addressTitle", title);
                resultIntent.putExtra("addressId", (int) addressId);
                setResult(RESULT_OK, resultIntent);
                finish();
            });
        }).start();
    }

    /**
     * Método updateMap
     * <p>
     * Atualiza o mapa com o marcador na posição de latitude e longitude obtidas.
     * Move a câmera para a posição do marcador.
     */
    private void updateMap() {
        if (mMap != null && latitude != 0 && longitude != 0) {
            LatLng latLng = new LatLng(latitude, longitude);
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng).title(titulo));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
    }
}
