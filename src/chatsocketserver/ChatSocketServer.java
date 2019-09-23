package chatsocketserver;

import java.util.LinkedList;

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
    
}