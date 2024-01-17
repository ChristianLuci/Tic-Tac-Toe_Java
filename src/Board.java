import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public abstract class Board extends JFrame {
    MyFrame frame;
    final ImageIcon O_big = new ImageIcon("O.png");
    final ImageIcon O = new ImageIcon(O_big.getImage().getScaledInstance(200,150, Image.SCALE_SMOOTH));
    final ImageIcon X_big = new ImageIcon("X.png");
    final ImageIcon X = new ImageIcon(X_big.getImage().getScaledInstance(200,150, Image.SCALE_SMOOTH));
    final int ROW = 3;
    final int COLUMN = 3;

    int scoreCount;
    boolean ended;
    boolean firstTurn = true;

    public int[]getAllEmptyCells(int[] boardState){
        int[]emptyCells;

        ArrayList<Integer> list = new ArrayList<>();


        for (int f = 0; f< ROW * COLUMN; f++){
            if (boardState[f] == 0){
                list.add(f);
            }

        }
        emptyCells = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            emptyCells[i] = list.get(i);
        }
        return emptyCells;
    }
    public int[] getBoardState(MyFrame myframe) {
        int[] _boardState = new int[ROW * COLUMN];
        int contador = 0;

        for(int f = 0; f< ROW; f++) {
            for (int c = 0; c < COLUMN; c++) {
                _boardState[contador] = myframe.boardState[f][c];
                contador++;
            }
        }

                /*
   B: 00-01-02
      10-11-12
      20-21-22

   A: 012-345-678

    B=[floor(A/3),A-(floor(A/3)*3)]
    B[(int)Math.floor(A/FILA)][(int)(A-(Math.floor(A/COLUMNA)*COLUMNA))]
     */

        return _boardState;
    }

    public void changeTurn(MyFrame myframe){
        if(myframe.turn_X){
            myframe.turn_X = false;
            myframe.turn_lb.setText("Turno de O");
        }else{
            myframe.turn_X = true;
            myframe.turn_lb.setText("Turno de X");
        }
        // se puede hacer: turno_X = !turno_X;
    }

    public void restart(MyFrame myframe){
        for (int f = 0; f< ROW; f++){
            for (int c = 0; c< COLUMN; c++){
                myframe.board[f][c].setIcon(null);
                myframe.board[f][c].setBackground(Color.white);
                myframe.boardState[f][c] = 0;
                myframe.turn_lb.setText("Turno de X");
                myframe.turn_X = true;
                myframe.ended = false;
            }
        }
        firstTurn = true;
    }
    public void checkWinner(MyFrame myframe){

        int winner = checkIfWinner(myframe);

        switch (winner){
            case 1:
                myframe.turn_lb.setText("X HA GANADO");
                ended = true;
                break;
            case -1:
                myframe.turn_lb.setText("O HA GANADO");
                ended = true;
                break;
            case 0:
                tieCheck(myframe);
                break;
        }
    }

    private void changeColour(MyFrame myframe,int i){
        switch (i){
            case 1:
                myframe.board[0][0].setBackground(Color.green);
                myframe.board[0][1].setBackground(Color.green);
                myframe.board[0][2].setBackground(Color.green);
                break;
            case 2:
                myframe.board[1][0].setBackground(Color.green);
                myframe.board[1][1].setBackground(Color.green);
                myframe.board[1][2].setBackground(Color.green);
                break;
            case 3:
                myframe.board[2][0].setBackground(Color.green);
                myframe.board[2][1].setBackground(Color.green);
                myframe.board[2][2].setBackground(Color.green);
                break;
            case 4:
                myframe.board[0][0].setBackground(Color.green);
                myframe.board[1][0].setBackground(Color.green);
                myframe.board[2][0].setBackground(Color.green);
                break;
            case 5:
                myframe.board[0][1].setBackground(Color.green);
                myframe.board[1][1].setBackground(Color.green);
                myframe.board[2][1].setBackground(Color.green);
                break;
            case 6:
                myframe.board[0][2].setBackground(Color.green);
                myframe.board[1][2].setBackground(Color.green);
                myframe.board[2][2].setBackground(Color.green);
                break;
            case 7:
                myframe.board[0][0].setBackground(Color.green);
                myframe.board[1][1].setBackground(Color.green);
                myframe.board[2][2].setBackground(Color.green);
                break;
            case 8:
                myframe.board[2][0].setBackground(Color.green);
                myframe.board[1][1].setBackground(Color.green);
                myframe.board[0][2].setBackground(Color.green);
                break;
        }
    }

    private int checkIfWinner(MyFrame myframe){
        int winner = 0;
        scoreCount = 0;
        for(int i = 0; i<9; i++){
            switch (i){
                case 1:
                    scoreCount = myframe.boardState[0][0] + myframe.boardState[0][1] + myframe.boardState[0][2];
                    break;
                case 2:
                    scoreCount = myframe.boardState[1][0] + myframe.boardState[1][1] + myframe.boardState[1][2];
                    break;
                case 3:
                    scoreCount = myframe.boardState[2][0] + myframe.boardState[2][1] + myframe.boardState[2][2];
                    break;
                case 4:
                    scoreCount = myframe.boardState[0][0] + myframe.boardState[1][0] + myframe.boardState[2][0];
                    break;
                case 5:
                    scoreCount = myframe.boardState[0][1] + myframe.boardState[1][1] + myframe.boardState[2][1];
                    break;
                case 6:
                    scoreCount = myframe.boardState[0][2] + myframe.boardState[1][2] + myframe.boardState[2][2];
                    break;
                case 7:
                    scoreCount = myframe.boardState[0][0] + myframe.boardState[1][1] + myframe.boardState[2][2];
                    break;
                case 8:
                    scoreCount = myframe.boardState[2][0] + myframe.boardState[1][1] + myframe.boardState[0][2];
                    break;
            }
            if(scoreCount == 3){
                winner = 1;
                changeColour(myframe,i);
                return winner;
            }else if(scoreCount == -3){
                winner = -1;
                changeColour(myframe,i);
                return winner;
            }

        }
        return winner;
    }

    private void tieCheck(MyFrame myframe){
        boolean tie = true;
        for (int f = 0; f< ROW; f++){
            for (int c = 0; c< COLUMN; c++){
                if(myframe.board[f][c].getIcon() == null){
                    tie = false;
                }
            }
        }

        if(tie){
            myframe.turn_lb.setText("EMPATE");
            ended = true;
        }
    }
}
