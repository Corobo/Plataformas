package com.plataformas;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Path;
import android.media.AudioManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.plataformas.gestores.Opciones;

public class OpcionesActivity extends Activity implements View.OnClickListener {

    ImageButton dificultad;
    ImageButton musica;
    ImageButton efectos;
    ImageButton empezar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establecer el control de sonido multimedia como predefinido
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // Pantalla completa, sin titulo
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_opciones);

        dificultad = (ImageButton) findViewById(R.id.dificultad);
        musica = (ImageButton) findViewById(R.id.musica);
        efectos = (ImageButton) findViewById(R.id.efectos);
        empezar = (ImageButton) findViewById(R.id.empezar);
        dificultad.setOnClickListener(this);
        musica.setOnClickListener(this);
        efectos.setOnClickListener(this);
        empezar.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if(v==empezar){
            Intent actividadJuego = new Intent(OpcionesActivity.this,
                    MainActivity.class);
            startActivity(actividadJuego);
            finish();
        }
        if(v==dificultad) {
            Opciones.dificultad =  Opciones.dificultad? false:true;
            if( Opciones.dificultad)
                dificultad.setBackgroundResource(R.drawable.dificultad_on);
            else
                dificultad.setBackgroundResource(R.drawable.dificultad_off);
        }
        if(v==musica){
            Opciones.musica = Opciones.musica? false:true;
            if(Opciones.musica)
                musica.setBackgroundResource(R.drawable.boton_music_on);
            else
                musica.setBackgroundResource(R.drawable.boton_music_off);
        }
        if(v==efectos){
            Opciones.efectos = Opciones.efectos ?false:true;
            if(Opciones.efectos)
                efectos.setBackgroundResource(R.drawable.boton_effects_on);
            else
                efectos.setBackgroundResource(R.drawable.boton_effects_off);
        }
    }
}
