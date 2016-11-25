package com.plataformas.modelos.controles;

import android.content.Context;

import com.plataformas.GameView;
import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.modelos.Modelo;

/**
 * Created by uo227602 on 19/10/2016.
 */
public class BotonDisparar extends Modelo {

    public BotonDisparar(Context context) {
        super(context, GameView.pantallaAncho*0.85 , GameView.pantallaAlto*0.6,
                100,100);

        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.pad);
    }

    public boolean estaPulsado(float clickX, float clickY) {
        boolean estaPulsado = false;

        if (clickX <= (x + ancho / 2) && clickX >= (x - ancho / 2)
                && clickY <= (y + altura / 2) && clickY >= (y - altura / 2)) {
            estaPulsado = true;
        }
        return estaPulsado;
    }

    public int getOrientacionX(float cliclX) {
        return (int) (x - cliclX);

    }
    public int getOrientacionY(float clicY){
        return (int) (y - clicY);
    }

    public int calcularAngulo(float orientacionX,float orientacionY){
        if(orientacionX<15 && orientacionX>-15){
            if(orientacionY>25){
                return 90;
            }
            if(orientacionY<-20){
                return -90;
            }
        }
        else if(orientacionX>15 || orientacionX<-15){
            if(orientacionY>15){
                return 45;
            }
            if(orientacionY>5 && orientacionY<15){
                return 0;
            }
            if(orientacionY<-15){
                return -45;
            }
        }
        return 0;
    }
}


