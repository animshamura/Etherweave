package gui;

import core.*;
import net.*;
import util.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class MainFrame extends JFrame {
    private NetworkHub hub;
    private JTextArea logArea;
    private JComboBox<String> senderBox, receiverBox;
    private JTextField messageField;
    private JButton sendButton, broadcastButton, fileButton;
    private JButton refreshButton, clearLogButton, toggleThemeButton;
    private JCheckBox encryptBox;
    private boolean darkMode = false;

    public MainFrame(NetworkHub hub) {
        this.hub = hub;
        setTitle("Etherweave");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Font font = new Font("Segoe UI", Font.PLAIN, 14);

        logArea = new JTextArea();
        logArea.setFont(font);
        logArea.setEditable(false);
        logArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scroll = new JScrollPane(logArea);
        scroll.setBorder(new TitledBorder("Activity Log"));
        add(scroll, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel senderLabel = new JLabel("Sender:");
        senderLabel.setFont(font);
        topRow.add(senderLabel);
        senderBox = new JComboBox<>();
        senderBox.setFont(font);
        topRow.add(senderBox);
        JLabel receiverLabel = new JLabel("Receiver:");
        receiverLabel.setFont(font);
        topRow.add(receiverLabel);
        receiverBox = new JComboBox<>();
        receiverBox.setFont(font);
        topRow.add(receiverBox);
        inputPanel.add(topRow);

        JPanel middleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel msgLabel = new JLabel("Message:");
        msgLabel.setFont(font);
        middleRow.add(msgLabel);
        messageField = new JTextField(40);
        messageField.setFont(font);
        middleRow.add(messageField);
        inputPanel.add(middleRow);

        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        sendButton = new JButton("Send");
        broadcastButton = new JButton("Broadcast");
        fileButton = new JButton("Send File");
        sendButton.setFont(font);
        broadcastButton.setFont(font);
        fileButton.setFont(font);
        bottomRow.add(sendButton);
        bottomRow.add(broadcastButton);
        bottomRow.add(fileButton);
        inputPanel.add(bottomRow);

        JPanel extrasRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        refreshButton = new JButton("🔄 Refresh Devices");
        clearLogButton = new JButton("🧹 Clear Log");
        toggleThemeButton = new JButton("🌓 Toggle Theme");
        encryptBox = new JCheckBox("Encrypt Message");
        refreshButton.setFont(font);
        clearLogButton.setFont(font);
        toggleThemeButton.setFont(font);
        encryptBox.setFont(font);
        extrasRow.add(refreshButton);
        extrasRow.add(clearLogButton);
        extrasRow.add(toggleThemeButton);
        extrasRow.add(encryptBox);
        inputPanel.add(extrasRow);

        add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> sendMessage());
        broadcastButton.addActionListener(e -> broadcastMessage());
        fileButton.addActionListener(e -> sendFile());
        refreshButton.addActionListener(e -> refreshDeviceList());
        clearLogButton.addActionListener(e -> logArea.setText(""));
        toggleThemeButton.addActionListener(e -> toggleDarkMode());

        refreshDeviceList();
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
            if (encryptBox.isSelected()) {
                msg = EncryptionUtil.encrypt(msg);
            }
            log("📤 Sending message from " + sender.getName() + " to " + receiver.getName());
            MessageClient.sendMessage(sender, receiver, msg);
        }
    }

    private void broadcastMessage() {
        Device sender = hub.getDeviceByName((String) senderBox.getSelectedItem());
        String msg = messageField.getText();
        if (sender != null && !msg.isEmpty()) {
            if (encryptBox.isSelected()) {
                msg = EncryptionUtil.encrypt(msg);
            }
            log("📡 Broadcasting message from " + sender.getName());
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
                log("📁 Sending file from " + sender.getName() + " to " + receiver.getName() + ": " + file.getName());
                MessageClient.sendFile(sender, receiver, file);
            }
        }
    }

    private void toggleDarkMode() {
        darkMode = !darkMode;
        Color bg = darkMode ? new Color(30, 30, 30) : Color.WHITE;
        Color fg = darkMode ? Color.LIGHT_GRAY : Color.BLACK;

        getContentPane().setBackground(bg);
        logArea.setBackground(bg);
        logArea.setForeground(fg);
        messageField.setBackground(bg);
        messageField.setForeground(fg);
    }

    public void log(String message) {
        logArea.append(message + "\n");
    }
}
