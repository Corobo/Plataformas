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

import static android.view.WindowManager.*;

public class MenuActivity extends Activity implements View.OnClickListener {

    ImageButton nuevoJuego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establecer el control de sonido multimedia como predefinido
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // Pantalla completa, sin titulo
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN,
                LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu);

        nuevoJuego = (ImageButton) findViewById(R.id.imageButton);
        nuevoJuego.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if(v==nuevoJuego){
            Intent actividadJuego = new Intent(MenuActivity.this,
                    SeleccionModoActivity.class);
            startActivity(actividadJuego);
            finish();
        }
    }
}
