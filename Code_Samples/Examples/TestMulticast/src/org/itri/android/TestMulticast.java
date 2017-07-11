package org.itri.android;

import android.content.Context;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;


import java.util.*;
import java.io.*;
import java.net.*;

public class TestMulticast extends Activity
{
    static boolean done = false;

    EditText et;
    TextView tv;
    MulticastSocket socket;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);

        LinearLayout h = new LinearLayout(this);
        h.setOrientation(LinearLayout.HORIZONTAL);

        et = new EditText(this);
        Button b = new Button(this);
        b.setText("Send");
        b.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                try {
                    String msg = et.getText().toString();
                    socket.send(new DatagramPacket(msg.getBytes(), 0, msg.getBytes().length, InetAddress.getByName("224.0.0.251"), 5353));
                    System.out.println("Sent");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        h.addView(b);
        h.addView(et, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        l.addView(h);

        tv = new TextView(this);

        l.addView(tv,  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));

        
        setContentView(l);

        if (!done) {
            try {

                WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiinfo = wifiManager.getConnectionInfo();
                int intaddr = wifiinfo.getIpAddress();

                if (intaddr == 0) {
                    tv.setText("Unable to get WIFI IP address");
                }
                else {
                    byte[] byteaddr = null;
                    byteaddr = new byte[] {
                        (byte)(intaddr & 0xff), 
                            (byte)(intaddr >> 8 & 0xff),
                            (byte)(intaddr >> 16 & 0xff),
                            (byte)(intaddr >> 24 & 0xff)
                    };

                    String machineName = "androidtestdevice";
                    InetAddress addr = InetAddress.getByAddress(machineName, byteaddr);


                    tv.append("Using address: " + addr + "\n");
                    System.out.println("Using address: " + addr);

                    // create socket
                    socket = new MulticastSocket(5353);

                    // set network interface
                    NetworkInterface iface = NetworkInterface.getByInetAddress(addr);

                    System.out.println("First address on interface is: " + getAddressFor(iface));
                    tv.append("First address on interface is: " + getAddressFor(iface) + "\n");

                    // The following line throws an exception in Android (Address is not available)
                    // If it's not called, the socket can receives packets
                    // Equivalent code in seems to C work.
                    //socket.setNetworkInterface(iface);

                    // join group
                    socket.joinGroup(InetAddress.getByName("224.0.0.251"));

                    tv.append("It worked\n");

                    // start receiving
                    new DatagramListener(socket, tv).start();
                }
            }
            catch (Exception e) {
                tv.append(e.toString() + "\n");
                e.printStackTrace();
            }
        }
    }

    class DatagramListener extends Thread {
        private DatagramSocket socket;
        private TextView tv;

        DatagramListener(DatagramSocket s, TextView tv) {
            socket = s;
            this.tv = tv;
        }

        public void run() {
            byte[] buf = new byte[1000];
            try {
                while (true) {
                    DatagramPacket recv = new DatagramPacket(buf, buf.length);
                    socket.receive(recv);

                    System.out.println("received: " + new String(recv.getData(), recv.getOffset(), recv.getLength()));
                    runOnUiThread(new MyRunnable(new String(recv.getData(), recv.getOffset(), recv.getLength()), tv));
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static private class MyRunnable implements Runnable {
        private TextView tv;
        private String text;

        public MyRunnable(String text, TextView tv) {
            this.tv = tv;
            this.text = text;
        }

        public void run() {
            tv.append(text + "\n");
        }
    }

    public static InetAddress getAddressFor(NetworkInterface iface) {
        Enumeration<InetAddress> theAddresses = iface.getInetAddresses();
        boolean found = false;
        InetAddress firstAddress = null;

        while ((theAddresses.hasMoreElements()) && (found != true)) {
            InetAddress theAddress = theAddresses.nextElement();
            if (theAddress instanceof Inet4Address) {
                firstAddress = theAddress;
                found = true;
            }
        }

        return firstAddress;
    }
}
