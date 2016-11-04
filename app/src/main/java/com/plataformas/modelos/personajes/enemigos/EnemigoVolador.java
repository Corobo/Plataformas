package com.plataformas.modelos.personajes.enemigos;

import android.content.Context;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.global.Estado;
import com.plataformas.graficos.Sprite;

/**
 * Created by uo227602 on 04/11/2016.
 */

public class EnemigoVolador extends Enemigo {

    public double velocidadY = 1.0;
    public double yInicial;

    public EnemigoVolador(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial);
        this.yInicial = yInicial;
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

    public void volar(){
        if(y>(yInicial+12)){
            velocidadY = (y - yInicial+16);
            y -= velocidadY;
        }else if(y<(yInicial-12)){
            velocidadY = (yInicial-12)-y;
            y += velocidadY;
        }
    }


}
