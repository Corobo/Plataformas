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

public class Enemigo extends Modelo {
    public static final String CAMINANDO_DERECHA = "Caminando_derecha";
    public static final String CAMINANDO_IZQUIERDA = "caminando_izquierda";

    public static final String MUERTE_DERECHA = "muerte_derecha";
    public static final String MUERTE_IZQUIERDA = "muerte_izquierda";


    protected Sprite sprite;
    protected HashMap<String,Sprite> sprites = new HashMap<String,Sprite>();

    public double velocidadX = 1.2;
    protected int orientacion;
    public int estado;

    public static final int DERECHA = 1;
    public static final int IZQUIERDA = -1;


    public Enemigo(Context context, double xInicial, double yInicial) {
        super(context, 0, 0, 40, 40);

        this.x = xInicial;
        this.y = yInicial - altura/2;

    }

    public void inicializar (){

        Sprite caminandoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemyrunright),
                ancho, altura,
                4, 4, true);
        sprites.put(CAMINANDO_DERECHA, caminandoDerecha);

        Sprite caminandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemyrun),
                ancho, altura,
                4, 4, true);
        sprites.put(CAMINANDO_IZQUIERDA, caminandoIzquierda);


        Sprite muerteDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemydieright),
                ancho, altura,
                4, 8, false);
        sprites.put(MUERTE_DERECHA, muerteDerecha);

        Sprite muerteIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemydie),
                ancho, altura,
                4, 8, false);
        sprites.put(MUERTE_IZQUIERDA, muerteIzquierda);



        sprite = caminandoDerecha;
    }


    public void actualizar (long tiempo) {
        boolean finSprite = sprite.actualizar(tiempo);

        if ( estado == Estado.INACTIVO && finSprite){
            estado = Estado.ELIMINAR;
        }
        if (estado == Estado.INACTIVO){
            if (velocidadX > 0) {
                sprite = sprites.get(MUERTE_DERECHA);
                orientacion = DERECHA;
            }
            else{
                sprite = sprites.get(MUERTE_IZQUIERDA);
                orientacion = IZQUIERDA;
            }
        } else {

            if (velocidadX > 0) {
                sprite = sprites.get(CAMINANDO_DERECHA);
                orientacion = DERECHA;
            }
            if (velocidadX < 0) {
                sprite = sprites.get(CAMINANDO_IZQUIERDA);
                orientacion = IZQUIERDA;
            }

        }
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


