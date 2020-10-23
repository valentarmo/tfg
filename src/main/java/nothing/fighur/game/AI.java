package nothing.fighur.game;

import java.util.Random;

import nothing.fighur.game.characters.Character;

/**
 * A very primitive AI
 */
public class AI extends Player
{
    private final Random mind = new Random(System.currentTimeMillis());
    private int thinkingTime = 200; /* Move every 200 ms */
    private long timeSinceLastMove = 0;

    /* It will have player 2's controls */
    private final char[] moves = {
        'i', 'i',
        'j', 'j', 'j', 'j', 'j',
        'l', 'l', 'l', 'l', 'l',
        'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n',
        'b',
    };

    public AI(Character fighter, boolean facingDirection)
    {
        super(fighter, facingDirection);
    }

    public char move()
    {
        long currentTime = System.currentTimeMillis();
        char move = '\u0000';
        if (currentTime - timeSinceLastMove > thinkingTime) {
            timeSinceLastMove = currentTime;
            move = moves[mind.nextInt(moves.length)];
        }
        return move;
    }
}