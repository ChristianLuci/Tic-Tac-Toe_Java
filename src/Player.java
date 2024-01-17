
public class Player extends Board{

    Player(MyFrame myframe){
        this.frame = myframe;
    }

    public void play(int f, int c, boolean turn_X){
        if(turn_X){
            frame.board[f][c].setIcon(X);
            frame.boardState[f][c]=1;
            changeTurn(frame);

        } else {
            frame.board[f][c].setIcon(O);
            frame.boardState[f][c]=-1;
            changeTurn(frame);
        }

        //System.out.println(Arrays.toString(getBoardState(frame)));
        //System.out.println(Arrays.toString(getEmptyIndex(frame)));

    }
}
