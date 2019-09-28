package chatsocketserver;

import java.util.LinkedList;
import java.util.ListIterator;

public class ChatSocketServer {

    static UserThread[] ut = new UserThread[50];
    
    public static void main(String[] args) 
    {
        //ut.add(new UserThread());
        boolean t = true;
        for(int i=0; i<ut.length && t;i++)
            if(ut[i] == null)
            {
                ut[i] = new UserThread();
                Thread tr = new Thread(ut[i],"UserThread-" + UserThread.threadNumber);
                tr.start();
                t = false;
            }
    }
    
    synchronized public static void createNewThread()
    {
        //ut.add(new UserThread());
        boolean t = true;
        for(int i=0; i<ut.length && t;i++)
            if(ut[i] == null)
            {
                ut[i] = new UserThread();
                Thread tr = new Thread(ut[i],"UserThread-" + UserThread.threadNumber);
                tr.start();
                t = false;
            }
    }
    
    synchronized public static void broadcastMsg(String msg)
    {
        Broadcast b = new Broadcast(msg,ut);
        Thread t = new Thread(b,"BroadCast-Thread");
        t.start();
    }
    
    static class Broadcast implements Runnable
    {
        String msg = null;
        UserThread[] ut = null;
        static boolean started = false;
        
        Broadcast(String msg, UserThread[] ut)
        {
            this.msg = msg;
            this.ut = ut;
        }
        
        @Override
        public void run() 
        {
            
            System.out.println("Invio messaggio broadcast: " + msg);
            for(int i = 0;i<this.ut.length;i++)
            {
                System.out.println("Broadcast a utente: " + i);
                if(this.ut[i] != null && this.ut[i].isConnected())
                {   
                    //System.out.println("Invio messaggio a: [" + this.ut[i].getUser().getName() + "]");
                    this.ut[i].sendMsg(msg, this.ut[i].getUser());
                }
            }
            Broadcast.started = false;
        }
    
    }
    
}