package com.plataformas.modelos.controles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.plataformas.GameView;
import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.modelos.Modelo;

/**
 * Created by uo227602 on 05/10/2016.
 */
public class Contador extends Modelo {

    int puntuacion=0;

    public Contador(Context context) {
        super(context, GameView.pantallaAncho*0.90 , GameView.pantallaAlto*0.1,
                GameView.pantallaAlto, GameView.pantallaAncho);

        altura = 40;
        ancho = 40;
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.score);
    }

    public void actualizarPuntuacion(int puntuacion){
        this.puntuacion+=puntuacion;
    }

    @Override
    public void dibujar(Canvas canvas){
        int yArriva = (int)  y - altura / 2;
        int xIzquierda = (int) x - ancho / 2;
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(20);
        canvas.drawText(String.valueOf(puntuacion), xIzquierda - ancho, yArriva + altura/2, paint);
        imagen.setBounds(xIzquierda, yArriva, xIzquierda
                + ancho, yArriva + altura);
        imagen.draw(canvas);
    }




}
