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
        mAuth = ConfigFirebase.getFirebaseAuth();

        //Chama o método que verifica se o usuário está logado
        verifyUser();

        //Recupera as Views do XML
        Button mBtnLogin = findViewById(R.id.btn_login_welcome);
        Button mBtnRegister = findViewById(R.id.btn_register_welcome);

        //Adiciona evento de click nos botões
        mBtnLogin.setOnClickListener(loginWelcome);
        mBtnRegister.setOnClickListener(registerWelcome);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Chama o método que verfica se o usuário está logado
        verifyUser();
    }

    //Chama a Ativity de login
    private View.OnClickListener loginWelcome = (v) -> startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));

    //Chama a Activity de register
    private View.OnClickListener registerWelcome = (v) -> startActivity(new Intent(WelcomeActivity.this, RegisterActivity.class));

    //Método que verifica se o usuário está logado
    private void verifyUser() {
        //Se o usuário atual for diferente de nulo, muda para tela inicial
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
            finish();
        }
    }
}
