package com.example.cowmap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {
Button crotales;
Button cerrar;

Button localizacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        crotales= findViewById(R.id.crotales);
        cerrar = findViewById(R.id.volver);
        localizacion = findViewById(R.id.locaclización);

        crotales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, Crotales.class);
                startActivity(intent);
            }
        });

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cer = new Intent(Menu.this,Login.class);
                startActivity(cer);
            }
        });

        localizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loc = new Intent(Menu.this, Map.class);
                startActivity(loc);
            }
        });
    }

}