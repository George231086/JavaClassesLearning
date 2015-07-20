package chatroom;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ChatServer extends Thread{
    private ServerSocket sock;
    private final HashMap<Socket,OutputStream> socketTable = new HashMap<>();
    volatile int id;

    
    
    public ChatServer(){
        super();
        try{
            sock = new ServerSocket(8888);            
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    
    
    public void run(){
        Socket client = null;
        
        while (true){
            if (sock == null)
                return;
            
            try{
                client = sock.accept();
                OutputStream os = client.getOutputStream();
                socketTable.put(client, os);
                new Thread(new Handler(client,this)).start();
            }catch (IOException e){
               e.printStackTrace() ;
            }
        
        }
    }
    
    public void removeClosedSockets() {
        synchronized (socketTable) {
            for (Socket socket : socketTable.keySet())
                if (socket.isClosed())
                    socketTable.remove(socket);
        }
    }
    
    public void messageAll(String message) {
        //Remove sockets that have been closed.
        removeClosedSockets();
        synchronized (socketTable) {
            for (OutputStream os : socketTable.values()) {
                PrintWriter out = new PrintWriter(os, true);
                out.println(message);
            }
        }

    }

   
    public static void main(String[] args){
        new ChatServer().run();
    }
        
    
    
}