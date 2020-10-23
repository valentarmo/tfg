package nothing.fighur.game;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.*;
import com.googlecode.lanterna.input.*;
import com.googlecode.lanterna.terminal.*;

import nothing.fighur.game.characters.*;
import nothing.fighur.game.characters.Character;

/**
 * Match between two players
 */
public class Match
{
    private TextGraphics painter;
    private final int timeLimit = 240000;
    private final int jumpSpeed = 150; /* AKA gravity */
    /* May move this to the Attack class to make attacks more dynamic */
    private final int attackSpeed = 150;
    private long startingTime;
    private final int maxJumpHeight = 8;
    /* Top Left corner of the players' characters */
    private int p1top;
    private int p1left;
    private int p2top;
    private int p2left;
    /* ----------------------------------------- */
    /* This is used because there are some asynchronous operations */
    private final CopyOnWriteArrayList<Attack> attacks = new CopyOnWriteArrayList<>();
    private TerminalPosition[][] p1hitbox = new TerminalPosition[5][5];
    private TerminalPosition[][] p2hitbox = new TerminalPosition[5][5];
    private char[][] screen;
    /* Drawing the screen is expensive so should be limited */

    protected Player player1;
    protected Player player2;
    protected Terminal terminal;

    /**
     * Create a new match
     */
    public Match(Terminal terminal, TextGraphics painter, Player p1, Player p2)
    {
        this.terminal = terminal;
        this.painter = painter;
        player1 = p1;
        player2 = p2;
    }

    /**
     * This is the main method of this class,
     * it will start and determine the result
     * of the match
     */
    public void begin() throws Exception
    {
        // TODO: Stop clearing the screen all the time
        /* set initial positions */
        TerminalSize tsize = terminal.getTerminalSize();
        int trows = tsize.getRows(); // TODO: Make these parameters
        int tcols = tsize.getColumns();

        p1top = trows-7;
        p2top = trows-7;
        p1left = 2;
        p2left = tcols - 8;
        updateHitbox();
        /* -------------------- */

        screen = new char[trows][tcols];

        terminal.clearScreen();
        drawCountDown();

        startingTime = System.currentTimeMillis();
        long timeLeft = 0;

        /* Start the fighting */
        while (player1.getLife()>0 && player2.getLife()>0 && timeLeft<timeLimit) {
            TerminalSize currtsize = terminal.getTerminalSize();
            int currtrows = currtsize.getRows();
            int currtcols = currtsize.getColumns();

            if (currtrows != trows || currtcols != tcols) {
                trows = currtrows;
                tcols = currtcols;
                screen = new char[trows][tcols];
            }

            mapRing();
            mapFighters();
            mapAttacks();

            draw(); // TODO: draw only when necessary
            terminal.flush();

            processKeyStrokes();
            checkHits();

            timeLeft = System.currentTimeMillis() - startingTime;
        }

        /* The last hit won't be reflected in the health bar when the loop ends
        so we draw once more */
        mapRing();
        mapFighters();
        mapAttacks();

        draw();

        terminal.flush();
        /* ------------------- */

        int mcol = tcols/2;
        int mrow = trows/2;

        /* Finaly draw the result */
        if (player1.getLife() == 0 && player2.getLife() == 0 ||
            player1.getLife() > 0 && player2.getLife() > 0 ) {
            String draw = "IT'S A DRAW";
            painter.putString(mcol-draw.length()/2, mrow, draw);
        } else if (player1.getLife() > 0) {
            String p1w = "PLAYER 1 WINS";
            painter.putString(mcol-p1w.length()/2, mrow, p1w);
        } else {
            String p2w = "PLAYER 2 WINS";
            painter.putString(mcol-p2w.length()/2, mrow, p2w);
        }
        terminal.flush();
        /* And wait a moment before going back
        to the main menu */
        Thread.sleep(5000);
    }

    /**
     * This method reads the keyboard in an asynchronous manner.
     * Now it has a problem I didn't anticipate which is that
     * if you hold a key pressed, it won't read other keys that
     * may be pressed afterwards, which makes all the sense in
     * the world, but that currently is the biggest problem with
     * game because if one of the player holds say his/her attack
     * key, the other player won't be able to do anything.
     *
     * @param tcols Number of columns of the terminal
     */
    protected void processKeyStrokes() throws IOException
    {
        // TODO: Maybe move control settings to a file
        KeyStroke keyStroke = terminal.pollInput();
        if (keyStroke != null) {
            KeyType keyType = keyStroke.getKeyType();
            if (keyType == KeyType.Character) {
                char key = keyStroke.getCharacter();
                switch (key) {
                /* Player 1 -------------- */
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
                /* ----------------------- */

                /* Player 2 --------------- */
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
                /* ---------------------- */
                default:
                    break;
                }
            }
        }
    }

