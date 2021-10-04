import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MinesweeperCell extends JButton {
    private Point coord;
    private int number;
    private boolean flag, uncovered;
    private final MinesweeperGrid grid;

    public MinesweeperCell(Point coord, MinesweeperGrid grid){
        this.coord = coord;
        this.number = 0;
        this.flag = false;
        this.uncovered = false;
        this.grid = grid;
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(grid.getWidth()/grid.getColumns(),grid.getHeight()/grid.getRows()));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getButton() == 1)
                    uncover();
                if(e.getButton() == 3)
                    setFlag();
            }
        });
    }

    public Point getCoord(){
        return coord;
    }

    public int getNumber(){
        return number;
    }

    public boolean isFlag() {
        return flag;
    }

    public boolean isMine(){
        return getNumber()==-1;
    }

    public boolean isUncovered() {
        return uncovered;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void update(){
        if(isUncovered()){
            this.getModel().setPressed(true);
            if(isMine()){
                this.setBackground(Color.RED);
                this.setText("*");
                grid.hasLost(true);
                grid.mineTriggered();
                JOptionPane.showMessageDialog(grid, "KABOOM! you lose.", "Minesweeper", JOptionPane.ERROR_MESSAGE);
            } else {
                this.setBackground(Color.GRAY);
                Color[] colorMap = {Color.WHITE, Color.BLUE, Color.GREEN.darker(), Color.RED, Color.MAGENTA.darker(), Color.RED.darker(), Color.CYAN, Color.BLACK, Color.DARK_GRAY};
                if(number==0){
                    grid.uncoverNeighborCells(coord);
                } else {
                    setText(""+number);
                    setForeground(colorMap[number]);
                }
            }
        } else {
            if(isFlag())
                setText("*");
            if(!isFlag())
                setText("");
        }
    }

    public void setUncovered(boolean uncover){
        uncovered = uncover;
    }

    public void uncover(){
        if(grid.countRemainingCells()==grid.getInitialCellAmount()){
            grid.makeBombs(coord);
            setUncovered(true);
        } else if (!grid.hasLost() && !grid.hasWon() && !isFlag() && isUncovered()){
            setUncovered(true);
            update();
        }
    }

    public void setFlag(){

    }
}
