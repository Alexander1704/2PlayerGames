package database;
import java.util.ArrayList;
import java.util.List;

public class DBController{
    DatabaseConnector database;
    public DBController(){
        database = new DatabaseConnector("", 0, "assets/2PlayerGames.db", "", "");
    }
    
    public int getTableLength(){
        database.executeStatement("SELECT * FROM PlayerInfo");
        return database.getCurrentQueryResult().getRowCount();
    }
    public String[] getPlayerInfo(String name){
        database.executeStatement("SELECT * FROM PlayerInfo WHERE name = '" + name + "'");
        QueryResult qr = database.getCurrentQueryResult();
        if(qr == null){
            System.err.println(database.getErrorMessage());
            return new String[0];
        }
        if(qr.getData().length == 0) return new String[0];
        return qr.getData()[0];
    }
    public ArrayList getPlayerNames(){
        database.executeStatement("SELECT name, unlocked, finished FROM PlayerInfo WHERE unlocked = 'true' AND finished = 'true'");
        QueryResult qr = database.getCurrentQueryResult();
        if(qr == null){
            System.err.println(database.getErrorMessage());
            return null;
        }
        ArrayList<String> temp = new ArrayList();
        for(int i = 0; i < qr.getData().length; i++){
            // if( qr.getData()[i][1].equals("true") &&  qr.getData()[i][2].equals("true")){
                temp.add(qr.getData()[i][0]);
            // }
            
        }
        return temp;
    }
    
    public void test(){
        print(getPlayerNames());
    }
    private void print(String[] temp){
        for(int i = 0; i < temp.length; i++){
            System.out.println(temp[i]);
        }
    }
    private void print(List temp){
        for(int i = 0; i < temp.size(); i++){
            System.out.println(temp.get(i));
        }
    }
}
