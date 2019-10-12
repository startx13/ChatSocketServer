package chatsocketserver;


public class ChatSocketServer {

    static UserThread[] ut = new UserThread[1000000];
    
    public static void main(String[] args) 
    {
        for(int i=0;i<ut.length;i++)
        {
            ut[i] = null;
        }
        
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
    
    public static void createNewThread()
    {
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
    
    public static void broadcastMsg(String msg)
    {
        Broadcast b = new Broadcast(msg);
        Thread t = new Thread(b,"BroadCast-Thread");
        t.start();
    }
    
    static String[] userList()
    {
        String[] userlist = new String[ut.length];
        boolean t = true;
        for(int i=0; i<ut.length && t;i++)
            if(ut[i] != null && ut[i].getUser() != null)
            {
                userlist[i] =  ut[i].getUser().getName() + ut[i].getUser().getIp();
            }
        return userlist;
    }
    
    static class Broadcast implements Runnable
    {
        String msg = null;
        static boolean started = false;
        
        Broadcast(String msg)
        {
            this.msg = msg;
        }
        
        @Override
        public void run() 
        {
            try
            {
                for(int i = 0;i<ChatSocketServer.ut.length;i++)
                {
                    if(ChatSocketServer.ut[i] != null && ChatSocketServer.ut[i].isConnected())
                    {   
                        ChatSocketServer.ut[i].sendMsg(msg + "\n", ChatSocketServer.ut[i].getUser());
                    }
                }
                Broadcast.started = false;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    
    }
    
}