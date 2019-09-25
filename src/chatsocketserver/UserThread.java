package chatsocketserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;


public class UserThread implements Runnable
{
    private int defUser = 6;
    String[] defaultUsername = new String[defUser];
    public static int threadNumber = 0;
    private static final int port = 99;
    private ServerSocket ss = null;
    private User u;
    private Socket us;
    
    UserThread()
    {
        defaultUsername[0] = "Mario";
        defaultUsername[1] = "Paolo";
        defaultUsername[2] = "Gennaro";
        defaultUsername[3] = "Marco";
        defaultUsername[4] = "Silvio";
        defaultUsername[5] = "Banana Joe";
        
        UserThread.threadNumber++;
        
        Thread t = new Thread(this,"UserThread-" + UserThread.threadNumber);
        t.start();
    }
    
    synchronized public void sendMsg(String msg)
    {
        u.sendMsg(msg);
    }

    @Override
    public void run() 
    {
        try 
        {
            this.ss = new ServerSocket(port);
            us = ss.accept();
            ss.close();
            ss = null;
        } 
        catch (Exception ex) 
        {
            System.out.println("[Errore] Impossibile ottenere il socket utente");
            ex.printStackTrace();
        }
        Random r = new Random();
        int rand = r.nextInt(defUser - 1);
        u = new User(us,defaultUsername[rand]);
        u.sendMsg("Connesso al Server! Username: " + defaultUsername[rand] + "\n");
        ChatSocketServer.createNewThread();
        
        System.out.println("Connesso: " + (UserThread.threadNumber - 1) + " utenti");
        
        while(true && u.isConnected())
        {
                System.out.println("[" + u.getName() + "]: " + u.getLastMsg());
                //ChatSocketServer.broadcastMsg("[" + u.getName() + "]: " + u.getLastMsg());
        }
    }
}
