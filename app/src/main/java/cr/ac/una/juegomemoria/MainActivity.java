package cr.ac.una.juegomemoria;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    //Creamos ImageButton para asignarle a los botones
    ImageButton i1,i2,i3,i4,i5,i6,i7,i8, i9;

    //Vector para almacenar el entero de las imagenes de los botones
    int imagenes[];

  //Vector donde verifica la cantidad de botones qe deben haber
    ImageButton[] botones = new ImageButton[8];

    //Se le asigna el valor de la imagen de fondo
    int fondo;

    //Lista de enteros para generar alatoriamente las imagenes de los botones
    ArrayList<Integer> baraja;


    //METODO VALIDAR
    //Para comparar los botones seleccionados
    ImageButton primero;
    //Variables qe recibe para comparar los dos numeros de imagen recibidos
    int numeroPrimero, numeroSegundo;

    //Para bloqear el juego y no poder seleccionar ningun boton despues de haber fallado o acertado
    boolean bloqueo = false;


    //Handler se utiliza para pausar durante cierto tiempo el juego
    final Handler handler = new Handler();

    //Para verificar si ya gano o no , cuantos aciertos ha tenido
    int aciertos=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cargarImagenes();
        iniciar();
    }

    //Se le asigna el valor entero de cada imagen , para poder verificarlas mas facil
    public void cargarImagenes(){
        imagenes = new int[]{
                R.drawable.chocolate,
                R.drawable.cookie,
                R.drawable.taco,
                R.drawable.papitas,
        };

        fondo = R.drawable.cocina;
    }


    public void cargarBotones()
    {
        i1 = (ImageButton) findViewById(R.id.imageButton);
        //almacenamos los ImageButton al vector botones
        botones[0] = i1;
        i2 = (ImageButton) findViewById(R.id.imageButton2);
        botones[1] = i2;
        i3 = (ImageButton) findViewById(R.id.imageButton3);
        botones[2] = i3;
        i4 = (ImageButton) findViewById(R.id.imageButton4);
        botones[3] = i4;
        i5 = (ImageButton) findViewById(R.id.imageButton5);
        botones[4] = i5;
        i6 = (ImageButton) findViewById(R.id.imageButton6);
        botones[5] = i6;
        i7 = (ImageButton) findViewById(R.id.imageButton7);
        botones[6] = i7;
        i8 = (ImageButton) findViewById(R.id.imageButton8);
        botones[7] = i8;
        i9 = (ImageButton) findViewById(R.id.imageButton8);


    }


    public ArrayList<Integer> barajar(int longitud)
    {
        ArrayList resultadoA = new ArrayList<Integer>();
        for(int i=0; i<longitud; i++)
            resultadoA.add(i % longitud/2);

        //Metodo para generar el ramdom de la lista recibida de imagenes o desordenar la lista
        Collections.shuffle(resultadoA);
        return  resultadoA;
    }


    public void iniciar(){

        //A la lista de enteros se le asigna el resultado del vector de imagenes
        //El metodo barajar hace el random para generar las imagenes aleatoriamente
        baraja = barajar(imagenes.length*2);

        //Cargamos los botones y las imagenes
        cargarBotones();


        //Ocultamos las imagenes
        for (int i = 0; i < botones.length; i++) {
            botones[i].setScaleType(ImageView.ScaleType.CENTER_CROP);
            botones[i].setImageResource(fondo);
        }


        //Se le añade los eventos de click a los botones
        for(int i=0; i <baraja.size(); i++){
            final int j=i;
            botones[i].setEnabled(true);
            botones[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Si el boton no sta bloqeado
                    if(!bloqueo)
                        //Comprobamos si son iguales o no
                        comprobar(j, botones[j]);
                }
            });
        }

        aciertos=0;

    }


    //Método para comprobar si se seleccionó correcto o no
    public void comprobar(int i, final ImageButton im){

        //si ningun botón ha sido pulsado
        if(primero==null){

            //le asignamos a primero el boton qe acabamos de seleccionar
            primero = im;

        /*le asignamos la imagen del vector imágenes situada
        en la posición baraja.get(i), con un valor entre 0 y 4*/
            primero.setScaleType(ImageView.ScaleType.CENTER_CROP);
            primero.setImageResource(imagenes[baraja.get(i)]);

            //bloqueamos el botón
            primero.setEnabled(false);

            //almacenamos el valor de baraja[i] para poder verificar luego
            numeroPrimero=baraja.get(i);

        }
        //ya hay un botón descubierto
        else
            {
            //bloqueamos todos los demás
            bloqueo=true;

            //el botón segundo sera el boton qe seleccionamos y le asignamos la imagen qe esta en su posicion
                // y se descubre
            im.setScaleType(ImageView.ScaleType.CENTER_CROP);
            im.setImageResource(imagenes[baraja.get(i)]);

            //bloqueamos el botón
            im.setEnabled(false);

            //almacenamos el valor de baraja.get(i) en el numeroSegundo para poder verificar si son el mismo o no
            numeroSegundo=baraja.get(i);

                //si numeroPrimero y mueroSegundo son los mismos
            if(numeroPrimero==numeroSegundo)
            {

                //Agregamos un sonido de acierto
                MediaPlayer gano = MediaPlayer.create(this,R.raw.correct);
                gano.start();

                //reiniciamos las variables
                primero = null;
                bloqueo = false;

                //aumentamos los aciertos
                aciertos++;

                //si ya lleva 4 aciertos se ha ganado el juego
                if (aciertos == 4)
                {
                    //Mostramos un mensaje de que ha ganado
                    Toast toast = Toast.makeText(getApplicationContext(), "¡¡Has ganado!!", Toast.LENGTH_LONG);
                    toast.show();

                    //Cuando finaliza , el juego vuelve a iniciar despues de 3 segundos
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //reiniciamos la variables auxiliares
                            primero = null;
                            bloqueo = false;

                            //Llamamos a la funcion iniciar
                            iniciar();

                        }
                    }, 2000);//esperamos 2 segundos
                }
            }

            //si NO coincide el valor  de los numeros volvemos a tapar las imagenes
            else
                {
                    //Agregamos un somido de fallo
                    MediaPlayer fallo = MediaPlayer.create(this,R.raw.incorrect);
                    fallo.start();
                    //Se le agrega el color rojo al boton de qe fallo
                    primero.setBackgroundColor(Color.RED);
                    im.setBackgroundColor(Color.RED);

                    //Llamamos a la variable handler para qe despues de 2 segundos voltee las imagenes
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //les ponemos la imagen de fondo
                            primero.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            primero.setImageResource(R.drawable.cocina);
                            primero.setBackgroundColor(Color.LTGRAY);
                            im.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            im.setImageResource(R.drawable.cocina);
                            im.setBackgroundColor(Color.LTGRAY);
                            //los volvemos a habilitar
                            primero.setEnabled(true);
                            im.setEnabled(true);

                            //reiniciamos la variables auxiliares
                            primero = null;
                            bloqueo = false;

                        }
                    }, 2000);//esperamos 2 segundos
            }
        }
    }




























    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Si se selecciono salir en el menu
        if (id == R.id.action_salir) {
            //Cerramos la aplicacion
            salir();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Metodo para cerrar la aplicacion
    public void salir()
    {
        this.finish();
    }
}
