package com.example.cowmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Crotales extends AppCompatActivity {

    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    EditText nombreVaca;

    Crotal crotales;

    EditText CrotalVaca;

    ListView listadoVacas;

    String clave;

    String res = "";

    Button agregar;

    Button borrar;

    Button listar;

    Button volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crotales);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference();
        dbRef = userRef.child("CROTALES DE : " + userId);
        nombreVaca = findViewById(R.id.NombreVaca);
        CrotalVaca = findViewById(R.id.CrotalVaca);
        listadoVacas = findViewById(R.id.ListadoVacas);
        agregar = findViewById(R.id.Agregar);
        borrar = findViewById(R.id.Borrar);
        listar = findViewById(R.id.Buscar);
        volver = findViewById(R.id.MenuPrin);

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nombreVaca.getText().equals("") || CrotalVaca.getText().equals("")) {
                    Crotal nuevocrotal = new Crotal(nombreVaca.getText().toString(), CrotalVaca.getText().toString());

                    dbRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean numero1 = false;
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                Crotal crotal = ds.getValue(Crotal.class);
                                if (crotal.getCrotal().equals(nuevocrotal.getCrotal())) {
                                    numero1 = true;
                                    break;
                                }
                            }
                            if (numero1) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Crotales.this);
                                builder.setTitle("GUARDADO INCORRECTO");
                                builder.setMessage("HA SURGIDO UN ERROR, YA EXISTE EL MISMO CROTAL");
                                builder.setPositiveButton("ACEPTAR", null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            } else {
                                dbRef.push().setValue(nuevocrotal);
                                AlertDialog.Builder builder = new AlertDialog.Builder(Crotales.this);
                                builder.setTitle("GUARDADO CORRECTO");
                                builder.setMessage("SE HA GUARDADO EL CROTAL EN NUESTRA BASE DE DATOS");
                                builder.setPositiveButton("ACEPTAR", null);
                                AlertDialog dialog = builder.create();
                                dialog.show();

                                nombreVaca.setText(res);
                                CrotalVaca.setText(res);


                                finish();
                            }
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = dbRef.orderByChild("crotal").equalTo(CrotalVaca.getText().toString());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            clave = data.getKey();
                            dbRef.child(clave).removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        listar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayAdapter<String> adapter;
                        ArrayList<String> listadoCrotales = new ArrayList<String>();
                        for (DataSnapshot dp : snapshot.getChildren()) {
                            crotales = dp.getValue(Crotal.class);
                            listadoCrotales.add(crotales.getCrotal());
                        }
                        adapter = new ArrayAdapter<>(Crotales.this, android.R.layout.simple_list_item_1, listadoCrotales);
                        listadoVacas.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        listadoVacas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                crotales = (Crotal) parent.getItemAtPosition(position);
                nombreVaca.setText(crotales.getNombre());
                CrotalVaca.setText(crotales.getCrotal());

            }
        });


    }
}