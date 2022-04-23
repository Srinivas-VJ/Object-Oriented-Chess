import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import Pieces.Piece;
import Pieces.Pawn;
import Pieces.Colour;
import Pieces.Variant;
import Pieces.Rook;
import Pieces.Bishop;
import Pieces.King;
import Pieces.Queen;
import Pieces.Knight;
import java.util.Date;

public class testUI extends JPanel implements MouseListener, MouseMotionListener {
  Piece[][] board; 
  static int oldMouseX, oldMouseY, newMouseX, newMouseY;
  static int squareSize = 87;
  testUI() {
    board = Board.getBoard(); 
    this.addMouseListener(this);
  }
  public static void main(String args[]) {
    System.out.print("Death ");
  }
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    for (int i = 0; i < 64; i += 2) {
      g.setColor(new Color(250, 250, 250));
      g.fillRect((i % 8 + (i / 8) % 2) * squareSize, (i / 8) * squareSize, squareSize, squareSize);
      g.setColor(new Color(133, 94, 66));
      g.fillRect(((i + 1) % 8 - ((i + 1) / 8) % 2) * squareSize, ((i + 1) / 8) * squareSize, squareSize, squareSize);
    }
    Image chessPiecesImage;
    chessPiecesImage = new ImageIcon("pieces_resized.png").getImage();
    int x, y, x1 = -1, y1 = -1;

    for (int i = 0; i < 64; i++) {
      x = -1;
      y = -1;
      Piece p = board[i / 8][i % 8];
      if (p == null)
        continue;
      switch (p.type) {
      case KING:
        if (p.color == Colour.BLACK) {
          x = 0;
          y = 1;
        } else {
          x = 0;
          y = 0;
        }
        break;
      case BISHOP:
        if (p.color == Colour.BLACK) {
          x = 2;
          y = 1;
        } else {
          x = 2;
          y = 0;
        }
        break;
      case KNIGHT:
        if (p.color == Colour.BLACK) {
          x = 3;
          y = 1;
        } else {
          x = 3;
          y = 0;
        }
        break;
      case PAWN:
        if (p.color == Colour.BLACK) {
          x = 5;
          y = 1;
        } else {
          x = 5;
          y = 0;
        }
        break;
      case QUEEN:
        if (p.color == Colour.BLACK) {
          x = 1;
          y = 1;
        } else {
          x = 1;
          y = 0;
        }
        break;
      case ROOK:
        if (p.color == Colour.BLACK) {
          x = 4;
          y = 1;
        } else {
          x = 4;
          y = 0;
        }
        break;
      default:
        System.out.println(" ");
      }
      if (x != -1 && y != -1)
        g.drawImage(chessPiecesImage, (i % 8) * squareSize, (i / 8) * squareSize, (i % 8 + 1) * squareSize, (i / 8 + 1) * squareSize, x * 64, y * 64, (x + 1) * 64, (y + 1) * 64, this);
    }
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    if (e.getX() < 8 * squareSize && e.getY() < 8 * squareSize) { //if mouse is dragged inside the chess board
      newMouseX = e.getX();
      newMouseY = e.getY();
    }
  }

  @Override
  public void mouseMoved(MouseEvent e) {
  }

  @Override
  public void mouseClicked(MouseEvent e) {
  }

  @Override
  public void mousePressed(MouseEvent e) {
     if(e.getButton()==MouseEvent.BUTTON1)
      {
    if (e.getX() < 8 * squareSize && e.getY() < 8 * squareSize) { //if mouse is pressed inside the chess board
      oldMouseX = e.getX() / squareSize;
      oldMouseY = e.getY() / squareSize;
    }
  }
  }

  @Override
  public void mouseReleased(MouseEvent e) {
      if(e.getButton()==MouseEvent.BUTTON1)
      {
    		if(e.getX()<8*squareSize && e.getY()<8*squareSize){//if mouse is released inside the chess board
    			newMouseX=e.getX()/squareSize;
    			newMouseY=e.getY()/squareSize;
          // make move here
            Piece p;
            p = board[oldMouseY][oldMouseX];
            board[oldMouseY][oldMouseX] = null;
            board[newMouseY][newMouseX] = p;
            repaint();

        }
      }
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void mouseExited(MouseEvent e) {
    // TODO Auto-generated method stub

  }
}