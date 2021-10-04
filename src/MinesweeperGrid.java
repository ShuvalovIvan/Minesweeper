import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

public class MinesweeperGrid extends JFrame {
    private int rows, columns, numBombs, numFlags, cellsRemaining, initialCellAmount;
    private boolean hasLost, hasWon;
    private MinesweeperCell[] cells;
    private List<Point> bombCells = new ArrayList<>();
    private JLabel flagsRemainingLabel;

    public MinesweeperGrid(int columns, int rows, int numBombs){
        setTitle("MineSweepers");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(450,500));
        setLayout(new GridLayout(rows+1, columns));
        this.rows = rows;
        this.columns = columns;
        this.numBombs = numBombs;
        this.numFlags = numBombs;
        this.cellsRemaining = columns*rows-numBombs;
        this.initialCellAmount = columns * rows;
        this.hasLost = false;
        this.hasWon = false;
        this.cells = new MinesweeperCell[rows*columns];
        for(int row=0;row<rows;row++){
            for(int col=0;col<columns;col++){
                Point coord = new Point(row, col);
                this.cells[row*columns+col] = new MinesweeperCell(coord, this);
                getContentPane().add(new MinesweeperCell(coord, this));
            }
        }
        for(int i=0; i<columns/2;i++){
            getContentPane().add(new Component() {});
        }
        flagsRemainingLabel = new JLabel(""+numFlags, JLabel.CENTER);
        getContentPane().add(flagsRemainingLabel);
    }

    public int getRows(){
        return rows;
    }

    public int getColumns(){
        return columns;
    }

    public void makeBombs(Point initCoord){
        while(bombCells.size()!=numBombs){
            Point loc = new Point((int)(Math.random()*(rows-1)), (int)(Math.random()*(columns-1)));
            List<Point> neighborCells = getNeighborCells(initCoord);
            if(!bombCells.contains(loc)&&!neighborCells.contains(loc)){
                bombCells.add(loc);
                cells[(int)(loc.getX()*loc.getY()+loc.getX())].setNumber(-1);
            }
        }
        for(Point loc : bombCells){
            for(int x = -1; x<=1;x++){
                for(int y = -1; y<=1; y++){
                    Point cell = new Point((int)loc.getX()+x,(int)loc.getY()+y);
                    if(rows>cell.getX() && cell.getX()>=0 && columns > loc.getY() && loc.getY() >= 0 && !cells[(int)(loc.getX()*loc.getY()+loc.getX())].isMine())
                        cells[(int)(loc.getX()*loc.getY()+loc.getX())].setNumber(cells[(int)(loc.getX()*loc.getY()+loc.getX())].getNumber()+1);
                }
            }
        }
        uncoverNeighborCells(initCoord);
    }

    public void mineTriggered(){
        for(MinesweeperCell cell : cells){
            if(!cell.isFlag()&&cell.isMine()){
                cell.setForeground(Color.RED);
                cell.setText("*");
            }
            if(cell.isFlag()&&!cell.isMine()){
                cell.setForeground(Color.RED);
                cell.setText("X");
            }
        }
    }

    private List<Point> getNeighborCells(Point loc){
        List<Point> neighbors = new ArrayList<>();
        for(int x = -1; x<=1;x++){
            for(int y = -1; y<=1; y++){
                neighbors.add(new Point((int)loc.getX()+x,(int)loc.getY()+y));
            }
        }
        return neighbors;
    }

    public int getInitialCellAmount(){
        return initialCellAmount;
    }

    public void uncoverNeighborCells(Point coord){
        for(int x = -1; x<=1;x++){
            for(int y = -1; y<=1; y++){
                Point cell = new Point((int)coord.getX()+x,(int)coord.getY()+y);
                if(rows>cell.getX() && cell.getX()>=0 && columns > coord.getY() && coord.getY() >= 0 && !cells[(int)(coord.getX()*coord.getY()+coord.getX())].isUncovered() && !cells[(int)(coord.getX()*coord.getY()+coord.getX())].isMine()) {
                    cells[(int) (coord.getX() * coord.getY() + coord.getX())].uncover();
                    cells[(int) (coord.getX() * coord.getY() + coord.getX())].update();
                }
            }
        }
    }

    public int countRemainingCells(){
        return 0;
    }

    public int countRemainingFlags(){
        return 0;
    }

    public boolean hasLost() {
        return hasLost;
    }

    public void hasLost(boolean bool) {
        hasLost=bool;
    }

    public boolean hasWon(){
        return hasWon;
    }
}
