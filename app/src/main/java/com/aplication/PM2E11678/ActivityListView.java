/* Programa realizado por los alumnos de Programacion Movil I:
 * Jimmy Orlando Zelaya Muñoz 201910050016 y Monica Rebeca Ramos Flores 201910110078*/

package com.aplication.PM2E11678;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.aplication.PM2E11678.configuraciones.SQLiteConexion;
import com.aplication.PM2E11678.configuraciones.Transacciones;
import com.aplication.PM2E11678.tablas.Contactos;

import java.util.ArrayList;

public class ActivityListView extends AppCompatActivity {

    private static final int REQUEST_CALL = 1;
    SearchView txtBuscar;
    SQLiteConexion conexion;
    RecyclerView listaContactos;
    ListView listacontactos;
    ArrayList<Contactos> lista;
    ArrayList<String> ArregloContactos;
    String nombre, telefono, nota;
    Button btnCompartir;
    Button btnImagen;
    Button btnEliminar;
    Button btnActualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
        listacontactos = (ListView) findViewById(R.id.lista);

        ArregloContactos = new ArrayList<>();

        txtBuscar = findViewById(R.id.txtBuscar);

     //   listacontactos.setLayoutManager(new LinearLayoutManager(this));

        ObtenerListaPersonas();

        ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ArregloContactos);
        listacontactos.setAdapter(adp);

    }
    private void ObtenerListaPersonas() {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Contactos list_personas = null;
        lista = new ArrayList<Contactos>();

        //cursor de la base de datos : nos apoya en recorrer  la info de la tabla a la cual consultamos

        Cursor cursor = db.rawQuery("SELECT * FROM " + Transacciones.tablaContactos, null);

        //recorremos la info del cursor
        while (cursor.moveToNext()){

            list_personas = new Contactos();

            list_personas.setId(cursor.getInt(0));
            list_personas.setNombre(cursor.getString(1));
            list_personas.setPais(cursor.getString(2));
            list_personas.setTelefono(cursor.getString(3));
            list_personas.setNota(cursor.getString(4));

            lista.add(list_personas);

        }

        db.close();

        filllist();

    }

    private void filllist() {
        ArregloContactos = new ArrayList<String>();

        for (int i = 0;i < lista.size(); i++)
        {
            ArregloContactos.add(lista.get(i).getId()+ "|"
                    +lista.get(i).getPais()+ "|"
                    +lista.get(i).getNombre()+ "|"
                    +lista.get(i).getTelefono()+ "|"
                    +lista.get(i).getNota());
        }


    }

    public void Llamar(){

        if (ContextCompat.checkSelfPermission(ActivityListView.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ActivityListView.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
        }else{
            String dial = "tel:"+telefono;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CALL){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Llamar();
            }else{
                Toast.makeText(this,"Permiso Denegado",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void AlertDialog() {
        AlertDialog.Builder myBuild = new AlertDialog.Builder(this);
        myBuild.setTitle("Accion");
        myBuild.setMessage("Desea realizar la llamada?");
        myBuild.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Llamar();
            }
        });
        myBuild.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = myBuild.create();
        dialog.show();
    }

    public void Compartir(){
        Intent intent = new Intent (Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,nombre+" "+telefono);
        Intent opcion = Intent.createChooser(intent,"Compartir usando");
        startActivity(opcion);
    }

    btnCompartir.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(nombre == null && telefono == null && nota == null){
                Toast.makeText(ActivityListView.this, "Seleccione un registro", Toast.LENGTH_SHORT).show();
            }else {
                Compartir();
            }
        }
    });



/*
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menuNuevo:
                nuevoRegistro();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

 */


}