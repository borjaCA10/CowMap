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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class Registro extends AppCompatActivity {
    Button cancelar;
    Button registro;
    private FirebaseAuth contactoBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        EditText usuario = findViewById(R.id.usuRegistro);
        EditText contraseña = findViewById(R.id.ContraRegistro);
        registro = findViewById(R.id.Entrar);
        cancelar = findViewById(R.id.Cancel);

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            registrarse(usuario.getText().toString(),contraseña.getText().toString());
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancelar = new Intent(Registro.this, Principal.class);
                startActivity(cancelar);
            }
        });

    }

    public void registrarse(String usuario, String contraseña) {
        contactoBase = FirebaseAuth.getInstance();
        if (usuario.equals("") || contraseña.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("REGISTRO INCORRECTO");
            builder.setMessage("NO SE HAN RELLENADOS TODOS LOS CAMPOS");
            builder.setPositiveButton("ACEPTAR", null);
            AlertDialog dialog = builder.create();
            dialog.show();

        } else {
            contactoBase.createUserWithEmailAndPassword(usuario, contraseña).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = contactoBase.getCurrentUser();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Registro.this);
                        builder.setTitle("REGISTRO CORRECTO");
                        builder.setMessage("SE HA REGISTRADO CORRECTAMENTE " + user.getUid());
                        builder.setPositiveButton("ACEPTAR", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        Intent avanzar = new Intent(Registro.this, Menu.class);
                        startActivity(avanzar);
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Registro.this);
                            builder.setTitle("REGISTRO INCORRECTO");
                            builder.setMessage("HA SURGIDO UN ERROR, YA EXISTE UN USUARIO ");
                            builder.setPositiveButton("ACEPTAR", null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Registro.this);
                            builder.setTitle("REGISTRO INCORRECTO");
                            builder.setMessage("HA SURGIDO UN ERROR");
                            builder.setPositiveButton("ACEPTAR", null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                }
            });
        }

    }
}