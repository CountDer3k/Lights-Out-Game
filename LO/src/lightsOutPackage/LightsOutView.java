package lightsOutPackage;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;

/**
 * Implements a Lights Out game with a GUI interface.
 * 
 * @author Der3k Burrola
 */
@SuppressWarnings("serial")
public class LightsOutView extends JFrame implements ActionListener
{
    // Some formatting constants
    public static final int Square_On = 1;
    /** The number used to indicate Player 2 */
    public static final int Square_Off = 0;
    private final static int WIDTH = 820;
    private final static int HEIGHT = 750;
    public final static int ROWS = 5;
    public final static int COLS = 5;
    public final static Color BACKGROUND_COLOR = Color.lightGray;
    public final static Color P1_COLOR = Color.yellow;
    public final static Color P2_COLOR = Color.black;
    public final static Color TIE_COLOR = Color.black;
    public final static Color BOARD_COLOR = Color.darkGray;
    public final static int BORDER = 5;
    public final static int FONT_SIZE = 13;
    private JButton playerMoves = new JButton("Player Moves:");

    /** The "smarts" of the game **/
    private LightsOutModel model;

    /** The portion of the GUI that contains the playing surface **/
    private Board board;

    /**
     * Creates and initializes the game.
     */
    public LightsOutView ()
    {
        // Set the title that appears at the top of the window
        setTitle("Who Turned Off The Lights");

        // Cause the application to end when the windows is closed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Give the window its initial dimensions
        setSize(WIDTH, HEIGHT);

        // The root panel contains everything
        JPanel root = new JPanel();
        root.setLayout(new BorderLayout());
        setContentPane(root);

        JPanel Controls = new JPanel();
        Controls.setLayout(new GridLayout());
        // setContentPane(Controls);

        // The center portion contains the playing board
        model = new LightsOutModel(ROWS, COLS);
        board = new Board(model, this);
        root.add(board, "Center");

        // The new game button
        JButton newGame = new JButton("New Game");
        newGame.setFont(new Font("SansSerif", Font.BOLD, FONT_SIZE));
        newGame.setForeground(TIE_COLOR);
        newGame.setBackground(BACKGROUND_COLOR);
        newGame.addActionListener(this);
        Controls.add(newGame);

        // Manual setup button

        JButton ManualSetup = new JButton(model.manualSetupWord());
        ManualSetup.setFont(new Font("SansSerif", Font.BOLD, FONT_SIZE));
        ManualSetup.setForeground(TIE_COLOR);
        ManualSetup.setBackground(BACKGROUND_COLOR);
        ManualSetup.addActionListener(this);
        Controls.add(ManualSetup);

        // Number of Moves performed
        JButton nMoves = new JButton("Number of Moves");
        nMoves.setFont(new Font("SansSerif", Font.BOLD, FONT_SIZE));
        nMoves.setForeground(TIE_COLOR);
        nMoves.setBackground(BACKGROUND_COLOR);
        nMoves.addActionListener(this);
        Controls.add(nMoves);

        // Percentage of board that is off
        JButton Percent = new JButton("Get Percent");
        Percent.setFont(new Font("SansSerif", Font.BOLD, FONT_SIZE));
        Percent.setForeground(TIE_COLOR);
        Percent.setBackground(BACKGROUND_COLOR);
        Percent.addActionListener(this);
        Controls.add(Percent);

        //Says the amount of moves the round can be beaten in
        JButton difficulty = new JButton("# of moves to Win");
        difficulty.setFont(new Font("SansSerif", Font.BOLD, FONT_SIZE));
        difficulty.setForeground(TIE_COLOR);
        difficulty.setBackground(BACKGROUND_COLOR);
        difficulty.addActionListener(this);
        Controls.add(difficulty);
        
        //shows player moves
        playerMoves.setAlignmentY(20);
      //  root.add(playerMoves, "East");
        
        root.add(Controls, "North");
        // Refresh the display and we're ready
        board.refresh();
    }

