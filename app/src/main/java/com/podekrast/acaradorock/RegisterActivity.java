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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.podekrast.acaradorock.common.view.LoadingButton;
import com.podekrast.acaradorock.helper.Base64Custom;
import com.podekrast.acaradorock.helper.ConfigFirebase;
import com.podekrast.acaradorock.model.User;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText mEdtName, mEdtEmail, mEdtPassword;
    private LoadingButton mBtnRegister;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar_register);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }

        //Recupera a instância do Firebase Auth
        mAuth = ConfigFirebase.getFirebaseAuth();

        //Recupera a instância da classe User
        mUser = new User();

        //Recupera as Views do XML
        mEdtName = findViewById(R.id.edt_name_register);
        mEdtEmail = findViewById(R.id.edt_email_register);
        mEdtPassword = findViewById(R.id.edt_password_register);
        mBtnRegister = findViewById(R.id.btn_register);

        //Adiciona o evento de clique para registrar o usuário
        mBtnRegister.setOnClickListener(register);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return true;
    }

    private View.OnClickListener register = v -> {
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

        //Ativa a progressBar
        mBtnRegister.progressBarEnabled(true);

        //Valida se as caixas de text o estão preenchidas
        if (fieldName.isEmpty() || fieldEmail.isEmpty() || fieldPassword.isEmpty()) {

            //Desativa a progressBar
            mBtnRegister.progressBarEnabled(false);

            //Se o e-mail ou a senha estiver vazio exibe um Toast
            Toast.makeText(RegisterActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
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

                        //Desativa a progressBar
                        mBtnRegister.progressBarEnabled(false);
                        //Exibe um Toast com a mensagem de erro
                        Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    };
}
