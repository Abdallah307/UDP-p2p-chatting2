package sample;

import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class Channel implements Runnable {

    private DatagramSocket socket;
    private boolean running;
    private TextArea area;



    public void bind(int port , TextArea board) throws SocketException {
        socket = new DatagramSocket(port);
        area = board;
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        running = false;
        socket.close();
    }

    @Override
    public void run() {
        byte buffer[] = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer , buffer.length);

        running = true;
        while (running) {
            try {
                socket.receive(packet);
                String msg = new String(buffer , 0 , packet.getLength());
                area.appendText(msg+"\n");


            }
            catch (IOException e) {
                break;
            }
        }
    }

    public void sendTo(InetSocketAddress address , String msg , TextArea board) throws IOException {
        byte [] buffer = msg.getBytes();

        DatagramPacket packet = new DatagramPacket(buffer , buffer.length);
        packet.setSocketAddress(address);

        socket.send(packet);
    }

    public void sendTxt(InetSocketAddress address , String msg , TextArea board) throws IOException {

    }
}