package com.samb.minesweeper.game;

import javax.swing.*;
import java.awt.*;

public class GameLauncher extends JFrame {

    public GameLauncher() {
        add(new Board());
        ImageIcon logo = new ImageIcon("resources/LOGO.png");
        setIconImage(logo.getImage());
        setBackground(new Color(185, 185, 185));
        setResizable(false);
        pack();
        setTitle("Minesweeper");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new GameLauncher();
    }
}
