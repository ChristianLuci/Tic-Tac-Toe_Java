import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.*;
import java.awt.*;
import java.util.Random;

public class MyFrame extends Board implements ActionListener {
    JLabel turn_lb, dificulty_lb;
    JPanel p, options_panel;
    JButton bot_bt;
    Bot bot;
    Player player;
    JFrame myframe = new JFrame();
    boolean turn_X = true;
    JButton[][] board;
    int[][] boardState = new int[ROW][COLUMN];
    String[] dificulties_available = {"Imposible","Dificil", "Normal", "Facil"};
    int dificulty;
    JComboBox dificulties_box;

    MyFrame(){
        p = new JPanel();
        player = new Player(this);
        bot = new Bot(this);
        bot.bot_in_use = false;

        Border border = BorderFactory.createLineBorder(Color.black,1,true);

        myframe.setSize(900,700);
        myframe.setTitle("Tic Tac Toe");
        myframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        myframe.setLayout(null);
        myframe.setLocationRelativeTo(null);
        myframe.setResizable(false);
        myframe.getContentPane().setBackground(Color.white);

        ImageIcon img_furina_grande = new ImageIcon("X.png");
        Image img_furina_resized = img_furina_grande.getImage().getScaledInstance(200,200, Image.SCALE_SMOOTH);
        ImageIcon img_furina = new ImageIcon(img_furina_resized);
        myframe.setIconImage(img_furina.getImage());

        board = new JButton[ROW][COLUMN];
        initialiceBoard(p);

        turn_lb = new JLabel("Turno de X");
        turn_lb.setBounds(-6,0,p.getWidth(),100);
        turn_lb.setFont(new Font(Font.DIALOG, Font.BOLD,25));
        turn_lb.setForeground(Color.black);
        turn_lb.setBackground(Color.white);
        turn_lb.setOpaque(true);
        turn_lb.setHorizontalAlignment(JLabel.CENTER);
        myframe.add(turn_lb);
        myframe.add(p);

        dificulty_lb = new JLabel("Dificultad");
        dificulty_lb.setBounds(turn_lb.getWidth()-4, 0, myframe.getWidth()-p.getWidth(), turn_lb.getHeight());
        dificulty_lb.setFont(new Font(Font.DIALOG, Font.BOLD,25));
        dificulty_lb.setForeground(Color.black);
        dificulty_lb.setBackground(Color.white);
        dificulty_lb.setOpaque(true);
        dificulty_lb.setBorder(border);
        dificulty_lb.setHorizontalAlignment(JLabel.CENTER);
        myframe.add(dificulty_lb);

/*
        options_panel = new JPanel();
        options_panel.setBounds(p.getWidth()-4, dificulty_lb.getHeight(), dificulty_lb.getWidth(), p.getHeight());
        options_panel.setBackground(Color.white);
        options_panel.setOpaque(true);
        options_panel.setBorder(border);
        myframe.add(options_panel);*/


        dificulties_box = new JComboBox(dificulties_available);
        dificulties_box.addActionListener(this);
        dificulties_box.setSize(100,50);
        dificulties_box.setBounds((p.getWidth()-4)+(int)(dificulty_lb.getWidth()/2)- dificulties_box.getWidth()/2, dificulty_lb.getHeight()+(p.getHeight()/2), dificulties_box.getWidth(), dificulties_box.getHeight());
        myframe.add(dificulties_box);

        /*
        En este link hay un ejemplo de como usar un array de botones
        https://stackoverflow.com/questions/22028923/java-minesweeper-gui
         */

        JButton restart = new JButton("Reiniciar tablero");
        restart.setBounds(-1,580,(p.getWidth()/2),83);
        restart.setVisible(true);
        restart.setBackground(Color.white);
        restart.setBorder(border);
        restart.setActionCommand("reiniciar");
        restart.addActionListener(this);
        myframe.add(restart);

        bot_bt = new JButton("Activar bot");
        bot_bt.setBounds(344,580,(p.getWidth()/2),83);
        bot_bt.setVisible(true);
        bot_bt.setBackground(Color.white);
        bot_bt.setBorder(border);
        bot_bt.setActionCommand("bot");
        bot_bt.addActionListener(this);
        myframe.add(bot_bt);

        myframe.setVisible(true);
    }



    private void initialiceBoard(JPanel p){
        p.setLayout(new GridLayout(ROW, COLUMN,0,0));
        for (int f = 0; f< ROW; f++){
            for (int c = 0; c< COLUMN; c++){
                board[f][c] = new JButton();
                board[f][c].setBackground(Color.white);
                String accion = String.valueOf(f) + String.valueOf(c);
                board[f][c].setActionCommand(accion);
                board[f][c].addActionListener(this);

                Border border = BorderFactory.createLineBorder(Color.black,2,false);
                board[f][c].setBorder(border);
                p.add(board[f][c]);
            }
        }
        p.setBounds(-2,100,690,480);
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource() == dificulties_box){
            dificulty = dificulties_box.getSelectedIndex();
            return;
        }

        String accion = e.getActionCommand();
        if("reiniciar".equals(accion)){
            restart(this);
            /*int[] asd = {0,1,-1,-1,1,1,-1,0,0};
            int[] prueba = bot.minimax(asd,1, 3);
            for(int i:prueba){
                System.out.println(i);
            }
            System.out.println(lastPlacement);
*/
            //System.out.println(Arrays.toString(getBoardState(this)));
            //System.out.println(Arrays.toString(getAllEmptyCells(getBoardState(this))));
            return;
        }
        if("bot".equals(accion)){
            bot.changeBotState();
            return;
        }
        if(!ended){
            int f = Character.getNumericValue(accion.toCharArray()[0]);
            int c = Character.getNumericValue(accion.toCharArray()[1]);
            if(firstTurn){
                firstTurn = false;
            }
            if(board[f][c].getIcon() != null){
                return;
            }
            player.play(f,c, turn_X);
            checkWinner(this);
        }
        if(!ended && bot.bot_in_use){
            botPlay(dificulty);
        }

    }

    private void botPlay(int dificulty){
        Random rd = new Random();
        System.out.println(dificulty);
        switch (dificulty){
            case 0:
                bot.bestPlay();

                break;
            case 1:
                if(rd.nextInt(10)+1>8){
                    bot.randomPlay();
                }else{
                    bot.bestPlay();
                }
                break;
            case 2:
                if(rd.nextInt(10)+1>6){
                    bot.randomPlay();
                }else{
                    bot.bestPlay();
                }
                break;
            case 3:
                if(rd.nextInt(10)+1>2){
                    bot.randomPlay();
                }else{
                    bot.bestPlay();
                }
                break;
        }
        checkWinner(this);
    }

}