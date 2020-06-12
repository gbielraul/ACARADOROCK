package com.podekrast.acaradorock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.podekrast.acaradorock.helper.ConfigFirebase;

public class WelcomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //Recupera a instância do FirebaseAuth
        mAuth = ConfigFirebase.getAuth();

        //Chama o método que verifica se o usuário está logado
        verifyUser();

        //Recupera as Views do XML
        Button mBtnSignIn = findViewById(R.id.btn_sign_in_welcome);
        Button mBtnSignUp = findViewById(R.id.btn_sign_up_welcome);

        //Adiciona evento de click nos botões
        mBtnSignIn.setOnClickListener(signInWelcome);
        mBtnSignUp.setOnClickListener(signUpWelcome);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Chama o método que verfica se o usuário está logado
        verifyUser();
    }

    //Chama a Ativity de signin
    private View.OnClickListener signInWelcome = v -> startActivity(new Intent(WelcomeActivity.this, SignInActivity.class));

    //Chama a Activity de signup
    private View.OnClickListener signUpWelcome = v -> startActivity(new Intent(WelcomeActivity.this, SignUpActivity.class));

    //Método que verifica se o usuário está logado
    private void verifyUser() {
        //Se o usuário atual for diferente de nulo, muda para tela inicial
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
            finish();
        }
    }
}
