package com.example.oscar.navegador_web;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by oscar on 03/11/2016.
 */

public class Lista extends AppCompatActivity {
    AdminSQL as;
    ArrayList url;

    protected void onCreate(Bundle savedInstaState) {
        super.onCreate(savedInstaState);
        setContentView(R.layout.activity_lista);

        as = new AdminSQL(this);

        url = new ArrayList<String>();

        try {
            ArrayList<Historial> listaHistorial= null;
            listaHistorial=as.obtenerHistorial();
            for(int i=0; i < listaHistorial.size(); i++){
                String x=listaHistorial.get(i).getUrl();
                if(x!=null)
                    url.add(x);
                Collections.reverse(url);

                ListView lv = (ListView) findViewById(R.id.lv);
                lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, url));
            }

        }catch (Exception e){
            Toast.makeText(this, "Historial vacio.", Toast.LENGTH_SHORT).show();
        }



    }

    public void borrar(View v){
        ArrayList<String> texto = new ArrayList<>();
        ArrayList<Historial> listaHistorial = null;

        try{
            listaHistorial = as.obtenerHistorial();

            for(int i=0; i< listaHistorial.size(); i++){
                String direccion = listaHistorial.get(i).getTexto();
                if(direccion != null){
                    texto.add(direccion);
                }
            }
            HashSet<String> hashSet = new HashSet<String>(texto);
            texto.clear();
            texto.addAll(hashSet);

            int nreg_afectados = as.borrarHistorial();

            if(nreg_afectados >= 0){
                Toast.makeText(this, "Se ha borrado el historial.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "No se ha borrado el historial.", Toast.LENGTH_SHORT).show();
            }
            for(int i=0; i< texto.size(); i++){
                Historial h = new Historial(texto.get(i));
                as.insertar(h);
            }

        }catch(Exception e){
            Toast.makeText(this, "Error al borrar el historial.", Toast.LENGTH_SHORT).show();
        }
        finish();

    }
}
