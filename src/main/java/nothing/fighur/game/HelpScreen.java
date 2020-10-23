package nothing.fighur.game;

import java.io.IOException;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.*;
import com.googlecode.lanterna.input.*;
import com.googlecode.lanterna.terminal.*;

/**
 * This class is responsible for displaying
 * help to the user, by help I mean how to play,
 * and by how to play I mean, the controls for
 * each player
 */
public class HelpScreen
{
    private Terminal terminal;
    private TextGraphics painter;

    /**
     * Get the painter and the terminal
     * @param terminal terminal in which to display the text
     * @param painter the object responsible to draw the text
     */
    public HelpScreen(Terminal terminal, TextGraphics painter)
    {
        this.terminal = terminal;
        this.painter = painter;
    }

    /**
     * Put the help on the screen
     */
    public void display() throws IOException
    {
        TerminalSize tsize = terminal.getTerminalSize();
        int trows = tsize.getRows();

        String row1 = "Action:          Player 1:          Player2:";
        String row2 = "Move Left:       a                  j";
        String row3 = "Move Right:      d                  l";
        String row4 = "Jump:            w                  i";
        String row5 = "Block:           v                  b";
        String row6 = "Attack:          c                  n";
        String footer = "Press ESC to go back";

        terminal.clearScreen();

        painter.putString(0, 1, row1);
        painter.putString(0, 2, row2);
        painter.putString(0, 3, row3);
        painter.putString(0, 4, row4);
        painter.putString(0, 5, row5);
        painter.putString(0, 6, row6);
        painter.putString(0, trows-1, footer);

        terminal.flush();

        /* Go back to the main menu */
        while (true) {
            KeyStroke keyStroke = terminal.readInput();
            if (keyStroke.getKeyType() == KeyType.Escape)
                return;
        }
    }
}