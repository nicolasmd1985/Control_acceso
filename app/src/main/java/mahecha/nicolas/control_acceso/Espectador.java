package mahecha.nicolas.control_acceso;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.squareup.picasso.Picasso;

public class Espectador extends AppCompatActivity {

    ImageButton imabutton1,imabutton2,imabutton3,imabutton4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espectador);

        imabutton1 = (ImageButton)findViewById(R.id.imageButton);
        imabutton2 = (ImageButton)findViewById(R.id.imageButton2);
        imabutton3 = (ImageButton)findViewById(R.id.imageButton3);
        imabutton4 = (ImageButton)findViewById(R.id.imageButton4);




        Picasso.with(this).load(R.drawable.calendar).resize(200, 200).centerCrop().into(imabutton1);
        Picasso.with(this).load(R.drawable.calendar1).resize(200, 200).centerCrop().into(imabutton2);
        Picasso.with(this).load(R.drawable.calendar2).resize(200, 200).centerCrop().into(imabutton3);
        Picasso.with(this).load(R.drawable.calendar3).resize(200, 200).centerCrop().into(imabutton4);


        final String  id_us = getIntent().getStringExtra("id_us");

        imabutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x = new Intent(Espectador.this,Evendisp.class);
                x.putExtra("id_us",id_us);
                startActivity(x);
            }
        });

        imabutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x = new Intent(Espectador.this,Compras_activ.class);
                x.putExtra("id_us",id_us);
                startActivity(x);
            }
        });


    }
}
