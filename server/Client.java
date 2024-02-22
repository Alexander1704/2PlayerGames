package server;

public class Client{
    public String ip;
    public int port;
    public Client(String pIP, int pPort){
        ip = pIP;
        port = pPort;
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
