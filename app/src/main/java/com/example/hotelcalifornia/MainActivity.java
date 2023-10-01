package com.example.hotelcalifornia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText emailLogin, contraseñaLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void home(View view) {

        Intent intento = new Intent(this, Home.class);
        startActivity(intento);
    }

    public void iraregistro(View view){
        Intent intent = new Intent(this, Registro.class);
        startActivity(intent);
    }
}
