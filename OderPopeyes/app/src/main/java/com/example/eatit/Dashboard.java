package com.example.eatit;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity {

    Button btnThucDon, btnGiaoHang, btnTuyenDung, btnDatTiec;
    Button btnPlayYoutube, btnPlayFacebook, btnPlayInstagram;
    ImageView imageGiaoHang,imageKhuyenMai;

    String hotline = "19006008";
    private static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnThucDon = findViewById(R.id.btnThucDon);
        btnGiaoHang = findViewById(R.id.btnGiaohang);
        btnTuyenDung = findViewById(R.id.btnTuyenDung);
        btnDatTiec = findViewById(R.id.btnDatTiec);
        btnPlayFacebook = findViewById(R.id.btnPlayFacebook);
        btnPlayInstagram = findViewById(R.id.btnPlayInstagram);
        btnPlayYoutube = findViewById(R.id.btnPlayVideo);
        imageGiaoHang = findViewById(R.id.image_giaoHang);
        imageKhuyenMai = findViewById(R.id.img_khuyenmai);


        //Thực Đơn
        btnThucDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(Dashboard.this, Home.class);
                startActivity(homeIntent);
            }
        });

        btnGiaoHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent giaoHangIntent = new Intent(Dashboard.this, GiaoHang.class);
                startActivity(giaoHangIntent);
            }
        });

        btnTuyenDung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tuyenDungIntent = new Intent(Dashboard.this, TuyenDung.class);
                startActivity(tuyenDungIntent);
            }
        });

        btnDatTiec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent datTiecIntent = new Intent(Dashboard.this, DatTiecSinhNhat.class);
                startActivity(datTiecIntent);
            }
        });

        btnPlayYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent youtubeIntent = new Intent(Dashboard.this, PlayYoutube.class);
                startActivity(youtubeIntent);
            }
        });

        btnPlayFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String idPagePopeyes = "104460023058222";
                String idPagePopeyes="PopeyesVN";
                gotoFacebookPage(idPagePopeyes);
            }
        });

        btnPlayInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idInstagram="popeyesvn";
                gotoInstagram(idInstagram);
            }
        });

        imageGiaoHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });
    }

    private void gotoInstagram(String idInstagram) {
        try {
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/" +idInstagram));
            startActivity(facebookIntent);
        }catch (ActivityNotFoundException e){
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.instagram.com/" +idInstagram));
            startActivity(facebookIntent);
        }
    }


    private void gotoFacebookPage(String idPagePopeyes) {
        try {
            //https://www.facebook.com/PopeyesVN
//            Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb//page/" + idPagePopeyes));
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + idPagePopeyes));
            startActivity(facebookIntent);
        }catch (ActivityNotFoundException e){
//            Intent facebookIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://facebook.com/" +idPagePopeyes));
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + idPagePopeyes));
            startActivity(facebookIntent);
        }
    }

    private void makePhoneCall() {
        if (ContextCompat.checkSelfPermission(Dashboard.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Dashboard.this
                    , new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            String dial = "tel:" + hotline;
            Intent callPhoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse(dial));
            startActivity(callPhoneIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makePhoneCall();
            }else
            {

                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
