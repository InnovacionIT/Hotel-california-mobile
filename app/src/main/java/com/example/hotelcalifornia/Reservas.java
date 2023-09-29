package com.example.hotelcalifornia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Reservas extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.menu){
                    Intent intent=new Intent(getApplicationContext(), Home.class);
                    startActivity(intent);
                    return true;
                }
                else if (id == R.id.compra){
                    Intent intent=new Intent(getApplicationContext(), Reservas.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

    }

   /*public void pago(View view){
        Intent pagar=new Intent()this,Detalle.class);
        startActivity(pagar);
    }*/

    public void cancelar(View view){
        Intent cancelar=new Intent(this, MainActivity.class);
        startActivity(cancelar);
    }


}