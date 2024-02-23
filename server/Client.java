package server;

public class Client{
    public final String ip;
    public final int port;
    private String name;
    
    public Client(String pIP, int pPort){
        ip = pIP;
        port = pPort;
        name = "";
    }

    public String getIp(){
        return ip;
    }
    
    public int getPort(){
        return port;
    }
    
    public void setName(String pName){
        name = pName;
    }
    
    public String getName(){
        return name;
    }
    
    @Override 
    public boolean equals(Object o){
        if(o instanceof Client){
            Client cl = (Client) o;
            if (ip.equals(cl.ip) && port == cl.port) return true;
        }
        return false;
    }

    @Override 
    public String toString(){
        return ip + ":" + port;
    }
}
