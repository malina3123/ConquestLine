package com.mygdx.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VictoryNotification {
    JLabel label;
    JButton button;
    int player;
    public VictoryNotification(int player){
        this.player = player;
    }

    public void run() {
        label = new JLabel();
        label.setText("С победой, " + Integer.toString(player) + " игрок");
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setBounds(500, 300, 300, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(label, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
