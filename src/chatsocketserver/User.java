package chatsocketserver;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class User 
{
    private static int userCount = 0;
    private int id;
    private String userName;
    private Socket s = null;
    private DataOutputStream os = null;
    private DataInputStream is = null;
    private BufferedReader b = null;
    
    User(Socket s, String username)
    {
        this.id = User.userCount;
        User.userCount++;
        this.s = s;
        this.userName = username;
        
        try 
        {
            s.setKeepAlive(true);
            is = new DataInputStream(s.getInputStream());
            os = new DataOutputStream(s.getOutputStream());
            this.b = new BufferedReader(new InputStreamReader(is));
        } catch (Exception ex) {
            System.out.println("[Errore] Impossibile creare lo stream I/O per l'utente " + this.id);
        }
    }
    
    synchronized public void sendMsg(String msg)
    {
        try 
        {
            os.writeBytes(msg);
            //os.writeUTF(msg);
        } catch (Exception ex) {
            System.out.println("[Errore] Impossibile inviare il messaggio all'utente " + this.id);
        }
        
    }
    
    public int getId()
    {
        return this.id;
    }
    
    public String getName()
    {
        return this.userName;
    }
    
    String oldMsg = null;
    
    synchronized public String getLastMsg()
    {
        String msg = null;
        try 
        {
            msg = this.b.readLine();
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }
        
        return msg; //Return null if message hasn't changed
    }
    
    synchronized public boolean isConnected()
    {
        return s.isConnected();
    }
}