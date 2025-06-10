import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TicTacToeGui extends JFrame implements ActionListener {
    private int xscore, oscore, movecounter;

    private boolean isPlayerone;

    private JLabel turnlabel, scorelabel, resultlabel;
    private JButton[][] board;
    private JDialog resultdialog;

    public TicTacToeGui() {

        super("Tic Tac Toe (Java String)");
        setSize(CommonConstants.Frame_size);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(CommonConstants.BACKGROUND_COLOR);

        //init vars
        cresteresultdialog();
        board = new JButton[3][3];

        // player x starts first
        isPlayerone = true;
        addGuiComponent();
    }

    private void addGuiComponent() {


        //barlabel

        JLabel barlabel = new JLabel();
        barlabel.setOpaque(true);
        barlabel.setBackground(CommonConstants.BAR_COLOR);
        barlabel.setBounds(0, 0, CommonConstants.Frame_size.width, 25);

        //turnlabel

        turnlabel = new JLabel(CommonConstants.X_LABEL);
        turnlabel.setHorizontalAlignment(SwingConstants.CENTER);
        turnlabel.setFont(new Font("Dialog", Font.PLAIN, 40));
        turnlabel.setPreferredSize(new Dimension(100, turnlabel.getPreferredSize().height));
        turnlabel.setOpaque(true);
        turnlabel.setBackground(CommonConstants.X_COLOR);
        turnlabel.setForeground(CommonConstants.BOARD_COLOR);
        turnlabel.setBounds(
                (CommonConstants.Frame_size.width - turnlabel.getPreferredSize().width) / 2,
                0,
                turnlabel.getPreferredSize().width,
                turnlabel.getPreferredSize().height
        );

        //score label

        scorelabel = new JLabel(CommonConstants.SCORE_LABEL);
        scorelabel.setFont(new Font("Dialog", Font.PLAIN, 40));
        scorelabel.setHorizontalAlignment(SwingConstants.CENTER);
        scorelabel.setForeground(CommonConstants.BOARD_COLOR);
        scorelabel.setBounds(0,
                turnlabel.getY() + turnlabel.getPreferredSize().height + 25,
                CommonConstants.Frame_size.width,
                scorelabel.getPreferredSize().height
        );

        //gridlayout
        GridLayout gridlayout = new GridLayout(3, 3);
        JPanel boardpanel = new JPanel(gridlayout);
        boardpanel.setBounds(0,
                scorelabel.getY() + scorelabel.getPreferredSize().height + 35,
                CommonConstants.BOARD_SIZE.width,
                CommonConstants.BOARD_SIZE.height
        );


        //create board
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                JButton button = new JButton();
                button.setFont(new Font("Dialog", Font.PLAIN, 100));
                button.setPreferredSize(CommonConstants.BUTTON_SIZE);
                button.setBackground(CommonConstants.BACKGROUND_COLOR);
                button.setBorder(BorderFactory.createLineBorder(CommonConstants.BOARD_COLOR));
                button.addActionListener(this);
                board[i][j] = button;
                boardpanel.add(board[i][j]);
            }


        }

        //reset button
        JButton resetbutton = new JButton("Reset");
        resetbutton.setFont(new Font("Dialog", Font.PLAIN, 25));
        resetbutton.addActionListener(this);
        resetbutton.setBackground(CommonConstants.BOARD_COLOR);
        resetbutton.setBounds(
                (CommonConstants.Frame_size.width - -resetbutton.getPreferredSize().width) / 2,
                CommonConstants.Frame_size.height - 100,
                resetbutton.getPreferredSize().width,
                resetbutton.getPreferredSize().height
        );

        getContentPane().add(turnlabel);
        getContentPane().add(barlabel);
        getContentPane().add(scorelabel);
        getContentPane().add(boardpanel);
        getContentPane().add(resetbutton);


    }

    private void cresteresultdialog() {
        resultdialog = new JDialog();
        resultdialog.getContentPane().setBackground(CommonConstants.BACKGROUND_COLOR);
        resultdialog.setResizable(false);
        resultdialog.setTitle("Result");
        resultdialog.setSize(CommonConstants.RESULT_DIALOG_SIZE);
        resultdialog.setLocationRelativeTo(this);
        resultdialog.setModal(true);
        resultdialog.setLayout(new GridLayout(2, 1));
        resultdialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                resetgame();
            }
        });

        //resultlabel
        resultlabel = new JLabel();
        resultlabel.setFont(new Font("Dialog", Font.BOLD, 18));
        resultlabel.setForeground(CommonConstants.BOARD_COLOR);
        resultlabel.setHorizontalAlignment(SwingConstants.CENTER);

        //restart button
        JButton restartbutton = new JButton("Play Again");
        restartbutton.setBackground(CommonConstants.BOARD_COLOR);
        restartbutton.addActionListener(this);

        resultdialog.add(resultlabel);
        resultdialog.add(restartbutton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Reset") || command.equals("Play Again")) {
            //reset the game
            resetgame();

            // only reset the score when pressing reset
            if (command.equals("Reset"))
                xscore = oscore = 0;

            if (command.equals("Play Again"))
                resultdialog.setVisible(false);
        } else {
            //PLAYER MOVE
            JButton button = (JButton) e.getSource();
            if (button.getText().equals("")) {
                movecounter++;

                // mark the button with x/o
                if (isPlayerone) {
                    //player one x
                    button.setText(CommonConstants.X_LABEL);
                    button.setForeground(CommonConstants.X_COLOR);

                    //update turen label
                    turnlabel.setText(CommonConstants.O_LABEL);
                    turnlabel.setBackground(CommonConstants.O_COLOR);

                    //update turns now
                    isPlayerone = false;
                } else {
                    //player o turn
                    button.setText(CommonConstants.O_LABEL);
                    button.setForeground(CommonConstants.O_COLOR);


                    //update turnlabel
                    turnlabel.setText(CommonConstants.X_LABEL);
                    turnlabel.setBackground(CommonConstants.X_COLOR);

                    //update turn
                    isPlayerone = true;
                }

                //check conditions of win
                if (isPlayerone) {
                    //to check to see if last move was from 0
                    checkowin();
                }
                {
                    // CHECK TT]O SEE IF LAST MOVE WAS FROM X FOR WINNING
                    checkxwin();
                }

                // check for draw
                checkdraw();

                scorelabel.setText("X: " + xscore + " | O: " + oscore);

            }

            repaint();
            revalidate();
        }

    }

    private void checkxwin() {
        String result = "X wins";

        //check rows
        for (int row = 0; row < board.length; row++) {
            if (board[row][0].getText().equals("X") && board[row][1].getText().equals("X") && board[row][2].getText().equals("X")) {
                resultlabel.setText(result);

                //display result dialog
                resultdialog.setVisible(true);

                //update score
                xscore++;
            }
        }

        //check columns
        for (int col = 0; col < board.length; col++) {
            if (board[0][col].getText().equals("X") && board[1][col].getText().equals("X") && board[2][col].getText().equals("X")) {
                resultlabel.setText(result);

                //display result dialog
                resultdialog.setVisible(true);

                //update score
                xscore++;

            }

        }

        //check diagonals
        if (board[0][0].getText().equals("X") && board[1][1].getText().equals("X") && board[2][2].getText().equals("X")) {
            resultlabel.setText(result);

            //display result dialog
            resultdialog.setVisible(true);

            //update score
            xscore++;

        }

        if (board[2][0].getText().equals("X") && board[1][1].getText().equals("X") && board[0][2].getText().equals("X")) {

            resultlabel.setText(result);

            //display result dialog
            resultdialog.setVisible(true);

            //update score
            xscore++;
        }
    }


    private void checkowin() {
        String result = "O wins";

        //check rows
        for (int row = 0; row < board.length; row++) {
            if (board[row][0].getText().equals("O") && board[row][1].getText().equals("O") && board[row][2].getText().equals("O")) {

                resultlabel.setText(result);

                //display result dialog
                resultdialog.setVisible(true);

                //update score
                oscore++;
            }
        }

        //check columns
        for (int col = 0; col < board.length; col++) {
            if (board[0][col].getText().equals("O") && board[1][col].getText().equals("O") && board[2][col].getText().equals("O")) {
                resultlabel.setText(result);

                //display result dialog
                resultdialog.setVisible(true);

                //update score
                oscore++;
            }

        }

        //check diagonals
        if (board[0][0].getText().equals("O") && board[1][1].getText().equals("O") && board[2][2].getText().equals("O")) {
            resultlabel.setText(result);

            //display result dialog
            resultdialog.setVisible(true);

            //update score
            oscore++;

        }

        if (board[2][0].getText().equals("O") && board[1][1].getText().equals("O") && board[0][2].getText().equals("O")) {

            resultlabel.setText(result);

            //display result dialog
            resultdialog.setVisible(true);

            //update score
            oscore++;
        }

    }

    private void checkdraw(){
        //if there are total of 9 moves then draw and no player has won yet
        if(movecounter >= 9){
            resultlabel.setText("Draw");
            resultdialog.setVisible(true);
        }
    }


    private void resetgame() {
        //reset player back
        isPlayerone = true;
        turnlabel.setText(CommonConstants.X_LABEL);
        turnlabel.setBackground(CommonConstants.X_COLOR);

        //rset score
        scorelabel.setText(CommonConstants.SCORE_LABEL);

        movecounter = 0;

        //reset board
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length ; j++) {
                board[i][j].setText("");
            }
        }
    }
}
