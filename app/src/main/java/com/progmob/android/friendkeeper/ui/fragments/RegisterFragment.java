package com.progmob.android.friendkeeper.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.progmob.android.friendkeeper.R;

public class RegisterFragment extends Fragment {

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onRegister(String name, String email, String password);
        void onLoginClicked();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        nameEditText = view.findViewById(R.id.editNome);
        emailEditText = view.findViewById(R.id.editEmail);
        passwordEditText = view.findViewById(R.id.editSenha);
        Button registerButton = view.findViewById(R.id.btRegistrar);
        TextView loginTextView = view.findViewById(R.id.txtTelaLogin);

        registerButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(getActivity(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            } else {
                mListener.onRegister(name, email, password);
            }
        });

        loginTextView.setOnClickListener(v -> mListener.onLoginClicked());

        return view;
    }
}
