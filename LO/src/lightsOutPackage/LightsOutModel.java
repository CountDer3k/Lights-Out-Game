package lightsOutPackage;

import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;

public class LightsOutModel
{
    // amount of moves to be made when creating a new game
    private int NewMovesMade = 0;
    // number of moves done before game over
    private int movesDone = 0;
    // number of moves done for the 'animation'
    private int AnimationMoves = 13;
    //debugging checker
    private boolean debugging;
    // number of moves done manually before game over
    private int manualMovesDone = 0;
    // a boolean in a form of an int to see manual setup is on
    private int setupText = 0;
    // a counter for calculating percentage
    private int count = 0;
    // boolean in a form of an int to see if a new game is needed to continue
    private int newGameSetter = 1;
    private String PlayerMoves ="";
    // a 2D array for the board
    private int[][] board;
    private ArrayList<Double> ArrayR = new ArrayList<Double>();
    private ArrayList<Double> ArrayC = new ArrayList<Double>();

    /**
     * Constructor used by the LightsOutView to call this class
     */
    public LightsOutModel (int rows, int cols)
    {
        if (rows < 5 || cols < 5)
        {
            throw new IllegalArgumentException();
        }
        this.board = new int[cols][rows];
    }

    /**
     * Clears the board, and places a few random moves to start a game with. Changes the manual setup text if it is not
     * in its proper state
     */
    public void newGame ()
    {
        PlayerMoves = "";
        ArrayR.clear();
        ArrayC.clear();
        debugging = false;
        Random rand = new Random();
        setNewGameSetter();
        setSetupTextOff();
        movesDone = 0;
        manualMovesDone = 0;
        //random number from 3-7 to use and # of random moves made
        NewMovesMade = rand.nextInt(8);
        while(NewMovesMade < 3)
        {
            NewMovesMade = rand.nextInt(8);
        }
        // clears board
        clearBoard();
        NewMovesMade = 3;
        // random generator, generates X amount moves
        for (int i = 0; i < NewMovesMade; i++)
        {
            int row = rand.nextInt(5);
            int col = rand.nextInt(5);
            ArrayR.add((double) row);
            ArrayC.add((double) col);
            moveTo(col, row);
        }

    }

    public void clearBoard ()
    {
        for (int i = 0; i < this.board.length; i++)
        {
            for (int j = 0; j < this.board[i].length; j++)
            {
                this.board[i][j] = 0;
            }
        }
    }

    /**
     * Toggles manual mode on & off
     */
    public void manualSetup ()
    {
        if (setupText == 1)
        {
            setupText = 0;
        }
        else
        {
            setupText = 1;
        }
    }

    /**
     * Toggle the word on the manual setup buttons
     */
    public String manualSetupWord ()
    {
        String ENTER = "Enter Manual Setup";
        String EXIT = "Exit Manual Setup";
        if (setupText == 0)
        {
            return ENTER;
        }
        else if (setupText == 1)
        {
            return EXIT;
        }
        return ENTER;
    }

    /*
     * Returns the value of the current board piece
     */
    public int getOccupant (int row, int col)
    {
        return this.board[col][row];
    }

    /**
     * returns the appropriate value to go in the square
     */
    public int getToMove (int rows, int col)
    {
        if (this.board[col][rows] == 1)
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }

    /**
     * Checks if manual setup mode is toggle on/off if off checks the relevant squares to toggle disregarding the
     * exceptions
     */
    public void moveTo (int col, int rows)
    {
        if (col < 0 || col >= this.board.length || rows < 0 || rows >= this.board.length)
        {
            throw new IllegalArgumentException();
        }
        if (newGameSetter == 0)
        {
            if (setupText == 0)
            {
                this.board[col][rows] = this.getToMove(rows, col);
                try
                {
                    this.board[col + 1][rows] = this.getToMove(rows, col + 1);
                }
                catch (ArrayIndexOutOfBoundsException e)
                {
                }
                try
                {
                    this.board[col - 1][rows] = this.getToMove(rows, col - 1);
                }
                catch (ArrayIndexOutOfBoundsException e)
                {
                }
                try
                {
                    this.board[col][rows + 1] = this.getToMove(rows + 1, col);
                }
                catch (ArrayIndexOutOfBoundsException e)
                {
                }
                try
                {
                    this.board[col][rows - 1] = this.getToMove(rows - 1, col);
                }
                catch (ArrayIndexOutOfBoundsException e)
                {
                }
                Moves();

            }
            else if (setupText == 1)
            {
                this.board[col][rows] = this.getToMove(rows, col);
                manualMoves();
            }

        }
        else
        {
            JOptionPane.showMessageDialog(null, "Press 'New Game' to begin Playing");
        }

        // areTheLightsOut(rows, cols);
    }

