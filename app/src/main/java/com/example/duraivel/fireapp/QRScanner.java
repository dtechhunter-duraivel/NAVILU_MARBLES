package com.example.duraivel.fireapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;
import android.Manifest;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class QRScanner extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
   public CountDownTimer countDownTimer;

    private int CAMERA_PERMISSION_CODE = 1;
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
        if (ContextCompat.checkSelfPermission(QRScanner.this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(QRScanner.this, "PERMISSION GRANTED FOR CAMERA",
                    Toast.LENGTH_SHORT).show();
        } else {
            requestCameraPermission();
        }


//TO Set the Timer For QR Scanner
         countDownTimer =  new CountDownTimer (10000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                Intent mainIntent = new Intent(QRScanner
                        .this,SearchType.class);
                QRScanner.this.startActivity(mainIntent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }

        }.start();


  // Set the scanner view as the content view
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();
        countDownTimer.start();
        // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
        countDownTimer.cancel();// Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
 // To play the beep sound for scanned QR Code
        try
        {
            ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
            toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,150);
            AlertDialog.Builder builder =new AlertDialog.Builder(this);
            builder.setMessage(rawResult.getText());
            mScannerView.resumeCameraPreview(this);
            countDownTimer.cancel();

            Intent i =new Intent(QRScanner.this,ItemsList.class);
            i.putExtra( "val","sku="+rawResult.getText());
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Sorry Something Went Wrong! Try Again!",Toast.LENGTH_SHORT).show();
        }

    }



    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.INTERNET)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because scan QR Code")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(QRScanner.this,
                                    new String[] {Manifest.permission.INTERNET}, CAMERA_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.INTERNET}, CAMERA_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
