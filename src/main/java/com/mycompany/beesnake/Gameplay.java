package com.mycompany.beesnake;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

public class Gameplay extends JPanel implements KeyListener, ActionListener {

    private final int[] beeXlength = new int[750];
    private final int[] beeYlength = new int[750];

    private final Timer timer;

    private boolean left = false;
    private boolean right = false;
    private boolean up = false;
    private boolean down = false;

    private int beelength = 1;
    private int score = 0;
    private int moves = 0;

    private ImageIcon flower;

    private Clip clip;

    //possible flower placements
    private final int[] flowerX = {50, 75, 100, 125, 150, 175, 200, 225, 250, 275, 300, 325, 350, 375, 400, 425, 450, 500, 525, 550, 575, 600, 625, 650, 675, 700, 725, 750, 775, 800, 825};
    private final int[] flowerY = {100, 125, 150, 175, 200, 225, 250, 275, 300, 325, 350, 375, 400, 425, 450, 500, 525, 550, 575, 600};

    private final Random random = new Random();

    private int xpos = random.nextInt(31);
    private int ypos = random.nextInt(20);

    public static String playRandomSong() {
        List<String> givenList = Arrays.asList(
                "./mp3/m1.wav",
                "./mp3/m2.wav");
        Random random = new Random();
        return givenList.get(random.nextInt(givenList.size()));
    }

    public void backgroundMusic() {
        String song = playRandomSong();
        try {
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File(song)));
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String playRandomSound() {
        List<String> givenList = Arrays.asList(
                "./mp3/s1.wav",
                "./mp3/s2.wav");
        Random random = new Random();
        return givenList.get(random.nextInt(givenList.size()));
    }

