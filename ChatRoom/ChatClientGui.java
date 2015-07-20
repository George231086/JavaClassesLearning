package chatroom;

/**
 *
 * @author George
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;   
import java.util.Iterator;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.UIManager;
        
public class ChatClientGui extends JFrame implements ActionListener{
    String name;
    String fullName;
    JTextArea messages;
    JTextField comment;
    JButton send;
    PrintWriter out;
    JTextArea onlinePeople;
    String input;
    ArrayList<String> onlinePeopleList= new ArrayList<>();
    
    
    public ChatClientGui(String ClientName){
        super("Chat Gui");
        name = ClientName;
        
        JPanel mainPane = new JPanel();
        JPanel commentPane = new JPanel();
        
        BoxLayout box = new BoxLayout(mainPane, BoxLayout.Y_AXIS);
        mainPane.setLayout(box);
        
        messages = new JTextArea(10, 40);
        onlinePeople = new JTextArea(10,20);
        JScrollPane scroll = new JScrollPane(messages);
        JScrollPane scrollPeople = new JScrollPane(onlinePeople);
        messages.setEditable(false);
        onlinePeople.setEditable(false);
        comment = new JTextField(20);
        JLabel label = new JLabel("Say something :");
        
        send = new JButton("Send");
        
        JPanel textAreas = new JPanel();
        textAreas.add(scroll);
        textAreas.add(scrollPeople);
        
        commentPane.add(label);
        commentPane.add(comment);
        commentPane.add(send);
        
        mainPane.add(textAreas);
        mainPane.add(commentPane);
        add(mainPane);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent){
               out.println("***NameHasGone***:"+fullName);
         }        
        }); 
        comment.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    out.println(name + ": " + comment.getText());
                    comment.setText("");
                }
            }
        });
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        setResizable(false);
        
    }
    
    public void addActionListener(){
        send.addActionListener(this);
    }
    
    public void getConnectedAndStartWork(int portNumber){
        //Set up connection, get readers and writers.
        try (
                Socket socket = new Socket("localhost", portNumber);
                PrintWriter out
                = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()));) {
            
            //Set the output stream equal to an instance variable
            //so that it's accessible to the action listener.
            
            this.out=out;
            addActionListener();
            DateFormat dateFormat = new SimpleDateFormat(/*yyyy/MM/dd*/ "HH:mm:ss");
            Date date = new Date();
            //Get id
            name+=in.readLine();
            fullName = name+" Time joined: "+dateFormat.format(date);
            out.println("***JustJoined***:"+fullName);
            
            
            
            
            //Start looping listening for incoming messages.
            while (true){
                input = in.readLine();
                processInput(input);
            }

        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } 
    }
    
    public boolean justJoined(String input){
        return input.startsWith("***JustJoined***:");
    }
    
    public boolean nameHere(String input){
        return input.startsWith("***NameIsHere***:");
    }
    
    public boolean nameLeaving(String input){
        return input.startsWith("***NameHasGone***:");
            
    }
    
    public void setOnlinePeople(){
        //Sort list to make it alphabetical. Then set the
        //text area to show who's online.
        Collections.sort(onlinePeopleList);
        Iterator ite = onlinePeopleList.iterator();
        StringBuilder builder= new StringBuilder();    
        while(ite.hasNext())
            builder.append((String)ite.next()+"\n");
        onlinePeople.setText(builder.toString());
    }
    
    public void processInput(String input){
        if (justJoined(input))
            out.println("***NameIsHere***:"+fullName);
        else if (nameHere(input)){
            String nameInput = input.substring(input.indexOf(":")+1);
            if (!onlinePeopleList.contains(nameInput))
                onlinePeopleList.add(nameInput);
            setOnlinePeople();
        }    
        else if (nameLeaving(input)){
            String nameInput = input.substring(input.indexOf(":")+1);
            onlinePeopleList.remove(nameInput);
            setOnlinePeople();
        }    
        else if (!justJoined(input) && !nameHere(input) && !nameLeaving(input))
            messages.append(input+"\n");
        
    }
    
    @Override
    public void actionPerformed(ActionEvent event){
        if (event.getSource()==send){
            out.println(name+": "+comment.getText());
            comment.setText("");
        }
    }
    
    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args){
        setLookAndFeel();
        new ChatClientGui("betty").getConnectedAndStartWork(8888);
        
    }

}
