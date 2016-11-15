package com.plataformas.modelos.escenarios;

import android.content.Context;
import android.graphics.Canvas;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.global.Estado;
import com.plataformas.modelos.Modelo;

/**
 * Created by uo227602 on 15/11/2016.
 */

public class Plataforma extends Modelo {

    double velocidadX = 3;
    public int tileAnterior = 0;
    public int tileActual = 0;

    public Plataforma(Context context, double x, double y) {
        super(context, x, y, 32, 40);

        this.y = y -altura/2;

        tileActual = (int) x / Tile.ancho;

        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.platform);
    }

    public void girar(){
        velocidadX = velocidadX*-1;
    }

    public void dibujar(Canvas canvas){
        int yArriva = (int)  y - altura  - Nivel.scrollEjeY;
        int xIzquierda = (int) x - ancho/2 - Nivel.scrollEjeX;

        imagen.setBounds(xIzquierda, yArriva, xIzquierda
                + ancho, yArriva + altura);
        imagen.draw(canvas);
    }
}
