package com.example.semana02;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.semana02.Util.ConnectionRest;
import com.example.semana02.Entity.User;
import com.example.semana02.service.ServiceUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    Spinner spnUsuarios;
    ArrayAdapter<String> adaptadorUsuarios;
    ArrayList<String> listaUsuarios = new ArrayList<String>();
    Button btnFiltrar;
    TextView txtResultado;

    //CONECTARNOS AL SERVICIO REST
    ServiceUser serviceUser;

    private List<User>listaTotalUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        adaptadorUsuarios = new ArrayAdapter<String>( this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listaUsuarios);
        spnUsuarios = findViewById(R.id.spnUsuarios);
        spnUsuarios.setAdapter(adaptadorUsuarios);

        //RELACIONA LAS VARIABLES CON LAS VARIANTES DE LA GUI
        spnUsuarios = findViewById(R.id.spnUsuarios);
        btnFiltrar = findViewById(R.id.btnFiltrar);
        txtResultado = findViewById(R.id.txtResultado);

        //CONECTA AL SERVICIO REST
        serviceUser = ConnectionRest.getConnecion().create(ServiceUser.class);

        cargaUsuarios();

        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = spnUsuarios.getSelectedItem().toString();
                int position = spnUsuarios.getSelectedItemPosition();
                String id = item.split("-")[0];
                String nombre = item.split("-")[1];

                User objUserSeleccionado = listaTotalUsuario.get(position);


                String salida =  "Users: \n\n";
                salida +=  "Position    :       " + position +"\n";
                salida +=  "Id  :                 " + id +"\n";
                salida +=  "Name    :               " + nombre +"\n";
                salida +=  "UserName    :       " + objUserSeleccionado.getUsername() +"\n";
                salida +=  "Email   :          " + objUserSeleccionado.getEmail() +"\n";
                salida +=  "Address :    " +"\n";
                salida +=  "        Street  :     " +objUserSeleccionado.getAddress().getStreet() +"\n";
                salida +=  "        City    :     " +objUserSeleccionado.getAddress().getCity() +"\n";
                salida +=  "        ZipCode :     " +objUserSeleccionado.getAddress().getZipcode() +"\n";
                salida +=  "        Geo :     " +"\n";
                salida +=  "            Lat :        " +objUserSeleccionado.getAddress().getGeo().getLat() +"\n";
                salida +=  "            Log :        " +objUserSeleccionado.getAddress().getGeo().getLng() +"\n";
                salida +=  "Phone   :      " + objUserSeleccionado.getPhone() +"\n";
                salida +=  "Web Site    :      " + objUserSeleccionado.getWebsite() +"\n";




                txtResultado.setText(salida);

            }
        });

    }

    void cargaUsuarios(){
        Call<List<User>> call = serviceUser.listausuarios();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()){
                    listaTotalUsuario = response.body();
                    for(User x:listaTotalUsuario){
                        listaUsuarios.add(x.getId() + " - " + x.getName());
                    }
                    adaptadorUsuarios.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });

    }
}