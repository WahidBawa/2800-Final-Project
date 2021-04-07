import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JFrame implements ActionListener {

    private static JFrame frame; //frame
    private static JLabel titleLabel; //title label
    private static JLabel player1L;
    private static JLabel player1LC;
    private static JLabel player2L;
    private static JLabel player2LC;
    private static JLabel player1LT;
    private static JLabel player2LT;
    private static JLabel player1LTC;
    private static JLabel player2LTC;
    private static JLabel outLabel;
    private static JButton buttonStart; //start button
    private static JButton buttonExit; //exit button


    public Menu() {

        frame = new JFrame();

        ImageIcon logo = new ImageIcon(new ImageIcon("Assets/checkered_flag.png").getImage().getScaledInstance(30,30, Image.SCALE_DEFAULT)); //create an image icon
        titleLabel = new JLabel("Drag Racer!"); //create label
        titleLabel.setIcon(logo);
        titleLabel.setFont(new Font("SansSerif",Font.BOLD, 30));
        titleLabel.setFont(new Font("SansSerif",Font.BOLD, 40));
        titleLabel.setIconTextGap(30); //sets the gap of image from text
        titleLabel.setHorizontalTextPosition(JLabel.RIGHT);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setVerticalAlignment(JLabel.TOP);
        titleLabel.setBounds(50, 50, 350,50);

        //label for connection of p1
        player1L = new JLabel("Player 1 data received:"); //create label
        player1L.setFont(new Font("SansSerif",Font.BOLD, 10));
        player1L.setIconTextGap(30); //sets the gap of image from text
        player1L.setHorizontalTextPosition(JLabel.RIGHT);
        player1L.setHorizontalAlignment(JLabel.CENTER);
        player1L.setVerticalAlignment(JLabel.TOP);
        player1L.setBounds(20, 140, 120,30);

        //p1 connection status
        player1LC = new JLabel("null"); //create label
        player1LC.setForeground(Color.red);
        player1LC.setFont(new Font("SansSerif",Font.BOLD, 10));
        player1LC.setIconTextGap(30); //sets the gap of image from text
        player1LC.setHorizontalTextPosition(JLabel.RIGHT);
        player1LC.setHorizontalAlignment(JLabel.CENTER);
        player1LC.setVerticalAlignment(JLabel.TOP);
        player1LC.setBounds(140, 140, 50,30);

        //label for connection of p2
        player2L = new JLabel("Player 2 data received:"); //create label
        player2L.setFont(new Font("SansSerif",Font.BOLD, 10));
        player2L.setIconTextGap(30); //sets the gap of image from text
        player2L.setHorizontalTextPosition(JLabel.RIGHT);
        player2L.setHorizontalAlignment(JLabel.CENTER);
        player2L.setVerticalAlignment(JLabel.TOP);
        player2L.setBounds(220, 140, 120,30);

        //p2 connection status
        player2LC = new JLabel("null"); //create label
        player2LC.setForeground(Color.red);
        player2LC.setFont(new Font("SansSerif",Font.BOLD, 10));
        player2LC.setIconTextGap(30); //sets the gap of image from text
        player2LC.setHorizontalTextPosition(JLabel.RIGHT);
        player2LC.setHorizontalAlignment(JLabel.CENTER);
        player2LC.setVerticalAlignment(JLabel.TOP);
        player2LC.setBounds(340, 140, 50,30);

        //label for time
        player1LT = new JLabel("Player 1 time: "); //create label
        player1LT.setFont(new Font("SansSerif",Font.BOLD, 14));
        player1LT.setIconTextGap(30); //sets the gap of image from text
        player1LT.setHorizontalTextPosition(JLabel.RIGHT);
        player1LT.setHorizontalAlignment(JLabel.CENTER);
        player1LT.setVerticalAlignment(JLabel.TOP);
        player1LT.setBounds(53, 180, 120,30);

        //label for time double value <--- we'd be setting this at the end of the race
        player1LTC = new JLabel("1.2334443 sec "); //create label
        player1LTC.setFont(new Font("SansSerif",Font.BOLD, 14));
        player1LTC.setIconTextGap(30); //sets the gap of image from text
        player1LTC.setHorizontalTextPosition(JLabel.RIGHT);
        player1LTC.setHorizontalAlignment(JLabel.CENTER);
        player1LTC.setVerticalAlignment(JLabel.TOP);
        player1LTC.setBounds(180, 180, 120,30);

        //label for p2 time
        player2LT = new JLabel("Player 2 time: "); //create label
        player2LT.setFont(new Font("SansSerif",Font.BOLD, 14));
        player2LT.setIconTextGap(30); //sets the gap of image from text
        player2LT.setHorizontalTextPosition(JLabel.RIGHT);
        player2LT.setHorizontalAlignment(JLabel.CENTER);
        player2LT.setVerticalAlignment(JLabel.TOP);
        player2LT.setBounds(53, 210, 120,30);

        //label for p2 time double value <--- we'd be setting this at the end of the race
        player2LTC = new JLabel("1.22222222 sec "); //create label
        player2LTC.setFont(new Font("SansSerif",Font.BOLD, 14));
        player2LTC.setIconTextGap(30); //sets the gap of image from text
        player2LTC.setHorizontalTextPosition(JLabel.RIGHT);
        player2LTC.setHorizontalAlignment(JLabel.CENTER);
        player2LTC.setVerticalAlignment(JLabel.TOP);
        player2LTC.setBounds(180, 210, 120,30);

        outLabel = new JLabel("Player 1 Wins!"); //create label
        outLabel.setFont(new Font("SansSerif",Font.BOLD, 20));
        outLabel.setHorizontalTextPosition(JLabel.RIGHT);
        outLabel.setHorizontalAlignment(JLabel.CENTER);
        outLabel.setVerticalAlignment(JLabel.TOP);
        outLabel.setBounds(50, 300, 350,50);

        buttonStart = new JButton();
        buttonStart.setBounds(70, 380, 100, 50);
        buttonStart.addActionListener(this);
        buttonStart.setText("Start!");
        buttonStart.setFocusable(false); //removes focus on buttonStart (text box)
        buttonStart.setSize(150, 50);
        buttonStart.setIconTextGap(-10);
        buttonStart.setFont(new Font("Comic Sans", Font.BOLD, 20));
        buttonStart.setBackground(Color.GREEN);
        buttonStart.setForeground(Color.BLACK);
        buttonStart.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        buttonExit = new JButton();
        buttonExit.setBounds(250, 380, 100, 50);
        buttonExit.addActionListener(this);
        buttonExit.setText("Exit");
        buttonExit.setFocusable(false); //removes focus on buttonStart (text box)
        buttonExit.setSize(150, 50);
        buttonExit.setIconTextGap(-10);
        buttonExit.setFont(new Font("Comic Sans", Font.BOLD, 20));
        buttonExit.setBackground(Color.RED);
        buttonExit.setForeground(Color.BLACK);
        buttonExit.setBorder(BorderFactory.createLineBorder(Color.BLACK));


        frame.setTitle("Drag Racer!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(0, 128, 255));
        frame.setSize(500, 500);
        frame.setLayout(null);
        frame.add(titleLabel);
        frame.add(buttonStart);
        frame.add(buttonExit);
        frame.add(player1L);
        frame.add(player2L);
        frame.add(player1LC);
        frame.add(player2LC);
        frame.add(player1LT);
        frame.add(player2LT);
        frame.add(player1LTC);
        frame.add(outLabel);
        frame.add(player2LTC);
        frame.setVisible(true);

    }

    public static void updateTimes(int PlayerID, String time){
        if (PlayerID==1){
            player1LC.setText("true");
            player1LC.setForeground(Color.green);
            player1LTC.setText(time);
        }
        else{
            player2LC.setText("true");
            player2LC.setForeground(Color.green);
            player2LTC.setText(time);
        }

    }

    public static void main(String[] args) {
        Menu frame = new Menu();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonStart) {
            System.out.println("Game started! please wait while the game loads");
            //buttonStart.setVisible(false);
            Road.main(null);
        }
        if (e.getSource() == buttonExit) {
            System.out.println("Bye Bye thanks for playing!");
            System.exit(0);
        }
    }


}