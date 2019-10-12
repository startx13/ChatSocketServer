package chatsocketserver;

import java.net.ServerSocket;
import java.net.Socket;

public class UserThread implements Runnable
{
    private static int defUser = 12;
    private static int user = 0;
    private static int counter = 0;
    String[] defaultUsername = new String[defUser];
    public static int threadNumber = 0;
    private static final int port = 2000;
    private ServerSocket ss = null;
    private User u;
    private Socket us;
    private boolean connected = false;
    
    UserThread()
    {
        defaultUsername[0] = "Mario";
        defaultUsername[1] = "Paolo";
        defaultUsername[2] = "Gennaro";
        defaultUsername[3] = "Marco";
        defaultUsername[4] = "Silvio";
        defaultUsername[5] = "Banana Joe";
        defaultUsername[6] = "Jotaro";
        defaultUsername[7] = "Joseph";
        defaultUsername[8] = "JoJo";
        defaultUsername[9] = "Giorno Giovanna";
        defaultUsername[10] = "Matteo";
        defaultUsername[11] = "Bruno";
        
        
        UserThread.threadNumber++;
        
    }
    
    public boolean isConnected()
    {
        return connected;
    }
    
    public void sendMsg(String msg, User u)
    {
        u.sendMsg(msg);
    }

    public User getUser()
    {
        return this.u;
    }
    
    
    @Override
    public void run() 
    {
        try 
        {
            this.ss = new ServerSocket(port);
            us = ss.accept();
            us.setKeepAlive(true);
            ss.close();
            ss = null;
        } 
        catch (Exception ex) 
        {
            System.out.println("[Errore] Impossibile ottenere il socket utente");
            ex.printStackTrace();
        }
      
        int rand = 0;
        
        if(UserThread.user<UserThread.defUser)
        {
            rand = UserThread.user;
            UserThread.user++;
        }
        else
        {
            UserThread.user = 0;
            UserThread.counter++;
            rand = UserThread.user;
            UserThread.user++;
        }
        
        this.u = new User(us,defaultUsername[rand] + " - " + UserThread.counter);
        this.u.sendMsg("Connesso al Server! Username: " + defaultUsername[rand] + " - " + UserThread.counter + "\n");
        ChatSocketServer.createNewThread();
        this.connected = true;
        
        System.out.println("Connesso: " + (UserThread.threadNumber - 1) + " utenti");
        
        boolean disconnected = false;
        
        String nome = this.u.getName();
        while(!(disconnected) && this.u.isConnected())
        {
            String msg = this.u.getLastMsg();
            if(msg != null)
            {
                System.out.println("[" + nome + "]: " + msg);
                ChatSocketServer.broadcastMsg("[" + nome + "]: " + msg); 
            }
            else
            { 
                disconnected = true;
            }
        }
        
        try 
        {
            this.us.close();
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }
    }
}