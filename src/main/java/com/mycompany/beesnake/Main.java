package com.mycompany.beesnake;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createWindowAndRunTheGame();
        });
    }

    private static void createWindowAndRunTheGame() {
        JFrame frame = new JFrame();
        Gameplay gameplay = new Gameplay();
        ImageIcon icon = new ImageIcon("./img/icon.png");

        frame.setSize(915, 700);
        frame.setBackground(Color.DARK_GRAY);
        frame.setIconImage(icon.getImage());
        frame.setTitle("Bee-snake Game");
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(gameplay);
    }
}