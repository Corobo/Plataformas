package com.plataformas.modelos.recolectables;

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
 * Created by uo227602 on 05/10/2016.
 */
public class Recolectable extends Modelo {

    public static final String GEMA_GIRANDO = "Gema_Girando";
    public static final String GEMA_DESAPARECIENDO = "Gema_desapareciendo";

    //Puntero sprite actual
    private Sprite sprite;

    private HashMap<String,Sprite> sprites = new HashMap<String,Sprite> ();


    int estado;


    public Recolectable(Context context, double xInicial, double yInicial) {
        super(context, xInicial,yInicial, 40, 40);

        inicializar();
    }

    private void inicializar() {

        Sprite gemaGirando = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.gem),
                ancho, altura,
                4, 8, true);
        sprites.put(GEMA_GIRANDO, gemaGirando);

        Sprite gemaDesapareciendo = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.item_on_collected),
                ancho, altura,
                5, 10, false);
        sprites.put(GEMA_DESAPARECIENDO, gemaDesapareciendo);
        estado = Estado.ACTIVO;
        sprite = gemaGirando;
    }

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int)x - Nivel.scrollEjeX , (int)y - Nivel.scrollEjeY );
    }

    public void actualizar(long tiempo){
        boolean finSprite = sprite.actualizar(tiempo);

        if (estado == Estado.INACTIVO) {
            sprite = sprites.get(GEMA_DESAPARECIENDO);
        }
        else{
            sprite = sprites.get(GEMA_GIRANDO);
        }
        if ( estado == Estado.INACTIVO && finSprite){
            estado = Estado.ELIMINAR;
        }
    }
    public void destruir(){
        estado = Estado.INACTIVO;
    }
    public int getEstado() {
        return estado;
    }
}
