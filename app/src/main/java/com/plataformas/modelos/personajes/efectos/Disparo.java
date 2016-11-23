package com.plataformas.modelos.personajes.efectos;

import android.content.Context;
import android.graphics.Canvas;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.graficos.Sprite;
import com.plataformas.modelos.Modelo;
import com.plataformas.modelos.escenarios.Nivel;
import com.plataformas.modelos.personajes.jugadores.Jugador;

/**
 * Created by uo227602 on 03/11/2016.
 */

public class Disparo extends Modelo {

    protected Sprite sprite;
    public double velocidadX = 5;

    public Disparo(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial, 35, 35);

    }

    public void inicializar (){
        sprite= new Sprite(
                CargadorGraficos.cargarDrawable(context,
                        R.drawable.animacion_disparo3),
                ancho, altura,
                24, 5, true);
    }

    public void actualizar (long tiempo) {
        sprite.actualizar(tiempo);
    }

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX, (int) y - Nivel.scrollEjeY);
    }

}
