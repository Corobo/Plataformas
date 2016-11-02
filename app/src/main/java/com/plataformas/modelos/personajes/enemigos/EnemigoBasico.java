package com.plataformas.modelos.personajes.enemigos;

import android.content.Context;
import android.graphics.Canvas;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.global.Estado;
import com.plataformas.graficos.Sprite;
import com.plataformas.modelos.Modelo;
import com.plataformas.modelos.escenarios.Nivel;

import java.util.HashMap;

/**
 * Created by uo227602 on 19/10/2016.
 */

public class EnemigoBasico extends Enemigo {

    public EnemigoBasico(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial);
    }

}


