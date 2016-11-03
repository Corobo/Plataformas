package com.plataformas.modelos.personajes.enemigos;

import android.content.Context;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.global.Estado;
import com.plataformas.graficos.Sprite;
import com.plataformas.modelos.personajes.efectos.Disparo;
import com.plataformas.modelos.personajes.efectos.DisparoEnemigo;

/**
 * Created by uo227602 on 03/11/2016.
 */

public class EnemigoAmpliacion extends Enemigo {

    protected int cadenciaDisparo = 3000;
    protected long milisegundosDisparo = 0;

    public EnemigoAmpliacion(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial);

        velocidadX = 1.8;
        cDerecha = 15;
        cIzquierda = 15;
        cArriba = 20;
        cAbajo = 20;

        estado = Estado.ACTIVO;

        inicializar();
    }

    @Override
    public void inicializar() {
        Sprite caminandoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemyrunrightampliacion),
                ancho, altura,
                4, 4, true);
        sprites.put(CAMINANDO_DERECHA, caminandoDerecha);

        Sprite caminandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemyrunampliacion),
                ancho, altura,
                4, 4, true);
        sprites.put(CAMINANDO_IZQUIERDA, caminandoIzquierda);


        Sprite muerteDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemydierightampliacion),
                ancho, altura,
                3, 6, false);
        sprites.put(MUERTE_DERECHA, muerteDerecha);

        Sprite muerteIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemydieampliacion),
                ancho, altura,
                3, 6, false);
        sprites.put(MUERTE_IZQUIERDA, muerteIzquierda);



        sprite = caminandoDerecha;
    }

    public Disparo disparar(long milisegundos) {
        if (milisegundos - milisegundosDisparo> cadenciaDisparo
                + Math.random()* cadenciaDisparo) {
            milisegundosDisparo = milisegundos;
            return new DisparoEnemigo(context, x, y,orientacion);
        }
        return null;
    }
}
