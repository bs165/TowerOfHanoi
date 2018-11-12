// Virginia Tech Honor Code Pledge:
//
// As a Hokie, I will conduct myself with honor and integrity at all times.
// I will not lie, cheat, or steal, nor will I 
// accept the actions of those who do.
// -- Brad Sullivan bs165

package towerofhanoi;
import java.awt.Color;
import java.util.Observable;
import java.util.Observer;
import CS2114.Button;
import CS2114.Shape;
import CS2114.Window;
import CS2114.WindowSide;

/**
 * GameWindow creates the window that the 
 * game is viewed in.
 * 
 * @author Brad Sullivan bs165
 * @version 10/22/2018
 */

public class GameWindow implements Observer
{
    private Window window;
    private Shape left;
    private Shape middle;
    private Shape right;
    private HanoiSolver game;
    private final int DISC_GAP = 2;
    public static final int DISC_HEIGHT = 10;
    
    public GameWindow(HanoiSolver game)
    {
        this.game = game;
        this.game.addObserver(this);
        this.window = new Window("Tower of Hanoi");
        
        this.left = new Shape(150, 50, 3, 125,
            new Color(50, 50, 50));
        this.middle = new Shape(300, 50, 3, 125,
            new Color(75, 75, 75));
        this.right = new Shape(450, 50, 3, 125,
            new Color(100, 100, 100));
        
        for (int i = 0; i < game.discs(); i++)
        {
            this.game.getTower(Position.LEFT).
            push(new Disc(30 - i * 5));
            this.window.addShape(
                this.game.getTower(Position.LEFT).peek());
            moveDisc(Position.LEFT);
        }
        
        this.window.addShape(this.left);
        this.window.addShape(this.middle);
        this.window.addShape(this.right);
        
        Button solve = new Button("Solve");
        this.window.addButton(solve, WindowSide.SOUTH);
        solve.onClick(this, "clickedSolve");
    }
    
    private void sleep() {
        try {
            Thread.sleep(500);
        }
        catch (Exception e) {
        }
    }

    public void clickedSolve(Button button) {
        button.disable();
        new Thread() {
            public void run() {
                game.solve();
            }
        }.start();
    }
    
    @Override
    public void update(Observable o, Object arg) 
    {
        if (arg.getClass() == Position.class)
        {
            Position p1 = (Position)arg;
            moveDisc(p1);
        }
        sleep();
    }
    
    private void moveDisc(Position position)
    {
        Disc currentDisc = this.game.
            getTower(position).peek();
        Shape currentPole;
        if (position == Position.MIDDLE)
        {
            currentPole = this.middle;
        }
        else if (position == Position.RIGHT)
        {
            currentPole = this.right;
        }
        else
        {
            currentPole = this.left;
        }
        
        int getX = currentPole.getX();
        int getY = currentPole.getY() + 125 - 
            (this.game.getTower(position).size() * 
                (DISC_HEIGHT + DISC_GAP));
        
        currentDisc.moveTo(getX - 
            (currentDisc.getWidth()-5)/2 , getY - 2);
        
    }
    
    
}
