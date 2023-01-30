package controller;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import pieces.*;
import board.Board;
// .Player;

public class UIController extends JPanel implements MouseListener, MouseMotionListener {
    private static final long serialVersionUID = 1L;
    Piece[][] board;
    static int oldMouseX, oldMouseY, newMouseX, newMouseY;
    static int squareSize = 87;
    Colour currentPlayer;
    boolean selected = false;
    int selectedX = -1, selectedY = -1;
    boolean[][] validMoves = new boolean[8][8];
    
    
    public UIController() {
        currentPlayer = Colour.WHITE;
        board = Board.getBoard();
        this.addMouseListener(this);
    }
    

    // renders the board    
    public void paintComponent(Graphics g) {
    	
        super.paintComponent(g);
        for (int i = 0; i < 64; i += 2) {
            g.setColor(new Color(250, 250, 250));
            g.fillRect((i % 8 + (i / 8) % 2) * squareSize, (i / 8) * squareSize, squareSize, squareSize);
            g.setColor(new Color(133, 94, 66));
            g.fillRect(((i + 1) % 8 - ((i + 1) / 8) % 2) * squareSize, ((i + 1) / 8) * squareSize, squareSize, squareSize);
        }
        Image chessPiecesImage = new ImageIcon(this.getClass().getResource("pieces_resized.png")).getImage();
        Image test = new ImageIcon(this.getClass().getResource("dot3.png")).getImage();


        for (int i = 0; i < 64; i++) {
            int x = -1;
            int y = -1;
            Piece p = board[i / 8][i % 8];
            if (selected && validMoves[i / 8][i % 8]) 
            	g.drawImage(test, (i % 8) * squareSize, (i / 8) * squareSize, squareSize, squareSize , getFocusCycleRootAncestor());
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
            if (x != -1 && y != -1) {
                g.drawImage(chessPiecesImage, (i % 8) * squareSize, (i / 8) * squareSize, (i % 8 + 1) * squareSize, (i / 8 + 1) * squareSize, x * 64, y * 64, (x + 1) * 64, (y + 1) * 64, this);
            }
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
        if (e.getX() < 8 * squareSize && e.getY() < 8 * squareSize) { //if mouse is moved inside the chess board
            newMouseX = e.getX();
            newMouseY = e.getY();
        }

        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (selected) {
                int newMouseX = e.getX() / squareSize;
                int newMouseY = e.getY() / squareSize;
                selected = !selected;
                System.out.println("make the move");
//    		 // make move here
                boolean isMoveMade = Board.makeMove(selectedY, selectedX, newMouseY, newMouseX, currentPlayer);
                if (isMoveMade) {
                    currentPlayer = currentPlayer == Colour.WHITE ? Colour.BLACK : Colour.WHITE;
                } else {
                    System.out.println("Invalid move");
                }

                // reset valid moves
                for (int i = 0; i < 8; i++)
                    for (int j = 0; j < 8; j++)
                        validMoves[i][j] = false;

                // reprint the board
                repaint();
                return;
            }
            selectedX = e.getX() / squareSize;
            selectedY = e.getY() / squareSize;
            if (board[selectedY][selectedX] != null && board[selectedY][selectedX].color == currentPlayer) {
                selected = !selected;
                int[][] moves = Board.getValidMoves(selectedY, selectedX, currentPlayer);
                for (int[] move: moves) {
                    validMoves[move[0]][move[1]] = true;
                    System.out.println(Arrays.toString(move));
                }
            }
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
//        if (e.getButton() == MouseEvent.BUTTON1) {
//            if (e.getX() < 8 * squareSize && e.getY() < 8 * squareSize) { //if mouse is released inside the chess board
//                newMouseX = e.getX() / squareSize;
//                newMouseY = e.getY() / squareSize;
//                // make move here
//                boolean isMoveMade = Board.makeMove(oldMouseY, oldMouseX, newMouseY, newMouseX, currentPlayer);
//                if (isMoveMade) {
//                    currentPlayer = currentPlayer == Colour.WHITE ? Colour.BLACK : Colour.WHITE;
//                } else {
//                    System.out.println("Invalid move");
//                }
//                oldMouseX = -1;
//                oldMouseY = -1;
//                newMouseX = -1;
//                newMouseY = -1;
//                repaint();
//            }
//        }
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