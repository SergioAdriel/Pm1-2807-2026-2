package com.example.androidya;

// Jesus Eduardo Diaz Pompa

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.*;

public class MainActivity extends AppCompatActivity {


    private EditText etNotas;

    private EditText etFecha, etContenido;

    private EditText etArchivo, etContenidoSD;

    private EditText etCodigo, etDescripcion, etPrecio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNotas = findViewById(R.id.etNotas);

        etFecha = findViewById(R.id.etFecha);
        etContenido = findViewById(R.id.etContenido);

        etArchivo = findViewById(R.id.etArchivo);
        etContenidoSD = findViewById(R.id.etContenidoSD);

        etCodigo = findViewById(R.id.etCodigo);
        etDescripcion = findViewById(R.id.etDescripcion);
        etPrecio = findViewById(R.id.etPrecio);

        cargarNotas();
    }


    private void cargarNotas() {
        try {
            InputStreamReader archivo = new InputStreamReader(openFileInput("notas.txt"));
            BufferedReader br = new BufferedReader(archivo);
            String linea, todo = "";
            while ((linea = br.readLine()) != null) {
                todo += linea + "\n";
            }
            br.close();
            archivo.close();
            etNotas.setText(todo);
        } catch (IOException e) {}
    }

    public void guardarNotas(View v) {
        try {
            OutputStreamWriter archivo = new OutputStreamWriter(
                    openFileOutput("notas.txt", Activity.MODE_PRIVATE));
            archivo.write(etNotas.getText().toString());
            archivo.close();
            Toast.makeText(this, "Notas guardadas", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {}
    }

    public void guardarFecha(View v) {
        String nombre = etFecha.getText().toString().replace('/', '-');
        try {
            OutputStreamWriter archivo = new OutputStreamWriter(
                    openFileOutput(nombre, Activity.MODE_PRIVATE));
            archivo.write(etContenido.getText().toString());
            archivo.close();
            Toast.makeText(this, "Guardado por fecha", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {}
    }

    public void recuperarFecha(View v) {
        String nombre = etFecha.getText().toString().replace('/', '-');
        try {
            InputStreamReader archivo = new InputStreamReader(openFileInput(nombre));
            BufferedReader br = new BufferedReader(archivo);
            String linea, todo = "";
            while ((linea = br.readLine()) != null) {
                todo += linea + "\n";
            }
            br.close();
            archivo.close();
            etContenido.setText(todo);
        } catch (IOException e) {
            Toast.makeText(this, "No existe esa fecha", Toast.LENGTH_SHORT).show();
        }
    }


    public void guardarSD(View v) {
        try {
            File file = new File(getExternalFilesDir(null), etArchivo.getText().toString());
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file));
            osw.write(etContenidoSD.getText().toString());
            osw.close();
            Toast.makeText(this, "Guardado en SD", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {}
    }

    public void recuperarSD(View v) {
        try {
            File file = new File(getExternalFilesDir(null), etArchivo.getText().toString());
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String linea, todo = "";
            while ((linea = br.readLine()) != null) {
                todo += linea + " ";
            }
            br.close();
            etContenidoSD.setText(todo);
        } catch (IOException e) {
            Toast.makeText(this, "Error al leer SD", Toast.LENGTH_SHORT).show();
        }
    }


    public void alta(View v) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        ContentValues registro = new ContentValues();
        registro.put("codigo", etCodigo.getText().toString());
        registro.put("descripcion", etDescripcion.getText().toString());
        registro.put("precio", etPrecio.getText().toString());

        bd.insert("articulos", null, registro);
        bd.close();

        Toast.makeText(this, "Artículo guardado", Toast.LENGTH_SHORT).show();
    }

    public void consultarCodigo(View v) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        Cursor fila = bd.rawQuery(
                "select descripcion,precio from articulos where codigo=" + etCodigo.getText(), null);

        if (fila.moveToFirst()) {
            etDescripcion.setText(fila.getString(0));
            etPrecio.setText(fila.getString(1));
        } else {
            Toast.makeText(this, "No existe", Toast.LENGTH_SHORT).show();
        }
        bd.close();
    }

    public void eliminar(View v) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        int cant = bd.delete("articulos", "codigo=" + etCodigo.getText(), null);
        bd.close();

        Toast.makeText(this, cant == 1 ? "Eliminado" : "No existe", Toast.LENGTH_SHORT).show();
    }

    public void modificar(View v) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        ContentValues registro = new ContentValues();
        registro.put("descripcion", etDescripcion.getText().toString());
        registro.put("precio", etPrecio.getText().toString());

        int cant = bd.update("articulos", registro,
                "codigo=" + etCodigo.getText(), null);

        bd.close();

        Toast.makeText(this, cant == 1 ? "Modificado" : "No existe", Toast.LENGTH_SHORT).show();
    }
}