package com.example.androidya;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Main Activity for the AndroidYa application.
 * Demonstrates internal storage, external storage, and SQLite usage.
 *
 * Modified for better practices and code quality.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String NOTAS_FILE = "notas.txt";

    private EditText etNotas;
    private EditText etFecha, etContenido;
    private EditText etArchivo, etContenidoSD;
    private EditText etCodigo, etDescripcion, etPrecio;

    private AdminSQLiteOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        etNotas = findViewById(R.id.etNotas);
        etFecha = findViewById(R.id.etFecha);
        etContenido = findViewById(R.id.etContenido);
        etArchivo = findViewById(R.id.etArchivo);
        etContenidoSD = findViewById(R.id.etContenidoSD);
        etCodigo = findViewById(R.id.etCodigo);
        etDescripcion = findViewById(R.id.etDescripcion);
        etPrecio = findViewById(R.id.etPrecio);

        dbHelper = new AdminSQLiteOpenHelper(this);

        cargarNotas();
    }

    // --- Internal Storage: Fixed File (notas.txt) ---

    private void cargarNotas() {
        try (InputStreamReader isr = new InputStreamReader(openFileInput(NOTAS_FILE));
             BufferedReader br = new BufferedReader(isr)) {
            StringBuilder sb = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea).append("\n");
            }
            etNotas.setText(sb.toString());
        } catch (IOException e) {
            Log.e(TAG, "Error loading notes", e);
        }
    }

    public void guardarNotas(View v) {
        try (OutputStreamWriter osw = new OutputStreamWriter(
                openFileOutput(NOTAS_FILE, Activity.MODE_PRIVATE))) {
            osw.write(etNotas.getText().toString());
            showToast("Notas guardadas");
        } catch (IOException e) {
            Log.e(TAG, "Error saving notes", e);
            showToast("Error al guardar notas");
        }
    }

    // --- Internal Storage: Dynamic Filenames (by date) ---

    public void guardarFecha(View v) {
        String nombre = etFecha.getText().toString().trim().replace('/', '-');
        if (nombre.isEmpty()) {
            showToast("Ingrese una fecha/nombre");
            return;
        }

        try (OutputStreamWriter osw = new OutputStreamWriter(
                openFileOutput(nombre, Activity.MODE_PRIVATE))) {
            osw.write(etContenido.getText().toString());
            showToast("Guardado por fecha");
        } catch (IOException e) {
            Log.e(TAG, "Error saving file by date", e);
            showToast("Error al guardar");
        }
    }

    public void recuperarFecha(View v) {
        String nombre = etFecha.getText().toString().trim().replace('/', '-');
        if (nombre.isEmpty()) {
            showToast("Ingrese una fecha/nombre");
            return;
        }

        try (InputStreamReader isr = new InputStreamReader(openFileInput(nombre));
             BufferedReader br = new BufferedReader(isr)) {
            StringBuilder sb = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea).append("\n");
            }
            etContenido.setText(sb.toString());
        } catch (IOException e) {
            Log.e(TAG, "File not found: " + nombre);
            showToast("No existe esa fecha");
            etContenido.setText("");
        }
    }

    // --- External Storage (SD/App Specific Folder) ---

    public void guardarSD(View v) {
        String fileName = etArchivo.getText().toString().trim();
        if (fileName.isEmpty()) {
            showToast("Ingrese nombre del archivo");
            return;
        }

        File file = new File(getExternalFilesDir(null), fileName);
        try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file))) {
            osw.write(etContenidoSD.getText().toString());
            showToast("Guardado en SD");
        } catch (IOException e) {
            Log.e(TAG, "Error saving to SD", e);
            showToast("Error al guardar en SD");
        }
    }

    public void recuperarSD(View v) {
        String fileName = etArchivo.getText().toString().trim();
        if (fileName.isEmpty()) {
            showToast("Ingrese nombre del archivo");
            return;
        }

        File file = new File(getExternalFilesDir(null), fileName);
        if (!file.exists()) {
            showToast("El archivo no existe en SD");
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            StringBuilder sb = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea).append(" ");
            }
            etContenidoSD.setText(sb.toString());
        } catch (IOException e) {
            Log.e(TAG, "Error reading from SD", e);
            showToast("Error al leer de SD");
        }
    }

    // --- SQLite Database Operations ---

    public void alta(View v) {
        String codigo = etCodigo.getText().toString().trim();
        String desc = etDescripcion.getText().toString().trim();
        String precio = etPrecio.getText().toString().trim();

        if (codigo.isEmpty() || desc.isEmpty() || precio.isEmpty()) {
            showToast("Complete todos los campos");
            return;
        }

        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues registro = new ContentValues();
            registro.put(AdminSQLiteOpenHelper.COLUMN_CODIGO, codigo);
            registro.put(AdminSQLiteOpenHelper.COLUMN_DESCRIPCION, desc);
            registro.put(AdminSQLiteOpenHelper.COLUMN_PRECIO, precio);

            long result = db.insert(AdminSQLiteOpenHelper.TABLE_ARTICULOS, null, registro);
            if (result != -1) {
                showToast("Artículo guardado");
                limpiarCamposArticulo();
            } else {
                showToast("Error al insertar. ¿Código duplicado?");
            }
        }
    }

    public void consultarCodigo(View v) {
        String codigo = etCodigo.getText().toString().trim();
        if (codigo.isEmpty()) {
            showToast("Ingrese el código a buscar");
            return;
        }

        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor fila = db.query(AdminSQLiteOpenHelper.TABLE_ARTICULOS,
                     new String[]{AdminSQLiteOpenHelper.COLUMN_DESCRIPCION, AdminSQLiteOpenHelper.COLUMN_PRECIO},
                     AdminSQLiteOpenHelper.COLUMN_CODIGO + "=?",
                     new String[]{codigo}, null, null, null)) {

            if (fila.moveToFirst()) {
                etDescripcion.setText(fila.getString(0));
                etPrecio.setText(fila.getString(1));
            } else {
                showToast("No existe un artículo con ese código");
            }
        }
    }

    public void eliminar(View v) {
        String codigo = etCodigo.getText().toString().trim();
        if (codigo.isEmpty()) {
            showToast("Ingrese el código a eliminar");
            return;
        }

        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            int cant = db.delete(AdminSQLiteOpenHelper.TABLE_ARTICULOS,
                    AdminSQLiteOpenHelper.COLUMN_CODIGO + "=?",
                    new String[]{codigo});

            if (cant == 1) {
                showToast("Artículo eliminado");
                limpiarCamposArticulo();
            } else {
                showToast("No existe un artículo con ese código");
            }
        }
    }

    public void modificar(View v) {
        String codigo = etCodigo.getText().toString().trim();
        String desc = etDescripcion.getText().toString().trim();
        String precio = etPrecio.getText().toString().trim();

        if (codigo.isEmpty() || desc.isEmpty() || precio.isEmpty()) {
            showToast("Complete todos los campos");
            return;
        }

        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues registro = new ContentValues();
            registro.put(AdminSQLiteOpenHelper.COLUMN_DESCRIPCION, desc);
            registro.put(AdminSQLiteOpenHelper.COLUMN_PRECIO, precio);

            int cant = db.update(AdminSQLiteOpenHelper.TABLE_ARTICULOS, registro,
                    AdminSQLiteOpenHelper.COLUMN_CODIGO + "=?",
                    new String[]{codigo});

            if (cant == 1) {
                showToast("Artículo modificado");
            } else {
                showToast("No existe un artículo con ese código");
            }
        }
    }

    // --- Helper Methods ---

    private void showToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    private void limpiarCamposArticulo() {
        etCodigo.setText("");
        etDescripcion.setText("");
        etPrecio.setText("");
    }
}
