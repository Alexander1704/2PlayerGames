package assetLoader;

import javax.swing.*;
import java.awt.Component;
import java.util.ArrayList;

public class FunctionLoader{
    public FunctionLoader(){

    }

    public static void position(Component pComp, double pX, double pY){
        if(pComp.getParent() == null) {
            System.err.println("Component hat keinen parent und kann daher nicht positioniert werden");
            return;
        }
        Component parent = pComp.getParent();
        pComp.setLocation( (int) ((parent.getWidth() - pComp.getWidth()) * pX), (int) ((parent.getHeight() - pComp.getHeight()) * pY));
    }

    public static void position(Component pComp, Component pReferenceComponent, double pX, double pY, boolean pAbsoluteX, boolean pAbsoluteY){
        if(pComp.getParent() == null) {
            System.err.println("Component hat keinen parent und kann daher nicht positioniert werden");
            return;
        }
        Component parent = pComp.getParent();
        int x = pAbsoluteX ? (int) (pReferenceComponent.getX() + pX) : (int) ((parent.getWidth() - pComp.getWidth()) * pX);
        int y = pAbsoluteY ? (int) (pReferenceComponent.getY() + pY) : (int) ((parent.getHeight() - pComp.getHeight()) * pY);
        pComp.setLocation( x, y);
    }

    public static void scale(Component pComp, double pX, double pY){
        if(pComp.getParent() == null) {
            System.err.println("Component hat keinen parent und kann daher nicht positioniert werden");
            return;
        }
        Component parent = pComp.getParent();
        pComp.setSize( (int) (parent.getWidth() * pX), (int) (parent.getHeight() * pY));
    }
    
    public static String listToString(ArrayList pList){
        String temp = "";
        for(int i = 0; i < pList.size(); i++){
            temp += pList.get(i).toString() + "|";
        }
        return pList.size() + ">" + temp;
    }
    
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
    
    public static void printList(ArrayList pList){
        for(int i = 0; i < pList.size(); i++){
            System.out.println(pList.get(i).toString());
        }
    }

    public static void warte(int pTime){
        if(pTime < 0) return;
        try{
            Thread.sleep(pTime);
        }
        catch (InterruptedException ie){
            ie.printStackTrace();
        }
    }

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