package assetLoader;

import javax.swing.*;
import java.awt.Component;
import java.util.ArrayList;

/**In dieser Klasse werden generell nützliche Funktionen implementiert.
 */
public class FunctionLoader{
    /**Erstellt ein neues Objekt der Klasse FunctionLoader. Ein FunctionLoader-Objekt wird
     * meist allerdings nicht benötigt, da alle Methoden statisch sind.
     */
    public FunctionLoader(){

    }

    /**Positioniere ein Component relativ im Verhältnis zu seinem parent-Objekt.
     * 
     *@param pComp das zu positionierende Objekt als pComp
     *@param pX relative x-Position (zwischen 0 und 1)
     *@param pY relative y-Position (zwischen 0 und 1)
     */
    public static void position(Component pComp, double pX, double pY){
        if(pComp.getParent() == null) {
            System.err.println("Component hat keinen parent und kann daher nicht positioniert werden");
            return;
        }
        Component parent = pComp.getParent();
        pComp.setLocation( (int) ((parent.getWidth() - pComp.getWidth()) * pX), (int) ((parent.getHeight() - pComp.getHeight()) * pY));
    }

    /**Positioniere ein Component relativ zu seinem parent-Objekt bzw. absolut zu einem Referenzobjekt
     * 
     * @param pComp zu positionierendes Component
     * @param pReferenceComponent Referenzobjekt 
     * @param pX x-Position
     * @param pY y-Position
     * @param pAbsoluteX x-Position absolut bzw. relativ? (true: absolut zum Referenzobjekt, 
     *      false: Position relativ zum parent-Objekt
     * @param pAbsolute> y-Position absolut bzw. relativ? (true: absolut zum Referenzobjekt, 
     *      false: Position relativ zum parent-Objekt
     */
    public static void position(Component pComp, Component pReferenceComponent, double pX, double pY, boolean pAbsoluteX, boolean pAbsoluteY){
        if(pComp.getParent() == null) {
            System.err.println("Component hat keinen parent und kann daher nicht positioniert werden");
            return;
        }
        Component parent = pComp.getParent();
        int x = pAbsoluteX ? (int) (pReferenceComponent.getX() + pX) : (int) ((parent.getWidth() - pComp.getWidth()) * pX);
        int y = pAbsoluteY ? (int) (pReferenceComponent.getY() + pY) : (int) ((parent.getHeight() - pComp.getHeight()) * pY);
        pComp.setLocation(x, y);
    }

    /**Skaliere ein Component im Verhältnis zu seinem parent-Objekt
     * 
     * @param pComp zu skalierendes Component
     * @param pXScale Breite im Verhältnis zum parent-Objekt (empfohlen: zwischen 0 und 1)
     * @param pYScale Höhe im Verhältnis zum parent-Objekt (empfohlen: zwischen 0 und 1)
     */
    public static void scale(Component pComp, double pXScale, double pYScale){
        if(pComp.getParent() == null) {
            System.err.println("Component hat keinen parent und kann daher nicht positioniert werden");
            return;
        }
        Component parent = pComp.getParent();
        pComp.setSize( (int) (parent.getWidth() * pXScale), (int) (parent.getHeight() * pYScale));
    }
    
    /**Mache aus einer ArrayListe einen String. Listenelemente werden durch die Methode toString() zu einem 
     * String verwandelt.
     * 
     * @param pList Liste, die in einen String verwandelt werden soll
     * @return List als umformulierter String; Form: <Listenlänge>">"<Element0String>"|"<Element1String>"|"<Element2String>"|"...
     */
    public static String listToString(ArrayList pList){
        String temp = "";
        for(int i = 0; i < pList.size(); i++){
            temp += pList.get(i).toString() + "|";
        }
        return pList.size() + ">" + temp;
    }
    
    /**Mache aus einem String eine String-ArrayListe
     * 
     * @param pString Liste als String
     * @return String-ArrayList; Form: 0:<Element0String>; 1:<Element1String>; 2:<Element2String>; ...
     */
    public static ArrayList<String> stringToList(String pString){
        ArrayList<String> temp = new ArrayList<String>();
        
        int i = 0;
        while(pString.charAt(i) != '>' && i < pString.length()){
            i++;
        }
        i++;
        
        String listContent = "";
        for(i = i; i< pString.length(); i++){
            if(pString.charAt(i) == '|'){
                temp.add(listContent);
                listContent = "";
            }else {
                listContent += pString.charAt(i);
            }
        }
        return temp;
    }
    
    /**Gibt alle Elemente einer ArrayList mithilfe des Methode toString() aus.
     * 
     * @param pList Liste, von der alle Elemente ausgedruckt werden sollen
     */
    public static void printList(ArrayList pList){
        for(int i = 0; i < pList.size(); i++){
            System.out.println(pList.get(i).toString());
        }
    }

    /**Pausiert den Thread für eine gewisse Zeit.
     * 
     * @param pTime Zeit, die der Thread pausiert in Millisekunden (darf nicht 
     *      kleiner als 0 sein, ansonsten passiert nichts)
     */
    public static void warte(int pTime){
        if(pTime < 0) return;
        try{
            Thread.sleep(pTime);
        }
        catch (InterruptedException ie){
            ie.printStackTrace();
        }
    }

    /**Pausiert den Thread für eine gewisse Zeit.
     * 
     * @param pTime Zeit, die der Thread pausiert in Millisekunden (darf nicht 
     *      kleiner als 0 sein, ansonsten passiert nichts)
     */
    public static void warte(long pTime){
        if(pTime < 0) return;
        try{
            Thread.sleep(pTime);
        }
        catch (InterruptedException ie){
            ie.printStackTrace();
        }
    }
    
    
}