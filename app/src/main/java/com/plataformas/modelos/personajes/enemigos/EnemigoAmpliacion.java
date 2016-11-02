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

public class EnemigoAmpliacion extends Enemigo {


    private Sprite sprite;
    private HashMap<String,Sprite> sprites = new HashMap<String,Sprite>();

    public double velocidadX = 1.2;


    public int estado;

    public EnemigoAmpliacion(Context context, double xInicial, double yInicial) {
        super(context, xInicial,yInicial);

        this.x = xInicial;
        this.y = yInicial - altura/2;

        cDerecha = 15;
        cIzquierda = 15;
        cArriba = 20;
        cAbajo = 20;
        enemyDieLeft = ;
        enemyDieRight = ;
        enemyLeft = ;
        enemyRight = ;

        estado = Estado.ACTIVO;

        inicializar();
    }

    @Override
    public void inicializar (){

        Sprite caminandoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemyrunrightampliacion),
                ancho, altura,
                4, 8, true);
        sprites.put(CAMINANDO_DERECHA, caminandoDerecha);

        Sprite caminandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemyrunleftampliacion),
                ancho, altura,
                4, 8, true);
        sprites.put(CAMINANDO_IZQUIERDA, caminandoIzquierda);


        Sprite muerteDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemydierightampliacion),
                ancho, altura,
                4, 12, false);
        sprites.put(MUERTE_DERECHA, muerteDerecha);

        Sprite muerteIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemydieleftampliacion),
                ancho, altura,
                4, 12, false);
        sprites.put(MUERTE_IZQUIERDA, muerteIzquierda);



        sprite = caminandoDerecha;
    }

    public void girar(){
        velocidadX = velocidadX*-1;
    }

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX, (int) y - Nivel.scrollEjeY);
    }
    public void destruir (){
        velocidadX = 0;
        estado = Estado.INACTIVO;
    }

}


