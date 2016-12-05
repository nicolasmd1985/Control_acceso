package mahecha.nicolas.control_acceso;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Detallecomp extends AppCompatActivity {

    TextView nombre,descripcion,donde,valor;
    ImageView foto;
    ProgressDialog prgDialog;
    Button comprar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detallecomp);

        nombre = (TextView)findViewById(R.id.nomb);
        descripcion = (TextView)findViewById(R.id.descri);
        donde = (TextView)findViewById(R.id.donde);
        valor = (TextView)findViewById(R.id.val);
        foto = (ImageView)findViewById(R.id.imageView2);


        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Conectando......");
        prgDialog.setCancelable(false);
        cargalist();






    }


    private void cargalist() {


        AsyncHttpClient client = new AsyncHttpClient();

        prgDialog.show();

        RequestParams params = new RequestParams();
        String  idusuar = getIntent().getStringExtra("id");



        params.add("id_even", idusuar);
        // params.add("password", password);
        client.post("http://elca.sytes.net:2122/app_android/operaciones/evento.php", params, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(String response) {
            System.out.println(response);
                prgDialog.hide();
                updatelis(response);



            }


            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {

                //prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Dispositivo Sin Conexión a Internet",
                            Toast.LENGTH_LONG).show();
                }
                prgDialog.hide();

            }


        });
    }


    public void updatelis(final String response)
    {

        ArrayList<HashMap<String, String>> eventos = new ArrayList<HashMap<String, String>>();

        try {
//                    int success;
            JSONArray arr = new JSONArray(response);
            System.out.println(response);

            System.out.println(arr.length());
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = (JSONObject) arr.get(i);
                HashMap<String,String> map = new HashMap<String, String>();
                System.out.println(obj);

                nombre.setText(obj.get("Nombre").toString());
                descripcion.setText(obj.get("Descripcion").toString());
                donde.setText(obj.get("Donde").toString());
                valor.setText(obj.get("Valor").toString());
                Picasso.with(Detallecomp.this).load(obj.get("Foto").toString()).resize(200, 200).centerCrop().into(foto);
                final String compra = obj.get("id_eventos").toString();

                comprar =(Button)findViewById(R.id.comp);
                comprar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showSimplePopUp(compra);
                        prgDialog.hide();
                    }
                });



            }


        } catch (JSONException e) {
            e.printStackTrace();
            prgDialog.hide();
        }



    }


    ///////////******************POP UP**************//////////////
    private void showSimplePopUp(String code) {

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle("COMPRAR!!");
        helpBuilder.setMessage("Realmente desea comprar la entrada de este evento?");
        helpBuilder.setPositiveButton("Si",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        //final String  id_us = getIntent().getStringExtra("id_us");
                        //final String  id_even = getIntent().getStringExtra("id");

                        //System.out.println(id_even+id_us);

                        updatecompra();

                    }
                });
        helpBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                // System.out.println("no");
            }
        });

        // Remember, create doesn't show the dialog
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }



    private void updatecompra() {


        AsyncHttpClient client = new AsyncHttpClient();

        prgDialog.show();

        RequestParams params = new RequestParams();
        final String  id_us = getIntent().getStringExtra("id_us");
        final String  id_even = getIntent().getStringExtra("id");

        params.add("id_us", id_us);
        params.add("id_even", id_even);

        client.post("http://elca.sytes.net:2122/app_android/operaciones/updatecomp.php", params, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(String response) {
                System.out.println(response);
                prgDialog.hide();
                Toast.makeText(getApplicationContext(), "¡COMPRA REALIZADA!", Toast.LENGTH_LONG).show();
                Intent w = new Intent(Detallecomp.this,Espectador.class);
                w.putExtra("id_us", id_us);
                startActivity(w);



            }


            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {

                //prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Dispositivo Sin Conexión a Internet",
                            Toast.LENGTH_LONG).show();
                }
                prgDialog.hide();

            }


        });
    }




}
