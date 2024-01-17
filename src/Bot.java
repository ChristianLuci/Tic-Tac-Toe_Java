import java.util.*;

public class Bot extends Board{
    boolean turn_bot;
    boolean bot_in_use;

    int bot_mark = -1;
    int player_mark = 1;

    Bot(MyFrame myFrame){
        this.turn_bot = false;
        this.frame = myFrame;
    }

public void randomPlay(){
    ArrayList<ArrayList<Integer>> emptyCells = new ArrayList<>();
    for (int f = 0; f< ROW; f++){
        for (int c = 0; c< COLUMN; c++){
            if(frame.board[f][c].getIcon()==null){
                ArrayList<Integer> coordinates = new ArrayList<>();
                coordinates.add(f);
                coordinates.add(c);
                emptyCells.add(coordinates);
            }
        }
    }
    random_num(emptyCells);
}
private void random_num(ArrayList<ArrayList<Integer>> disponible) {
    Random random = new Random();
    int fila_random;
    int columna_random;
    int random_num;
    do {
        random_num = random.nextInt(disponible.size());
        fila_random = disponible.get(random_num).get(0);
        columna_random = disponible.get(random_num).get(1);
    } while (frame.board[fila_random][columna_random].getIcon() != null);

    if (frame.turn_X) {
        frame.board[fila_random][columna_random].setIcon(frame.X);
        frame.boardState[fila_random][columna_random]=1;
    } else {
        frame.board[fila_random][columna_random].setIcon(frame.O);
        frame.boardState[fila_random][columna_random]=-1;
    }
    changeTurn(frame);
}

public void changeBotState(){
    if(!bot_in_use){
        frame.bot_bt.setText("Desactivar bot");
        bot_in_use = true;
    } else{
        frame.bot_bt.setText("Activar bot");
        bot_in_use = false;
    }

}

private boolean checkWinner(int currMark, int[] boardState){
    if (
        (boardState[0] == currMark && boardState[1] == currMark && boardState[2] == currMark) ||
        (boardState[3] == currMark && boardState[4] == currMark && boardState[5] == currMark) ||
        (boardState[6] == currMark && boardState[7] == currMark && boardState[8] == currMark) ||
        (boardState[0] == currMark && boardState[3] == currMark && boardState[6] == currMark) ||
        (boardState[1] == currMark && boardState[4] == currMark && boardState[7] == currMark) ||
        (boardState[2] == currMark && boardState[5] == currMark && boardState[8] == currMark) ||
        (boardState[0] == currMark && boardState[4] == currMark && boardState[8] == currMark) ||
        (boardState[2] == currMark && boardState[4] == currMark && boardState[6] == currMark)
    ) {
        return true;
    } else {
        return false;
    }
}
public void bestPlay(){
    //https://www.freecodecamp.org/news/minimax-algorithm-guide-how-to-create-an-unbeatable-ai/
    //https://www.geeksforgeeks.org/minimax-algorithm-in-game-theory-set-4-alpha-beta-pruning/
    int[] bestPlayArray = minimax(getBoardState(frame),bot_mark);


    int f = (int)Math.floor(bestPlayArray[0]/3);
    int c = (int) (bestPlayArray[0] - (Math.floor(bestPlayArray[0]/3)*3));


    if (frame.turn_X) {
        frame.board[f][c].setIcon(frame.X);
        frame.boardState[f][c]=1;
    } else {
        frame.board[f][c].setIcon(frame.O);
        frame.boardState[f][c]=-1;
    }

    changeTurn(frame);

    //Usar esto para transformar la coordenada dada por esta función
    /*
   B: 00-01-02
      10-11-12
      20-21-22

   B: [f,c]

   A: 012-345-678

    B=[floor(A/3),A-(floor(A/3)*3)]
    B[(int)Math.floor(A/FILA)][(int)(A-(Math.floor(A/COLUMNA)*COLUMNA))]
    A=(f*3)+c
     */
}


//HACER DEBUG PARA VER PORQUE AL QUITAR EL ULTIMO MOVIMIENTO SIEMPRE
//HACE LAS JUGADAS EN LAS CASILLAS CON EL INDICE MAS PEQUEÑO
private int[] minimax(int[] currBoardState, int currMark){

    //el array interior guarda las coordenadas de cada casilla
    int[] emptyCells = getAllEmptyCells(currBoardState);

    //check if the board is on a terminal state
    if (checkWinner(player_mark,currBoardState)) {
        int[] a = {0,1};
        return a;
    } else if (checkWinner(bot_mark,currBoardState)) {
        int[] a = {0,-1};
        return a;
    } else if (emptyCells.length == 0) {
        int[] a = {0,0};
        return a;
    }

    ArrayList<int[]> allTestPlayInfos = new ArrayList<>();

    for(int i = 0; i<emptyCells.length; i++){

        int[] result = {emptyCells[i],0};
        //here we store the index of the current and create a place to store the score of that cell

        int[] currentTestPlayInfo;

        //here we store the coordenates of the empty cell and put a mark in it
        currBoardState[emptyCells[i]] = currMark;

        //recursively checks down the tree of possible plays
        if(currMark == bot_mark){
            result[1] = minimax(currBoardState, player_mark)[1];
            currentTestPlayInfo = result;
        }else {
            result[1] = minimax(currBoardState, bot_mark)[1];
            currentTestPlayInfo = result;
        }

        //reset the cell value
        currBoardState[emptyCells[i]] = 0;
        //store the minimax value of the cell in the array
        allTestPlayInfos.add(currentTestPlayInfo);


    }

    int bestTestPlay=0,bestScore;

    if(currMark==bot_mark){
        bestScore = 100000;
        for(int i = 0; i<allTestPlayInfos.size(); i++){
            if(allTestPlayInfos.get(i)[1] < bestScore){
                bestScore = allTestPlayInfos.get(i)[1];
                bestTestPlay = i;
            }
        }

    }else{
        bestScore = -100000;
        for(int i = 0; i<allTestPlayInfos.size(); i++){
            if(allTestPlayInfos.get(i)[1] > bestScore){
                bestScore = allTestPlayInfos.get(i)[1];
                bestTestPlay = i;
            }
        }
    }
    return allTestPlayInfos.get(bestTestPlay);


    /*
   B: 00-01-02
      10-11-12
      20-21-22

   B: [f,c]

   A: 012-345-678
   B=[floor(A/3),A-(floor(A/3)*3)]
   A=(f*3)+c
   */

}


}
