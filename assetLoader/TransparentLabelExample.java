package assetLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TransparentLabelExample {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        // JLabel erstellen
        JLabel label = new JLabel("Hello, World!");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setOpaque(true); // Setze undurchsichtig, damit der Hintergrund sichtbar wird
        
        // Anfangs Transparenz einstellen
        setLabelTransparency(label, 100); // Setze die Transparenz auf 50%

        // ActionListener für die Schaltflächen hinzufügen, um die Transparenz zu ändern
        JButton increaseButton = new JButton("Increase Transparency");
        increaseButton.addActionListener(e -> setLabelTransparency(label, label.getBackground().getAlpha() + 50)); // Erhöhe die Transparenz um 50
        JButton decreaseButton = new JButton("Decrease Transparency");
        decreaseButton.addActionListener(e -> setLabelTransparency(label, label.getBackground().getAlpha() - 50)); // Verringere die Transparenz um 50

        // JPanel für die Schaltflächen erstellen und zum JFrame hinzufügen
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(increaseButton);
        buttonPanel.add(decreaseButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // JLabel zum JFrame hinzufügen
        frame.add(label, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    // Methode zum Ändern der Transparenz eines JLabels
    private static void setLabelTransparency(JLabel label, int alpha) {
        // Stellen Sie sicher, dass der Alpha-Wert im gültigen Bereich von 0 bis 255 liegt
        alpha = Math.max(0, Math.min(alpha, 255));

        // Aktualisieren Sie die Hintergrundfarbe des Labels mit dem neuen Alpha-Wert
        Color backgroundColor = label.getBackground();
        Color newColor = new Color(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue(), alpha);
        label.setBackground(newColor);
    }
}
