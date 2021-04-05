import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Menu extends JFrame implements ActionListener {

    private JFrame frame; //frame
    private JLabel titleLabel; //title label
    private JButton buttonStart;
    private JButton buttonExit;
  

    public Menu(){

        frame= new JFrame();

        //creating title label
        ImageIcon logo = new ImageIcon(new ImageIcon("Assets/checkered_flag.png").getImage().getScaledInstance(30,30, Image.SCALE_DEFAULT)); //create an image icon
        titleLabel = new JLabel("Drag Racer!"); //create label
        titleLabel.setIcon(logo);
        titleLabel.setFont(new Font("SansSerif",Font.BOLD, 30));
        titleLabel.setIconTextGap(30); //sets the gap of image from text
        titleLabel.setHorizontalTextPosition(JLabel.RIGHT);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setVerticalAlignment(JLabel.TOP);
        titleLabel.setBounds(50, 50, 350,50);

        buttonStart = new JButton();
        buttonStart.setBounds(70,380,100,50);
        buttonStart.addActionListener(this);
        buttonStart.setText("Start!");
        buttonStart.setFocusable(false); //removes focus on buttonStart (text box)
        buttonStart.setSize(150,50);
        buttonStart.setIconTextGap(-10);
        buttonStart.setFont(new Font("Comic Sans", Font.BOLD, 20));
        buttonStart.setBackground(Color.GREEN);
        buttonStart.setForeground(Color.BLACK);
        buttonStart.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        buttonExit = new JButton();
        buttonExit.setBounds(250,380,100,50);
        buttonExit.addActionListener(this);
        buttonExit.setText("Exit");
        buttonExit.setFocusable(false); //removes focus on buttonStart (text box)
        buttonExit.setSize(150,50);
        buttonExit.setIconTextGap(-10);
        buttonExit.setFont(new Font("Comic Sans", Font.BOLD, 20));
        buttonExit.setBackground(Color.RED);
        buttonExit.setForeground(Color.BLACK);
        buttonExit.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        

        frame.setTitle("Drag Racer!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(0,128,255));
        frame.setSize(500,500);
        frame.setLayout(null);
        frame.add(titleLabel);
        frame.add(buttonStart);
        frame.add(buttonExit);
        frame.setVisible(true);

    }

    public static void main(String[] args){
        Menu frame = new Menu();
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource()== buttonStart){
            System.out.println("Game started! please wait while the game loads");
            Road.main(null);
        }
        if (e.getSource()== buttonExit){
            System.out.println("Bye Bye thanks for playing!");
            System.exit(0);
        }
    }


}