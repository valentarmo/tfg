package nothing.fighur.game.characters;

/**
 * Interface every character in the game should follow
 * I regret the name, I would've preferred Fighter
 */
public interface Character
{
    /* --------------------------------------
    Each entry in the array will be treated as a row
    thumbnails will be 5x4
    models will be 5x5
    i.e 4 & 5 array entries each with strings of length 5 & 5.
    And they should be in descending order, by this I mean,
    e.g. model[0] -> head
         model[1] -> neck
         model[2] -> body
         model[3] -> legs
         model[4] -> feet
    */
    abstract String[] thumbnail();
    abstract String[] standingModel();
    abstract String[] blockingRightModel();
    abstract String[] blockingLeftModel();
    abstract String[] attackingRightModel();
    abstract String[] attackingLeftModel();
    /* -------------------------------------- */
    /* Name of the character in lowercase */
    abstract String name();
    abstract char attackShape();
}