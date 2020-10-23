package nothing.fighur.game.characters;

/**
 * Sample Fighter Clayton
 */
public class Clayton implements Character
{
    private String[] standingModel;
    private String[] blockingRightModel;
    private String[] blockingLeftModel;
    private String[] attackingRightModel;
    private String[] attackingLeftModel;

    public Clayton()
    {
        standingModel = new String[5];
        standingModel[0] = " /=\\ ";
        standingModel[1] = " ( ) ";
        standingModel[2] = "--|--";
        standingModel[3] = "  |  ";
        standingModel[4] = " / \\ ";

        blockingRightModel = new String[5];
        blockingRightModel[0] = " /=\\ ";
        blockingRightModel[1] = " ( ) ";
        blockingRightModel[2] = "--|-|";
        blockingRightModel[3] = "  |  ";
        blockingRightModel[4] = " / \\ ";

        blockingLeftModel = new String[5];
        blockingLeftModel[0] = " /=\\ ";
        blockingLeftModel[1] = " ( ) ";
        blockingLeftModel[2] = "|-|--";
        blockingLeftModel[3] = "  |  ";
        blockingLeftModel[4] = " / \\ ";

        attackingRightModel = new String[5];
        attackingRightModel[0] = " /=\\ ";
        attackingRightModel[1] = " ( ) ";
        attackingRightModel[2] = "--|-@";
        attackingRightModel[3] = "  |  ";
        attackingRightModel[4] = " / \\ ";

        attackingLeftModel = new String[5];
        attackingLeftModel[0] = " /=\\ ";
        attackingLeftModel[1] = " ( ) ";
        attackingLeftModel[2] = "@-|--";
        attackingLeftModel[3] = "  |  ";
        attackingLeftModel[4] = " / \\ ";
    }

    public String[] thumbnail()
    {
        String[] thumbnail = new String[4];
        thumbnail[0] = " /=\\ ";
        thumbnail[1] = " ( ) ";
        thumbnail[2] = " -|- ";
        thumbnail[3] = "CLAYT";
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
        return "clayton";
    }

    public char attackShape()
    {
        return '@';
    }
}