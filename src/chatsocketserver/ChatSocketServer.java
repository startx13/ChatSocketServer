package chatsocketserver;

import java.util.LinkedList;
import java.util.ListIterator;

public class ChatSocketServer {

    static LinkedList<UserThread> ut = new LinkedList();
    
    public static void main(String[] args) 
    {
        ut.add(new UserThread());
    }
    
    public static void createNewThread()
    {
        ut.add(new UserThread());
    }
    
    synchronized public static void broadcastMsg(String msg)
    {
       ListIterator litr = ut.listIterator();
        while(litr.hasNext()){
            UserThread uThread = (UserThread) litr.next();
            uThread.sendMsg(msg);
        }
    }
    
}