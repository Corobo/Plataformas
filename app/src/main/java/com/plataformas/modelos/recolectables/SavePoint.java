package com.plataformas.modelos.recolectables;

import android.content.Context;
import android.graphics.Canvas;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.modelos.Modelo;
import com.plataformas.modelos.escenarios.Nivel;

/**
 * Created by uo227602 on 09/11/2016.
 */

public class SavePoint extends Modelo {


    double xSalvada;
    double ySalvada;
    boolean salvado;

    public SavePoint(Context context, double x, double y) {
            super(context, x, y, 32,40);

            this.y =  y - altura/2;;

            xSalvada = x;
            ySalvada = y;

            imagen = CargadorGraficos.cargarDrawable(context, R.drawable.flagyellow_down);
    }


    public void dibujar(Canvas canvas){
        int yArriba = (int)  y - altura / 2 - Nivel.scrollEjeY;
        int xIzquierda = (int) x - ancho / 2 - Nivel.scrollEjeX;

        imagen.setBounds(xIzquierda, yArriba, xIzquierda
                + ancho, yArriba + altura);
        imagen.draw(canvas);

    }

    public double getxSalvada(){
        return xSalvada;
    }
    public double getySalvada(){
        return  ySalvada;
    }
    public boolean getSalvado(){
        return salvado;
    }
    public void setSalvado(boolean salvado){
        this.salvado=salvado;
    }

    public void changeImage() {
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.flagyellow);
    }
}
