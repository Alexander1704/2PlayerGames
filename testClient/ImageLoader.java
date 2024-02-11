package testClient;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageLoader extends JFrame {

    private BufferedImage image;

    public ImageLoader() {
        super("Bildanzeige");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BufferedImage image = null;
        // Laden des Bildes
        String imagePath = "C:\\Dokumente\\Programmieren\\Java\\2PlayerGames\\assets\\test.png";
        try {
            image = ImageIO.read(new File(imagePath));
            if (image != null) {
                System.out.println("Das Bild wurde erfolgreich geladen.");
            } else {
                System.out.println("Das Bild konnte nicht geladen werden. Stelle sicher, dass der Pfad korrekt ist.");
            }
        } catch (IOException e) {
            System.out.println("Ein Fehler ist aufgetreten: " + e.getMessage());
            e.printStackTrace();
        }

        // Erstellen eines JLabels und Einfügen des Bildes
        JLabel label = new JLabel(new ImageIcon(image));
        getContentPane().add(label);

        // Packen des Frames
        pack();

        // Zentrieren des Fensters auf dem Bildschirm
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ImageLoader imageLoader = new ImageLoader();
            imageLoader.setVisible(true);
        });
    }
}
