package com.podekrast.acaradorock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.podekrast.acaradorock.helper.ConfigFirebase;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText mEditEmail, mEditPassword;
    private RelativeLayout mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Recupera a instância do Firebase Auth
        mAuth = ConfigFirebase.getAuth();

        //Recupera as caixas de texto
        mEditEmail = findViewById(R.id.edit_email);
        mEditPassword = findViewById(R.id.edit_password);

        //Recupera a progressBar
        mProgressBar = findViewById(R.id.progress_bar_login);
    }

    public void logInUser(View view) {
        //Recupera o texto das caixas de texto
        String boxEmail = mEditEmail.getText().toString();
        String boxPassword = mEditPassword.getText().toString();

        //Ativa a progressBar
        mProgressBar.setVisibility(View.VISIBLE);

        //Valida se as caixas de texto estão preenchidas
        if (boxEmail.isEmpty() || boxPassword.isEmpty()) {

            //Se o e-mail ou a senha estiver vazio exibe um Toast
            Toast.makeText(LoginActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();

            //Desativa a progressBar
            mProgressBar.setVisibility(View.GONE);
        } else {

            //Se todas as caixas de texto estiver preenchidas, realiza o login do usuário
            mAuth.signInWithEmailAndPassword(boxEmail, boxPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                //Se o login foi efetuado com sucesso, chama o método que verifica se o usuário esta logado
                                finish();
                            } else {

                                //Se o login não foi efetuado com sucesso, exibe um Toast, indicando para o usuário, qual foi o erro
                                String error;

                                //Recupera a exceção
                                try {

                                    throw task.getException();
                                } catch (FirebaseAuthInvalidUserException e) {

                                    error = "Não foi encontrada nenhuma conta com esse endereço de e-mail";
                                } catch (FirebaseAuthInvalidCredentialsException e) {

                                    error = "Senha incorreta";
                                } catch (Exception e) {

                                    error = "Erro inesperado! Por favor, tente novamente!";
                                }

                                //Desativa a progressBar
                                mProgressBar.setVisibility(View.GONE);
                                //Exibe um Toast com a mensagem de erro
                                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    public void loginReturn(View view) {

        //Muda para tela de bem-vindo
        finish();
    }
}
