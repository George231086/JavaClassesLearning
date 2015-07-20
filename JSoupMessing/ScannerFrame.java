package jsoupmessing;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.DriverManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.Date;
import java.sql.*;
import javax.swing.JOptionPane;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Stack;
import java.io.FileWriter;
import java.io.BufferedWriter;
/**
 *
 * @author George
 */
public class ScannerFrame extends JFrame implements ActionListener, Runnable {
    //Declare instance variables, many swing related for the gui.

    private ScheduledExecutorService scheduledExecutorService;
    private boolean autoUpdateOn;

    private JTextField url;
    private JTextField tag;

    private String webPage = "http://www.dailymail.co.uk/home/index.html";
    private String searchedForTag = "h2";

    private JTextArea currentResults;
    private JTextArea results;

    private JButton update;
    private JButton currentResultsButton;
    private JButton showDBEntries;
    private JButton exportToTextFile;

    private JRadioButton autoUpdateRButton;

    private String datasource;
    private String userName;
    private String password;
    private String driverClassName;
    private String tableName;

    // Constructor to set up gui and hook up components to action listeners. Also to
    // provide the login, etc for the default database and table to use.
    public ScannerFrame(String dataSource,String userName, String password,
            String driverClassName,String tableName ) {
        super("Scanner");
        this.datasource = dataSource;
        this.userName = userName;
        this.password = password;
        this.driverClassName = driverClassName;
        this.tableName = tableName;

        JPanel mainPane = new JPanel();
        JPanel buttonPane1 = new JPanel();
        JPanel buttonPane2 = new JPanel();
        JPanel headlineDBPane = new JPanel();
        JPanel headlineCurrentPane = new JPanel();
        JPanel urlPane = new JPanel();
        JPanel tagPane = new JPanel();

        JLabel urlLab = new JLabel("URL:");
        url = new JTextField(20);
        url.setText(webPage);
        JLabel tagLab = new JLabel("Tag:");
        tag = new JTextField(20);
        tag.setText(searchedForTag);
        JLabel rbuttonLab = new JLabel("Auto-Update");

        update = new JButton("Update Database");
        currentResultsButton = new JButton("Current headlines");
        showDBEntries = new JButton("Show latest db entries");
        exportToTextFile = new JButton("Export to text file");

        autoUpdateRButton = new JRadioButton();

        urlPane.add(urlLab);
        urlPane.add(url);
        tagPane.add(tagLab);
        tagPane.add(tag);

        buttonPane1.add(currentResultsButton);

        buttonPane2.add(rbuttonLab);
        buttonPane2.add(autoUpdateRButton);
        buttonPane2.add(update);
        buttonPane2.add(showDBEntries);
        buttonPane2.add(exportToTextFile);

        results = new JTextArea(10, 40);
        currentResults = new JTextArea(10, 40);
        JScrollPane scroll = new JScrollPane(results);
        JScrollPane scroll2 = new JScrollPane(currentResults);

        headlineDBPane.add(scroll);
        headlineCurrentPane.add(scroll2);

        BoxLayout box = new BoxLayout(mainPane, BoxLayout.Y_AXIS);
        mainPane.setLayout(box);

        mainPane.add(urlPane);
        mainPane.add(tagPane);
        mainPane.add(headlineCurrentPane);
        mainPane.add(buttonPane1);
        mainPane.add(headlineDBPane);
        mainPane.add(buttonPane2);

        add(mainPane);
        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
    
    // Alternative constructor to allow different table to be given.
    public ScannerFrame(String table) {
        this("jdbc:derby://localhost:1527/sample","app","app",
                "org.apache.derby.jdbc.ClientDriver",table);
    }
    
    //Default constructor connecting to derby db.
    public ScannerFrame(){
        this("jdbc:derby://localhost:1527/sample","app","app",
                "org.apache.derby.jdbc.ClientDriver","APP.Test");
    }
    
    //Method to add action listeners.
    public void addActionListeners(){
        update.addActionListener(this);
        currentResultsButton.addActionListener(this);
        autoUpdateRButton.addActionListener(this);
        showDBEntries.addActionListener(this);
        exportToTextFile.addActionListener(this);

    }

    //Hook up components
    @Override
    public void actionPerformed(ActionEvent e) {
        //Check if headlines are in db table, add them if not and display
        //last 100 results in text area.
        if (e.getSource() == update) {
            updateHeadlineDB();
            showDBHeadlines();

        } else if (e.getSource() == autoUpdateRButton) {
            // Start auto update if radio button is selected, turn it off if not.
            JRadioButton r = (JRadioButton) e.getSource();
            if (r.isSelected()) {
                autoUpdateOn = true;
                System.out.println("Auto update on");
                this.autoUpdate();
            } else {
                autoUpdateOn = false;
                System.out.println("Auto update off");
                scheduledExecutorService.shutdown();
            }
        } else if (e.getSource() == currentResultsButton) {
            this.showCurrentResults();
        } else if (e.getSource() == showDBEntries) {
            this.showDBHeadlines();
        }else if (e.getSource() == exportToTextFile){
            this.exportToTextFile();
            System.out.println("Exported");
        }

    }

    // Update database.
    public String updateHeadlineDB() {

        StringBuilder builder = new StringBuilder();
        try (
                Connection conn = DriverManager.getConnection(datasource, userName, password)) {
            Class.forName(driverClassName);

            //After connection to db is made, prepare statements to insert new headlines.
            PreparedStatement prepInsert = conn.prepareStatement("INSERT INTO "+ tableName+
                    " (HEADLINE,DATESCRAPED,href)"
                    + "VALUES(?,?,?)");
            
            //Use JSoup to connect to given url and then obtain all elements of a given tag type on the page
            Document doc = Jsoup.connect(url.getText()).get();
            Elements s = doc.select(tag.getText());

            //Extract text from tag, execute query to see if text is already in the Headline column.
            //If the result set is empty then we need to add the headline to our table along with
            //the date it was scraped and a link to the story.
            int count = 0;
            for (Element element : s) {
                String text = element.text();
                PreparedStatement prepCheck = conn.prepareStatement("SELECT * FROM " + tableName 
                        + " WHERE CAST(HEADLINE AS VARCHAR(2000)) = ?");
                prepCheck.setString(1, text);

                ResultSet rset = prepCheck.executeQuery();

                if (!rset.next()) {
                    //Needed due to "CAST(HEADLINE AS VARCHAR(2000))" above,
                    //otherwise big strings will cause a sql exception.
                    if (text.length()>2000){
                        text = text.substring(0, 2000);
                    }
                    prepInsert.setString(1, text);
                    Date date = new Date();
                    prepInsert.setString(2, new java.sql.Timestamp(date.getTime()).toString());
                    String href = element.getElementsByAttribute("href").attr("abs:href");
                    prepInsert.setString(3, href);
                    prepInsert.executeUpdate();
                    builder.append(text);
                    count++;
                }

            }
            if (count > 1 && !autoUpdateOn) {
                JOptionPane.showMessageDialog(null, "There are " + count + " new entries!");
                System.out.println(count + " new entries!");
            } else if (count == 1 && !autoUpdateOn) {
                JOptionPane.showMessageDialog(null, "There is 1 new entry!");
                System.out.println("1 new entry!");
            } else if (count == 0 && !autoUpdateOn) {
                JOptionPane.showMessageDialog(null, "There are no new entries!");
                System.out.println("No new entries");
            }
            conn.close();

        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

    //Needed to make the class runnable so that scheduledExectorService could be used
    // for auto update.
    @Override
    public void run() {

        System.out.println("running update");
        updateHeadlineDB();
        showDBHeadlines();
    }
    //Schedule the database to be updated every minute.
    public void autoUpdate() {
        scheduledExecutorService
                = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(
                this, 0, 1, TimeUnit.MINUTES);

    }
    
    //For the given url and tag name this method will show the current results in
    //the first text area.
    //Essentially a tag reader method, can be used to see what's there before 
    //updating.
    public void showCurrentResults() {

        StringBuilder builder = new StringBuilder();
        try {
            Document doc = Jsoup.connect(url.getText()).get();
            Elements s = doc.select(tag.getText());

            for (Element element : s) {
                String text = element.text();
                builder.append(text + "\n");
            }
            currentResults.setText(builder.toString());
            currentResults.setCaretPosition(0);
        } catch (IOException ioe) {
            currentResults.setText("Error loading current headlines! Please check the URL"
                    + "and Tag given!");
            ioe.printStackTrace();
        }

    }
    
    //Method to show the most recent 100 additions to the database.
    public void showDBHeadlines() {
        try (
                Connection conn = DriverManager.getConnection(datasource, userName, password)) {
            Class.forName(driverClassName);

            //Get total number of rows in database.
            PreparedStatement prepGetRowNo = conn.prepareStatement("SELECT COUNT(*) from " + tableName);
            ResultSet resCount = prepGetRowNo.executeQuery();
            resCount.next();
            int noRows = Integer.parseInt(resCount.getString("1"));

            //Get last 100 rows from database.
            PreparedStatement prepGetHeadLines = conn.prepareStatement("SELECT * from " + tableName + " WHERE ID >" + (noRows - 100));
            ResultSet res = prepGetHeadLines.executeQuery();

            /* Get values from headline column, add them all to a stack then
             pop them into a StringBuilder to reverse order so that most recent
             additions to database are displayed first when text is set in results
             text area. Carat in text area is moved to the top so user can scroll
             down rather than have to scroll to the top.
             */
            StringBuilder builder = new StringBuilder();
            Stack<String> headlineStack = new Stack<>();
            for (int i = 0; i < 100; i++) {
                if (res.next()) {

                    headlineStack.add(res.getString("HEADLINE") + "\n");
                }

            }
            for (int i = 0; i < headlineStack.size(); i++) {
                builder.append(headlineStack.pop());
            }
            results.setText(builder.toString());
            results.setCaretPosition(0);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    //Wrote as intially did not put an auto increment column in my table
    // and didn't seem to be able to alter to add one to an existing table in Java Derby.
    public void createAndPopulateTableJDB(String oldTable, String newTable) {
        try (
                Connection conn = DriverManager.getConnection(datasource, userName, password)) {
            Class.forName(driverClassName);

            PreparedStatement getRows = conn.prepareStatement("SELECT * from " + oldTable);
            ResultSet resRows = getRows.executeQuery();

            PreparedStatement createTable = conn.prepareStatement("CREATE TABLE APP." + newTable + " (id INTEGER NOT NULL"
                    + "                     GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                    + "                     HEADLINE LONG VARCHAR NOT NULL,"
                    + "                     DATESCRAPED LONG VARCHAR NOT NULL,"
                    + "                     HREF LONG VARCHAR ,"
                    + "                     CONSTRAINT primary_key PRIMARY KEY (id))");
            createTable.execute();
            PreparedStatement prepInsert = conn.prepareStatement("INSERT INTO " + newTable + "(HEADLINE,DATESCRAPED,href)"
                    + "VALUES(?,?,?)");
            while (resRows.next()) {
                prepInsert.setString(1, resRows.getString("HEADLINE"));
                prepInsert.setString(2, resRows.getString("DATESCRAPED"));
                prepInsert.setString(3, resRows.getString("HREF"));
                prepInsert.executeUpdate();
            }
        } catch (SQLException | ClassNotFoundException sqe) {
            sqe.printStackTrace();
        }

    }
    
    //Method to output the database to a text file for further analysis.
    public void exportToTextFile(){
        try (   FileWriter fw = new FileWriter(tableName + ".txt");
                BufferedWriter buff = new BufferedWriter(fw);
                Connection conn = DriverManager.getConnection(datasource, userName, password)
                ) {
            //Get Filewriter and wrap it in a BufferedOutputStream
            StringBuilder builder;
            Class.forName(driverClassName);
            //Retrieve database entries
            PreparedStatement getRows = conn.prepareStatement("SELECT * from " + tableName);
            ResultSet resRows = getRows.executeQuery();
            builder = new StringBuilder();
            //Add rows to stringbuilder, the data is separated by ; as commas are too common in the
            //headline data.
            while (resRows.next()) {
                builder.append(resRows.getString("HEADLINE") + " ; " + resRows.getString("DATESCRAPED") + " ; "
                        + resRows.getString("HREF") + "\n");
            }   //Output to file.
            buff.write(builder.toString());
            System.out.println(builder.toString());
        } catch (SQLException | ClassNotFoundException | IOException sqe) {
            sqe.printStackTrace();
        }

    }
    
    public static void main(String args[]){
        ScannerFrame sc =  new ScannerFrame("APP.TEST2");
        sc.addActionListeners();
    }
        
    
}
