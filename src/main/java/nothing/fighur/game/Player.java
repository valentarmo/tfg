package nothing.fighur.game;

import nothing.fighur.game.characters.Character;

/**
 * This class represents the players
 */
public class Player
{
    private Character fighter;
    private int life;
    private boolean isBlocking = false;
    private boolean isAttacking = false;
    private boolean isStanding = true;
    private boolean facingDirection;
    private boolean isJumping = false;
    private boolean isFalling = false;
    private long timeSinceJumpUpdate = 0;
    private int jumpHeight = 0;

    /**
     * A player can be either blocking, attacking,
     * or doing nothing (standing).
     */
    public enum Stance {
        Standing, Blocking, Attacking
    }

    /**
     * @param fighter A character
     * @param facingDirection true -> right, false -> left
     */
    public Player(Character fighter, boolean facingDirection)
    {
        this.fighter = fighter;
        this.facingDirection = facingDirection;
        life = 10;
    }

    public static int getMaxLife()
    { return 10; }

    public int getLife()
    { return life; }

    public void reduceLife()
    { life -= 1; }

    public Character getCharacter()
    { return fighter; }

    public boolean isBlocking()
    { return isBlocking; }

    public boolean isAttacking()
    { return isAttacking; }

    public boolean isStanding()
    { return isStanding; }

    public boolean isJumping()
    { return isJumping; }

    public boolean isFalling()
    { return isFalling; }

    public long getTimeSinceJumpUpdate()
    { return timeSinceJumpUpdate; }

    public int getJumpHeight()
    { return jumpHeight; }

    /**
     * Get the direction the player's character is facing
     * @return true -> right | false -> left
     */
    public boolean facingDirection()
    { return facingDirection; }

    /**
     * Change the direction the player's character is facing
     * @param dir true -> right | false -> left
     */
    public void setFacingDirection(boolean dir)
    { facingDirection = dir; }

    public void setIsJumping(boolean isJumping)
    { this.isJumping = isJumping; }

    public void setIsFalling(boolean isFalling)
    { this.isFalling = isFalling; }

    public void setTimeSinceJumpUpdate(long time)
    { timeSinceJumpUpdate = time; }

    public void setJumpHeight(int height)
    { jumpHeight = height; }

    /**
     * Change the stance of the player
     * @param stance Standing, Blocking or Attacking
     */
    public void setStance(Stance stance)
    {
        switch (stance) {
            case Standing:
                isStanding = true;
                isBlocking = false;
                isAttacking = false;
                break;
            case Blocking:
                isStanding = false;
                isBlocking = true;
                isAttacking = false;
                break;
            case Attacking:
                isStanding = false;
                isBlocking = false;
                isAttacking = true;
                break;
            default:
                break;
        }
    }
}
