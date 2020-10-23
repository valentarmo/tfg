package nothing.fighur.game;

/**
 * tfg - terminal fighting game
 * This is the entry point of the program.
 * As you can see, there is not much to it,
 * it only creates a new game.
 */
public class App
{
    public static void main( String[] args )
    {
        Game tfg = new Game();
        tfg.start();
    }
}
