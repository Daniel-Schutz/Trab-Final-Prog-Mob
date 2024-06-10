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
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.progmob.android.friendkeeper.R;

/**
 * A classe LoginFragment é responsável por gerenciar a interface de login do aplicativo.
 * Ela inclui campos para o usuário inserir seu e-mail e senha, além de botões para
 * efetuar o login ou navegar para a tela de registro.
 */
public class LoginFragment extends Fragment {

    // EditText para o usuário inserir o e-mail.
    private EditText emailEditText;

    // EditText para o usuário inserir a senha.
    private EditText passwordEditText;

    // Listener para interações do fragmento, como login e navegação para o registro.
    private OnFragmentInteractionListener mListener;

    private static final String TAG = "LoginFragment";

    /**
     * Interface para comunicação entre o fragmento e a atividade.
     * A atividade deve implementar esta interface para gerenciar as interações do fragmento.
     */
    public interface OnFragmentInteractionListener {
        void onLogin(String email, String password);
        void onRegisterClicked();
    }

    /**
     * Método chamado quando o fragmento é anexado ao contexto.
     * Verifica se o contexto implementa a interface de interação.
     *
     * @param context O contexto ao qual o fragmento está sendo anexado.
     * @throws RuntimeException se o contexto não implementar a interface de interação.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (context instanceof OnFragmentInteractionListener) {
                mListener = (OnFragmentInteractionListener) context;
            } else {
                throw new RuntimeException(context.toString()
                        + "deve implementar OnFragmentInteractionListener");
            }
        } catch (RuntimeException e) {
            Log.e(TAG, "Erro ao anexar o contexto: ", e);
            throw e;
        }
    }

    /**
     * Método chamado para inflar a view do fragmento.
     *
     * @param inflater           O LayoutInflater que pode ser usado para inflar qualquer view no fragmento.
     * @param container          Se não-nulo, este é o pai da view que o fragmento deve inflar.
     * @param savedInstanceState Se não-nulo, este fragmento está sendo reconstruído a partir de um estado salvo anteriormente.
     * @return A View para o layout do fragmento.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        try {
            // Inicializa os componentes da interface do usuário
            emailEditText = view.findViewById(R.id.editEmail);
            passwordEditText = view.findViewById(R.id.editSenha);
            Button loginButton = view.findViewById(R.id.btEntrar);
            TextView registerTextView = view.findViewById(R.id.txtTelaCadastro);

            // Define o comportamento do botão de login
            loginButton.setOnClickListener(v -> {
                try {
                    String email = emailEditText.getText().toString();
                    String password = passwordEditText.getText().toString();

                    if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                        Toast.makeText(getActivity(), R.string.verif_todos_campos, Toast.LENGTH_SHORT).show();
                    } else {
                        mListener.onLogin(email, password);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Erro ao fazer login: ", e);
                }
            });

            // Define o comportamento do texto de registro
            registerTextView.setOnClickListener(v -> {
                try {
                    mListener.onRegisterClicked();
                } catch (Exception e) {
                    Log.e(TAG, "Erro ao clicar no registro: ", e);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Erro ao criar a view: ", e);
            throw e;
        }

        return view;
    }
}
