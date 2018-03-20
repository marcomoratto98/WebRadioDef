package com.example.morattomarco.webradio;

import android.widget.ImageView;

/**
 * Created by Marco on 28/02/2018.
 */

public class Radio {

    private String stream,nome;


    public Radio(String str,String nom){
        stream=str;
        nome=nom;
    }

    public void setStream(String s){
        stream=s;
    }

    public String getStream(){
        return stream;
    }

    public void setNome(String n){
        nome=n;
    }

    public String getNome(){
        return nome;
    }




}
