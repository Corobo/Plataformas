package com.plataformas.modelos.personajes.efectos;


import android.content.Context;

import com.plataformas.modelos.personajes.enemigos.Enemigo;
import com.plataformas.modelos.personajes.jugadores.Jugador;

/**
 * Created by uo227602 on 03/11/2016.
 */

public class DisparoEnemigo extends Disparo{
    public DisparoEnemigo(Context context, double xInicial, double yInicial, int orientacion) {
        super(context, xInicial, yInicial);

        if (orientacion == Enemigo.IZQUIERDA)
            velocidadX = velocidadX*-1;

        cDerecha = 6;
        cIzquierda = 6;
        cArriba = 6;
        cAbajo = 6;

        inicializar();
    }


}