    public void gameOverSound() {
        String sound = playRandomSound();
        try {
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File(sound)));
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Gameplay() {
        backgroundMusic();

        addKeyListener(this);
        setFocusable(true);

        int delay = 100;
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {
        // bee placement at the beginning
        if (moves == 0) {
            beeXlength[0] = 100;
            beeYlength[0] = 100;
        }

        setFrameAndScoreDisplay(g);
        setIcons(g);
        addScoreAndSpawnNextFlower(g);
        gameOver(g);
        g.dispose();
    }

    private void setFrameAndScoreDisplay(Graphics g) {
        setTitleBorder(g);
        setTitleImage(g);
        setGameplayBorder(g);
        setGameplayBackground(g);
        scoreDisplay(g);
        beeLengthDisplay(g);
    }

    private void setTitleBorder(Graphics g) {
        g.setColor(Color.black);
        g.drawRect(24, 10, 851, 55);
    }

    private void setTitleImage(Graphics g) {
        ImageIcon titleImage = new ImageIcon("./img/title.png");
        titleImage.paintIcon(this, g, 25, 11);
    }

    private void setGameplayBorder(Graphics g) {
        g.setColor(Color.black);
        g.drawRect(24, 74, 851, 576);
    }

    private void setGameplayBackground(Graphics g) {
        ImageIcon backgroundImage = new ImageIcon("./img/background1.png");
        backgroundImage.paintIcon(this, g, 25, 75);
    }

    private void scoreDisplay(Graphics g) {
        g.setColor(Color.black);
        g.setFont(new Font("arial", Font.PLAIN, 14));
        g.drawString("Score: " + score, 780, 33);
    }

    private void beeLengthDisplay(Graphics g) {
        g.setColor(Color.black);
        g.setFont(new Font("arial", Font.PLAIN, 14));
        g.drawString("Length: " + beelength, 780, 53);
    }

    private void setIcons(Graphics g) {
        if (moves == 0) {
            ImageIcon rightSide = new ImageIcon("./img/beeR.png");
            rightSide.paintIcon(this, g, beeXlength[0], beeYlength[0]);
        }

        for (int a = 0; a < beelength; a++) {
            if (a == 0) {
                changeIconDirections(g, a);
            }
            if (a != 0) {
                ImageIcon nectar = new ImageIcon("./img/nectar.png");
                nectar.paintIcon(this, g, beeXlength[a], beeYlength[a]);
            }
        }
    }

    private void changeIconDirections(Graphics g, int a) {
        if (right) {
            ImageIcon rightSide = new ImageIcon("./img/beeR.png");
            rightSide.paintIcon(this, g, beeXlength[a], beeYlength[a]);
        }
        if (left) {
            ImageIcon leftSide = new ImageIcon("./img/beeL.png");
            leftSide.paintIcon(this, g, beeXlength[a], beeYlength[a]);
        }
        if (up) {
            ImageIcon upSide = new ImageIcon("./img/beeU.png");
            upSide.paintIcon(this, g, beeXlength[a], beeYlength[a]);
        }
        if (down) {
            ImageIcon downSide = new ImageIcon("./img/beeD.png");
            downSide.paintIcon(this, g, beeXlength[a], beeYlength[a]);
        }
    }

    private void addScoreAndSpawnNextFlower(Graphics g) {
        /* if bee is at the spot of a flower:
        - add score, length and spawn next flower */
        if ((flowerX[xpos] == beeXlength[0] && flowerY[ypos] == beeYlength[0])) {
            score++;
            beelength++;
            xpos = random.nextInt(31);
            ypos = random.nextInt(20);
        }
        if (beelength % 2 == 0) {
            flower = new ImageIcon("./img/flower.png");
            flower.paintIcon(this, g, flowerX[xpos], flowerY[ypos]);
        } else if (beelength % 3 == 0 || beelength % 5 == 0) {
            flower = new ImageIcon("./img/flower2.png");
            flower.paintIcon(this, g, flowerX[xpos], flowerY[ypos]);
        } else
            flower = new ImageIcon("./img/flower3.png");
        flower.paintIcon(this, g, flowerX[xpos], flowerY[ypos]);
    }

    private void gameOver(Graphics g) {
        for (int b = 1; b < beelength; b++) {
            if (beeXlength[b] == beeXlength[0] && beeYlength[b] == beeYlength[0]) {
                right = false;
                left = false;
                up = false;
                down = false;

                setGameOverBorderBackgroundAndMessage(g);
            }
        }
    }

    private void setGameOverBorderBackgroundAndMessage(Graphics g) {
        clip.close();
        gameOverSound();
        setGameOverBorder(g);
        setGameOverBackground(g);
        gameOverMessageDisplay(g);
    }

    private void setGameOverBorder(Graphics g) {
        g.setColor(Color.black);
        g.drawRect(284, 299, 331, 121);
    }

    private void setGameOverBackground(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(285, 300, 330, 120);
    }

    private void gameOverMessageDisplay(Graphics g) {
        g.setColor(Color.lightGray);
        g.setFont(new Font("arial", Font.BOLD, 50));
        g.drawString("Game Over", 315, 360);

        g.setFont(new Font("arial", Font.BOLD, 20));
        g.drawString("Press SPACE to restart", 335, 395);
    }

    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (right) {
            for (int r = beelength - 1; r >= 0; r--) {
                beeYlength[r + 1] = beeYlength[r];
            }
            for (int r = beelength; r >= 0; r--) {
                if (r == 0) {
                    beeXlength[r] = beeXlength[r] + 25;
                } else {
                    beeXlength[r] = beeXlength[r - 1];
                }
                if (beeXlength[r] > 850) {
                    beeXlength[r] = 25;
                }
            }
            repaint();
        }
        if (left) {
            for (int r = beelength - 1; r >= 0; r--) {
                beeYlength[r + 1] = beeYlength[r];
            }
            for (int r = beelength; r >= 0; r--) {
                if (r == 0) {
                    beeXlength[r] = beeXlength[r] - 25;
                } else {
                    beeXlength[r] = beeXlength[r - 1];
                }
                if (beeXlength[r] < 25) {
                    beeXlength[r] = 850;
                }
            }
            repaint();
        }
        if (up) {
            for (int r = beelength - 1; r >= 0; r--) {
                beeXlength[r + 1] = beeXlength[r];
            }
            for (int r = beelength; r >= 0; r--) {
                if (r == 0) {
                    beeYlength[r] = beeYlength[r] - 25;
                } else {
                    beeYlength[r] = beeYlength[r - 1];
                }
                if (beeYlength[r] < 75) {
                    beeYlength[r] = 625;
                }
            }
            repaint();
        }
        if (down) {
            for (int r = beelength - 1; r >= 0; r--) {
                beeXlength[r + 1] = beeXlength[r];
            }
            for (int r = beelength; r >= 0; r--) {
                if (r == 0) {
                    beeYlength[r] = beeYlength[r] + 25;
                } else {
                    beeYlength[r] = beeYlength[r - 1];
                }
                if (beeYlength[r] > 625) {
                    beeYlength[r] = 75;
                }
            }
            repaint();
        }
    }

    public void keyPressed(KeyEvent e) {
        handleKeySpace(e);
        handleKeyRight(e);
        handleKeyLeft(e);
        handleKeyUp(e);
        handleKeyDown(e);
    }

    private void handleKeySpace(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            moves = 0;
            score = 0;
            beelength = 1;
            repaint();
            backgroundMusic();
        }
    }

    private void handleKeyRight(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            moves++;
            if (!left) {
                right = true;
            }
//            else {
//                right = false;
//                left = true;
            up = false;
            down = false;
        }
    }

    private void handleKeyLeft(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            moves++;
            if (!right) {
                left = true;
            }
//            else {
//                left = false;
//                right = true;
//            }
            up = false;
            down = false;
        }
    }

    private void handleKeyUp(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            moves++;
            if (!down) {
                up = true;
            }
//            else {
//                up = false;
//                down = true;

            left = false;
            right = false;
        }
    }

    private void handleKeyDown(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            moves++;
            if (!up) {
                down = true;
            }
//            else {
//                up = false;
//                down = true;
            left = false;
            right = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}