    /**
     * Called when a button is clicked. Checks which button is clicked, then refreshes the display.
     */
    @Override
    public void actionPerformed (ActionEvent e)
    {
        JButton x = (JButton) e.getSource();
        if (x.getText().equals("New Game"))
        {
            model.newGame();
        }
        else if (x.getText().equals(model.manualSetupWord()))
        {
            if (model.getNewGameSetter() == 0)
            {
                SetupFix(x);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Press 'New Game' to begin playing");
            }
        }
        else if (x.getText().equals("Number of Moves"))
        {
            if (model.getNewGameSetter() == 0)
            {
                JOptionPane.showMessageDialog(null, model.getMoves() - 1 + " Moves done\n" + model.getManualMoves()
                        + " Manual moves done. \n" + model.getTotalMoves() + " Total Moves");
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Press 'New Game' to begin playing");
            }
        }
        else if (x.getText().equals("Get Percent"))
        {
            if (model.getNewGameSetter() == 0)
            {
                JOptionPane.showMessageDialog(null, model.getPercent() + "% Turned Off");
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Press 'New Game' to begin playing");
            }
        }
        else if(x.getText().equals("# of moves to Win"))
        {
            if (model.getNewGameSetter() == 0)
            {
                JOptionPane.showMessageDialog(null, "This round could be beaten in " + model.getNewMovesMade() + " moves.");
                JOptionPane.showMessageDialog(null, "If you have not made any moves, \nThese are the moves:" + model.getArrayMoves());
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Press 'New Game' to begin playing");
            }
        }
        else
        {
            SetupFix(x);
        }
        board.refresh();
    }

    public void SetupFix (JButton x)
    {
        model.manualSetupWord();
        model.manualSetup();
        x.setText(model.manualSetupWord());
    }

    /**
     * The playing surface of the game.
     */
    @SuppressWarnings("serial")
    class Board extends JPanel implements MouseListener
    {
        /** The "smarts" of the game */
        private LightsOutModel model;

        /** The top level GUI for the game */
        private LightsOutView display;

        /**
         * Creates the board portion of the GUI.
         */
        public Board (LightsOutModel model, LightsOutView display)
        {
            // Record the model and the top-level display
            this.model = model;
            this.display = display;

            // Set the background color and the layout
            setBackground(BOARD_COLOR);
            setLayout(new GridLayout(ROWS, COLS));

            // Create and lay out the grid of squares that make up the game.
            for (int i = ROWS - 1; i >= 0; i--)
            {
                for (int j = 0; j < COLS; j++)
                {
                    Square s = new Square(i, j);
                    s.addMouseListener(this);
                    add(s);
                }
            }
        }

        /**
         * Refreshes the display. This should be called whenever something changes in the model.
         */
        public void refresh ()
        {
            // Iterate through all of the squares that make up the grid
            Component[] squares = getComponents();
            for (Component c : squares)
            {
                Square s = (Square) c;

                // Set the color of the squares appropriately
                int status = model.getOccupant(s.getRow(), s.getCol());
                if (status == Square_On)
                {
                    s.setColor(P1_COLOR);
                }
                else
                {
                    s.setColor(P2_COLOR);
                }
            }
            // Ask that this board be repainted
            repaint();
        }