    protected void p1Jump() {
        if (!player1.isJumping()) {
            long startedJump = System.currentTimeMillis();
            player1.setIsJumping(true);
            player1.setTimeSinceJumpUpdate(startedJump);
            p1top--;
            p1MovedUp();
            player1.setJumpHeight(1);
        }
    }

    protected void p2Jump() {
        if (!player2.isJumping()) {
            long startedJump = System.currentTimeMillis();
            player2.setIsJumping(true);
            player2.setTimeSinceJumpUpdate(startedJump);
            p2top--;
            p2MovedUp();
            player2.setJumpHeight(1);
        }
    }

    protected void p2MoveLeft() {
        player2.setFacingDirection(false);
        player2.setStance(Player.Stance.Standing);
        if (p2left > 0) {
            p2left--;
            p2MovedLeft();
            updateHitbox();
        }
    }

    protected void p2MoveRight() throws IOException {
        int tcols = terminal.getTerminalSize().getColumns();
        player2.setFacingDirection(true);
        player2.setStance(Player.Stance.Standing);
        if (p2left < tcols - 5) {
            p2left++;
            p2MovedRigth();
            updateHitbox();
        }
    }

    protected void p1MoveLeft() {
        player1.setFacingDirection(false);
        player1.setStance(Player.Stance.Standing);
        if (p1left > 0) {
            p1left--;
            p1MovedLeft();
            updateHitbox();
        }
    }

    protected void p1MoveRight() throws IOException {
        int tcols = terminal.getTerminalSize().getColumns();
        player1.setFacingDirection(true);
        player1.setStance(Player.Stance.Standing);
        if (p1left < tcols - 5) {
            p1left++;
            p1MovedRigth();
            updateHitbox();
        }
    }

    protected void drawCountDown() throws IOException, InterruptedException
    {
        // TODO: Stop clearing the screen all the time
        String three1 = " ______ ";
        String three2 = "|____  |";
        String three3 = "  __|  |";
        String three4 = " |__   |";
        String three5 = " ___|  |";
        String three6 = "|______|";

        String two1 = " ______ ";
        String two2 = "|____  |";
        String two3 = " ____| |";
        String two4 = "|  ____|";
        String two5 = "| |____ ";
        String two6 = "|______|";

        String one1 = "   _   ";
        String one2 = " |_ |  ";
        String one3 = "  | |  ";
        String one4 = "  | |  ";
        String one5 = " _| |_ ";
        String one6 = "|_____|";

        int tcols = terminal.getTerminalSize().getColumns();
        int trows = terminal.getTerminalSize().getRows();

        int lefc = tcols/2-4;
        int topr = trows/2-3;

        terminal.clearScreen();

        painter.putString(lefc, topr, three1);
        painter.putString(lefc, topr+1, three2);
        painter.putString(lefc, topr+2, three3);
        painter.putString(lefc, topr+3, three4);
        painter.putString(lefc, topr+4, three5);
        painter.putString(lefc, topr+5, three6);

        terminal.flush();
        Thread.sleep(1000);
        terminal.clearScreen();

        painter.putString(lefc, topr, two1);
        painter.putString(lefc, topr+1, two2);
        painter.putString(lefc, topr+2, two3);
        painter.putString(lefc, topr+3, two4);
        painter.putString(lefc, topr+4, two5);
        painter.putString(lefc, topr+5, two6);

        terminal.flush();
        Thread.sleep(1000);
        terminal.clearScreen();

        painter.putString(lefc, topr, one1);
        painter.putString(lefc, topr+1, one2);
        painter.putString(lefc, topr+2, one3);
        painter.putString(lefc, topr+3, one4);
        painter.putString(lefc, topr+4, one5);
        painter.putString(lefc, topr+5, one6);

        terminal.flush();
        Thread.sleep(1000);
    }

    /**
     * This method draws the surroundings to the screen,
     * by surroundings I mean health bars,
     * the floor, and the timer
     */
    protected void mapRing() throws IOException
    {
        TerminalSize tsize = terminal.getTerminalSize();
        int tcols = tsize.getColumns();
        int trows = tsize.getRows();

        int p1HealthCol = 1;
        int p2HealthCol = tcols - Player.getMaxLife() - 1;
        int topRow = 1;
        int timerCol = tcols/2 - 1;
        int bottomRow = trows-2;

        long currentTime = System.currentTimeMillis();
        int time = 240 - (int)(currentTime-startingTime)/1000;

        /* Player 1's health bar */
        for (int i = p1HealthCol; i <= Player.getMaxLife(); i++)
            if (i <= player1.getLife())
                screen[topRow][i] = '*';
            else
                screen[topRow][i] = ' ';

        /* Player 2's health bar */
        for (int i = p2HealthCol; i < tcols-1; i++)
            if (i >= tcols - player2.getLife() - 1)
                screen[topRow][i] = '*';
            else
                screen[topRow][i] = ' ';

        /* Draw timer */
        for (int i = 0; i < 3; i++)
            screen[topRow][timerCol+i] = ' ';
        String t = Integer.toString(time);
        for (int i = 0; i < t.length(); i++)
            screen[topRow][timerCol+i] = t.charAt(i);

        /* Draw floor */
        for (int i = 0; i < tcols; i++)
            screen[bottomRow][i] = '_';
    }

