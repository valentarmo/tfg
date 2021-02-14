package nothing.fighur.game;

import java.io.IOException;

import com.googlecode.lanterna.graphics.*;
import com.googlecode.lanterna.terminal.*;

public class SinglePlayerMatch extends Match
{
    public SinglePlayerMatch(Terminal terminal, TextGraphics painter, Player p1, AI ai)
    {
        super(terminal, painter, p1, ai);
    }

    @Override
    protected void processKeyStrokes()
    {
        /* Player 1 -------------- */
        if (p1jumping) p1Jump();
        if (p1mright) p1MoveRight();
        if (p1mleft) p1MoveLeft();
        if (player1.isAttacking()) newAttack(player1);

        /* AI ----------------- */
        AI tmp = (AI) player2;
        char aiMove = tmp.move();
        switch (aiMove) {
            case 'i':
                p2Jump();
                break;
            case 'l':
                p2MoveRight();
                break;
            case 'j':
                p2MoveLeft();
                break;
            case 'b':
                player2.setStance(Player.Stance.Blocking);
                break;
            case 'n':
                player2.setStance(Player.Stance.Attacking);
                newAttack(player2);
                break;
            default:
                break;
        }
    }
}