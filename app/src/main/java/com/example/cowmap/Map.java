package com.example.cowmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Map extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private DatabaseReference dbRef;
    // Se obtienen los datos de Firebase y se guardan en una base de datos local, por si se pierde la conexión a internet.
    BaseDatos bd = new BaseDatos(Map.this);


    ArrayList<Vaca> listadoPosicion = new ArrayList<Vaca>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        dbRef = FirebaseDatabase.getInstance().getReference().child("Crotales").child("CROTALES DE : " + userId);

        getPosicionBase();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        listadoPosicion = bd.getVacas();
        dibujarVacas(mMap);

        LatLng latLng = new LatLng(42.96, -6.513);
        Float zoom = Float.valueOf(13);
         mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
         mMap.getUiSettings().setZoomControlsEnabled(true);

    }


    private void getPosicionBase() {
        dbRef.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String latitud = ds.child("latitud").getValue().toString();
                        String longitud = ds.child("longitud").getValue().toString();
                        String nombre = ds.child("nombre").getValue().toString();

                        bd.insertar(nombre, latitud, longitud);


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }

    private void dibujarVacas(GoogleMap mMap) {

        for (Vaca vaca : listadoPosicion) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(vaca.getLatitud()), Double.parseDouble(vaca.getLongitud()))).title(vaca.getNombre()).icon(BitmapDescriptorFactory.fromResource(R.drawable.cow_1)));
        }
    }

}