    /**
     * This method is very important it maps
     * both fighters to the screen
     */
    protected void mapFighters() throws IOException
    {
        Character p1Fighter = player1.getCharacter();
        Character p2Fighter = player2.getCharacter();

        String[] p1Model;
        String[] p2Model;

        /* First get the right model */
        if (player1.isStanding())
            p1Model = p1Fighter.standingModel();
        else if (player1.isBlocking() && player1.facingDirection())
            p1Model = p1Fighter.blockingRightModel();
        else if (player1.isBlocking() && !player1.facingDirection())
            p1Model = p1Fighter.blockingLeftModel();
        else if (player1.isAttacking() && player1.facingDirection())
            p1Model = p1Fighter.attackingRightModel();
        else
            p1Model = p1Fighter.attackingLeftModel();

        if (player2.isStanding())
            p2Model = p2Fighter.standingModel();
        else if (player2.isBlocking() && player2.facingDirection())
            p2Model = p2Fighter.blockingRightModel();
        else if (player2.isBlocking() && !player2.facingDirection())
            p2Model = p2Fighter.blockingLeftModel();
        else if (player2.isAttacking() && player2.facingDirection())
            p2Model = p2Fighter.attackingRightModel();
        else
            p2Model = p2Fighter.attackingLeftModel();

        handleJump();

        /* Map player 1 to the screen */
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                screen[p1top+i][p1left+j] = p1Model[i].charAt(j);

        /* Map player 2 to the screen */
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                screen[p2top+i][p2left+j] = p2Model[i].charAt(j);
    }

    /**
     * This method checks the heigth at which
     * the fighters should be drawn
     */
    protected void handleJump() {
        /*
         * I wasn't planning on allowing jumping at first, but then
         * I changed my mind. D=
         */

        /* First we check if the height should be managed
        i.e. check if the fighter is jumping */
        if (player1.isJumping()) {
            /* Time is very important here. With it we can tell
            when to change the height */
            long currentTime = System.currentTimeMillis();
            long timeSinceLastMoved = player1.getTimeSinceJumpUpdate();
            if (currentTime - timeSinceLastMoved > jumpSpeed) {
                int currentJumpHeight = player1.getJumpHeight();
                if (!player1.isFalling()) {
                    /* Fighter going up */
                    if (currentJumpHeight < maxJumpHeight) {
                        p1top--;
                        p1MovedUp();
                        updateHitbox();
                        player1.setJumpHeight(currentJumpHeight + 1);
                        player1.setTimeSinceJumpUpdate(currentTime);
                    } else {
                        player1.setIsFalling(true);
                    }
                } else {
                    /* Fighter goind down */
                    if (currentJumpHeight > 0) {
                        p1top++;
                        p1MovedDown();
                        updateHitbox();
                        player1.setJumpHeight(currentJumpHeight - 1);
                        player1.setTimeSinceJumpUpdate(currentTime);
                    } else {
                        player1.setIsFalling(false);
                        player1.setIsJumping(false);
                    }
                }
            }
        }

        /* Same for player 2 */
        if (player2.isJumping()) {
            long currentTime = System.currentTimeMillis();
            long timeSinceLastMoved = player2.getTimeSinceJumpUpdate();
            if (currentTime - timeSinceLastMoved > 150) {
                int currentJumpHeight = player2.getJumpHeight();
                if (!player2.isFalling()) {
                    if (currentJumpHeight < maxJumpHeight) {
                        p2top--;
                        p2MovedUp();
                        updateHitbox();
                        player2.setJumpHeight(currentJumpHeight + 1);
                        player2.setTimeSinceJumpUpdate(currentTime);
                    } else {
                        player2.setIsFalling(true);
                    }
                } else {
                    if (currentJumpHeight > 0) {
                        p2top++;
                        p2MovedDown();
                        updateHitbox();
                        player2.setJumpHeight(currentJumpHeight - 1);
                        player2.setTimeSinceJumpUpdate(currentTime);
                    } else {
                        player2.setIsFalling(false);
                        player2.setIsJumping(false);
                    }
                }
            }
        }
    }

