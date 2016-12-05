package mahecha.nicolas.control_acceso;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Login extends AppCompatActivity implements View.OnClickListener {


    private EditText user, pass;

    private Button login;

    ProgressDialog prgDialog;

    HashMap<String, String> queryValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Conectando......");
        prgDialog.setCancelable(false);

    }




    @Override
    public void onClick(View view) {

        registro();

    }


    ////////////**********************FUNCION DE INGRESO**********************/////////
    public void registro() {

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();


        String username = user.getText().toString();
        String password = pass.getText().toString();


        prgDialog.show();
        params.add("username", username);
        params.add("password", password);
        client.post("http://elca.sytes.net:2122/app_android/loginuser/loginuser.php", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                System.out.println(response);
                prgDialog.hide();
                try {
                    int success;
                    JSONArray arr = new JSONArray(response);

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = (JSONObject) arr.get(i);

                        success = Integer.parseInt(obj.get("success").toString());


                        if (success == 1) {
                            Toast.makeText(Login.this, obj.get("message").toString(), Toast.LENGTH_LONG).show();
                            Intent x = new Intent(Login.this,Espectador.class);
                            x.putExtra("id_us",obj.get("id_usuario").toString());

                            startActivity(x);
                            //finish();

                        } else {

                            Toast.makeText(Login.this, obj.get("message").toString(), Toast.LENGTH_LONG).show();
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
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



