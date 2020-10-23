package nothing.fighur.game.characters;

/**
 * Create one of each
 */
public class CharacterFactory
{
    public Character[] createAll()
    {
        Character[] characters = {
            new Bob(),
            new Clayton(),
            new Jin()
        };
        return characters;
    }
}