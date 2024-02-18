package serverGame;


public interface Positionable{
    public abstract double getXPos();
    public abstract double getYPos();
    public abstract int getHeight();
    public abstract int getWidth();
    public abstract int getScaleWidth();
    public abstract void setLocation(int x, int y);
}