    /**
     * Checks if all of the lights are off Play winning animations
     */
    public boolean areTheLightsOut ()
    {
        if (countBlocks() == 25)
        {
            if (newGameSetter == 0)
            {
                return true;
            }
            else
            {
            }
        }
        else
        {
            return false;
        }
        return false;

    }

    /**
     * Returns the percent of squares turned off
     */
    public int getPercent ()
    {
        int x;
        if (debugging == true)
        {
            x = count * 4;
        }
        else
        {
            x = countBlocks() * 4;
        }
        return x;
    }

    /**
     * Returns the amount of squares set to 0/off
     */
    public int countBlocks ()
    {
        count = 0;

        if (setupText == 0)
        {
            for (int i = 0; i < this.board.length; i++)
            {
                for (int j = 0; j < this.board[i].length; j++)
                {
                    if (this.board[i][j] == 0)
                    {
                        count++;
                    }
                    else
                    {

                    }
                }
            }
        }
        else
        {

        }
        return count;
    }

    /**
     * Increments an int to keep track of the moves made
     */
    public void Moves ()
    {
        movesDone++;
    }

    /**
     * Increments an int to keep track of moves done in manual mode
     */
    public void manualMoves ()
    {
        manualMovesDone++;
    }

    /*
     * returns the amount of moves done by the player
     */
    public int getMoves ()
    {
        // returns all moves done minus the moves done to create a new board
        return movesDone - NewMovesMade + 1;
    }

    /**
     * returns the amount of manual moves done by the player
     */
    public int getManualMoves ()
    {
        if (newGameSetter == 1)
        {
            return manualMovesDone - AnimationMoves;
        }
        else
        {
            return manualMovesDone;
        }
    }

    /**
     * returns the total amount of moves done by the player
     */
    public int getTotalMoves ()
    {
        if (newGameSetter == 1)
        {
            return manualMovesDone + movesDone - NewMovesMade - AnimationMoves;
        }
        else

        {
            return manualMovesDone + movesDone - NewMovesMade;
        }

    }

    /**
     * Cancels execution of any more moves
     */
    public void gameOver ()
    {
        newGameSetter = 1;
    }

    /**
     * set the game setter checker to 0.
     */
    public void setNewGameSetter ()
    {
        newGameSetter = 0;
    }

    /**
     * sets the setupText to 1.
     */
    public void setSetupTextOn ()
    {
        setupText = 1;
    }

    /**
     * sets the setupText to 0.
     */
    public void setSetupTextOff ()
    {
        setupText = 0;
    }

    /**
     * returns the player's score
     */
    public String getPlayerScore ()
    {
        if (getTotalMoves() <= NewMovesMade)
        {
            return "Perfect!";
        }
        else if (getTotalMoves() <= NewMovesMade + 1)
        {
            return "Semi-Perfect! \nBetter Luck Next Time";
        }
        else if (getTotalMoves() <= (NewMovesMade * 2) - 1)
        {
            return "Amateur!";
        }
        else
        {
            return "The Failure level is beyond calculation!";
        }
    }

    /*
     * returns gameSetter value
     */
    public int getNewGameSetter ()
    {
        return newGameSetter;
    }

    // debugging/ test cases methods
    public int getSetupText ()
    {
        return setupText;
    }
    public int getNewMovesMade()
    {
        return NewMovesMade;
    }

    public void setCountBlocks (int a)
    {
        debugging = true;
        count = a;
    }
    /**
     * returns the moves need to win, IF a new game was started, & the player has not made any moves
     */
    public String getArrayMoves()
    {
        String a = "";
        for(int i = NewMovesMade; i >0 ; i--)
        {
            a = a + "\n(" + ArrayR.get(i-1) + "," + ArrayC.get(i-1) + ")";
        }
        
        
        return a;
    }
    /**
     * returns the move the player made
     */
    public String getPlayerMoves(int c, int r)
    {
        PlayerMoves = PlayerMoves + "\n(" + Integer.toString(c) + "," + Integer.toString(r) +")";  
        
        return PlayerMoves;
    }
    
}
