package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import server.Board;
import server.BoardChaos;
import server.BoardClassic;
import server.Tile;

import java.awt.*;

public class GUIController
{
    // 192.168.1.10 59001
    public Client client;

    public int playerCount;
    public String variant;
    public Button endTurnButton;

    @FXML
    public TextField yourTurnField;

    @FXML
    private GridPane boardPane;

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField chatInput;

    String highlight = "";
    Board board;
    Point initialPoint = null;


    public void generateBoard()
    {
        if(variant.equals("Chaos")) board = new BoardChaos(playerCount);
            else board = new BoardClassic(playerCount);
        yourTurnField.setVisible(false);
        boardPane.setAlignment(Pos.CENTER);
        boardPane.setVgap(5);
        boardPane.setHgap(-10);
        for (int row = 0; row < 17; row++) {
            for (int col = 0; col < 25; col++) {
                Tile tile = board.GetTileAt(row, col);
                if (tile.getOwner() != 9) {
                    Circle circle = new Circle(15, getColor(tile.getPiece()));
                    if(!variant.equals("Chaos")) circle.setStroke(getColor(tile.getPiece()));
                    circle.setStrokeWidth(2);
                    circle.setOnMouseClicked(event -> circlePressed(circle));
                    boardPane.add(circle, col, row);
                }
            }
        }
    }

    public void setYourTurnField(boolean value)
    {
        yourTurnField.setVisible(value);
    }

    public void highlightCircles(String coordinates)
    {
        highlight = coordinates;

        String[] splitMove = coordinates.split(" ");
        int[] coords = new int[splitMove.length];

        for (int i = 0; i < splitMove.length; i++) {
            coords[i] = Integer.parseInt(splitMove[i]);
        }

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(11);
        dropShadow.setOffsetX(1);
        dropShadow.setOffsetY(1);
        for (int i = 0; i < splitMove.length; i+=2){
            Circle circle = getCircleAt(coords[i], coords[i+1]);
            circle.setEffect(dropShadow);
        }
    }

    public void unlightCircles()
    {
        if (highlight.isEmpty()) return;
        String[] splitMove = highlight.split(" ");
        int[] coords = new int[splitMove.length];

        for (int i = 0; i < splitMove.length; i++) {
            coords[i] = Integer.parseInt(splitMove[i]);
        }

        for (int i = 0; i < splitMove.length; i+=2){
            Circle circle = getCircleAt(coords[i], coords[i+1]);
            if(circle != null) circle.setEffect(null);
        }

        highlight = "";
    }

    public void movePiece(int initialX, int initialY, int finalX, int finalY)
    {
        unlightCircles();

        Circle fromCircle = getCircleAt(initialX, initialY);
        Circle toCircle = getCircleAt(finalX, finalY);

        if(fromCircle != null && toCircle != null)
        {
            Color color = (Color) fromCircle.getFill();
            fromCircle.setFill(toCircle.getFill());
            toCircle.setFill(color);
        }
    }

    private void circlePressed(Circle circle)
    {
        Point point = getCircleCoords(circle);
        int posX = point.x;
        int posY = point.y;
        if(isCircleHighlighted(posX, posY))
        {
            String message = String.format("/m_%d %d %d %d", initialPoint.x, initialPoint.y, posX, posY);
            client.sendMessage(message);
        }
        else
        {
            initialPoint = point;
            unlightCircles();

            String message = String.format("/h_%d %d", posX, posY);
            client.sendMessage(message);
        }
    }

    private Boolean isCircleHighlighted(int posX, int posY)
    {
        if(highlight.isEmpty()) return false;

        String[] splitMove = highlight.split(" ");
        int[] coords = new int[splitMove.length];

        for (int i = 0; i < splitMove.length; i++) {
            coords[i] = Integer.parseInt(splitMove[i]);
        }

        for (int i = 0; i < splitMove.length; i+=2){
            if( posX == coords[i] && posY == coords[i+1])
                return true;
        }

        return false;
    }

    private Circle getCircleAt(int row, int col)
    {
        for (var child : boardPane.getChildren()) {
            Integer childCol = GridPane.getColumnIndex(child);
            Integer childRow = GridPane.getRowIndex(child);
            if ((childCol == null ? 0 : childCol) == col && (childRow == null ? 0 : childRow) == row) {
                return (Circle) child;
            }
        }
        return null;
    }

    private Point getCircleCoords(Circle circle)
    {
        Integer circleCol = GridPane.getColumnIndex(circle);
        Integer circleRow = GridPane.getRowIndex(circle);
        return new Point(circleRow, circleCol);
    }

    private Color getColor(int player)
    {
        return switch (player) {
            case 0 -> Color.GRAY;
            case 1 -> Color.RED;
            case 2 -> Color.BLUE;
            case 3 -> Color.GREEN;
            case 4 -> Color.YELLOW;
            case 5 -> Color.PURPLE;
            case 6 -> Color.ORANGE;
            default -> Color.BLACK;
        };
    }

    public void endTurnButton()
    {
        client.sendMessage("/e");
    }

    public void printChatMessage(String message)
    {
        chatArea.appendText(message+"\n");
    }
}
