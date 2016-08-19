package net.kojira.excellentball;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.cgutman.adblib.AdbBase64;
import com.cgutman.adblib.AdbConnection;
import com.cgutman.adblib.AdbCrypto;
import com.cgutman.adblib.AdbStream;

import net.kojira.util.L;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

public class PokeOverlayService extends Service {
    private LinearLayout mOverlay;
    private AdbConnection adb;
    private AdbStream stream;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createOverlay();
        connect();

        return super.onStartCommand(intent, flags, startId);
    }

    private void createOverlay() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.LEFT | Gravity.TOP;

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mOverlay = (LinearLayout) inflater.inflate(R.layout.overlay, null);
        Button button = (Button) mOverlay.findViewById(R.id.buttonShort);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommand(0);
            }
        });
        button = (Button) mOverlay.findViewById(R.id.buttonMiddle);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommand(1);
            }
        });
        button = (Button) mOverlay.findViewById(R.id.buttonLong);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommand(2);
            }
        });

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.addView(mOverlay, params);
        register();
    }

    private void sendCommand(final int type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String command = null;
                    switch (type) {
                        case 0:
                            command = "/system/bin/input swipe 540 1700 540 820";
                            break;
                        case 1:
                            command = "/system/bin/input swipe 540 1700 540 680";
                            break;
                        case 2:
                            command = "/system/bin/input swipe 540 1700 540 282";
                            break;
                    }
                    L.e("send shell command:[".concat(command).concat("]"));
                    stream.write(command.concat("\n"));
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }).start();
    }

    public void connect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket sock;
                AdbCrypto crypto;

                // Setup the crypto object required for the AdbConnection
                try {
                    crypto = AdbCrypto.generateAdbKeyPair(new AdbBase64() {
                        @Override
                        public String encodeToString(byte[] data) {
                            return android.util.Base64.encodeToString(data, 16);
                        }
                    });
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    return;
                }

                // Connect the socket to the remote host
                L.i("Socket connecting...");
                try {
                    sock = new Socket("127.0.0.1", 5555);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                L.i("Socket connected");

                // Construct the AdbConnection object
                try {
                    adb = AdbConnection.create(sock, crypto);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                // Start the application layer connection process
                L.i("ADB connecting...");
                try {
                    adb.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
                L.i("ADB connected");

                // Open the shell stream of ADB
                try {
                    stream = adb.open("shell:");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }

                // Start the receiving thread
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        L.i("receiving thread start");
                        while (!stream.isClosed()) {
                            try {
                                // Print each thing we read from the shell stream
                                System.out.print(new String(stream.read(), "US-ASCII"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                                return;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                return;
                            } catch (IOException e) {
                                e.printStackTrace();
                                return;
                            }
                        }
                    }
                }).start();
            }
        }).start();
    }

    private void register() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(PairingConst.ACTION_NOTIFY);
        registerReceiver(localReceiver, filter);

    }

    BroadcastReceiver localReceiver = new BroadcastReceiver() {
        int buttonId = -1;

        @Override
        public void onReceive(Context context, Intent intent) {
            buttonId = intent.getIntExtra(PairingConst.EXTRA_BUTTON_ID, -1);

            switch (buttonId) {
                default:
                    break;
                case 2://Single Click
                    sendCommand(0);
                    break;
                case 4://Double Click
                    sendCommand(1);
                    break;
                case 7://Long Click
                    sendCommand(2);
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(localReceiver);
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.removeView(mOverlay);
        try {
            if (stream != null) {
                stream.close();
            }
            if (adb != null) {
                adb.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}