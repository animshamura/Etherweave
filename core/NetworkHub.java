
package core;

import java.util.ArrayList;
import java.util.List;

public class NetworkHub {
    private List<Device> devices = new ArrayList<>();

    public void connect(Device device) {
        devices.add(device);
    }

    public List<Device> getAllDevices() {
        return devices;
    }

    public Device getDeviceByName(String name) {
        for (Device d : devices) {
            if (d.getName().equals(name)) return d;
        }
        return null;
    }
}
