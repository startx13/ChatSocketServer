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
        
        //WELCOME MESSAGE
        this.u.sendMsg("Connesso al Server! Username: " + defaultUsername[rand] + " - " + UserThread.counter + "\n");
        this.u.sendMsg("Comandi disponibili: \n");
        this.u.sendMsg("                    /nickname <nuovo_nome>\n");
        this.u.sendMsg("                    /getmyip\n");
        this.u.sendMsg("                    /userlist\n");
        
        
        ChatSocketServer.createNewThread();
        this.connected = true;
        
        System.out.println("Connesso: " + (UserThread.threadNumber - 1) + " utenti");
        
        boolean disconnected = false;
        
        
        while(!(disconnected) && this.u.isConnected())
        {
            String msg = this.u.getLastMsg();
            if(msg != null)
            {
                if(msg.startsWith("/"))
                {
                    String parsed = msg.replace("/", "");
                    String[] parsed2 = parsed.split(" ");
                    
                    if(parsed2[0].equals("nickname") && parsed2.length >1)
                    {
                        String nuovoNome = "";
                        for(int i = 1; i<parsed2.length;i++)
                        {
                            if(nuovoNome.equals(""))
                                nuovoNome = parsed2[i];
                            else
                                nuovoNome = nuovoNome + " " + parsed2[i];
                        }
                        this.u.setUsername(nuovoNome);
                        this.u.sendMsg("[Server]: Nome cambato in: " + nuovoNome + "\n");
                    }
                    else if(parsed2[0].equals("getmyip") && parsed2.length == 1)
                    {
                        this.u.sendMsg("[Server]: " + this.u.getIp() + "\n");
                    }
                    else if(parsed2[0].equals("userlist") && parsed2.length == 1)
                    {
                        String[] userlist = ChatSocketServer.userList();
                        for(int i = 0;i<userlist.length;i++)
                            if(userlist[i] != null)
                            {
                                this.u.sendMsg(userlist[i] + "\n");
                            }
                    }
                    else
                    {
                        this.u.sendMsg("[Server]: Comando errato\n");
                    }
                }
                else
                {
                    System.out.println("[" + this.u.getName() + "]: " + msg);
                    ChatSocketServer.broadcastMsg("[" + this.u.getName() + "]: " + msg); 
                }
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