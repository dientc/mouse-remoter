package com.remote.dien.mouseremoter;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.*;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    Vector m_Desktops;
    UdpClient m_Client = null;
    boolean m_IsConnected = false;
    byte[] m_Payload;
    int[] m_StartPointMoved;
    int[] m_StartPointClicked;
    int m_PushDownCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_Payload = new byte[Utilities.PAYLOAD_SIZE];
        m_Payload[0] = 0x00;
        m_StartPointMoved = new int[]{0, 0};
        m_StartPointClicked = new int[]{0, 0};
    }

    @Override
    protected void onStart() {
        super.onStart();
        new ScannerTask().execute("");
    }

    @Override
    public boolean onTouchEvent(MotionEvent Event) {
        if (m_IsConnected) {
            int x = (int)Event.getX();
            int y = (int)Event.getY();
            int eventaction = Event.getAction();

            switch(eventaction) {
                case MotionEvent.ACTION_DOWN:
                {
                    m_StartPointMoved[0] = x;
                    m_StartPointMoved[1] = y;
                    m_StartPointClicked[0] = x;
                    m_StartPointClicked[1] = y;
                } break;
                case MotionEvent.ACTION_MOVE:
                {
                    int[] distance = new int[]{x - m_StartPointMoved[0], y - m_StartPointMoved[1]};
                    //Log.d("Touch", "distance " + distance[0] + " " + distance[1]);
                    m_StartPointMoved[0] = x;
                    m_StartPointMoved[1] = y;

                    m_Payload[0] = Utilities.MOVE;
                    m_Payload[1] = (byte)((distance[0] >> 8) & 0xFF);
                    m_Payload[2] = (byte)(distance[0] & 0xFF);
                    m_Payload[3] = (byte)((distance[1] >> 8) & 0xFF);
                    m_Payload[4] = (byte)(distance[1] & 0xFF);
                    SendPayload();
                    Log.d("Touch", "Move");
                }
                case MotionEvent.ACTION_UP:
                {
                    int[] distance = new int[]{x - m_StartPointClicked[0], y - m_StartPointClicked[1]};
                    //Log.d("Touch", "distance " + distance[0] + " " + distance[1]);
                    if (Math.sqrt(Math.pow(distance[0], 2) + Math.pow(distance[1], 2)) < 2)
                    {
                        m_PushDownCounter++;
                        if (m_PushDownCounter % 2 == 0) {
                            m_Payload[0] = Utilities.CLICK;
                            SendPayload();
                            Log.d("Touch", "Click");
                        }
                    }
                } break;
            }
        }

        return true;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        m_Payload[0] = Utilities.GOODBYE;
        SendPayload();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            m_Client.Close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ScannerTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            DesktopScanner scanner = null;
            try {
                scanner = new DesktopScanner();
                m_Desktops = scanner.ScanDesktop();
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            TextView txt = (TextView)findViewById(R.id.status);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            ShowIcons();
            txt.setText("Transferring data ...");
            new CommunicateTask().execute("");
        }
    }

    class CommunicateTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            Utilities.PrepareData(m_Payload);
            try {
                m_Client = new UdpClient();
                m_IsConnected = m_Client.Connect(m_Desktops.get(0).toString());// HARDCODE 0 to choose a destop to pair.
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (m_IsConnected) {
                    Log.d("Connecting", "True");
                    m_Client.Send(m_Payload);
                }
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    m_Client.Close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //
        }
    }

    void SendPayload() {
        new Thread() {
            @Override
            public void run() {
                try {
                    m_Client.Send(m_Payload);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    void ShowIcons() {
        ImageView image = null;
        if (m_Desktops.size() >= 1)
        {
            image = (ImageView) findViewById(R.id.imageView0);
            image.setVisibility(View.VISIBLE);
        }
        if (m_Desktops.size() >= 2)
        {
            image = (ImageView) findViewById(R.id.imageView1);
            image.setVisibility(View.VISIBLE);
        }
        if (m_Desktops.size() >= 3)
        {
            image = (ImageView) findViewById(R.id.imageView2);
            image.setVisibility(View.VISIBLE);
        }
    }
}
