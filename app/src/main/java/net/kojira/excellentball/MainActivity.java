package net.kojira.excellentball;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_SETTINGS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button)findViewById(R.id.btn_start);
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                if (Settings.canDrawOverlays(MainActivity.this)) {
                    startService(new Intent(MainActivity.this, PokeOverlayService.class));
                } else {
                    Uri uri = Uri.parse("package:" + getPackageName());
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uri);
                    startActivityForResult(intent, REQUEST_SETTINGS);
                }
            }

        });
        btn = (Button)findViewById(R.id.btn_stop);
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                stopService(new Intent(MainActivity.this, PokeOverlayService.class));
            }

        });

        copyFile("adbkey");
        copyFile("adbkey.pub");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SETTINGS) {
            if (Settings.canDrawOverlays(MainActivity.this)) {
                startService(new Intent(MainActivity.this, PokeOverlayService.class));
            }
        }
    }

    private void copyFile(String filename) {
        File file = new File(getFilesDir().getAbsolutePath()+"/"+filename);
        if (file.exists()) {
            return;
        }
        try {
            InputStream inputStream = getAssets().open(filename);
            FileOutputStream fileOutputStream = openFileOutput(filename, MODE_PRIVATE);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) >= 0) {
                fileOutputStream.write(buffer, 0, length);
            }
            fileOutputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
