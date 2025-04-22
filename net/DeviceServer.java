
package net;

import core.Device;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class DeviceServer extends Thread {
    private Device device;

    public DeviceServer(Device device) {
        this.device = device;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(device.getPort())) {
            System.out.println(device.getName() + " listening on port " + device.getPort());
            while (true) {
                Socket clientSocket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println("[" + device.getName() + " RECEIVED]: " + line);
                }
                in.close();
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

