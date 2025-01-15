package client;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import server.Board;
import server.BoardClassic;
import server.Tile;

import java.awt.*;

public class GUIController
{
    // 192.168.1.10 59001
    public Client client;

    @FXML
    private GridPane boardPane;

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField chatInput;

    @FXML
    private Button generateButton;

    String highlight = "13 9 12 10";
    Circle chosenCircle = null;
    Point initialPoint = new Point(13,9);
    Point finalPoint = null;
    Board board = new BoardClassic(6);

    public void highlightCircles(String coordinates)
    {
        //highlight = coordinates
    }

    public void unlightCircles()
    {
        //unlight
        //highlight = ""
    }

    public void movePiece(int initialX, int initialY, int finalX, int finalY) // initiaL X Y, FINAL X Y
    {

    }

    // onactionevent dla kolek

    public void generateBoard() {
        boardPane.setAlignment(Pos.CENTER);
        boardPane.setVgap(7);
        boardPane.setHgap(-3);
        for (int row = 0; row < 17; row++) {
            for (int col = 0; col < 25; col++) {
                Tile tile = board.GetTileAt(row, col);
                if (tile.getOwner() != 9) {
                    Circle circle = new Circle(12, getColor(tile.getPiece()));
                    circle.setStroke(getColor(tile.getPiece())); // DOES IT EVEN WORK???
                    circle.setOnMouseClicked(event -> circlePressed(circle));
                    boardPane.add(circle, col, row);
                }
            }
        }

        chosenCircle = getCircleAt(8,12);
        if (chosenCircle != null) {
            chosenCircle.setFill(Color.ORANGE); // Change its color
        }
    }

    private void circlePressed(Circle circle) {
        circle.setFill(Color.BLACK);
        if(highlight == "")
        {
            //client.sendMessage("");
            //highlight shit
        }
        else //if(myTurn)
        {
            finalPoint = getCircleCoords(circle);
            int finalX = finalPoint.x;
            int finalY = finalPoint.y;
            //VALIDATE MOVE() FROM GAME
            //client.sendMessage("\vxyxy");
            //if ValidateMove == true

            if(finalX == 0 && finalY == 0)
            {
                movePiece(initialPoint.x, initialPoint.y, finalPoint.x, finalPoint.y);
                //send to others
            }
        }
    }

    private Circle getCircleAt(int row, int col) {
        for (var child : boardPane.getChildren()) {
            Integer childCol = GridPane.getColumnIndex(child);
            Integer childRow = GridPane.getRowIndex(child);
            if ((childCol == null ? 0 : childCol) == col && (childRow == null ? 0 : childRow) == row) {
                return (Circle) child;
            }
        }
        return null; // No matching circle found
    }

    private Point getCircleCoords(Circle circle) {
        Integer childCol = GridPane.getColumnIndex(circle);
        Integer childRow = GridPane.getRowIndex(circle);
        return new Point(childCol, childRow);
    }

    private Color getColor(int player) {
        return switch (player) {
            case 0 -> Color.GRAY;
            case 1 -> Color.RED;
            case 2 -> Color.BLUE;
            case 3 -> Color.GREEN;
            case 4 -> Color.YELLOW;
            case 5 -> Color.PURPLE;
            case 6 -> Color.ORANGE;
            default -> Color.BLACK; // Default color
        };
    }

}
