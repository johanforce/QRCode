package com.example.qrcode;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

public class Frg_CreateQR extends Fragment implements View.OnClickListener {
    EditText editText;
    Button button;
    ImageView imageView, ivShare;
    View view;
    public Frg_CreateQR() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_create_qr, container, false);
        setHasOptionsMenu(true);

        initView();
        return view;
    }

    private void createQR() {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try{
            BitMatrix bitMatrix = multiFormatWriter.encode(editText.getText().toString(), BarcodeFormat.QR_CODE,500,500,hints);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            saveToInternalStorage(bitmap);
            imageView.setImageBitmap(bitmap);
            ivShare.setVisibility(View.VISIBLE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initView() {
        editText = (EditText)view.findViewById(R.id.et_text_input);
        button = (Button)view.findViewById(R.id.bt_create);
        button.setOnClickListener(this);
        imageView = (ImageView)view.findViewById(R.id.iv_QR);

        ivShare=view.findViewById(R.id.iv_share);
        ivShare.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.bt_create){
            createQR();
        }
        if(v.getId()==R.id.iv_share){
            Bitmap bitmap=getBitmapFromView(imageView);
            try {
                File file = new File(getActivity().getExternalCacheDir(),"profile.jpg");
                FileOutputStream fOut = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.flush();
                fOut.close();
                file.setReadable(true, false);
                final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri photoURI = FileProvider.getUriForFile(getActivity().getApplicationContext(), BuildConfig.APPLICATION_ID +".provider", file);
                intent.putExtra(Intent.EXTRA_STREAM, photoURI);
                intent.setType("image/jpg");
                startActivity(Intent.createChooser(intent, "Share image via"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public Bitmap getBitmapFromView(View view){
        Bitmap bitmap= Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas= new Canvas(bitmap);
        Drawable drawable= view.getBackground();
        if(drawable!=null){
            drawable.draw(canvas);
        }else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return bitmap;
    }
    private void saveToInternalStorage(Bitmap bitmapImage){
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir  = new File(root + "/saved_images");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        String pattern = "MM.dd.yyyy_HH.mm.ss";
        DateFormat df = new SimpleDateFormat(pattern);
        Date today = Calendar.getInstance().getTime();
        String todayAsString = df.format(today);


        String fname = todayAsString+".jpg";
        File file = new File (myDir, fname);
        if (file.exists ())
            file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
