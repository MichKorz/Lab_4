package client;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.awt.*;

public class GUIController
{
    // 192.168.1.10 59001
    public Client client;

    public int playerCount;
    public String variant;
    public Button endTurnButton;
    public String boardString;

    @FXML
    public TextField yourTurnField;

    @FXML
    private GridPane boardPane;

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField chatInput;

    String highlight = "";
    Point initialPoint = null;

    public void generateBoard()
    {
        yourTurnField.setVisible(false);
        boardPane.setAlignment(Pos.CENTER);
        boardPane.setVgap(5); //5
        boardPane.setHgap(-10); //-10

        String[] splitBoard = boardString.split(" ");
        Point[] boardCoords = new Point[splitBoard.length/2]; // piece, owner

        for (int i = 0; i < splitBoard.length; i += 2) {
            boardCoords[i/2] = new Point(Integer.parseInt(splitBoard[i]), Integer.parseInt(splitBoard[i+1]));
        }

        for (int i = 0; i < boardCoords.length; i++)
        {
            int piece = boardCoords[i].x;
            int owner = boardCoords[i].y;
            if (owner != 9) {
                Circle circle = new Circle(15, getColor(piece));
                if(!variant.equals("Chaos")) circle.setStroke(getColor(piece));
                circle.setStrokeWidth(2);
                circle.setOnMouseClicked(event -> circlePressed(circle));
                boardPane.add(circle, i%25, i/25);
            }
        }
    }

    public void setYourTurnField(boolean value)
    {
        yourTurnField.setVisible(value);
    }

    public void highlightCircles(String coordinates)
    {
        System.out.println("DEBUG coordinates = " + coordinates);
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
            if(circle != null) circle.setEffect(dropShadow);
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
