package com.podekrast.acaradorock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.podekrast.acaradorock.helper.Base64Custom;
import com.podekrast.acaradorock.helper.ConfigFirebase;
import com.podekrast.acaradorock.model.User;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText mEdtName, mEdtEmail, mEdtPassword;
    private User mUser;
    private RelativeLayout mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Recupera a instância do Firebase Auth
        mAuth = ConfigFirebase.getAuth();

        //Recupera a instância da classe User
        mUser = new User();

        //Recupera as Views do XML
        mEdtName = findViewById(R.id.edt_name_sign_up);
        mEdtEmail = findViewById(R.id.edt_email_sign_up);
        mEdtPassword = findViewById(R.id.edt_password_sign_up);
        Button mBtnSignUp = findViewById(R.id.btn_sign_up);
        Button mBtnReturn = findViewById(R.id.btn_return_sign_up);
        mProgressBar = findViewById(R.id.progress_bar_register);

        //Adiciona o evento de clique para registrar o usuário
        mBtnSignUp.setOnClickListener(signUp);
        //Adiciona o evento de clique para retornar para tela anterior
        mBtnReturn.setOnClickListener(signUpReturn);
    }

    private View.OnClickListener signUp = v -> {

        //Recupera o gerenciador de método de entrada
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //Se im for diferente de nulo, oculta o teclado
        if (im != null) {
            im.hideSoftInputFromWindow(mEdtName.getWindowToken(), 0);
            im.hideSoftInputFromWindow(mEdtEmail.getWindowToken(), 0);
            im.hideSoftInputFromWindow(mEdtPassword.getWindowToken(), 0);
        }

        //Recupera o texto das caixas de texto
        String fieldName = mEdtName.getText().toString();
        String fieldEmail = mEdtEmail.getText().toString();
        String fieldPassword = mEdtPassword.getText().toString();

        //Torna a ProgressBar ativa
        mProgressBar.setVisibility(View.VISIBLE);

        //Valida se as caixas de text o estão preenchidas
        if (fieldName.isEmpty() || fieldEmail.isEmpty() || fieldPassword.isEmpty()) {

            //Torna a ProgressBar inativa
            mProgressBar.setVisibility(View.GONE);

            //Se o e-mail ou a senha estiver vazio exibe um Toast
            Toast.makeText(SignUpActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
        } else {
            //Adiciona os dados do usuário no model User
            mUser.setNome(fieldName);
            mUser.setEmail(fieldEmail);
            mUser.setSenha(fieldPassword);

            //Registra o usuário
            mAuth.createUserWithEmailAndPassword(
                    mUser.getEmail(), mUser.getSenha()//Indica quais são os dados do usuário
            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        //Se o registro foi executado com êxito, indica o Id do usuário e salva ele no Firebase Database
                        mUser.setIdUsuario(Base64Custom.codificarBase64(mUser.getEmail()));
                        mUser.salvar();

                        //Volta para a tela anterior para verificar se o usuário está logado
                        finish();
                    } else {

                        //Se o login não foi efetuado com sucesso, exibe um Toast, indicando para o usuário, qual foi o erro
                        String error = "";

                        //Recupera a exceção
                        try {
                            if (task.getException() != null)
                                throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            error = "Digite uma senha mais forte!";
                        } catch (FirebaseAuthUserCollisionException e) {
                            error = "Esse endereço de e-mail já esta registrado";
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            error = "Digite um e-mail válido!";
                        } catch (Exception e) {
                            error = "Erro inesperado! Por favor, tente novamente!";
                        }

                        mProgressBar.setVisibility(View.GONE);
                        //Exibe um Toast com a mensagem de erro
                        Toast.makeText(SignUpActivity.this, error, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    };

    private View.OnClickListener signUpReturn = v -> finish();
}