        /**
         * Called whenever a Square is clicked. Notifies the model that a move has been attempted.
         */
        @Override
        public void mouseClicked (MouseEvent e)
        {

            String a = "You win!! \nWith " + model.getMoves() + " moves. \nPress 'New Game' to start over";
            
            Square s = (Square) e.getSource();
            playerMoves.setText("Player Moves \n" + model.getPlayerMoves(s.getCol(), s.getRow()));
            model.moveTo(s.getCol(), s.getRow());
            refresh();
            if (model.areTheLightsOut())
            {
                refresh();
                // 1/2 second pause
                try
                {
                    TimeUnit.SECONDS.sleep(1 / 2);
                }
                catch (InterruptedException e1)
                {
                }
                model.setNewGameSetter();
                model.setSetupTextOn();
                // diamond shape
                model.moveTo(2, 0);
                model.moveTo(1, 1);
                model.moveTo(3, 3);
                model.moveTo(2, 4);
                model.moveTo(3, 1);
                model.moveTo(1, 3);
                model.moveTo(0, 2);
                model.moveTo(4, 2);
                refresh();

                // full checkered
                // model.moveTo(2, 0);
                // model.moveTo(1, 1);
                // model.moveTo(3, 3);
                // model.moveTo(2, 4);
                // model.moveTo(3, 1);
                // model.moveTo(1, 3);
                // model.moveTo(0, 2);
                // model.moveTo(4, 2);
                model.moveTo(2, 2);
                model.moveTo(0, 0);
                model.moveTo(4, 4);
                model.moveTo(4, 0);
                model.moveTo(0, 4);
                refresh();
                model.gameOver();
                JOptionPane.showMessageDialog(null, a);
                JOptionPane.showMessageDialog(null, "Player Moves: " + model.getTotalMoves() +
                        "\nCheating Moves Made: " + model.getManualMoves() +
                        "\nPlayer Score: " + model.getPlayerScore());

            }
        }

        @Override
        public void mousePressed (MouseEvent e)
        {
        }

        @Override
        public void mouseReleased (MouseEvent e)
        {
        }

        @Override
        public void mouseEntered (MouseEvent e)
        {
        }

        @Override
        public void mouseExited (MouseEvent e)
        {
        }
    }

    /**
     * A single square on the board where a move can be made
     */
    @SuppressWarnings("serial")
    class Square extends JPanel
    {
        /**
         * The row within the board of this Square. Rows are numbered from zero; row zero is at the bottom of the board.
         */
        private int row;

        /** The column within the board of this Square. Columns are numbered from zero; column zero is at the left */
        private int col;

        /** The current Color of this Square */
        private Color color;

        /**
         * Creates a square and records its row and column
         */
        public Square (int row, int col)
        {
            this.row = row;
            this.col = col;
            this.color = BACKGROUND_COLOR;
        }

        /**
         * Returns the row of this Square
         */
        public int getRow ()
        {
            return row;
        }

        /**
         * Returns the column of this Square
         */
        public int getCol ()
        {
            return col;
        }

        /**
         * Sets the color of this square
         */
        public void setColor (Color color)
        {
            this.color = color;
        }

        /**
         * Paints this Square onto g
         */
        @Override
        public void paintComponent (Graphics g)
        {
            g.setColor(BOARD_COLOR);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.gray);
            g.fillRect(BORDER, BORDER, getWidth() - 2 * BORDER, getHeight() - 2 * BORDER);

            // cyan square
            g.setColor(Color.lightGray);
            g.fillRect(BORDER, BORDER, getWidth() - 4 * BORDER, getHeight() - 4 * BORDER);
            // regular color
            g.setColor(color);
            g.fillRect(BORDER, BORDER, getWidth() - 6 * BORDER, getHeight() - 6 * BORDER);

        }
    }

    /**
     * A move indicator circle for use in the user interface.
     */
    @SuppressWarnings("serial")
    class MoveIndicator extends JPanel
    {
        // The color of this MoveIndicator
        private Color color;

        /**
         * Creates a MoveIndicator
         */
        public MoveIndicator ()
        {
            setPreferredSize(new Dimension(FONT_SIZE, FONT_SIZE));
            this.color = BACKGROUND_COLOR;
        }

        /**
         * Changes the color of this indicator
         */
        public void setColor (Color color)
        {
            this.color = color;
            repaint();
        }

        /**
         * Paints this MoveIndicator onto g
         */
        @Override
        public void paintComponent (Graphics g)
        {
            g.setColor(BACKGROUND_COLOR);
            g.fillRect(0, 0, getHeight(), getHeight());
            g.setColor(color);
            g.fillOval(0, 0, getHeight(), getHeight());
        }
    }
}
