import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class SpaceInvaders extends JPanel {
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

    Block ship;

    SpaceInvaders() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.black);

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
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        g.drawImage(ship.img, ship.x, ship.y, ship.width, ship.height, null);
    }
}
