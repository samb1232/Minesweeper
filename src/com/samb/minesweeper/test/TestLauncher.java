package com.samb.minesweeper.test;

import javax.swing.*;
import java.awt.*;

public class TestLauncher extends JFrame{

    public TestLauncher() {
        add(new TestBoard(1000));
        ImageIcon logo = new ImageIcon("resources/LOGO.png");
        setIconImage(logo.getImage());
        setBackground(new Color(185, 185, 185));
        setResizable(false);
        pack();
        setTitle("Minesweeper tests");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new TestLauncher();
    }
}
