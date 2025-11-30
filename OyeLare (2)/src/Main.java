import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.awt.Color;

/**
 * Programme principal pour la compression d'images bitmap
 * Utilise R-quadtree et AVL
 */
public class Main {
    
    private static Scanner scanner = new Scanner(System.in);
    private static RQuadtree currentQuadtree = null;
    private static AVL currentAVL = null;
    private static String currentImageName = null;
    
    public static void main(String[] args) {
        // Mode non-interactif si arguments fournis
        if (args.length == 3) {
            try {
                executeNonInteractiveMode(args[0], args[1], args[2]);
            } catch (Exception e) {
                System.err.println("Erreur : " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
            return;
        } else if (args.length > 0 && args.length != 3) {
            System.out.println("Usage : java Main <fichier.png> <Lambda|Phi> <paramètre>");
            System.out.println("Exemple : java Main images/i.png Lambda 20");
            System.exit(1);
        }
        
        // Mode interactif
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
                    case 6:
                        compareImages();
                        break;
                    case 7:
                        buildAVLFromImage();
                        break;
                    case 8:
                        buildAVLFromQuadtree();
                        break;
                    case 9:
                        saveAVLToText();
                        break;
                    case 10:
                        manageAVL();
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
        System.out.println("1.  Construire R-quadtree depuis une image PNG");
        System.out.println("2.  Appliquer compression Lambda (qualité contrôlée)");
        System.out.println("3.  Appliquer compression Phi (poids contrôlé)");
        System.out.println("4.  Sauvegarder R-quadtree en image PNG");
        System.out.println("5.  Sauvegarder R-quadtree en fichier TXT");
        System.out.println("6.  Comparer deux images PNG (poids et EQM)");
        System.out.println("7.  Construire AVL depuis une image PNG");
        System.out.println("8.  Construire AVL depuis le R-quadtree");
        System.out.println("9.  Sauvegarder AVL en fichier TXT");
        System.out.println("10. Gérer l'AVL (recherche/ajout/suppression)");
        System.out.println("0.  Quitter");
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
    
    /**
     * 6. Comparer deux images PNG (poids et EQM)
     */
    private static void compareImages() throws IOException {
        String img1Path = readString("Nom de la première image PNG : ");
        String img2Path = readString("Nom de la deuxième image PNG : ");
        
        ImagePNG img1 = new ImagePNG(img1Path);
        ImagePNG img2 = new ImagePNG(img2Path);
        
        // Vérifier les dimensions
        if (img1.width() != img2.width() || img1.height() != img2.height()) {
            System.out.println("\n✗ Les images n'ont pas les mêmes dimensions !");
            System.out.println("  Image 1 : " + img1.width() + "x" + img1.height());
            System.out.println("  Image 2 : " + img2.width() + "x" + img2.height());
            return;
        }
        
        // Calculer l'EQM (Erreur Quadratique Moyenne)
        double eqm = calculateEQM(img1, img2);
        
        // Calculer le rapport de poids
        File file1 = new File(img1Path);
        File file2 = new File(img2Path);
        double weightRatio = (double) file2.length() / file1.length() * 100;
        
        System.out.println("\n✓ Comparaison terminée !");
        System.out.println("  Image 1 : " + img1Path + " (" + file1.length() + " octets)");
        System.out.println("  Image 2 : " + img2Path + " (" + file2.length() + " octets)");
        System.out.println("  Rapport de poids : " + String.format("%.2f", weightRatio) + "%");
        System.out.println("  Indice EQM : " + String.format("%.2f", eqm) + "%");
    }
    
    /**
     * Calcule l'Erreur Quadratique Moyenne entre deux images
     * Utilise la méthode fournie dans ImagePNG.java
     */
    private static double calculateEQM(ImagePNG img1, ImagePNG img2) {
        return ImagePNG.computeEQM(img1, img2);
    }
    
    /**
     * 7. Construire AVL depuis une image PNG
     */
    private static void buildAVLFromImage() throws IOException {
        String filename = readString("Nom du fichier PNG : ");
        
        ImagePNG img = new ImagePNG(filename);
        currentAVL = new AVL(img);  // Utilise le constructeur AVL(ImagePNG)
        
        System.out.println("\n✓ AVL construit avec succès !");
        System.out.println("  Image source : " + filename);
        System.out.println("  Dimensions : " + img.width() + "x" + img.height() + " pixels");
        System.out.println("  Nombre de couleurs uniques : " + currentAVL.size());
    }
    
    /**
     * 8. Construire AVL depuis le R-quadtree
     */
    private static void buildAVLFromQuadtree() {
        if (currentQuadtree == null) {
            System.out.println("\n✗ Aucun R-quadtree en mémoire.");
            System.out.println("  Veuillez d'abord charger une image (option 1).");
            return;
        }
        
        currentAVL = new AVL(currentQuadtree);
        
        System.out.println("\n✓ AVL construit depuis le R-quadtree !");
        System.out.println("  Nombre de couleurs uniques : " + currentAVL.size());
    }
    
    /**
     * 9. Sauvegarder AVL en fichier TXT
     */
    private static void saveAVLToText() throws IOException {
        if (currentAVL == null) {
            System.out.println("\n✗ Aucun AVL en mémoire.");
            System.out.println("  Veuillez d'abord construire un AVL (options 7 ou 8).");
            return;
        }
        
        String filename = readString("Nom du fichier de sortie TXT : ");
        
        String repr = currentAVL.toStr();
        saveToFile(repr, filename);
        
        System.out.println("\n✓ Représentation textuelle de l'AVL sauvegardée : " + filename);
        System.out.println("  Nombre de couleurs : " + currentAVL.size());
        System.out.println("  Format : codes hexadécimaux triés (parcours infixe)");
    }
    
    /**
     * 10. Gérer l'AVL (recherche/ajout/suppression)
     */
    private static void manageAVL() {
        if (currentAVL == null) {
            System.out.println("\n✗ Aucun AVL en mémoire.");
            System.out.println("  Veuillez d'abord construire un AVL (options 7 ou 8).");
            return;
        }
        
        System.out.println("\n--- Gestion de l'AVL ---");
        System.out.println("1. Rechercher une couleur");
        System.out.println("2. Ajouter une couleur");
        System.out.println("3. Supprimer une couleur");
        
        int choice = readInt("Votre choix : ");
        String hexCode = readString("Code hexadécimal (ex: ffffff) : ");
        
        switch (choice) {
            case 1:
                Color found = currentAVL.search(hexCode);
                if (found != null) {
                    System.out.println("\n✓ Couleur trouvée !");
                    System.out.println("  Code hex : " + hexCode);
                    System.out.println("  RGB : (" + found.getRed() + ", " + found.getGreen() + ", " + found.getBlue() + ")");
                } else {
                    System.out.println("\n✗ Couleur non trouvée dans l'AVL.");
                }
                break;
            case 2:
                Color color = ImagePNG.hexToColor(hexCode);
                currentAVL.insert(hexCode, color);
                System.out.println("\n✓ Couleur ajoutée !");
                System.out.println("  Nombre de couleurs dans l'AVL : " + currentAVL.size());
                break;
            case 3:
                currentAVL.remove(hexCode);
                System.out.println("\n✓ Couleur supprimée !");
                System.out.println("  Nombre de couleurs dans l'AVL : " + currentAVL.size());
                break;
            default:
                System.out.println("Choix invalide !");
        }
    }
    
    /**
     * Mode non-interactif : exécution en ligne de commande
     * Usage : java Main <fichier.png> <Lambda|Phi> <paramètre>
     * Exemple : java Main images/i.png Lambda 20
     */
    private static void executeNonInteractiveMode(String inputFile, String method, String paramStr) throws IOException {
        System.out.println("=== Mode Non-Interactif ===");
        System.out.println("Fichier : " + inputFile);
        System.out.println("Méthode : " + method);
        System.out.println("Paramètre : " + paramStr);
        System.out.println();
        
        // Charger l'image et construire le quadtree
        ImagePNG img = new ImagePNG(inputFile);
        RQuadtree quad = new RQuadtree(img);
        int initialLeaves = quad.getLeafCount();
        
        System.out.println("Image chargée : " + img.width() + "x" + img.height() + " pixels");
        System.out.println("Feuilles initiales : " + initialLeaves);
        
        // Appliquer la compression
        int param = Integer.parseInt(paramStr);
        String methodLower = method.toLowerCase();
        
        if (methodLower.equals("lambda")) {
            quad.compressLambda(param);
            System.out.println("Compression Lambda(" + param + ") appliquée");
        } else if (methodLower.equals("phi")) {
            quad.compressPhi(param);
            System.out.println("Compression Phi(" + param + ") appliquée");
        } else {
            throw new IllegalArgumentException("Méthode inconnue : " + method + " (utilisez Lambda ou Phi)");
        }
        
        int finalLeaves = quad.getLeafCount();
        System.out.println("Feuilles après compression : " + finalLeaves);
        System.out.println();
        
        // Générer les noms de fichiers de sortie
        String baseName = inputFile;
        if (baseName.toLowerCase().endsWith(".png")) {
            baseName = baseName.substring(0, baseName.length() - 4);
        }
        String suffix = "-" + methodLower + paramStr;
        
        String pngOutput = baseName + suffix + ".png";
        String txtOutput = baseName + suffix + "R.txt";
        String avlOutput = baseName + suffix + "AVL.txt";
        
        // Sauvegarder l'image compressée
        ImagePNG compressedImg = quad.toPNG();
        compressedImg.save(pngOutput);
        System.out.println("✓ Image compressée : " + pngOutput);
        
        // Sauvegarder la représentation textuelle du quadtree
        saveToFile(quad.toStr(), txtOutput);
        System.out.println("✓ Représentation R-quadtree : " + txtOutput);
        
        // Construire et sauvegarder l'AVL
        AVL avl = new AVL(quad);
        saveToFile(avl.toStr(), avlOutput);
        System.out.println("✓ AVL des couleurs : " + avlOutput);
        System.out.println();
        
        // Calculer les métriques
        File originalFile = new File(inputFile);
        File compressedFile = new File(pngOutput);
        double weightRatio = (double) compressedFile.length() / originalFile.length() * 100;
        
        // Recharger les images pour calculer l'EQM
        ImagePNG originalImg = new ImagePNG(inputFile);
        ImagePNG finalImg = new ImagePNG(pngOutput);
        double eqm = calculateEQM(originalImg, finalImg);
        
        // Afficher les résultats
        System.out.println("=== Résultats ===");
        System.out.println("Fichier original : " + originalFile.length() + " octets");
        System.out.println("Fichier compressé : " + compressedFile.length() + " octets");
        System.out.println("Rapport de poids : " + String.format("%.2f", weightRatio) + "%");
        System.out.println("Indice EQM : " + String.format("%.4f", eqm) + "%");
        System.out.println("Nombre de couleurs dans l'AVL : " + avl.size());
        System.out.println("Réduction de feuilles : " + initialLeaves + " → " + finalLeaves + 
            " (" + String.format("%.1f", 100.0 * (initialLeaves - finalLeaves) / initialLeaves) + "%)");
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