    /**
     * This method is very important it maps all the attacks
     * to the screen
     */
    protected void mapAttacks() throws IOException
    {
        /* Time again will be very important to update attacks */
        long currentTime = System.currentTimeMillis();
        int tcols = terminal.getTerminalSize().getColumns();

        for (Attack a : attacks) {
            int arow = a.getRow();
            int acol = a.getColumn();

            screen[arow][acol] = a.getShape();
            long timeSinceLastMoved = a.getTimeSinceLastMoved();
            if (currentTime - timeSinceLastMoved > attackSpeed) {
                if (a.getDirection())
                    a.setColumn(acol+1);
                else
                    a.setColumn(acol-1);
                screen[arow][acol] = ' ';
                a.setTimeSinceLastMoved(currentTime);
            }
            /* Check if the attack reached the end of the screen */
            if (a.getColumn() < 0 || a.getColumn() == tcols)
                attacks.remove(a);
        }
    }

    protected void draw() throws IOException
    {
        for (int row = 0; row < screen.length; row++) {
            terminal.setCursorPosition(0, row);
            for (int col = 0; col < screen[0].length; col++) {
                if (screen[row][col] == '\u0000')
                    terminal.putCharacter(' ');
                else
                    terminal.putCharacter(screen[row][col]);
            }
        }
    }

    protected void p1MovedRigth()
    {
        for (int row = p1top; row < p1top+5; row++)
            screen[row][p1left-1] = ' ';
    }

    protected void p2MovedRigth()
    {
        for (int row = p2top; row < p2top+5; row++)
            screen[row][p2left-1] = ' ';
    }

    protected void p1MovedLeft()
    {
        for (int row = p1top; row < p1top+5; row++)
            screen[row][p1left+5] = ' ';
    }

    protected void p2MovedLeft()
    {
        for (int row = p2top; row < p2top+5; row++)
            screen[row][p2left+5] = ' ';
    }

    protected void p1MovedUp()
    {
        for (int col = p1left; col < p1left+5; col++)
            screen[p1top+5][col] = ' ';
    }

    protected void p2MovedUp()
    {
        for (int col = p2left; col < p2left+5; col++)
            screen[p2top+5][col] = ' ';
    }

    protected void p1MovedDown()
    {
        for (int col = p1left; col < p1left+5; col++)
            screen[p1top-1][col] = ' ';
    }

    protected void p2MovedDown()
    {
        for (int col = p2left; col < p2left+5; col++)
            screen[p2top-1][col] = ' ';
    }

    /**
     * Create a new attack
     * @param attacker the player who attacked
     */
    protected void newAttack(Player attacker)
    {
        char shape = attacker.getCharacter().attackShape();
        int attackerTopRow;
        int attackerLeftmostCol;

        int attackRow;
        int attackCol;

        if (attacker == player1) {
            attackerTopRow = p1top;
            attackerLeftmostCol = p1left;
        } else {
            attackerTopRow = p2top;
            attackerLeftmostCol = p2left;
        }
        attackRow = attackerTopRow + 2;

        if (attacker.facingDirection()) {
            // Attack comes out from the right
            attackCol = attackerLeftmostCol + 4;
        } else {
            // Attack comes out from the left
            attackCol = attackerLeftmostCol;
        }

        long time = System.currentTimeMillis();
        attacks.add(new Attack(attacker, shape, attackRow, attackCol, attacker.facingDirection(), time));
    }

    /**
     * Keep both hitbox and fighters consistent
     */
    protected void updateHitbox()
    {
        for (int i = 0; i < p1hitbox.length; i++)
            for (int j = 0; j < p1hitbox[0].length; j++) {
                p1hitbox[i][j] = new TerminalPosition(p1left+j, p1top+i);
                p2hitbox[i][j] = new TerminalPosition(p2left+j, p2top+i);
            }
    }

    /**
     * Very important method, it checks when an attack and
     * a hitbox collide, and takes appropriate action if
     * that's the case
     */
    protected void checkHits()
    {
        for (Attack a : attacks) {
            if (a.getAttacker() == player1) { /*  Check if player 2 was hit */
                if (collision(a, p2hitbox)) {
                    if (!player2.isBlocking() || player2.facingDirection() == a.getDirection())
                        player2.reduceLife(); /* If not blocking or hit in the back */
                    attacks.remove(a); /* The attack hit, it should dissapear */
                }
            } else { /* Now check if player 1 was hit */
                if (collision(a, p1hitbox)) {
                    if (!player1.isBlocking() || player1.facingDirection() == a.getDirection())
                        player1.reduceLife();
                    attacks.remove(a);
                }
            }
        }
    }

    /**
     * Check if an attack collides with a hitbox
     */
    protected boolean collision(Attack attack, TerminalPosition[][] hitbox)
    {
        int attackRow = attack.getRow();
        int attackCol = attack.getColumn();

        for (int i = 0; i < hitbox.length; i++)
            for (int j = 0; j < hitbox[0].length; j++)
                if (hitbox[i][j].equals(attackCol, attackRow))
                    return true;
        return false;
    }
}
