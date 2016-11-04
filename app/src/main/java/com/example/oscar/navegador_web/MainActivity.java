package com.example.oscar.navegador_web;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import java.util.ArrayList;
import java.util.HashSet;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressBar pBar;
    private WebView myWebView;
    private AutoCompleteTextView editText;
    private AdminSQL bd;
    private Historial historial;
    long nreg_afectados= -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myWebView = (WebView) this.findViewById(R.id.myWebView);//Componente donde se muestra la pagina web

        WebSettings webSettings = myWebView.getSettings();//Obtenemos los ajustes del webview en cuestion
        webSettings.setJavaScriptEnabled(true);//Habilita javaScript

        bd = new AdminSQL(this);

        String url="http://www.google.es";
        myWebView.loadUrl(url);



        editText=(AutoCompleteTextView)this.findViewById(R.id.editText);
        url = editText.getText().toString();

        //Cargamos la web con el siguiente metodo
        myWebView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }
        });

        editText.setOnKeyListener(new View.OnKeyListener(){
            public boolean onKey(View v, int KeyCode, KeyEvent event){
                if((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (KeyCode == KeyEvent.KEYCODE_ENTER)){
                        goWeb();
                        return true;
                }
                return false;
            }
        });


        myWebView.setWebViewClient(new WebViewClient());

        InputMethodManager imn = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imn.hideSoftInputFromWindow(myWebView.getWindowToken(),0);

        pBar = (ProgressBar) findViewById(R.id.pBar);

        myWebView.setWebChromeClient(new WebChromeClient(){
            private int progress;

            public void setProgress(int progress){this.progress = progress;}

            public void onProgressChanged(WebView view, int progress){

                pBar.setProgress(0);
                pBar.setVisibility(View.VISIBLE);
                this.setProgress(progress*1000);

                pBar.incrementProgressBy(progress);

                if(progress==100){
                    pBar.setVisibility(View.GONE);
                }
            }
        });

        obtenerSugerencias();
    }

    @Override
    public void onClick(View v) {
        goWeb();

    }

    public void goWeb(){
        try {
            String header = "http://www.";
            String url = editText.getText().toString();

            if (URLUtil.isValidUrl(url)) {
                historial = new Historial(url, url);
                guardarUrl(historial);
                myWebView.loadUrl(url);

            } else {
                myWebView.loadUrl(header + url);
                historial = new Historial(editText.getText().toString(), editText.getText().toString());
                guardarUrl(historial);
                editText.setText("");
            }
        }catch (Exception e){
            //Toast.makeText(this, "No se guardaron datos en el historial", Toast.LENGTH_SHORT).show();
        }

        obtenerSugerencias();
    }

    public void home(View v){
        String url="http://www.google.es";
        myWebView.loadUrl(url);
    }

    public void obtenerSugerencias(){
        ArrayList<String> sugerencias = new ArrayList();
        ArrayList<Historial> listaHistorial = null;
        try {
            listaHistorial = bd.obtenerHistorial();

            for(int i=0; i < listaHistorial.size(); i++){
                String direccion = listaHistorial.get(i).getTexto();
                if(direccion != null){
                    sugerencias.add(direccion);
                }
            }

            HashSet<String> hashSet = new HashSet<String>(sugerencias);
            sugerencias.clear();
            sugerencias.addAll(hashSet);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item, sugerencias);
            editText.setThreshold(1);
            editText.setAdapter(adapter);
            editText.setTextColor(Color.RED);
        }catch (Exception e){}
    }

    public void lanzarListaHistorial(View v){
        Intent i = new Intent(this, Lista.class);
        startActivityForResult(i,0);
    }

    public void guardarUrl(Historial historial) {
        nreg_afectados = bd.insertar(historial);
    }

    public void anterior(View v){
        if(myWebView.canGoBack()){
            myWebView.goBack();
        }
    }

    public void siguiente(View v){
        if(myWebView.canGoForward()){
            myWebView.goForward();
        }
    }
    
}

