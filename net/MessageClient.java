package net;

import core.Device;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class MessageClient {
    public static void sendMessage(Device sender, Device receiver, String message) {
        try (Socket socket = new Socket(receiver.getHost(), receiver.getPort())) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("[" + sender.getName() + "]: " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcastMessage(Device sender, List<Device> receivers, String message) {
        for (Device receiver : receivers) {
            if (!receiver.getName().equals(sender.getName())) {
                sendMessage(sender, receiver, message);
            }
        }
    }

    public static void sendFile(Device sender, Device receiver, File file) {
        try (Socket socket = new Socket(receiver.getHost(), receiver.getPort());
             FileInputStream fis = new FileInputStream(file);
             OutputStream os = socket.getOutputStream()) {
            os.write(("[" + sender.getName() + " FILE]: " + file.getName() + "
").getBytes());
            byte[] buffer = new byte[4096];
            int count;
            while ((count = fis.read(buffer)) > 0) {
                os.write(buffer, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
