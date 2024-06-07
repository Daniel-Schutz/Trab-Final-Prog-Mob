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

public class LoginFragment extends Fragment {

    private EditText emailEditText;
    private EditText passwordEditText;
    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onLogin(String email, String password);
        void onRegisterClicked();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailEditText = view.findViewById(R.id.editEmail);
        passwordEditText = view.findViewById(R.id.editSenha);
        Button loginButton = view.findViewById(R.id.btEntrar);
        TextView registerTextView = view.findViewById(R.id.txtTelaCadastro);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(getActivity(), R.string.preencher_campos, Toast.LENGTH_SHORT).show();
            } else {
                mListener.onLogin(email, password);
            }
        });

        registerTextView.setOnClickListener(v -> mListener.onRegisterClicked());

        return view;
    }
}
