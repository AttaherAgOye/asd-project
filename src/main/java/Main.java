import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Programme principal - Version simplifiée pour présentation
 * Conversion Image ↔ R-quadtree (sans compression)
 */
public class Main {
    
    private static Scanner scanner = new Scanner(System.in);
    private static RQuadtree currentQuadtree = null;
    private static String currentImageName = null;
    
    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = readInt("Votre choix : ");
            
            try {
                switch (choice) {
                    case 1:
                        buildQuadtreeFromImage();
                        break;
                    case 2:
                        applyLambdaCompression();
                        break;
                    case 3:
                        applyPhiCompression();
                        break;
                    case 4:
                        saveQuadtreeToImage();
                        break;
                    case 5:
                        saveQuadtreeToText();
                        break;
                    case 0:
                        running = false;
                        System.out.println("\nAu revoir !");
                        break;
                    default:
                        System.out.println("Choix invalide !");
                }
            } catch (Exception e) {
                System.err.println("Erreur : " + e.getMessage());
                e.printStackTrace();
            }
            
            if (running && choice != 0) {
                System.out.println("\nAppuyez sur Entrée pour continuer...");
                scanner.nextLine();
            }
        }
        
        scanner.close();
    }
    
    /**
     * Affiche le menu principal
     */
    private static void displayMenu() {
        System.out.println("\n========== MENU PRINCIPAL ==========");
        System.out.println("1. Construire R-quadtree depuis une image PNG");
        System.out.println("2. Appliquer compression Lambda (qualité contrôlée)");
        System.out.println("3. Appliquer compression Phi (poids contrôlé)");
        System.out.println("4. Sauvegarder R-quadtree en image PNG");
        System.out.println("5. Sauvegarder R-quadtree en fichier TXT");
        System.out.println("0. Quitter");
        System.out.println("====================================");
    }
    
    /**
     * 1. Construire R-quadtree depuis une image PNG
     */
    private static void buildQuadtreeFromImage() throws IOException {
        String filename = readString("Nom du fichier PNG : ");
        
        ImagePNG img = new ImagePNG(filename);
        currentQuadtree = new RQuadtree(img);
        currentImageName = filename;
        
        System.out.println("\n R-quadtree construit avec succès !");
        System.out.println("  Image source : " + filename);
        System.out.println("  Dimensions : " + img.width() + " x " + img.height() + " pixels");
        System.out.println("  Nombre de feuilles : " + currentQuadtree.getLeafCount());
        System.out.println("  Compression naturelle : " + 
            String.format("%.1f", 100.0 - (100.0 * currentQuadtree.getLeafCount() / (img.width() * img.height()))) + "%");
    }
    
    /**
     * 2. Appliquer compression Lambda (qualité contrôlée)
     */
    private static void applyLambdaCompression() {
        if (currentQuadtree == null) {
            System.out.println("\n Aucun R-quadtree en mémoire.");
            System.out.println("  Veuillez d'abord charger une image (option 1).");
            return;
        }
        
        double lambda = readDouble("Valeur de Lambda (0-255) : ");
        int leafCountBefore = currentQuadtree.getLeafCount();
        
        currentQuadtree.compressLambda(lambda);
        int leafCountAfter = currentQuadtree.getLeafCount();
        
        System.out.println("\n Compression Lambda appliquée !");
        System.out.println("  Lambda = " + lambda);
        System.out.println("  Feuilles avant : " + leafCountBefore);
        System.out.println("  Feuilles après : " + leafCountAfter);
        System.out.println("  Réduction : " + (leafCountBefore - leafCountAfter) + " feuilles (" +
            String.format("%.1f", 100.0 * (leafCountBefore - leafCountAfter) / leafCountBefore) + "%)");
    }
    
    /**
     * 3. Appliquer compression Phi (poids contrôlé)
     */
    private static void applyPhiCompression() {
        if (currentQuadtree == null) {
            System.out.println("\n Aucun R-quadtree en mémoire.");
            System.out.println("  Veuillez d'abord charger une image (option 1).");
            return;
        }
        
        int phi = readInt("Valeur de Phi (nombre max de feuilles) : ");
        int leafCountBefore = currentQuadtree.getLeafCount();
        
        currentQuadtree.compressPhi(phi);
        int leafCountAfter = currentQuadtree.getLeafCount();
        
        System.out.println("\n Compression Phi appliquée !");
        System.out.println("  Phi = " + phi);
        System.out.println("  Feuilles avant : " + leafCountBefore);
        System.out.println("  Feuilles après : " + leafCountAfter);
        if (leafCountAfter < leafCountBefore) {
            System.out.println("  Réduction : " + (leafCountBefore - leafCountAfter) + " feuilles (" +
                String.format("%.1f", 100.0 * (leafCountBefore - leafCountAfter) / leafCountBefore) + "%)");
        }
    }
    
    /**
     * 4. Sauvegarder R-quadtree en image PNG
     */
    private static void saveQuadtreeToImage() throws IOException {
        if (currentQuadtree == null) {
            System.out.println("\n Aucun R-quadtree en mémoire.");
            System.out.println("  Veuillez d'abord charger une image (option 1).");
            return;
        }
        
        String filename = readString("Nom du fichier de sortie PNG : ");
        
        ImagePNG img = currentQuadtree.toPNG();
        img.save(filename);
        
        System.out.println("\n✓ Image sauvegardée : " + filename);
        System.out.println("  L'image a été reconstituée depuis le R-quadtree");
    }
    
    /**
     * 3. Sauvegarder R-quadtree en fichier TXT
     */
    private static void saveQuadtreeToText() throws IOException {
        if (currentQuadtree == null) {
            System.out.println("\n✗ Aucun R-quadtree en mémoire.");
            System.out.println("  Veuillez d'abord charger une image (option 1).");
            return;
        }
        
        String filename = readString("Nom du fichier de sortie TXT : ");
        
        String repr = currentQuadtree.toStr();
        saveToFile(repr, filename);
        
        System.out.println("\n✓ Représentation textuelle sauvegardée : " + filename);
        System.out.println("  Nombre de feuilles : " + currentQuadtree.getLeafCount());
        System.out.println("  Format : codes hexadécimaux des couleurs");
    }
    
    // Méthodes utilitaires
    
    private static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private static int readInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Valeur invalide. " + prompt);
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne
        return value;
    }
    
    private static double readDouble(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.print("Valeur invalide. " + prompt);
            scanner.next();
        }
        double value = scanner.nextDouble();
        scanner.nextLine(); // Consommer la nouvelle ligne
        return value;
    }
    
    private static void saveToFile(String content, String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(content);
        }
    }
}
