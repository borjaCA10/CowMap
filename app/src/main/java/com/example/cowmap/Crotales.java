package com.example.cowmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Crotales extends AppCompatActivity {

    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    EditText nombreVaca;

    Crotal crotales;

    EditText CrotalVaca;
    EditText latitud, longitud;

    ListView listadoVacas;

    String clave;

    String res = "";

    ImageButton agregar;

    ImageButton borrar;

    ImageButton listar;

    Button volver;

    private List<Crotal> listCrotal = new ArrayList<Crotal>();

    BaseDatos bd = new BaseDatos(Crotales.this);
    ArrayAdapter<Crotal> arrayAdapterCrotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crotales);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Crotales");
        dbRef = userRef.child("CROTALES DE : " + userId);
        nombreVaca = findViewById(R.id.NombreVaca);
        CrotalVaca = findViewById(R.id.CrotalVaca);
        listadoVacas = findViewById(R.id.ListadoVacas);
        latitud = findViewById(R.id.Latitud);
        longitud = findViewById(R.id.Longitud);
        agregar = findViewById(R.id.Agregar);
        borrar = findViewById(R.id.Borrar);
        listar = findViewById(R.id.Buscar);
        volver = findViewById(R.id.MenuPrin);

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crotal nuevocrotal = new Crotal(nombreVaca.getText().toString(), CrotalVaca.getText().toString(),latitud.getText().toString(),longitud.getText().toString());

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
                            nombreVaca.setText(res);
                            CrotalVaca.setText(res);

                        } else {
                            bd.insertar(nombreVaca.getText().toString(),latitud.getText().toString(),longitud.getText().toString());

                            dbRef.push().setValue(nuevocrotal);



                            Toast.makeText(Crotales.this, "GUARDADO CORRECTO", Toast.LENGTH_SHORT).show();

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
                        AlertDialog.Builder builder = new AlertDialog.Builder(Crotales.this);
                        builder.setTitle("BORRADO CORRECTO");
                        builder.setMessage("SE HA BORRADO TODOS LOS CROTALES CON NUMERO " + CrotalVaca.getText().toString());
                        builder.setPositiveButton("ACEPTAR", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();

                        CrotalVaca.setText(res);
                        nombreVaca.setText(res);
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
                        listCrotal.clear();
                        for(DataSnapshot ds: snapshot.getChildren()){
                            Crotal c = ds.getValue(Crotal.class);
                            listCrotal.add(c);

                            arrayAdapterCrotal = new ArrayAdapter<Crotal>(Crotales.this, android.R.layout.simple_list_item_1,listCrotal);
                            listadoVacas.setAdapter(arrayAdapterCrotal);
                        }
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
                latitud.setText(crotales.getLatitud());
                longitud.setText(crotales.getLongitud());

            }
        });

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent volverAtras = new Intent(Crotales.this, Menu.class);

                startActivity(volverAtras);
            }
        });

    }
}