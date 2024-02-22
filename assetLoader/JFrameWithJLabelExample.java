package assetLoader;

import javax.swing.*;
import java.awt.*;

public class JFrameWithJLabelExample {
    public static void main(String[] args) {
        // Erstellen des JFrame
        JFrame frame = new JFrame("JFrame mit JLabel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Programm beenden, wenn das JFrame geschlossen wird
        frame.setSize(300, 200); // Größe des JFrames festlegen

        // Erstellen des JLabels
        JLabel label = new JLabel("Hallo Welt!"); // Text für das JLabel festlegen
        label.setHorizontalAlignment(SwingConstants.CENTER); // Text horizontal zentrieren
        label.setVerticalAlignment(SwingConstants.CENTER); // Text vertikal zentrieren
        FontLoader.setLabelTransparency(label, 200);

        // JLabel zum JFrame hinzufügen
        frame.getContentPane().add(label, BorderLayout.CENTER);

        // JFrame sichtbar machen
        frame.setVisible(true);
    }
}
