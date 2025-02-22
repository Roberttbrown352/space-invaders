import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SpaceInvaders extends JPanel implements ActionListener, KeyListener{
    class Block {
        int x;
        int y;
        int width;
        int height;
        Image img;
        boolean alive = true;
        boolean used = false;

        Block(int x, int y, int width, int height, Image img){
            this.x = x;
            this.y = y;
            this.width = width;
            this. height = height;
            this.img = img;
        }
    }

    // Board Variables
    int tileSize = 32;
    int rows = 16;
    int columns = 16;
    int boardWidth = tileSize * columns;
    int boardHeight = tileSize * rows;

    // Image variables
    Image shipImg;
    Image alienImg;
    Image alienCyanImg;
    Image alienMagentaImg;
    Image alienYellowImg;
    ArrayList<Image> alienImgArray;

    // Ship Variables
    int shipWidth = tileSize * 2;
    int shipHeight = tileSize;
    int shipX = tileSize * columns / 2 - tileSize;
    int shipY = boardHeight - tileSize * 2;
    int shipVelocityX = tileSize;
    Block ship;

    // Alien Variables
    ArrayList<Block> alienArray;
    int alienWidth = tileSize * 2;
    int alienHeight = tileSize;
    int alienX = tileSize;
    int alienY = tileSize;
    int alienVelocityX = 1;

    int alienRows = 2;
    int alienColumns = 3;
    int alienCount = 0;

    // Bullet Variables
    ArrayList<Block> bulletArray;
    int bulletWidth = tileSize / 8;
    int bulletHeight = tileSize / 2;
    int bulletVelocityY = -10;

    Timer gameLoop;
    int score = 0;
    boolean gameOver = false;

    SpaceInvaders(){
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(this);

        // Load images
        shipImg = new ImageIcon(getClass().getResource("./images/ship.png")).getImage();
        alienImg = new ImageIcon(getClass().getResource("./images/alien.png")).getImage();
        alienCyanImg = new ImageIcon(getClass().getResource("./images/alien-cyan.png")).getImage();
        alienMagentaImg = new ImageIcon(getClass().getResource("./images/alien-magenta.png")).getImage();
        alienYellowImg = new ImageIcon(getClass().getResource("./images/alien-yellow.png")).getImage();

        alienImgArray = new ArrayList<Image>();
        alienImgArray.add(alienImg);
        alienImgArray.add(alienCyanImg);
        alienImgArray.add(alienMagentaImg);
        alienImgArray.add(alienYellowImg);

        ship = new Block(shipX, shipY, shipWidth, shipHeight, shipImg);
        alienArray = new ArrayList<Block>();
        bulletArray = new ArrayList<Block>();

        gameLoop = new Timer(1000/60, this);
        createAliens();
        gameLoop.start();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        g.drawImage(ship.img, ship.x, ship.y, ship.width, ship.height, null);

        for(int i = 0; i < alienArray.size(); i++){
            Block alien = alienArray.get(i);
            if(alien.alive){
                g.drawImage(alien.img, alien.x, alien.y, alien.width, alien.height, null);
            }
        }

        g.setColor(Color.white);

        for(int i = 0; i < bulletArray.size(); i++){
            Block bullet = bulletArray.get(i);

            if (!bullet.used){
                g.fillRect(bullet.x, bullet.y, bullet.width, bullet.height);
            }
        }

        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if(gameOver){
            g.drawString("Game Over: " + String.valueOf(score), 10, 35);
        }
        else{
            g.drawString(String.valueOf(score), 10, 35);
        }
    }

    public void move(){
        for(int i = 0; i < alienArray.size(); i++){
            Block alien = alienArray.get(i);
            if(alien.alive){
                alien.x += alienVelocityX;

                // Alien border collision check
                if(alien.x + alienWidth >= boardWidth || alien.x <= 0){
                    alienVelocityX *= -1;
                    alien.x += alienVelocityX * 2;

                    for(int j = 0; j < alienArray.size(); j++){
                        alienArray.get(j).y += alienHeight;
                    }
                }
            }

            // Alien ship collision check
            if(alien.alive && alien.y >= ship.y){
                gameOver = true;
            }
        }

        for(int i = 0; i < bulletArray.size(); i++){
            Block bullet = bulletArray.get(i);
            bullet.y += bulletVelocityY;

            for(int j = 0; j < alienArray.size(); j++){
                Block alien = alienArray.get(j);
                if(!bullet.used && alien.alive && detectCollision(bullet, alien)){
                    bullet.used = true;
                    alien.alive = false;
                    alienCount--;
                    score += 100;
                }
            }
        }

        // Bullet Cleanup
        while (!bulletArray.isEmpty() && (bulletArray.get(0).used || bulletArray.get(0).y < 0)){
            bulletArray.remove(0);
        }

        // Next Level
        if (alienCount == 0){
            score += alienColumns * alienRows * 100;
            alienColumns = Math.min(alienColumns + 1, columns/2 - 2);
            alienRows = Math.min(alienRows + 1, rows - 6);
            alienVelocityX = 1;
            alienArray.clear();
            bulletArray.clear();
            createAliens();
        }
    }

    public void createAliens(){
        Random random = new Random();
        for (int r = 0; r < alienRows; r++){
            for (int c = 0; c < alienColumns; c++){
                int randomImageIndex = random.nextInt(alienImgArray.size());
                Block alien = new Block(
                        alienX + c * alienWidth,
                        alienY + r * alienHeight,
                        alienWidth,
                        alienHeight,
                        alienImgArray.get(randomImageIndex)
                    );
                alienArray.add(alien);
            }
        }
        alienCount = alienArray.size();
    }

    public boolean detectCollision(Block a, Block b){
        return a.x < b.x + b.width &&
               a.x + a.width > b.x &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e){}

    @Override
    public void keyPressed(KeyEvent e){}

    @Override
    public void keyReleased(KeyEvent e){
        if(gameOver){
            ship.x = shipX;
            alienArray.clear();
            bulletArray.clear();
            score = 0;
            alienVelocityX = 1;
            alienColumns = 3;
            alienRows = 2;
            gameOver = false;
            createAliens();
            gameLoop.start();
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT && ship.x - shipVelocityX >= 0){
            ship.x -= shipVelocityX;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship.x + ship.width + shipVelocityX <= boardWidth){
            ship.x += shipVelocityX;
        }
        else if (e.getKeyCode() == KeyEvent.VK_SPACE){
            Block bullet = new Block(ship.x + shipWidth * 15 / 32, ship.y, bulletWidth, bulletHeight, null);
            bulletArray.add(bullet);
        }
    }
}
