package com.plataformas.modelos.escenarios;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.plataformas.GameView;
import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.gestores.Utilidades;
import com.plataformas.global.Estado;
import com.plataformas.modelos.controles.IconoVida;
import com.plataformas.modelos.personajes.efectos.Disparo;
import com.plataformas.modelos.personajes.efectos.DisparoEnemigo;
import com.plataformas.modelos.personajes.efectos.DisparoJugador;
import com.plataformas.modelos.personajes.enemigos.Enemigo;
import com.plataformas.modelos.personajes.enemigos.EnemigoAmpliacion;
import com.plataformas.modelos.personajes.enemigos.EnemigoBasico;
import com.plataformas.modelos.personajes.jugadores.Jugador;
import com.plataformas.modelos.recolectables.Meta;
import com.plataformas.modelos.recolectables.Recolectable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Nivel {
    private Context context = null;
    private int numeroNivel;
    private Fondo[] fondos;
    private Jugador jugador;
    private IconoVida[] iconosVida;
    private Meta meta;

    public boolean inicializado;
    private Tile[][] mapaTiles;

    public float orientacionPad = 0;
    public boolean botonSaltarPulsado = false;

    public static int scrollEjeX = 0;
    public static int scrollEjeY = 0;
    private float velocidadGravedad = 0.8f;
    private float velocidadMaximaCaida = 10;

    private List<Enemigo> enemigos;

    private List<Disparo> disparosJugador;
    private List<Disparo> disparosEnemigos;

    private List<Recolectable> recolectables = new ArrayList<>();

    public boolean botonDispararPulsado = false;

    public GameView gameView;

    public Bitmap mensaje ;
    public boolean nivelPausado;

    public Nivel(Context context, int numeroNivel) throws Exception {
        inicializado = false;

        this.context = context;
        this.numeroNivel = numeroNivel;
        inicializar();

        inicializado = true;
    }

    public void inicializar() throws Exception {
        scrollEjeX = 0;
        scrollEjeY = 0;
        mensaje = CargadorGraficos.cargarBitmap(context, R.drawable.description);
        nivelPausado = true;
        GameView.contador.reiniciarPuntuacion();
        enemigos = new LinkedList<Enemigo>();
        disparosJugador = new LinkedList<Disparo>();
        disparosEnemigos = new LinkedList<Disparo>();
        fondos = new Fondo[2];
        fondos[0] = new Fondo(context,CargadorGraficos.cargarBitmap(context,
                R.drawable.capa1), 0);
        fondos[1] = new Fondo(context,CargadorGraficos.cargarBitmap(context,
                R.drawable.capa2), 1f);
        iconosVida = new IconoVida[3];

        iconosVida[0] = new IconoVida(context, GameView.pantallaAncho*0.05,
                GameView.pantallaAlto*0.1);
        iconosVida[1] = new IconoVida(context, GameView.pantallaAncho*0.15,
                GameView.pantallaAlto*0.1);
        iconosVida[2] = new IconoVida(context, GameView.pantallaAncho*0.25,
                GameView.pantallaAlto*0.1);
        inicializarMapaTiles();
        scrollEjeY = (int) (altoMapaTiles() - tilesEnDistanciaY(GameView.pantallaAlto))*Tile.altura;
    }


    public void actualizar(long tiempo) throws Exception {
        if (inicializado) {
            for (Enemigo enemigo : enemigos) {
                enemigo.actualizar(tiempo);
            }
            for (Recolectable n : recolectables) {
                n.actualizar(tiempo);
            }
            for(Disparo disparoJugador: disparosJugador){
                disparoJugador.actualizar(tiempo);
            }
            jugador.procesarOrdenes(orientacionPad, botonSaltarPulsado,botonDispararPulsado);
            if (botonSaltarPulsado)
                botonSaltarPulsado = false;
            if (botonDispararPulsado) {
                disparosJugador.add(new DisparoJugador(context,jugador.x,jugador.y,jugador.orientacion));
                botonDispararPulsado = false;
            }

            jugador.actualizar(tiempo);
            aplicarReglasMovimiento();
        }
    }


    public void dibujar(Canvas canvas) {
        if (inicializado) {
            fondos[0].dibujar(canvas);
            fondos[1].dibujar(canvas);
            dibujarTiles(canvas);
            for (Recolectable n : recolectables) {
                n.dibujar(canvas);
            }
            for(Disparo disparoJugador: disparosJugador){
                disparoJugador.dibujar(canvas);
            }
            for(Disparo disparoEnemigo: disparosEnemigos){
                disparoEnemigo.dibujar(canvas);
            }
            jugador.dibujar(canvas);
            for (Enemigo enemigo : enemigos) {
                enemigo.dibujar(canvas);
            }
            meta.dibujar(canvas);
            for(int i=0; i < jugador.vidas; i++)
                iconosVida[i].dibujar(canvas);
            if (nivelPausado){
                // la foto mide 480x320
                Rect orgigen = new Rect(0,0 ,
                        480,320);

                Paint efectoTransparente = new Paint();
                efectoTransparente.setAntiAlias(true);

                Rect destino = new Rect((int)(GameView.pantallaAncho/2 - 480/2),
                        (int)(GameView.pantallaAlto/2 - 320/2),
                        (int)(GameView.pantallaAncho/2 + 480/2),
                        (int)(GameView.pantallaAlto/2 + 320/2));
                canvas.drawBitmap(mensaje,orgigen,destino, null);
            }
        }
    }

    public int anchoMapaTiles() {
        return mapaTiles.length;
    }

    public int altoMapaTiles() {

        return mapaTiles[0].length;
    }

    private void inicializarMapaTiles() throws Exception {
        InputStream is = context.getAssets().open(numeroNivel + ".txt");
        int anchoLinea;

        List<String> lineas = new LinkedList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        {
            String linea = reader.readLine();
            anchoLinea = linea.length();
            while (linea != null) {
                lineas.add(linea);
                if (linea.length() != anchoLinea) {
                    Log.e("ERROR", "Dimensiones incorrectas en la línea");
                    throw new Exception("Dimensiones incorrectas en la línea.");
                }
                linea = reader.readLine();
            }
        }

        // Inicializar la matriz
        mapaTiles = new Tile[anchoLinea][lineas.size()];
        // Iterar y completar todas las posiciones
        for (int y = 0; y < altoMapaTiles(); ++y) {
            for (int x = 0; x < anchoMapaTiles(); ++x) {
                char tipoDeTile = lineas.get(y).charAt(x);//lines[y][x];
                mapaTiles[x][y] = inicializarTile(tipoDeTile, x, y);
            }
        }
    }

    private Tile inicializarTile(char codigoTile, int x, int y) {
        switch (codigoTile) {
            case 'M':
                int xCentroAbajoTileM = x * Tile.ancho + Tile.ancho/2;
                int yCentroAbajoTileM = y * Tile.altura + Tile.altura;
                meta = new Meta(context,xCentroAbajoTileM,yCentroAbajoTileM);

                return new Tile(null, Tile.PASABLE);
            case 'N':
                // Enemigo
                // Posición centro abajo
                int xCentroAbajoTileN = x * Tile.ancho + Tile.ancho / 2;
                int yCentroAbajoTileN = y * Tile.altura + Tile.altura;
                enemigos.add(new EnemigoAmpliacion(context, xCentroAbajoTileN, yCentroAbajoTileN));

                return new Tile(null, Tile.PASABLE);
            case 'E':
                // Enemigo
                // Posición centro abajo
                int xCentroAbajoTileE = x * Tile.ancho + Tile.ancho / 2;
                int yCentroAbajoTileE = y * Tile.altura + Tile.altura;
                enemigos.add(new EnemigoBasico(context, xCentroAbajoTileE, yCentroAbajoTileE));

                return new Tile(null, Tile.PASABLE);
            case '1':
                // Jugador
                // Posicion centro abajo
                int xCentroAbajoTile = x * Tile.ancho + Tile.ancho / 2;
                int yCentroAbajoTile = y * Tile.altura + Tile.altura;
                jugador = new Jugador(context, xCentroAbajoTile, yCentroAbajoTile);

                return new Tile(null, Tile.PASABLE);
            case '.':
                // en blanco, sin textura
                return new Tile(null, Tile.PASABLE);
            case '#':
                // bloque de musgo, no se puede pasar
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.musgo), Tile.SOLIDO);
            case 'a':
                int xCentroAbajoTileR = x * Tile.ancho + Tile.ancho/2;
                int yCentroAbajoTileR = y * Tile.altura + Tile.altura;
                recolectables.add(new Recolectable(context, xCentroAbajoTileR, yCentroAbajoTileR));

                return new Tile(null, Tile.PASABLE);
            default:
                //cualquier otro caso
                return new Tile(null, Tile.PASABLE);
        }
    }

    private void dibujarTiles(Canvas canvas) {

        int tileXJugador = (int) jugador.x / Tile.ancho;
        int izquierda = (int) (tileXJugador - tilesEnDistanciaX(jugador.x - scrollEjeX));
        izquierda = Math.max(0,izquierda); // Que nunca sea < 0, ej -1
        if ( jugador.x  <
                (anchoMapaTiles() - tilesEnDistanciaX( GameView.pantallaAncho*0.3)) * Tile.ancho )
            if( jugador .x - scrollEjeX > GameView.pantallaAncho * 0.7 ){
                fondos[0].moverX((int) ((jugador .x - scrollEjeX) - GameView.pantallaAncho* 0.7));
                fondos[1].moverX((int) ((jugador .x - scrollEjeX) - GameView.pantallaAncho* 0.7));
                scrollEjeX += (int) ((jugador .x - scrollEjeX) - GameView.pantallaAncho* 0.7);
                Log.v("Fondo.mover","Fondo.mover: Scroll aumentado");

            }

        if ( jugador.x  >
                tilesEnDistanciaX(GameView.pantallaAncho*0.3) * Tile.ancho )
            if( jugador .x - scrollEjeX < GameView.pantallaAncho *0.3 ){
                fondos[0].moverX(-(int) (GameView.pantallaAncho*0.3 -(jugador .x - scrollEjeX)));
                fondos[1].moverX(-(int) (GameView.pantallaAncho*0.3 -(jugador .x - scrollEjeX)));
                scrollEjeX -= (int) (GameView.pantallaAncho*0.3 -(jugador .x - scrollEjeX));
                Log.v("Fondo.mover","Fondo.mover: Scroll reducido");
            }

        int derecha = izquierda +
                GameView.pantallaAncho / Tile.ancho + 1;

        derecha = Math.min(derecha, anchoMapaTiles() - 1);

        int tileYJugador = (int) jugador.y / Tile.altura;

        int arriba = (int) (tileYJugador - tilesEnDistanciaY(jugador.y - scrollEjeY));
        arriba = Math.max(0,arriba);

        if ( jugador .y  <
                altoMapaTiles()* Tile.altura - GameView.pantallaAlto*0.3 )
            if( jugador .y- scrollEjeY > GameView.pantallaAlto * 0.7 )
                scrollEjeY += (int) ((jugador .y - scrollEjeY) - GameView.pantallaAlto* 0.7);


        if ( jugador .y  > GameView.pantallaAlto*0.3 )
            if( jugador .y - scrollEjeY < GameView.pantallaAlto *0.3 )
                scrollEjeY -= (int) (GameView.pantallaAlto*0.3 -(jugador .y - scrollEjeY));


        int abajo = arriba +
                GameView.pantallaAlto / Tile.altura + 1;

        abajo = Math.min(abajo, altoMapaTiles() - 1);


        for (int y = arriba; y <= abajo; ++y) {
            for (int x = izquierda; x <= derecha; ++x) {
                if (mapaTiles[x][y].imagen != null) {
                    // Calcular la posición en pantalla correspondiente
                    // izquierda, arriba, derecha , abajo

                    mapaTiles[x][y].imagen.setBounds(
                            (x * Tile.ancho) - scrollEjeX,
                            (y * Tile.altura) - scrollEjeY,
                            (x * Tile.ancho) + Tile.ancho - scrollEjeX,
                            (y * Tile.altura + Tile.altura) - scrollEjeY);

                    mapaTiles[x][y].imagen.draw(canvas);
                }
            }
        }
    }


    private void aplicarReglasMovimiento() throws Exception {

        int tileXJugadorIzquierda
                = (int) (jugador.x - (jugador.ancho / 2 - 1)) / Tile.ancho;
        int tileXJugadorDerecha
                = (int) (jugador.x + (jugador.ancho / 2 - 1)) / Tile.ancho;

        int tileYJugadorInferior
                = (int) (jugador.y + (jugador.altura / 2 - 1)) / Tile.altura;
        int tileYJugadorCentro
                = (int) jugador.y / Tile.altura;
        int tileYJugadorSuperior
                = (int) (jugador.y - (jugador.altura / 2 - 1)) / Tile.altura;


        for (Iterator<Disparo> iterator = disparosJugador.iterator(); iterator.hasNext();) {

            DisparoJugador disparoJugador = (DisparoJugador) iterator.next();

            int tileXDisparo = (int)disparoJugador.x / Tile.ancho ;
            int tileYDisparoInferior =
                    (int) (disparoJugador.y  + disparoJugador.cAbajo) / Tile.altura;

            int tileYDisparoSuperior =
                    (int) (disparoJugador.y  - disparoJugador.cArriba)  / Tile.altura;

            if(disparoJugador.velocidadX > 0){
                // Tiene delante un tile pasable, puede avanzar.
                if (tileXDisparo+1 <= anchoMapaTiles()-1 &&
                        mapaTiles[tileXDisparo+1][tileYDisparoInferior].tipoDeColision
                                == Tile.PASABLE &&
                        mapaTiles[tileXDisparo+1][tileYDisparoSuperior].tipoDeColision
                                == Tile.PASABLE ){

                    disparoJugador.x +=  disparoJugador.velocidadX;

                } else if (tileXDisparo <= anchoMapaTiles() - 1){

                    int TileDisparoBordeDerecho = tileXDisparo*Tile.ancho + Tile.ancho ;
                    double distanciaX =
                            TileDisparoBordeDerecho - (disparoJugador.x +  disparoJugador.cDerecha);

                    if( distanciaX  > 0) {
                        double velocidadNecesaria =
                                Math.min(distanciaX, disparoJugador.velocidadX);
                        disparoJugador.x += velocidadNecesaria;
                    } else {
                        iterator.remove();
                        continue;
                    }
                }

            }
            // izquierda
            if (disparoJugador.velocidadX <= 0){
                if (tileXDisparo-1 >= 0 &&
                        tileYDisparoSuperior < altoMapaTiles()-1 &&
                        mapaTiles[tileXDisparo-1][tileYDisparoSuperior].tipoDeColision ==
                                Tile.PASABLE &&
                        mapaTiles[tileXDisparo-1][tileYDisparoInferior].tipoDeColision ==
                                Tile.PASABLE){

                    disparoJugador.x +=  disparoJugador.velocidadX;

                    // No tengo un tile PASABLE detras
                    // o es el INICIO del nivel o es uno SOLIDO
                } else if(tileXDisparo >= 0 ){
                    // Si en el propio tile del jugador queda espacio para
                    // avanzar más, avanzo
                    int TileDisparoBordeIzquierdo = tileXDisparo*Tile.ancho ;
                    double distanciaX =
                            (disparoJugador.x - disparoJugador.cIzquierda) - TileDisparoBordeIzquierdo ;

                    if( distanciaX  > 0) {
                        double velocidadNecesaria =
                                Utilidades.proximoACero(-distanciaX, disparoJugador.velocidadX);
                        disparoJugador.x += velocidadNecesaria;
                    } else {
                        iterator.remove();
                        continue;
                    }
                }
            }
            for(Enemigo enemigo : enemigos){
                if (disparoJugador.colisiona(enemigo)){
                    enemigo.destruir();
                    iterator.remove();
                    break;
                }
            }
        }
        for(Iterator<Recolectable> iterator = recolectables.iterator();iterator.hasNext();){
            Recolectable recolectable = iterator.next();
            if(jugador.colisiona(recolectable) && recolectable.getEstado()==Estado.ACTIVO){
                recolectable.destruir();
                gameView.contador.actualizarPuntuacion(1);
                break;
            }
            if (recolectable.getEstado() == Estado.ELIMINAR){
                iterator.remove();
                continue;
            }
        }
        for (Iterator<Enemigo> iterator = enemigos.iterator(); iterator.hasNext();) {
            Enemigo enemigo = iterator.next();

            if (enemigo.estado == Estado.ELIMINAR){
                iterator.remove();
                continue;
            }

            if(enemigo.estado != Estado.ACTIVO)
                continue;

            int tileXEnemigoIzquierda =
                    (int) (enemigo.x - (enemigo.ancho / 2 - 1)) / Tile.ancho;
            int tileXEnemigoDerecha =
                    (int) (enemigo.x + (enemigo.ancho / 2 - 1)) / Tile.ancho;

            int tileYEnemigoInferior =
                    (int) (enemigo.y + (enemigo.altura / 2 - 1)) / Tile.altura;
            int tileYEnemigoCentro =
                    (int) enemigo.y / Tile.altura;
            int tileYEnemigoSuperior =
                    (int) (enemigo.y - (enemigo.altura / 2 - 1)) / Tile.altura;

            int rango = 4;
            if (tileXJugadorIzquierda - rango < tileXEnemigoIzquierda &&
                    tileXJugadorIzquierda + rango > tileXEnemigoIzquierda){

                if(jugador.colisiona(enemigo)){
                    if(jugador.golpeado() <= 0){
                        nivelPausado = true;
                        mensaje = CargadorGraficos.cargarBitmap(context, R.drawable.you_lose);
                        inicializar();
                        jugador.restablecerPosicionInicial();
                        return;
                    }
                }
                long tiempo = System.currentTimeMillis();
                if (enemigo.estado == Estado.ACTIVO){
                        if(enemigo instanceof EnemigoAmpliacion) {
                            DisparoEnemigo disparo = (DisparoEnemigo) ((EnemigoAmpliacion)enemigo).disparar(tiempo);
                            if (disparo != null) {
                                disparosEnemigos.add(disparo);
                            }
                        }
                    }
            }


            if (enemigo.velocidadX > 0) {
                //  Solo una condicion para pasar:  Tile delante libre, el de abajo solido
                if (tileXEnemigoDerecha + 1 <= anchoMapaTiles() - 1 &&
                        mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoInferior].tipoDeColision ==
                                Tile.PASABLE &&
                        mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoCentro].tipoDeColision ==
                                Tile.PASABLE &&
                        mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoSuperior].tipoDeColision ==
                                Tile.PASABLE &&
                        mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoInferior + 1].tipoDeColision ==
                                Tile.SOLIDO) {

                    enemigo.x += enemigo.velocidadX;

                    // Sino, me acerco al borde del que estoy
                } else if (tileXEnemigoDerecha + 1 <= anchoMapaTiles() - 1) {

                    int TileEnemigoDerecho = tileXEnemigoDerecha * Tile.ancho + Tile.ancho;
                    double distanciaX = TileEnemigoDerecho - (enemigo.x + enemigo.ancho / 2);

                    if (distanciaX > 0) {
                        double velocidadNecesaria = Math.min(distanciaX, enemigo.velocidadX);
                        enemigo.x += velocidadNecesaria;
                    } else {
                        enemigo.girar();
                    }

                    // No hay Tile, o es el final del mapa
                } else {
                    enemigo.girar();
                }
            }

            if (enemigo.velocidadX < 0) {
                // Solo una condición para pasar: Tile izquierda pasable y suelo solido.
                if (tileXEnemigoIzquierda - 1 >= 0 &&
                        mapaTiles[tileXEnemigoIzquierda - 1][tileYEnemigoInferior].tipoDeColision ==
                                Tile.PASABLE &&
                        mapaTiles[tileXEnemigoIzquierda - 1][tileYEnemigoCentro].tipoDeColision ==
                                Tile.PASABLE &&
                        mapaTiles[tileXEnemigoIzquierda - 1][tileYEnemigoSuperior].tipoDeColision ==
                                Tile.PASABLE &&
                        mapaTiles[tileXEnemigoIzquierda - 1][tileYEnemigoInferior + 1].tipoDeColision
                                == Tile.SOLIDO) {

                    enemigo.x += enemigo.velocidadX;

                    // Solido / borde del tile acercarse.
                } else if (tileXEnemigoIzquierda - 1 >= 0) {

                    int TileEnemigoIzquierdo = tileXEnemigoIzquierda * Tile.ancho;
                    double distanciaX = (enemigo.x - enemigo.ancho / 2) - TileEnemigoIzquierdo;

                    if (distanciaX > 0) {
                        double velocidadNecesaria =
                                Utilidades.proximoACero(-distanciaX, enemigo.velocidadX);
                        enemigo.x += velocidadNecesaria;
                    } else {
                        enemigo.girar();
                    }
                } else {
                    enemigo.girar();
                }
            }

        }

        for (Iterator<Disparo> iterator = disparosEnemigos.iterator(); iterator.hasNext();) {
            DisparoEnemigo disparoEnemigo = (DisparoEnemigo) iterator.next();

            int tileXDisparo = (int) disparoEnemigo.x / Tile.ancho;
            int tileYDisparoInferior =
                    (int) (disparoEnemigo.y + disparoEnemigo.cAbajo) / Tile.altura;

            int tileYDisparoSuperior =
                    (int) (disparoEnemigo.y - disparoEnemigo.cArriba) / Tile.altura;
            //derecha
            if (disparoEnemigo.velocidadX > 0) {
                // Tiene delante un tile pasable, puede avanzar.
                if (tileXDisparo + 1 <= anchoMapaTiles() - 1 &&
                        mapaTiles[tileXDisparo + 1][tileYDisparoInferior].tipoDeColision
                                == Tile.PASABLE &&
                        mapaTiles[tileXDisparo + 1][tileYDisparoSuperior].tipoDeColision
                                == Tile.PASABLE) {

                    disparoEnemigo.x += disparoEnemigo.velocidadX;

                } else if (tileXDisparo <= anchoMapaTiles() - 1) {

                    int TileDisparoBordeDerecho = tileXDisparo * Tile.ancho + Tile.ancho;
                    double distanciaX =
                            TileDisparoBordeDerecho - (disparoEnemigo.x + disparoEnemigo.cDerecha);

                    if (distanciaX > 0) {
                        double velocidadNecesaria =
                                Math.min(distanciaX, disparoEnemigo.velocidadX);
                        disparoEnemigo.x += velocidadNecesaria;
                    } else {
                        iterator.remove();
                        continue;
                    }
                }

            }
            // izquierda
            if (disparoEnemigo.velocidadX <= 0){
                if (tileXDisparo-1 >= 0 &&
                        tileYDisparoSuperior < altoMapaTiles()-1 &&
                        mapaTiles[tileXDisparo-1][tileYDisparoSuperior].tipoDeColision ==
                                Tile.PASABLE &&
                        mapaTiles[tileXDisparo-1][tileYDisparoInferior].tipoDeColision ==
                                Tile.PASABLE){

                    disparoEnemigo.x +=  disparoEnemigo.velocidadX;

                    // No tengo un tile PASABLE detras
                    // o es el INICIO del nivel o es uno SOLIDO
                } else if(tileXDisparo >= 0 ){
                    // Si en el propio tile del jugador queda espacio para
                    // avanzar más, avanzo
                    int TileDisparoBordeIzquierdo = tileXDisparo*Tile.ancho ;
                    double distanciaX =
                            (disparoEnemigo.x - disparoEnemigo.cIzquierda) - TileDisparoBordeIzquierdo ;

                    if( distanciaX  > 0) {
                        double velocidadNecesaria =
                                Utilidades.proximoACero(-distanciaX, disparoEnemigo.velocidadX);
                        disparoEnemigo.x += velocidadNecesaria;
                    } else {
                        iterator.remove();
                        continue;
                    }
                }
            }
            if (disparoEnemigo.colisiona(jugador)){
                if(jugador.golpeado() <= 0){
                    nivelPausado = true;
                    mensaje = CargadorGraficos.cargarBitmap(context, R.drawable.you_lose);
                    inicializar();
                    jugador.restablecerPosicionInicial();
                    return;
                }
             }
        }
        // Gravedad Jugador
        if (jugador.enElAire) {
            // Recordar los ejes:
            // - es para arriba       + es para abajo.
            jugador.setVelocidadY(jugador.getVelocidadY() + velocidadGravedad);
            if (jugador.getVelocidadY() > velocidadMaximaCaida) {
                jugador.setVelocidadY(velocidadMaximaCaida);
            }
        }

        // derecha o parado
        if (jugador.getVelocidadX() > 0) {
            // Tengo un tile delante y es PASABLE
            // El tile de delante está dentro del Nivel
            if (tileXJugadorDerecha + 1 <= anchoMapaTiles() - 1 &&
                    tileYJugadorInferior <= altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorDerecha + 1][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha + 1][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha + 1][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE) {
                jugador.x += jugador.getVelocidadX();

                // No tengo un tile PASABLE delante
                // o es el FINAL del nivel o es uno SOLIDO
            } else if (tileXJugadorDerecha <= anchoMapaTiles() - 1 &&
                    tileYJugadorInferior <= altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE) {

                // Si en el propio tile del jugador queda espacio para
                // avanzar más, avanzo
                int TileJugadorBordeDerecho = tileXJugadorDerecha * Tile.ancho + Tile.ancho;
                double distanciaX = TileJugadorBordeDerecho - (jugador.x + jugador.ancho / 2);

                if (distanciaX > 0) {
                    double velocidadNecesaria = Math.min(distanciaX, jugador.getVelocidadX());
                    jugador.x += velocidadNecesaria;
                } else {
                    // Opcional, corregir posición
                    jugador.x = TileJugadorBordeDerecho - jugador.ancho / 2;
                }
            }
        }

        // izquierda
        if (jugador.getVelocidadX() <= 0) {
            // Tengo un tile detrás y es PASABLE
            // El tile de delante está dentro del Nivel
            if (tileXJugadorIzquierda - 1 >= 0 &&
                    tileYJugadorInferior < altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorIzquierda - 1][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda - 1][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda - 1][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE) {
                jugador.x += jugador.getVelocidadX();

                // No tengo un tile PASABLE detrás
                // o es el INICIO del nivel o es uno SOLIDO
            } else if (tileXJugadorIzquierda >= 0 && tileYJugadorInferior <= altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior].tipoDeColision
                            == Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorCentro].tipoDeColision
                            == Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorSuperior].tipoDeColision
                            == Tile.PASABLE) {

                // Si en el propio tile del jugador queda espacio para
                // avanzar más, avanzo
                int TileJugadorBordeIzquierdo = tileXJugadorIzquierda * Tile.ancho;
                double distanciaX = (jugador.x - jugador.ancho / 2) - TileJugadorBordeIzquierdo;

                if (distanciaX > 0) {
                    double velocidadNecesaria = Utilidades.proximoACero(-distanciaX, jugador.getVelocidadX());
                    jugador.x += velocidadNecesaria;
                } else {
                    // Opcional, corregir posición
                    jugador.x = TileJugadorBordeIzquierdo + jugador.ancho / 2;
                }
            }
        }
        // Hacia arriba
        if (jugador.getVelocidadY() < 0) {
            // Tile superior PASABLE
            // Podemos seguir moviendo hacia arriba
            if (tileYJugadorSuperior - 1 >= 0 &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorSuperior - 1].tipoDeColision
                            == Tile.PASABLE
                    && mapaTiles[tileXJugadorDerecha][tileYJugadorSuperior - 1].tipoDeColision
                    == Tile.PASABLE) {
                jugador.y += jugador.getVelocidadY();

                // Tile superior != de PASABLE
                // O es un tile SOLIDO, o es el TECHO del mapa
            } else {

                // Si en el propio tile del jugador queda espacio para
                // subir más, subo
                int TileJugadorBordeSuperior = (tileYJugadorSuperior) * Tile.altura;
                double distanciaY = (jugador.y - jugador.altura / 2) - TileJugadorBordeSuperior;

                if (distanciaY > 0) {
                    jugador.y += Utilidades.proximoACero(-distanciaY, jugador.getVelocidadY());

                } else {
                    // Efecto Rebote -> empieza a bajar;
                    jugador.setVelocidadY(velocidadGravedad);
                    jugador.y += jugador.getVelocidadY();
                }

            }

        }

        // Hacia abajo
        if (jugador.getVelocidadY() >= 0) {
            // Tile inferior PASABLE
            // Podemos seguir moviendo hacia abajo
            // NOTA - El ultimo tile es especial (caer al vacío )
            if (tileYJugadorInferior + 1 <= altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior + 1].tipoDeColision
                            == Tile.PASABLE
                    && mapaTiles[tileXJugadorDerecha][tileYJugadorInferior + 1].tipoDeColision
                    == Tile.PASABLE) {
                // si los dos están libres cae
                jugador.y += jugador.getVelocidadY();
                jugador.enElAire = true; // Sigue en el aire o se cae
                // Tile inferior SOLIDO
                // El ULTIMO, es un caso especial

            } else if (tileYJugadorInferior + 1 <= altoMapaTiles() - 1 &&
                    (mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior + 1].tipoDeColision
                            == Tile.SOLIDO ||
                            mapaTiles[tileXJugadorDerecha][tileYJugadorInferior + 1].tipoDeColision ==
                                    Tile.SOLIDO)) {

                // Con que uno de los dos sea solido ya no puede caer
                // Si en el propio tile del jugador queda espacio para bajar más, bajo
                int TileJugadorBordeInferior =
                        tileYJugadorInferior * Tile.altura + Tile.altura;

                double distanciaY =
                        TileJugadorBordeInferior - (jugador.y + jugador.altura / 2);

                jugador.enElAire = true; // Sigue en el aire o se cae
                if (distanciaY > 0) {
                    jugador.y += Math.min(distanciaY, jugador.getVelocidadY());

                } else {
                    // Toca suelo, nos aseguramos de que está bien
                    jugador.y = TileJugadorBordeInferior - jugador.altura / 2;
                    jugador.setVelocidadY(0);
                    jugador.enElAire = false;
                }

                // Esta cayendo por debajo del ULTIMO
                // va a desaparecer y perder.
            } else {

                jugador.y += jugador.getVelocidadY();
                jugador.enElAire = true;

                if (jugador.y + jugador.altura / 2 > GameView.pantallaAlto) {
                    // ha perdido
                    nivelPausado = true;
                    mensaje = CargadorGraficos.cargarBitmap(context, R.drawable.you_lose);
                    inicializar();
                    jugador.restablecerPosicionInicial();
                }

            }
        }
        if(jugador.colisiona(meta)) {
            gameView.nivelCompleto();
        }

    }

    private float tilesEnDistanciaX(double distanciaX) {
        return (float) distanciaX / Tile.ancho;
    }
    private float tilesEnDistanciaY(double distanciaY){
        return (float) distanciaY / Tile.altura;
    }

}