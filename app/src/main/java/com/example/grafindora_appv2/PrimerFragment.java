package com.example.grafindora_appv2;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.xml.transform.Result;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrimerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrimerFragment extends Fragment {



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private ImageView imgPerfil;
    final int CAPTURA_IMAGEN = 1;
    String []archivos;

    public PrimerFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PrimerFragment newInstance(String param1, String param2) {
        PrimerFragment fragment = new PrimerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        archivos= getContext().fileList();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView nombre = view.findViewById(R.id.tvnombrepet);
        TextView edad = view.findViewById(R.id.tvedad);
        TextView raza = view.findViewById(R.id.tvraza);
        TextView sexo = view.findViewById(R.id.tvsexo);
        Button btnCamara= view.findViewById(R.id.btncam);
        imgPerfil = view.findViewById(R.id.imgperfil);
        
        btnCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAPTURA_IMAGEN);
            }
        });

        try {
            SQLiteDatabase db = getContext().openOrCreateDatabase("DB_GRAFIN",Context.MODE_PRIVATE ,null);
            db.execSQL("CREATE TABLE IF NOT EXISTS mascota(id INTEGER PRIMARY KEY AUTOINCREMENT,nombre VARCHAR,edad VARCHAR,raza VARCHAR,sexo VARCHAR)");

            final Cursor c = db.rawQuery("select * from mascota",null);
            c.moveToLast();
            int id = c.getColumnIndex("id");
            int nom = c.getColumnIndex("nombre");
            int ed = c.getColumnIndex("edad");
            int raz = c.getColumnIndex("raza");
            int sex = c.getColumnIndex("sexo");

            Mascota mascota = new Mascota();

            mascota.id = c.getInt(id);
            mascota.nombre = c.getString(nom).toString();
            mascota.edad = c.getString(ed).toString();
            mascota.raza = c.getString(raz).toString();
            mascota.sexo = c.getString(sex).toString();


            nombre.append(" "+mascota.nombre);
            edad.append(" "+mascota.edad);
            raza.append(" " +mascota.raza);
            sexo.append(" "+mascota.sexo);


        }catch (Exception ex){}

        verFoto(0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CAPTURA_IMAGEN && resultCode == RESULT_OK){

            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            try {
                FileOutputStream fos = getContext().openFileOutput(crearNombreArchivo(),Context.MODE_PRIVATE);
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
                imgPerfil.setImageBitmap(bitmap);
                fos.close();


            }catch (Exception e){}
        }
        verFoto(0);
    }


    private String crearNombreArchivo() {
        String fecha = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        return fecha+".jpg";
    }

    public void verFoto(int position){

        try {
            FileInputStream fileInputStream= getContext().openFileInput(archivos[position]);
            Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
            imgPerfil.setImageBitmap(bitmap);
            fileInputStream.close();
        }catch (Exception e){}

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_primer, container, false);

        return view;
    }
}