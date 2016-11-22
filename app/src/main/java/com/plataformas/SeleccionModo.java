package com.plataformas;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

public class SeleccionModo extends Activity implements View.OnClickListener {

    ImageButton unJugador;
    ImageButton dosJugadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establecer el control de sonido multimedia como predefinido
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // Pantalla completa, sin titulo
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_seleccion_modo);

        unJugador = (ImageButton) findViewById(R.id.unJugadorButton);
        dosJugadores = (ImageButton) findViewById(R.id.dosJugadoresButton);
        unJugador.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if(v==unJugador){
            Intent actividadJuego = new Intent(SeleccionModo.this,
                    MainActivity.class);
            actividadJuego.putExtra("Modo","Un jugador");
            startActivity(actividadJuego);
            finish();
        }
        if(v==dosJugadores){
            Intent actividadJuego = new Intent(SeleccionModo.this,
                    MainActivity.class);
            actividadJuego.putExtra("Modo","Dos jugadores");
            startActivity(actividadJuego);
            finish();
        }
    }
}
