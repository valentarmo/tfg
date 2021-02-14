package nothing.fighur.game;

import java.io.IOException;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.*;
import com.googlecode.lanterna.input.*;
import com.googlecode.lanterna.terminal.*;

import nothing.fighur.game.characters.*;
import nothing.fighur.game.characters.Character;

/**
 * Character selection screen
 */
public class SelectionScreen
{
    private final Terminal terminal;
    private final TextGraphics painter;
    private final Character[] characters = new CharacterFactory().createAll();
    private int currentThumbnail = 0;

    public SelectionScreen(Terminal terminal, TextGraphics painter)
    {
        this.terminal = terminal;
        this.painter = painter;
    }

    /**
     * Display the character selection screen and create the players
     * @return Array of players A[0] -> player 1 A[1] -> Player 2
     *         null if any of the players cancels
     */
    public Player[] display(boolean withAI) throws IOException
    {
        Player[] players = new Player[2];

        terminal.clearScreen();

        KeyStroke keyStroke;

        boolean p1Done = false;
        boolean p2Done = false;

        /* Help to be displayed at the bottom of the screen */
        String p1Help
            = "Player 1: Pick a Fighter || <- -> Enter || ESC to cancel";
        String p2Help
            = "Player 2: Pick a Fighter || <- -> Enter || ESC to cancel";

        currentThumbnail = 0;

        while ( !(p1Done && p2Done) ) {
            /* Check whose turn to choose is */
            if (!p1Done)
                drawScreen(p1Help);
            else
                drawScreen(p2Help);

            /* Now listen to the current player decisions */
            keyStroke = terminal.readInput();

            if (keyStroke != null) {
                KeyType keyStrokeType = keyStroke.getKeyType();
                switch (keyStrokeType) {
                    case Escape:
                        return null;
                    case Enter:
                        if ( !p1Done ) {
                            players[0] = new Player(characters[currentThumbnail], true);
                            p1Done = true;
                            currentThumbnail = 0;
                        } else {
                            if (withAI)
                                players[1] = new AI(characters[currentThumbnail], false);
                            else
                                players[1] = new Player(characters[currentThumbnail], false);
                            p2Done = true;
                        }
                        break;
                    case ArrowLeft:
                        if (currentThumbnail > 0)
                            currentThumbnail--;
                        break;
                    case ArrowRight:
                        if (currentThumbnail < characters.length-1)
                            currentThumbnail++;
                        break;
                    default:
                        break;
                }
            }
        }
        return players;
    }

    /**
     * Draw the selection screen
     * @param pStatus this is the help message for each player
     *                displayed at the bottom of the screen
     */
    private void drawScreen(String pStatus) throws IOException
    {
        terminal.clearScreen();

        TerminalSize terminalSize = terminal.getTerminalSize();
        int cols = terminalSize.getColumns();
        int rows = terminalSize.getRows();

        String header = "Pick a Fighter";
        StringBuilder delimiter = new StringBuilder();

        for (int i = 0; i < cols; i++)
            delimiter.append("_");

        int headerDiff = header.length() / 2;

        painter.putString(cols/2 - headerDiff, 0, header);

        /* Draw thumbnails */
        int tcol = 1;
        int trow = 2;

        for (int i = 0; i < characters.length; i++) {
            if (i == currentThumbnail) /* Highlight the thumbnail the player's at */
                painter.setBackgroundColor(TextColor.ANSI.BLUE);

            Character cr = characters[i];
            String[] thumbnail = cr.thumbnail();

            /* Thumbnails are 4x5 but they don't include the frame,
            and we assume they are in descending order, i.e.
            thumbnail[0] is the top and thumbnail[3] is the bottom */
            painter.putString(tcol, trow, "|-----|");
            painter.putString(tcol, trow+1, "|" + thumbnail[0] + "|");
            painter.putString(tcol, trow+2, "|" + thumbnail[1] + "|");
            painter.putString(tcol, trow+3, "|" + thumbnail[2] + "|");
            painter.putString(tcol, trow+4, "|" + thumbnail[3] + "|");
            painter.putString(tcol, trow+5, "|-----|");

            painter.setBackgroundColor(TextColor.ANSI.DEFAULT);
            tcol += 7;
        }

        painter.putString(0, 1, delimiter.toString());
        painter.putString(0, rows-2, delimiter.toString());
        painter.putString(0, rows-1, pStatus);

        terminal.flush();
    }
}