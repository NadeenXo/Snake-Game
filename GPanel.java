/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;
import jdk.nashorn.api.tree.BreakTree;

/**
 *
 * @author nadee
 */
public class GPanel extends JPanel implements ActionListener {

    private static final int SCREEN_WIDTH = 600;
    private static final int SCREEN_HIGHT = 600;
    private static final int UNIT_SIZE = 30;
    private static final int GAME_UNIT = (SCREEN_WIDTH * SCREEN_HIGHT) / UNIT_SIZE;
    private static int DELAY = 150;
    private final int x[] = new int[GAME_UNIT];
    private final int y[] = new int[GAME_UNIT];
    private int bodyParts = 6;
    private int applesEaten;
    private int appleX;
    private int appleY;
    private int wall1X;
    private int wall1Y;
    private int wall2X;
    private int wall2Y;
    private int wall3X;
    private int wall3Y;
    private char direction = 'R';
    private boolean running = false;
    private Color c = Color.GREEN;

    private Timer timer;
    private Random random;

    public GPanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HIGHT));
        this.setBackground(Color.DARK_GRAY);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();

    }

    public void startGame() {

        newApple();
        newWall();
        running = true;
        switch (User.choose[1]) {
            case "slow":
                DELAY = 150;
                break;
            case "medium":
                DELAY = 125;
                break;
            case "fast":
                DELAY = 100;
                break;
        }
        timer = new Timer(DELAY, this);
        timer.start();

    }

    public void newApple() {
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void newWall() {
        wall1X = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        wall1Y = random.nextInt((int) (SCREEN_HIGHT / UNIT_SIZE)) * UNIT_SIZE;

        wall2X = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        wall2Y = random.nextInt((int) (SCREEN_HIGHT / UNIT_SIZE)) * UNIT_SIZE;

        wall3X = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        wall3Y = random.nextInt((int) (SCREEN_HIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (running) {
            for (int i = 0; i < SCREEN_HIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HIGHT);

                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
                g.setColor(Color.darkGray);
            }
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            switch (User.choose[2]) {
                case "Without Walls":
                    break;
                case "With Walls":
                    g.setColor(Color.BLUE);
                    g.fillRect(wall1X, wall1Y, UNIT_SIZE, UNIT_SIZE);
                    g.fill3DRect(wall2X, wall2Y, UNIT_SIZE, UNIT_SIZE, true);
                    g.fill3DRect(wall3X, wall3Y, UNIT_SIZE, UNIT_SIZE, false);
                    break;
            }
            //draw snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.WHITE);
                    g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    g.drawString(User.user_Name, appleX, appleY);

                } else {
                    g.setColor(c);
                    switch (User.player_Color) {
                        case "Green":
                            c = Color.GREEN;
                            break;
                        case "Cyan":
                            c = Color.cyan;
                            break;
                        case "Orange":
                            c = Color.ORANGE;
                            break;
                        case "colorful":
                            c = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
                            break;
                    }
                    g.setColor(c);
                    g.fillOval(x[i], y[i], UNIT_SIZE - 3, UNIT_SIZE - 3);
                }
            }
        } else {
            gameOver(g);
            //new GFrame(); 
        }
    }

    public void move() {

        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {

        //eat the apple
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
            newWall();
        }
        //eat thr wall
        if (((x[0] == wall1X) && (y[0] == wall1Y)) || ((x[0] == wall2X) && (y[0] == wall2Y)) || ((x[0] == wall3X) && (y[0] == wall3Y))) {
            bodyParts--;
            newApple();
            newWall();
        }
        if (bodyParts <= 2) {
            running = false;
        }

    }

    public void checkCollision() {
        //eat itself
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        if ("infinite mode".equals(User.choose[3])) {
            //right
            if (x[0] > SCREEN_WIDTH) {
                x[0] = 0;
            }
            //left
            if (x[0] < 0) {
                x[0] = SCREEN_WIDTH;
            }
            //top
            if (y[0] < 0) {
                y[0] = SCREEN_HIGHT;
            }
            //if head touches Down B
            if (y[0] > SCREEN_HIGHT) {
                y[0] = 0;
            }
        } else {
            //if head touches Right B
            if (x[0] > SCREEN_WIDTH) {
                running = false;
            }
            //if head touches Left B
            if (x[0] < 0) {
                running = false;
            }
            //if head touches Top B
            if (y[0] < 0) {
                running = false;
            }
            //if head touches Down B
            if (y[0] > SCREEN_HIGHT) {
                running = false;
            }
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {

        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 60));
        g.drawString("Game Over", SCREEN_WIDTH / 5, SCREEN_HIGHT / 2);
        //score 
        g.setColor(Color.GREEN);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        g.drawString("Score: " + applesEaten, SCREEN_WIDTH / 3, SCREEN_HIGHT / 3);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollision();
        }
        repaint();

    }

    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }

        }
    }
}
