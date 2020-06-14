package com.podekrast.acaradorock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.podekrast.acaradorock.helper.ConfigFirebase;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText mEdtEmail, mEdtPassword;
    private TextView mTxtSignIn;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //Recupera a instância do Firebase Auth
        mAuth = ConfigFirebase.getAuth();

        //Recupera as Views do XML
        mEdtEmail = findViewById(R.id.edt_email_sign_in);
        mEdtPassword = findViewById(R.id.edt_password_sign_in);
        ImageView mBtnSignIn = findViewById(R.id.btn_sign_in);
        Button mBtnReturn = findViewById(R.id.btn_return_sign_in);
        mTxtSignIn = findViewById(R.id.txt_sign_in);
        mProgressBar = findViewById(R.id.progress_bar_sign_in);

        //Adiciona o evento de clique para realizar o login
        mBtnSignIn.setOnClickListener(signIn);
        //Adiciona o evento de clique para retornar para a tela anterior
        mBtnReturn.setOnClickListener(signInReturn);
    }

    //Realiza o login do usuário
    private View.OnClickListener signIn = v -> {
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

        //Ativa a progressBar e desativa o texto do botão
        mProgressBar.setVisibility(View.VISIBLE);
        mTxtSignIn.setVisibility(View.GONE);

        //Chama o método que valida se os campos estão preenchidas
        if (fieldEmail.isEmpty() || fieldPassword.isEmpty()) {
            //Se o e-mail ou a senha estiver vazio exibe um Toast
            Toast.makeText(SignInActivity.this, R.string.validate, Toast.LENGTH_SHORT).show();

            //Desativa a progressBar e ativa o texto do botão
            mProgressBar.setVisibility(View.GONE);
            mTxtSignIn.setVisibility(View.VISIBLE);
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
                                mProgressBar.setVisibility(View.GONE);
                                mTxtSignIn.setVisibility(View.VISIBLE);
                                //Exibe um Toast com a mensagem de erro
                                Toast.makeText(SignInActivity.this, error, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    };

    //Retorna para tela anterior
    private View.OnClickListener signInReturn = v -> finish();
}
