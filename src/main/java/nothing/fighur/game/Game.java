package nothing.fighur.game;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.*;
import com.googlecode.lanterna.input.*;
import com.googlecode.lanterna.terminal.*;

import java.io.IOException;

/**
 * This class contains the main loop of the game
 * i.e. the main menu loop, and handles most of
 * the exceptions
 */
public class Game
{
    private final Terminal terminal = createTerminal();
    private final TextGraphics painter = createPainter();
    private final String[] options = { "SINGLE PLAYER", "MULTIPLAYER", "HELP", "EXIT" };
    private int currentOption = 0;

    /**
     * Start the game, this means, display the main menu,
     * and listen to the user to determine what happens
     * next
     */
    public void start()
    {
        try {
            /* Ready the screen */
            terminal.enterPrivateMode();
            terminal.setCursorVisible(false);
            terminal.clearScreen();

            KeyStroke keyStroke;
            int numberOfOptions = options.length;

            while (true) {
                terminal.clearScreen();
                displayMenu();
                terminal.flush();

                /* Now start listening to the user */
                keyStroke = terminal.readInput();
                KeyType keyType = keyStroke.getKeyType();

                switch (keyType) {
                    case ArrowUp:
                        if (currentOption > 0)
                            currentOption--;
                        break;
                    case ArrowDown:
                        if (currentOption < numberOfOptions-1)
                            currentOption++;
                        break;
                    case Enter:
                        String option = options[currentOption];
                        switch (option) {
                            case "MULTIPLAYER": {
                                SelectionScreen selectionScreen
                                        = new SelectionScreen(terminal, painter);
                                Player[] players = selectionScreen.display(false);
                                if (players != null) {
                                    Player p1 = players[0];
                                    Player p2 = players[1];
                                    Match match
                                            = new Match(terminal, painter, p1, p2);
                                    match.begin();
                                }
                                break;
                            }
                            case "SINGLE PLAYER": {
                                SelectionScreen selectionScreen
                                        = new SelectionScreen(terminal, painter);
                                Player[] players = selectionScreen.display(true);
                                if (players != null) {
                                    Player p1 = players[0];
                                    AI ai = (AI) players[1];
                                    Match match
                                            = new SinglePlayerMatch(terminal, painter, p1, ai);
                                    match.begin();
                                }
                                break;
                            }
                            case "HELP":
                                HelpScreen helpScreen
                                        = new HelpScreen(terminal, painter);
                                helpScreen.display();
                                break;
                            default:
                                return;
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                terminal.exitPrivateMode();
                terminal.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Create the main menu
     */
    private void displayMenu() throws IOException
    {
        TerminalSize terminalSize = terminal.getTerminalSize();
        int col = terminalSize.getColumns() / 2;
        int row = terminalSize.getRows() / 2;

        painter.putString(col - 2, row - 2, "TFG");

        for (int i = 0; i < options.length; i++) {
            if (i == currentOption) /* Highlight where the user is currently at */
                painter.setBackgroundColor(TextColor.ANSI.BLUE);

            int offset = options[i].length() / 2;

            painter.putString(col - offset, row + i, options[i]);
            painter.setBackgroundColor(TextColor.ANSI.DEFAULT);
        }
    }

    /**
     * Create the terminal abstraction on top of
     * which the game will run
     * @return The newly created terminal
     */
    private Terminal createTerminal()
    {
        try {
            return new DefaultTerminalFactory().createTerminal();
        } catch (IOException e) {
            System.err.println("tfg couldn't initialize");
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    /**
     * Create the object responsible for drawing
     * everything to the screen
     * @return TextGraphics -> The "painter"
     */
    private TextGraphics createPainter()
    {
        try {
            return terminal.newTextGraphics();
        } catch (IOException e) {
            System.err.println("tfg couldn't initialize");
            e.printStackTrace();
            System.exit(2);
            return null;
        }
    }
}
