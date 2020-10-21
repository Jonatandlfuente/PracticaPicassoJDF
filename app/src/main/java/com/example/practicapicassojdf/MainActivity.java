package com.example.practicapicassojdf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private int rcMY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1;
    private int rcMY_PERMISSIONS_INTERNET = 2;
    private String avisoAceptar = "Permisos aceptados!!!!!!";
    private String avisoYaAceptado = "Permisos ya aceptados";
    private String save = "save";
    private Timer timer = null;
    private TimerTask timerTask = null;
    private int countBanner = 0;
    private String img1 = "https://i2.bssl.es/miusyk/2020/09/ACDC-newwebpwrup-1024x698.jpg";
    private String img2 = "https://www.soy-de.com/images/thumbs/vuelve-a-disfrutar-de-metallica-en-madrid-0048526.jpeg";
    private String img3 = "https://rockandblog.net/wp-content/uploads/2020/04/10-curiosidades-guns-and-roses-1.jpg";
    private String url1 = "https://www.youtube.com/watch?v=v2AC41dglnM";
    private String url2 = "https://www.youtube.com/watch?v=WM8bTdBs-cw";
    private String url3 = "https://www.youtube.com/watch?v=8SbUC-UaAxE";
    private ImageView image = null;
    private String aux = "";
    private String TAG = "";
    private int contador = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Si lo inicializamos fuera no funciona la aplicación*/
        Button download = (Button) findViewById(R.id.btnDownload);
        Button permissions = (Button) findViewById(R.id.btnPermissions);
        Button save = (Button) findViewById(R.id.btnSave);
        image = findViewById(R.id.imgView);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasarImagenes();
            }
        });

        permissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPermissions();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage("x");
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aux.equalsIgnoreCase(img1)) {
                    Uri uri = Uri.parse(url1);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else if (aux.equalsIgnoreCase(img2)) {
                    Uri uri = Uri.parse(url2);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else if (aux.equalsIgnoreCase(img3)) {
                    Uri uri = Uri.parse(url3);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Método de la librería Picasso para cargar imágenes
    public void cargarImagenes(String url) {
        Picasso.get().load(url).into(image);
    }

    //metodo para pasar imagenes por la tarea schedulada
    public void pasarImagenes() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                //Para que no se interrumpa la interfaz principal
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "estoy dentro del run");
                        cargarImagenes(img1);
                        aux = img1;
                        contador++;
                        cargarImagenes(img2);
                        aux = img2;
                        contador++;
                        cargarImagenes(img3);
                        aux = img3;
                        contador++;
                        if (contador == 3) {
                            contador = 0;
                        }
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 1, 3000);

    }

    //metodo para aceptar permisos
    public void userPermissions() {

        //Si se acepta el permiso escribir en el manifest:<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"></uses-permission>
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, rcMY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);//MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE es un requestCode
            Toast.makeText(this, avisoAceptar, Toast.LENGTH_SHORT).show();
            //Si se acepta el permiso escribir en el manifest: <uses-permission android:name="android.permission.INTERNET"></uses-permission>
        }
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET}, rcMY_PERMISSIONS_INTERNET);//rcMY_PERMISSIONS_INTERNET es un requestCode
            Toast.makeText(this, avisoAceptar, Toast.LENGTH_SHORT).show();
            //Si ya están escritos mensaje de aceptado
        } else {
            Toast.makeText(this, avisoYaAceptado, Toast.LENGTH_SHORT).show();
        }
    }

    //metodo para guardar imagenes
    public void saveImage(String image) {
        Picasso.get().load(image).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                try {
                    File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString());
                    if (!directory.exists()) {
                        directory.mkdirs();
                    }
                    //Abre un archivo File en un canal con nombre generado con la fecha la variable countBanner y la extensión
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(directory,
                            new Date().toString().concat(String.valueOf(countBanner)).concat(".jpg")));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
                    //Limpia el buffer
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    Toast.makeText(getApplicationContext(), save, Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }
}