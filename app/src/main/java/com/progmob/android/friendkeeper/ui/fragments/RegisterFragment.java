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
 * A classe RegisterFragment é responsável por gerenciar a interface de registro do aplicativo.
 * Ela inclui campos para o usuário inserir seu nome, e-mail e senha, além de botões para
 * efetuar o registro ou navegar para a tela de login.
 */
public class RegisterFragment extends Fragment {

    // EditText para o usuário inserir o nome.
    private EditText nameEditText;

    // EditText para o usuário inserir o e-mail.
    private EditText emailEditText;

    // EditText para o usuário inserir a senha.
    private EditText passwordEditText;

    // Listener para interações do fragmento, como registro e navegação para o login.
    private OnFragmentInteractionListener mListener;

    private static final String TAG = "RegisterFragment";

    /**
     * Interface para comunicação entre o fragmento e a atividade.
     * A atividade deve implementar esta interface para gerenciar as interações do fragmento.
     */
    public interface OnFragmentInteractionListener {
        void onRegister(String name, String email, String password);
        void onLoginClicked();
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        try {
            // Inicializa os componentes da interface do usuário
            nameEditText = view.findViewById(R.id.editNome);
            emailEditText = view.findViewById(R.id.editEmail);
            passwordEditText = view.findViewById(R.id.editSenha);
            Button registerButton = view.findViewById(R.id.btRegistrar);
            TextView loginTextView = view.findViewById(R.id.txtTelaLogin);

            // Define o comportamento do botão de registro
            registerButton.setOnClickListener(v -> {
                try {
                    String name = nameEditText.getText().toString();
                    String email = emailEditText.getText().toString();
                    String password = passwordEditText.getText().toString();

                    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                        Toast.makeText(getActivity(), R.string.verif_todos_campos, Toast.LENGTH_SHORT).show();
                    } else {
                        mListener.onRegister(name, email, password);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Erro ao registrar: ", e);
                }
            });

            // Define o comportamento do texto de login
            loginTextView.setOnClickListener(v -> {
                try {
                    mListener.onLoginClicked();
                } catch (Exception e) {
                    Log.e(TAG, "Erro ao clicar no login: ", e);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Erro ao criar a view: ", e);
            throw e;
        }

        return view;
    }
}

