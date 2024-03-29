package com.plataformas.modelos.controles;

import android.content.Context;

import com.plataformas.GameView;
import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.modelos.Modelo;

/**
 * Created by uo227602 on 05/10/2016.
 */
public class Pad extends Modelo {

    public Pad(Context context) {
        super(context, GameView.pantallaAncho*0.15 , GameView.pantallaAlto*0.8 ,
                GameView.pantallaAlto, GameView.pantallaAncho);

        altura = 100;
        ancho = 100;
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.pad);
    }

    public boolean estaPulsado(float clickX, float clickY) {
        boolean estaPulsado = false;

        if (clickX <= (x + ancho / 2) && clickX >= (x - ancho / 2)
                && clickY <= (y + altura / 2) && clickY >= (y - altura / 2)
                ) {
            estaPulsado = true;
        }
        return estaPulsado;
    }

    public int getOrientacionX(float cliclX) {
        int xPulsado = (int) (x - cliclX);
        return xPulsado>15 || xPulsado<-15 ? xPulsado : 0;
    }
    public int getOrientacionY(float clicY){
        int yPulsado = (int) (y - clicY);
        return yPulsado>15 || yPulsado<-15 ? yPulsado : 0;
    }

}

