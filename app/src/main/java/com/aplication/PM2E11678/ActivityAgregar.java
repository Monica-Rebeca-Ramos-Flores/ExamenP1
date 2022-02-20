/* Programa realizado por los alumnos de Programacion Movil I:
 * Jimmy Orlando Zelaya Muñoz 201910050016 y Monica Rebeca Ramos Flores 201910110078*/

package com.aplication.PM2E11678;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aplication.PM2E11678.configuraciones.SQLiteConexion;
import com.aplication.PM2E11678.configuraciones.Transacciones;

import java.io.File;
import java.io.IOException;

public class ActivityAgregar extends AppCompatActivity {

    Button btnLista, btnGuardar;
    Spinner spin;
    ImageView foto;
    TextView txtNombre,txtTelefono,txtNota;
    String CurrentPhotoPath;
    static final int PETICION_ACCESO_CAM = 100;
    static final int TAKE_PIC_REQUEST = 101;
    String rutaImagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);

        spin = (Spinner) findViewById(R.id.spin);
        foto = (ImageView) findViewById(R.id.foto);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnLista = (Button) findViewById(R.id.btnLista);

        txtNombre = (TextView) findViewById(R.id.txtNombre);
        txtTelefono = (TextView) findViewById(R.id.txtTelefono);
        txtNota = (TextView) findViewById(R.id.txtNota);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.opciones,android.R.layout.simple_spinner_item);
        spin.setAdapter(adapter);


//TOMAR FOTO
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                permisos();
            }
        });

//GUARDAR CONTACTO
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                validar();
            }
        });

//VER LISTA
        btnLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), ActivityListView.class);
                startActivity(intent);
            }
        });
    }


    private void permisos()
    {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PETICION_ACCESO_CAM);
        }
        else
        {
            tomarfoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PETICION_ACCESO_CAM)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                tomarfoto();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Se necesitan permisos de acceso a la camara", Toast.LENGTH_LONG).show();
        }
    }

    private void tomarfoto()
    {
        Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takepic.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takepic, TAKE_PIC_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Byte[] arreglo;

        if(requestCode == TAKE_PIC_REQUEST && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap imagen = (Bitmap) extras.get("data");
            foto.setImageBitmap(imagen);
        }
    }

//VALIDAR
    public boolean validar(){

        boolean retorno=true;


        String c1=txtNombre.getText().toString();
        String c2=txtTelefono.getText().toString();
        String c3=txtNota.getText().toString();

        if(c1.isEmpty()){

            txtNombre.setError("Este campo no puede estar vacio");
            retorno = false;
        }
        if(c2.isEmpty()){

            txtTelefono.setError("Este campo no puede estar vacio");

            android:
            retorno = false;
        }
        if(c3.isEmpty()){

            txtNota.setError("Este campo no puede estar vacio");
            retorno = false;
        }

        else {


            agregarcontactos();

        }
        return retorno;
    }


        private void agregarcontactos(){

            SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
            SQLiteDatabase db = conexion.getWritableDatabase();

            ContentValues valores = new ContentValues();
            valores.put(Transacciones.pais, spin.getSelectedItem().toString());
            valores.put(Transacciones.nombre, txtNombre.getText().toString());
            valores.put(Transacciones.telefono, txtTelefono.getText().toString());
            valores.put(Transacciones.nota,txtNota.getText().toString());

            Long resultado = db.insert(Transacciones.tablaContactos,Transacciones.id,valores);

            Toast.makeText(getApplicationContext(), "Contacto Guardado : Codigo : " + resultado.toString(), Toast.LENGTH_LONG).show();

            db.close();

            LimpiarPantalla();
        }

        private void eliminar(){
            SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
            SQLiteDatabase db = conexion.getWritableDatabase();
            String [] parametros = {txtTelefono.getText().toString()};

            db.delete(Transacciones.tablaContactos,Transacciones.telefono+"=?", parametros);
            Toast.makeText(getApplicationContext(), "Usuario eliminado " , Toast.LENGTH_LONG).show();
            db.close();
        }


        private void LimpiarPantalla() {
            txtNombre.setText("");
            txtTelefono.setText("");
            txtNota.setText("");
        }


        //
    private File crearimagen() throws IOException {
        String nombreImagen = "foto_";
        File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile(nombreImagen, ".jpg", directorio);

        rutaImagen = imagen.getAbsolutePath();
        return imagen;
    }

}