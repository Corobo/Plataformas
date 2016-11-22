package com.plataformas.modelos.escenarios;

import android.content.Context;
import android.graphics.Canvas;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.modelos.Modelo;

/**
 * Created by uo227602 on 22/11/2016.
 */

public class Puerta extends Modelo {

    public int numero;

    public Puerta(Context context, double x, double y, int numero) {
        super(context, x, y, 64, 40);
        this.numero = numero;
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.puerta);
    }

    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }

    public void dibujar(Canvas canvas){
        int yArriva = (int)  y - altura  - Nivel.scrollEjeY;
        int xIzquierda = (int) x - ancho/2 - Nivel.scrollEjeX;

        imagen.setBounds(xIzquierda, yArriva, xIzquierda
                + ancho, yArriva + altura);
        imagen.draw(canvas);
    }
}
