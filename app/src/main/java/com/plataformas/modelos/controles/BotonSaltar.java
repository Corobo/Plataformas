package com.plataformas.modelos.controles;

import android.content.Context;

import com.plataformas.GameView;
import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.modelos.Modelo;

/**
 * Created by uo227602 on 05/10/2016.
 */
public class BotonSaltar extends Modelo {

    public BotonSaltar(Context context) {
        super(context, GameView.pantallaAncho*0.75, GameView.pantallaAlto*0.8,
                70,70);


        altura = 70;
        ancho = 70;
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.buttonjump);
    }

    public boolean estaPulsado(float clickX, float clickY) {
        boolean estaPulsado = false;

        if (clickX <= (x + ancho / 2) && clickX >= (x - ancho / 2)
                && clickY <= (y + altura / 2) && clickY >= (y - altura / 2)) {
            estaPulsado = true;
        }
        return estaPulsado;
    }

}
