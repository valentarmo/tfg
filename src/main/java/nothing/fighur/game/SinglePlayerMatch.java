package nothing.fighur.game;

import java.io.IOException;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.*;
import com.googlecode.lanterna.input.*;
import com.googlecode.lanterna.terminal.*;

public class SinglePlayerMatch extends Match
{
    public SinglePlayerMatch(Terminal terminal, TextGraphics painter, Player p1, AI ai)
    {
        super(terminal, painter, p1, ai);
    }

    @Override
    protected void processKeyStrokes() throws IOException
    {
        /* Player 1 -------------- */
        KeyStroke keyStroke = terminal.pollInput();
        if (keyStroke != null) {
            KeyType keyType = keyStroke.getKeyType();
            if (keyType == KeyType.Character) {
                char key = keyStroke.getCharacter();
                switch (key) {
                case 'w':
                    p1Jump();
                    break;
                case 'd':
                    p1MoveRight();
                    break;
                case 'a':
                    p1MoveLeft();
                    break;
                case 'v':
                    player1.setStance(Player.Stance.Blocking);
                    break;
                case 'c':
                    player1.setStance(Player.Stance.Attacking);
                    newAttack(player1);
                    break;
                default:
                    break;
                }
            }
        }

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