package testClient;

import javax.swing.JPanel;


public abstract class Page extends JPanel{
    public abstract void reloadData();
    public abstract void finish();
    public abstract void componentResized();
    public abstract void updateElements();
}
