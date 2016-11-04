package com.example.oscar.navegador_web;

/**
 * Created by oscar on 28/10/2016.
 */

public class Historial {
    private String texto;
    private String url;

    public Historial(String texto){
        this.texto = texto;
    }

    public Historial(String texto, String url){
        this.texto = texto;
        this.url = url;
    }

    public String getTexto(){
        return texto;
    }

    public String getUrl(){
        return url;
    }

    public void setTexto(String text){
        this.texto = text;
    }

    public void setUrl(String url){
        this.url = url;
    }
}
