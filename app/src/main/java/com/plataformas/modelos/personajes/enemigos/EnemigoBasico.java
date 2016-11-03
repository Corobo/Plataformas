package com.plataformas.modelos.personajes.enemigos;

import android.content.Context;

import com.plataformas.global.Estado;

/**
 * Created by uo227602 on 03/11/2016.
 */

public class EnemigoBasico extends Enemigo {
    public EnemigoBasico(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial);
        cDerecha = 15;
        cIzquierda = 15;
        cArriba = 20;
        cAbajo = 20;

        estado = Estado.ACTIVO;

        inicializar();
    }
}
