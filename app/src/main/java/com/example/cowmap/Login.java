package com.example.cowmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity {
    Button inicio;
    Button cancelar;

    private FirebaseAuth contactoBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        EditText usu = findViewById(R.id.usuLogin);
        EditText con = findViewById(R.id.ContraLogin);

    cancelar = findViewById(R.id.CancelLogin);

    cancelar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent volveratras = new Intent(Login.this, Principal.class);
            startActivity(volveratras);
        }
    });

    inicio = findViewById(R.id.EntrarLogin);

    inicio.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        iniciarSesion(usu.getText().toString(),con.getText().toString());
        }
    });



    }

    public void iniciarSesion(String usuario, String contraseña) {
        contactoBase = FirebaseAuth.getInstance();
        if (usuario.equals("") || contraseña.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("LOGIN INCORRECTO");
            builder.setMessage("NO SE HAN RELLENADOS TODOS LOS CAMPOS");
            builder.setPositiveButton("ACEPTAR", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            contactoBase.signInWithEmailAndPassword(usuario, contraseña).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = contactoBase.getCurrentUser();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        builder.setTitle("LOGIN CORRECTO");
                        builder.setMessage("BIENVENIDO DE NUEVO " + user.getUid());
                        builder.setPositiveButton("ACEPTAR", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        Intent avanzar = new Intent(Login.this, Menu.class);
                        startActivity(avanzar);

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        builder.setTitle("LOGIN INCORRECTO");
                        builder.setMessage("NO ESTA REGISTRADO EN NUESTRA BASE DE DATOS");
                        builder.setPositiveButton("ACEPTAR", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
            });
        }

}

}