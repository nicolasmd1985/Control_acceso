package mahecha.nicolas.control_acceso;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Evendisp extends AppCompatActivity {

    ProgressDialog prgDialog;
    private ArrayList<HashMap<String, String>> listas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evendisp);


        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Conectando......");
        prgDialog.setCancelable(false);
        cargalist();


    }

    private void cargalist() {


        AsyncHttpClient client = new AsyncHttpClient();

        prgDialog.show();

        client.get("http://elca.sytes.net:2122/app_android/operaciones/get_pedido.php", new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(String response) {

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

                map.put("id_eventos",obj.get("id_eventos").toString());
                map.put("Nombre",obj.get("Nombre").toString());
                map.put("Descripcion",obj.get("Descripcion").toString());
                map.put("Foto",obj.get("Foto").toString());
             eventos.add(map);


            }
            listas=eventos;
            prgDialog.hide();
//
//
                    ListView listView = (ListView)findViewById(R.id.recep);
                    listView.setAdapter(adapter);


            listView.setOnItemClickListener(new ListView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(getApplicationContext(), "Pesiono"+id, Toast.LENGTH_LONG).show();
                    Intent x = new Intent(Evendisp.this,Detallecomp.class);
                    String nn = String.valueOf(id);
                    final String  id_us = getIntent().getStringExtra("id_us");
                    x.putExtra("id_us",id_us);
                    x.putExtra("id",nn);
                    startActivity(x);
                }


            });




//
//
        } catch (JSONException e) {
            e.printStackTrace();
            prgDialog.hide();
        }



    }


    private BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return listas.size();
        }

        @Override
        public HashMap<String, String> getItem(int i) {

            return listas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return Long.parseLong(getItem(i).get("id_eventos"));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater li = getLayoutInflater();
                convertView = li.inflate(R.layout.items, parent, false);
                //Log.d("Prueba De Lista", "Estamos creando item nuevo para " + position);
            } else {
                TextView textView = (TextView) convertView.findViewById(R.id.id_even);
                String nombreViejo = textView.getText().toString();
                // Log.d("Prueba De Lista", "Estamos reciclando una vista para " + position + " (" + nombreViejo + ")");
            }

            TextView textView = (TextView) convertView.findViewById(R.id.id_even);
            TextView textview2 = (TextView) convertView.findViewById(R.id.evento);
            TextView textview3 = (TextView) convertView.findViewById(R.id.descripcion);
            ImageView fotos = (ImageView) convertView.findViewById(R.id.imageView);

            HashMap<String, String> numero = getItem(position);


            textView.setText(numero.get("id_eventos"));
            textview2.setText(numero.get("Nombre"));
            textview3.setText(numero.get("Descripcion"));
            Picasso.with(Evendisp.this).load(numero.get("Foto")).resize(200, 200).centerCrop().into(fotos);


            return convertView;
        }
    };





}



