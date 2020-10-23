package nothing.fighur.game.characters;

/**
 * Sample fighter Bob
 */
public class Bob implements Character
{
    private String[] standingModel;
    private String[] blockingRightModel;
    private String[] blockingLeftModel;
    private String[] attackingRightModel;
    private String[] attackingLeftModel;

    public Bob()
    {
        standingModel = new String[5];
        standingModel[0] = "  _  ";
        standingModel[1] = " ( ) ";
        standingModel[2] = "<-|->";
        standingModel[3] = "  |  ";
        standingModel[4] = " L L ";

        blockingRightModel = new String[5];
        blockingRightModel[0] = "  _  ";
        blockingRightModel[1] = " ( ) ";
        blockingRightModel[2] = "<-|-|";
        blockingRightModel[3] = "  |  ";
        blockingRightModel[4] = " L L ";

        blockingLeftModel = new String[5];
        blockingLeftModel[0] = "  _  ";
        blockingLeftModel[1] = " ( ) ";
        blockingLeftModel[2] = "|-|->";
        blockingLeftModel[3] = "  |  ";
        blockingLeftModel[4] = " L L ";

        attackingRightModel = new String[5];
        attackingRightModel[0] = "  _  ";
        attackingRightModel[1] = " ( ) ";
        attackingRightModel[2] = "<-|-*";
        attackingRightModel[3] = "  |  ";
        attackingRightModel[4] = " L L ";

        attackingLeftModel = new String[5];
        attackingLeftModel[0] = "  _  ";
        attackingLeftModel[1] = " ( ) ";
        attackingLeftModel[2] = "*-|->";
        attackingLeftModel[3] = "  |  ";
        attackingLeftModel[4] = " L L ";
    }

    public String[] thumbnail()
    {
        String[] thumbnail = new String[4];
        thumbnail[0] = "  _  ";
        thumbnail[1] = " ( ) ";
        thumbnail[2] = " -|- ";
        thumbnail[3] = " BOB ";
        return thumbnail;
    }

    public String[] standingModel()
    {
        return standingModel;
    }

    public String[] blockingRightModel()
    {
        return blockingRightModel;
    }

    public String[] blockingLeftModel()
    {
        return blockingLeftModel;
    }

    public String[] attackingRightModel()
    {
        return attackingRightModel;
    }

    public String[] attackingLeftModel()
    {
        return attackingLeftModel;
    }

    public String name()
    {
        return "bob";
    }

    public char attackShape()
    {
        return '*';
    }
}