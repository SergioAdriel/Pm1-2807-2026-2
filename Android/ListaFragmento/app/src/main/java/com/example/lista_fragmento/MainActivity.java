//Jesus Eduardo Diaz Pompa

package com.example.lista_fragmento;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

    ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lista = findViewById(R.id.listado);

        String[] animales = {
                "Gato",
                "Perro",
                "Vaca"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                animales
        );

        lista.setAdapter(adapter);

        // Fragment inicial
        reemplazarFragmento(new FragmentoGato());

        lista.setOnItemClickListener((parent, view, position, id) -> {

            switch (position){

                case 0:
                    reemplazarFragmento(new FragmentoGato());
                    break;

                case 1:
                    reemplazarFragmento(new FragmentoPerro());
                    break;

                case 2:
                    reemplazarFragmento(new FragmentoVaca());
                    break;
            }

        });
    }

    public void reemplazarFragmento(Fragment fragmento){

        FragmentManager manager = getFragmentManager();

        manager.beginTransaction()
                .replace(R.id.contenedorFragmentos, fragmento)
                .commit();
    }
}