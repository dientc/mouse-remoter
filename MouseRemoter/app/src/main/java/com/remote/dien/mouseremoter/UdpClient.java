package com.remote.dien.mouseremoter;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by Dien on 8/9/2016.
 */
public class UdpClient {
    static final int SERVER_PORT = 8080;

    DatagramSocket m_ClientSocket;
    DataOutputStream m_Out;
    InetAddress m_RemoteIPAddress;
    byte[] m_SendData = new byte[512];
    byte[] m_ReceiveData = new byte[512];

    public UdpClient() throws SocketException {
        m_ClientSocket = new DatagramSocket();
    }

    public boolean Connect(String RemoteAddr) throws IOException {

        Log.d("Address: ", RemoteAddr);
        m_RemoteIPAddress = InetAddress.getByName(RemoteAddr);
        String sentence = new String("Hello from vyvanveo ");
        m_SendData = sentence.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(m_SendData, m_SendData.length, m_RemoteIPAddress, SERVER_PORT);
        m_ClientSocket.send(sendPacket);

        DatagramPacket receivePacket = new DatagramPacket(m_ReceiveData, m_ReceiveData.length);
        m_ClientSocket.receive(receivePacket);
        String response = new String(receivePacket.getData());

        Log.d("Response: ", response);
        if(response.indexOf("Successful") != -1)
        {
            Log.d("Connect", "True");
            return true;
        }

        Log.d("Connect", "False");
        return false;
    }

    public void Send(String Message) throws IOException {
        m_SendData = Message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(m_SendData, m_SendData.length, m_RemoteIPAddress, SERVER_PORT);
        m_ClientSocket.send(sendPacket);
    }

    public void Send(byte[] Payload) throws IOException {
        DatagramPacket sendPacket = new DatagramPacket(Payload, Payload.length, m_RemoteIPAddress, SERVER_PORT);
        m_ClientSocket.send(sendPacket);
    }

    public void Close() throws IOException {
        m_ClientSocket.close();
    }
}
