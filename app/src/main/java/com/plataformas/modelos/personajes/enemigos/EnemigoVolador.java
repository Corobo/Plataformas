package com.plataformas.modelos.personajes.enemigos;

import android.content.Context;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.global.Estado;
import com.plataformas.graficos.Sprite;

/**
 * Created by uo227602 on 05/11/2016.
 */

public class EnemigoVolador extends Enemigo {

    public double velocidadY = 1.0;
    public double yInicial;
    public double yDestino;
    public boolean direccion;

    public EnemigoVolador(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial);

        this.yInicial = y;
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
                CargadorGraficos.cargarDrawable(context, R.drawable.enemyrightvolador),
                ancho, altura,
                3, 6, true);
        sprites.put(CAMINANDO_DERECHA, caminandoDerecha);

        Sprite caminandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemyvolador),
                ancho, altura,
                3, 6, true);
        sprites.put(CAMINANDO_IZQUIERDA, caminandoIzquierda);


        Sprite muerteDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemydievolador),
                ancho, altura,
                3, 6, false);
        sprites.put(MUERTE_DERECHA, muerteDerecha);

        Sprite muerteIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemydievolador),
                ancho, altura,
                3, 6, false);
        sprites.put(MUERTE_IZQUIERDA, muerteIzquierda);



        sprite = caminandoDerecha;
    }
    public void volar() {
        if (direccion && y >= yDestino) {
            y -= velocidadY;
            if (y <= yDestino) {
                direccion = false;
                yDestino = y + calcularMovimiento();
                if (yDestino > yInicial + 33)
                    yDestino = yInicial + 33;
            }
        }
        if (!direccion && y <= yDestino) {
            y += velocidadY;
            if (y >= yDestino) {
                direccion = true;
                yDestino = y - calcularMovimiento();
                if (yDestino < yInicial - 33)
                    yDestino = yInicial - 33;
            }
        }
        if (yDestino == 0 && y == yInicial) {
            int movimiento = calcularMovimiento();
            int posicion = (int) Math.floor(Math.random() * (2 - 0 + 1) + (0));
            if (posicion == 0) {
                yDestino = y - movimiento;
                direccion = true;
            } else {
                yDestino = movimiento + y;
                direccion = false;
            }
        }
    }

    private int calcularMovimiento(){
        return (int)Math.floor(Math.random()*(25-1+1)+(1));
    }
}
