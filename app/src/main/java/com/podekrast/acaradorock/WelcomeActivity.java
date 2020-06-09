package com.podekrast.acaradorock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.podekrast.acaradorock.helper.ConfigFirebase;

public class WelcomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_welcome);

        //Recupera a instância do FirebaseAuth
        mAuth = ConfigFirebase.getAuth();

        //Chama o método que verifica se o usuário está logado
        verifyUser();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Chama o método que verfica se o usuário está logado
        verifyUser();
    }

    //Método que chama a Activity de login
    public void loginWelcome(View view) {

        //Chama a Ativity de login
        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    //Método que chama a Activity de signin
    public void registerWelcome(View view) {

        //Chama a Ativity de signin
        Intent intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    //Método que verifica se o usuário está logado
    private void verifyUser() {

        //Se o usuário atual for diferente de nulo, muda para tela inicial
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
