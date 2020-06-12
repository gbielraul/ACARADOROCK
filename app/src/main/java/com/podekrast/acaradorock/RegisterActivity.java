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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.podekrast.acaradorock.helper.Base64Custom;
import com.podekrast.acaradorock.helper.ConfigFirebase;
import com.podekrast.acaradorock.model.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEditName, mEditEmail, mEditPassword;
    private FirebaseAuth mAuth;
    private User mUser;
    private RelativeLayout mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Recupera a instância do Firebase Auth
        mAuth = ConfigFirebase.getAuth();

        //Recupera as caixas de texto
        mEditName = findViewById(R.id.edit_name_register);
        mEditEmail = findViewById(R.id.edit_email_register);
        mEditPassword = findViewById(R.id.edit_password_register);

        //Recupera a ProgressBar
        mProgressBar = findViewById(R.id.progress_bar_register);

        //Recupera a instância da classe User
        mUser = new User();
    }

    public void registerReturn(View view) {
        finish();
    }

    public void registerAccount(View view) {

        //Torna a ProgressBar ativa
        mProgressBar.setVisibility(View.VISIBLE);

        //Recupera o texto das caixas de texto
        String boxName = mEditName.getText().toString();
        String boxEmail = mEditEmail.getText().toString();
        String boxPassword = mEditPassword.getText().toString();

        //Valida se as caixas de text o estão preenchidas
        if (boxName.isEmpty() || boxEmail.isEmpty() || boxPassword.isEmpty()) {

            //Torna a ProgressBar inativa
            mProgressBar.setVisibility(View.GONE);

            //Se o e-mail ou a senha estiver vazio exibe um Toast
            Toast.makeText(RegisterActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
        } else {

            //Adiciona os dados do usuário no model User
            mUser.setNome(boxName);
            mUser.setEmail(boxEmail);
            mUser.setSenha(boxPassword);

            //Chama o método que registra o usuário
            registerUser();
        }
    }

    private void registerUser() {

        //Registra o usuário
        mAuth.createUserWithEmailAndPassword(
                mUser.getEmail(), mUser.getSenha()//Indica quais são os dados do usuário
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    //Se o registro foi executado com êxito, indica o Id do usuário e salva ele no Firebase Database
                    String userId = Base64Custom.codificarBase64(mUser.getEmail());
                    mUser.setIdUsuario(userId);
                    mUser.salvar();

                    //Chama o método que volta para a tela anterior e verifica se o usuário esta logado
                    verifyUser();
                } else {

                    //Se o login não foi efetuado com sucesso, exibe um Toast, indicando para o usuário, qual foi o erro
                    String error = "";

                    //Recupera a exceção
                    try {
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
                    Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void verifyUser() {
        finish();
    }
}
