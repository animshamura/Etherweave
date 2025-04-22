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

        hub.connect(d1);
        hub.connect(d2);
        hub.connect(d3);

        new DeviceServer(d1).start();
        new DeviceServer(d2).start();
        new DeviceServer(d3).start();

        Thread.sleep(1000); // Allow servers to bind

        javax.swing.SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(hub);
            frame.setVisible(true);
        });
    }
}

