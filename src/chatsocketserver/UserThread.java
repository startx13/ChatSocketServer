package chatsocketserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class UserThread implements Runnable
{
    private final int defUser = 6;
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
        
        UserThread.threadNumber++;
        
    }
    
    public boolean isConnected()
    {
        return connected;
    }
    
    public void sendMsg(String msg, User u)
    {
        //System.out.println("USERTHREAD: MSG: " + msg + " U: " + u);
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
        Random r = new Random();
        int rand = r.nextInt(defUser);
        
        this.u = new User(us,defaultUsername[rand]);
        this.u.sendMsg("Connesso al Server! Username: " + defaultUsername[rand] + "\n");
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
                ChatSocketServer.broadcastMsg("[" + nome + "]: " + msg); //+ "\n");
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