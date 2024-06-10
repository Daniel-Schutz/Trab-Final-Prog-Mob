package com.progmob.android.friendkeeper.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.progmob.android.friendkeeper.R;

public class PermissionSettingsActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 200;

    @SuppressLint("InlinedApi")
    private static final String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.RECEIVE_BOOT_COMPLETED,
            Manifest.permission.SCHEDULE_EXACT_ALARM
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_settings);

        LinearLayout containerPermissions = findViewById(R.id.containerPermissions);

        for (String permission : REQUIRED_PERMISSIONS) {
            addPermissionItem(containerPermissions, permission);
        }
    }

    private void addPermissionItem(LinearLayout container, String permission) {
        ConstraintLayout permissionItem = (ConstraintLayout) getLayoutInflater().inflate(R.layout.item_permission, container, false);

        TextView tvPermissionName = permissionItem.findViewById(R.id.tvPermissionName);
        Button btnPermissionAction = permissionItem.findViewById(R.id.btnPermissionAction);

        // Exibir apenas a parte relevante da permissão
        String permissionName = permission.substring(permission.lastIndexOf('.') + 1);
        tvPermissionName.setText(permissionName);

        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            btnPermissionAction.setText(R.string.permission_granted);
            btnPermissionAction.setEnabled(false);
            btnPermissionAction.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
        } else {
            btnPermissionAction.setText(R.string.permission_allow);
            btnPermissionAction.setEnabled(true);
            btnPermissionAction.setOnClickListener(v -> requestPermission(permission));
        }

        container.addView(permissionItem);
    }

    private void requestPermission(String permission) {
        ActivityCompat.requestPermissions(this, new String[]{ permission }, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), R.string.permission_granted, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), R.string.permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
        recreate();
    }
}
