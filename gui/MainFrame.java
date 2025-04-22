package gui;

import core.*;
import net.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

public class MainFrame extends JFrame {
    private NetworkHub hub;
    private JTextArea logArea;
    private JComboBox<String> senderBox, receiverBox;
    private JTextField messageField;
    private JButton sendButton, broadcastButton, fileButton;

    public MainFrame(NetworkHub hub) {
        this.hub = hub;
        setTitle("Ethernet LAN Control Panel");
        setSize(700, 500);
        setLayout(new BorderLayout());

        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(logArea);
        add(scroll, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(3, 1));

        // Device selectors
        JPanel topRow = new JPanel();
        senderBox = new JComboBox<>();
        receiverBox = new JComboBox<>();
        topRow.add(new JLabel("Sender:"));
        topRow.add(senderBox);
        topRow.add(new JLabel("Receiver:"));
        topRow.add(receiverBox);
        inputPanel.add(topRow);

        // Message field
        JPanel middleRow = new JPanel();
        messageField = new JTextField(30);
        middleRow.add(new JLabel("Message:"));
        middleRow.add(messageField);
        inputPanel.add(middleRow);

        // Action buttons
        JPanel bottomRow = new JPanel();
        sendButton = new JButton("Send");
        broadcastButton = new JButton("Broadcast");
        fileButton = new JButton("Send File");
        bottomRow.add(sendButton);
        bottomRow.add(broadcastButton);
        bottomRow.add(fileButton);
        inputPanel.add(bottomRow);

        add(inputPanel, BorderLayout.SOUTH);

        // Actions
        sendButton.addActionListener(e -> sendMessage());
        broadcastButton.addActionListener(e -> broadcastMessage());
        fileButton.addActionListener(e -> sendFile());

        refreshDeviceList();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void refreshDeviceList() {
        senderBox.removeAllItems();
        receiverBox.removeAllItems();
        for (Device d : hub.getAllDevices()) {
            senderBox.addItem(d.getName());
            receiverBox.addItem(d.getName());
        }
    }

    private void sendMessage() {
        Device sender = hub.getDeviceByName((String) senderBox.getSelectedItem());
        Device receiver = hub.getDeviceByName((String) receiverBox.getSelectedItem());
        String msg = messageField.getText();
        if (sender != null && receiver != null && !msg.isEmpty()) {
            log("Sending message from " + sender.getName() + " to " + receiver.getName());
            MessageClient.sendMessage(sender, receiver, msg);
        }
    }

    private void broadcastMessage() {
        Device sender = hub.getDeviceByName((String) senderBox.getSelectedItem());
        String msg = messageField.getText();
        if (sender != null && !msg.isEmpty()) {
            log("Broadcasting message from " + sender.getName());
            MessageClient.broadcastMessage(sender, hub.getAllDevices(), msg);
        }
    }

    private void sendFile() {
        Device sender = hub.getDeviceByName((String) senderBox.getSelectedItem());
        Device receiver = hub.getDeviceByName((String) receiverBox.getSelectedItem());

        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (sender != null && receiver != null) {
                log("Sending file from " + sender.getName() + " to " + receiver.getName());
                MessageClient.sendFile(sender, receiver, file);
            }
        }
    }

    public void log(String message) {
        logArea.append(message + "\n");
    }
}

