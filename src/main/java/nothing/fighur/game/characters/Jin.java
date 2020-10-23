package nothing.fighur.game.characters;

/**
 * Sample Fighter Jin
 */
public class Jin implements Character
{
    private String[] standingModel;
    private String[] blockingRightModel;
    private String[] blockingLeftModel;
    private String[] attackingRightModel;
    private String[] attackingLeftModel;

    public Jin()
    {
        standingModel = new String[5];
        standingModel[0] = " ___ ";
        standingModel[1] = "[1.1]";
        standingModel[2] = "--|--";
        standingModel[3] = " _|_ ";
        standingModel[4] = " V V ";

        blockingRightModel = new String[5];
        blockingRightModel[0] = " ___ ";
        blockingRightModel[1] = "[1.1]";
        blockingRightModel[2] = "--|-O";
        blockingRightModel[3] = " _|_ ";
        blockingRightModel[4] = " V V ";

        blockingLeftModel = new String[5];
        blockingLeftModel[0] = " ___ ";
        blockingLeftModel[1] = "[1.1]";
        blockingLeftModel[2] = "O-|--";
        blockingLeftModel[3] = " _|_ ";
        blockingLeftModel[4] = " V V ";

        attackingRightModel = new String[5];
        attackingRightModel[0] = " ___ ";
        attackingRightModel[1] = "[1.1]";
        attackingRightModel[2] = "--|-+";
        attackingRightModel[3] = " _|_ ";
        attackingRightModel[4] = " V V ";

        attackingLeftModel = new String[5];
        attackingLeftModel[0] = " ___ ";
        attackingLeftModel[1] = "[1.1]";
        attackingLeftModel[2] = "+-|--";
        attackingLeftModel[3] = " _|_ ";
        attackingLeftModel[4] = " V V ";
    }

    public String[] thumbnail()
    {
        String[] thumbnail = new String[4];
        thumbnail[0] = " ___ ";
        thumbnail[1] = "[1.1]";
        thumbnail[2] = " -|- ";
        thumbnail[3] = " JIN ";
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
        return "jin";
    }

    public char attackShape()
    {
        return '+';
    }
}