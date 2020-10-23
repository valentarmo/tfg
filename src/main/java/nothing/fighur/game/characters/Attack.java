package nothing.fighur.game.characters;

import nothing.fighur.game.Player;

/**
 * Fighters' attacks
 */
public class Attack
{
    private char shape;
    private int row;
    private int col;
    private boolean direction;
    private long timeSinceLastMoved;
    private Player attacker;

    public Attack(Player attacker, char shape, int row, int col, boolean direction, long creationTime)
    {
        this.shape = shape;
        this.row = row;
        this.col = col;
        this.direction = direction;
        this.attacker = attacker;
    }

    public char getShape()
    { return shape; }

    /* Attacks will only move horizontally so the row shoudn't change */
    public int getRow()
    { return row; }

    public int getColumn()
    { return col;}

    public long getTimeSinceLastMoved()
    { return timeSinceLastMoved; }

    public boolean getDirection()
    { return direction; }

    public Player getAttacker()
    { return attacker; }

    public void setColumn(int col)
    { this.col = col; }

    public void setTimeSinceLastMoved(long time)
    { timeSinceLastMoved = time; }
}