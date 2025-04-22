import core.*;
import gui.*;
import net.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        NetworkHub hub = new NetworkHub();

        // Setup devices
        Device d1 = new Device("PC1", "localhost", 6001);
        Device d2 = new Device("PC2", "localhost", 6002);
        Device d3 = new Device("PC3", "localhost", 6003);
        Device d4 = new Device("PC4", "localhost", 6004);
        Device d5 = new Device("PC5", "localhost", 6005);


        hub.connect(d1);
        hub.connect(d2);
        hub.connect(d3);
        hub.connect(d4);
        hub.connect(d5);


        new DeviceServer(d1).start();
        new DeviceServer(d2).start();
        new DeviceServer(d3).start();
        new DeviceServer(d4).start();
        new DeviceServer(d5).start();



        Thread.sleep(1000); // Allow servers to bind

        javax.swing.SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(hub);
            frame.setVisible(true);
        });
    }
}

