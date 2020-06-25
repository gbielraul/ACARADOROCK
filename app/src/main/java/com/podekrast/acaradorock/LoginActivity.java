package com.podekrast.acaradorock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.podekrast.acaradorock.common.view.LoadingButton;
import com.podekrast.acaradorock.helper.ConfigFirebase;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText mEdtEmail, mEdtPassword;
    private LoadingButton mBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }

        //Recupera a instância do Firebase Auth
        mAuth = ConfigFirebase.getFirebaseAuth();

        //Recupera as Views do XML
        mEdtEmail = findViewById(R.id.edt_email_login);
        mEdtPassword = findViewById(R.id.edt_password_login);
        mBtnLogin = findViewById(R.id.btn_login);

        //Adiciona o evento de clique para realizar o login
        mBtnLogin.setOnClickListener(login);
        //Adiciona o evento de clique para retornar para a tela anterior
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return true;
    }

    //Realiza o login do usuário
    private View.OnClickListener login = v -> {
        //Recupera o gerenciador de método de entrada
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //Se im for diferente de nulo, oculta o teclado
        if (im != null) {
            im.hideSoftInputFromWindow(mEdtEmail.getWindowToken(), 0);
            im.hideSoftInputFromWindow(mEdtPassword.getWindowToken(), 0);
        }

        //Recupera o texto das caixas de texto
        String fieldEmail = mEdtEmail.getText().toString();
        String fieldPassword = mEdtPassword.getText().toString();

        //Ativa a progressBar
        mBtnLogin.progressBarEnabled(true);

        //Valida se os campos estão preenchidos
        if (fieldEmail.isEmpty() || fieldPassword.isEmpty()) {
            //Se o e-mail ou a senha estiver vazio exibe um Toast
            Toast.makeText(LoginActivity.this, R.string.validate, Toast.LENGTH_SHORT).show();

            //Desativa a progressBar e ativa o texto do botão
            mBtnLogin.progressBarEnabled(false);
        } else {
            //Se todas as caixas de texto estiver preenchidas, realiza o login do usuário
            mAuth.signInWithEmailAndPassword(fieldEmail, fieldPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //Se o login foi efetuado com sucesso, chama o método que verifica se o usuário esta logado
                                finish();
                            } else {
                                //Se o login não foi efetuado com sucesso, exibe um Toast, indicando para o usuário, qual foi o erro
                                String error = "";

                                //Recupera a exceção
                                try {
                                    if (task.getException() != null)
                                        throw task.getException();
                                } catch (FirebaseAuthInvalidUserException e) {

                                    error = "Não foi encontrada nenhuma conta com esse endereço de e-mail";
                                } catch (FirebaseAuthInvalidCredentialsException e) {

                                    error = "Senha incorreta";
                                } catch (Exception e) {

                                    error = "Erro inesperado! Por favor, tente novamente!";
                                }

                                //Desativa a progressBar e ativa o texto do botão
                                mBtnLogin.progressBarEnabled(false);

                                //Exibe um Toast com a mensagem de erro
                                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    };
}
