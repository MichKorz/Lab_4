public class Tile
{
    private final int owner; // 1-6 domy graczy, 0 - puste pole planszy, 9 - nie nalezy do gwiazdy
    private final int piece; // 0 - bez pionka, 1-6 pionki graczy

    public Tile(int owner, int piece)
    {
        this.owner = owner;
        this.piece = piece;
    }

    public int getOwner()
    {
        return owner;
    }

    public int getPiece()
    {
        return piece;
    }
}