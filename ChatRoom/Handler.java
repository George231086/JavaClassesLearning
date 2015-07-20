package chatroom;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.*;

/**
 *
 * @author George
 */
public class Handler implements Runnable{
    private Socket clientSocket;
    private ChatServer server;
    
    public Handler(Socket clientSocket,ChatServer server){
        this.clientSocket=clientSocket;
        this.server = server;
    }
    
  
    
    @Override
    public void run() {
        try (
                PrintWriter out
                = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                clientSocket.getInputStream()))) {
            String input;
            
            out.println(server.id++);
            
            while (true) {
                
                input = in.readLine();
                server.messageAll(input);
                System.out.println(input);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
