package com.ucsc.gaweshana.gaweshanav3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Anuradha on 8/4/2015.
 */


    public class ScanActivity extends Activity implements ZXingScannerView.ResultHandler {
        private ZXingScannerView mScannerView;

        @Override
        public void onCreate(Bundle state) {
            super.onCreate(state);
            mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
            setContentView(mScannerView);                // Set the scanner view as the content view
        }

        @Override
        public void onResume() {
            super.onResume();
            mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
            mScannerView.startCamera();          // Start camera on resume
        }

        @Override
        public void onPause() {
            super.onPause();
            mScannerView.stopCamera();           // Stop camera on pause
        }

        @Override
        public void handleResult(Result rawResult) {
            // Do something with the result here
            ///Log.v(TAG, rawResult.getText()); // Prints scan results
            //Log.v(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
            Intent x=new Intent(ScanActivity.this,SingleActivity.class);
            x.putExtra("Result",rawResult.getText());
            startActivity(x);
        }
    }
