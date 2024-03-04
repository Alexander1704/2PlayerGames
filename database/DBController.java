package database;
import java.util.ArrayList;
import java.util.List;

/**DBController stellt Verbindung zur SQL-Datenbank her und liefert Ergebnisse einzelner
 * Abfragen.
 */
public class DBController{
    DatabaseConnector database;

    /**Erstellt neues Objekt der Klasse DBController und initialisiert es.
     * DBController erstellt neue Verbindung zur 2PlayerGames.db-Database
     */
    public DBController(){
        database = new DatabaseConnector("", 0, "assets/2PlayerGames.db", "", "");
    }

    /**Gibt die Anzahl aller Spielercharaktere wieder
     * 
     * @return Zahl wieviele Charaktereinträge vorhanden sind
     */
    public int getTableLength(){
        database.executeStatement("SELECT * FROM PlayerInfo");
        return database.getCurrentQueryResult().getRowCount();
    }

    /**Liefert alle Informationen zu einem Charakter. Wenn Charakter nicht gefunden werden
     * konnte/ ein anderer Fehler auftritt wird ein leerer String zurückgegeben.
     * 
     * @param pName Name des Charakters
     * @return String-Array der Informationen über diesen Charakter
     */
    public String[] getPlayerInfo(String pName){
        database.executeStatement("SELECT * FROM PlayerInfo WHERE name = '" + pName + "'");
        QueryResult qr = database.getCurrentQueryResult();
        if(qr == null){
            System.err.println(database.getErrorMessage());
            return new String[0];
        }
        if(qr.getData().length == 0) return new String[0];
        return qr.getData()[0];
    }

    /**Gib alle verfügbaren Spieler als ArrayList wieder. Wenn es einen Fehler gibt wird
     * eine leere ArrayList zurückgegeben
     * 
     * @return String ArrayList mit allen Namen aller Charaktere
     */
    public ArrayList<String> getPlayerNames(){
        database.executeStatement("SELECT name, unlocked, finished FROM PlayerInfo WHERE unlocked = 'true' AND finished = 'true'");
        QueryResult qr = database.getCurrentQueryResult();
        if(qr == null){
            System.err.println(database.getErrorMessage());
            return new ArrayList<String>();
        }
        ArrayList<String> temp = new ArrayList();
        for(int i = 0; i < qr.getData().length; i++){
            temp.add(qr.getData()[i][0]);
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
