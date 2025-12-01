import java.awt.Color;
import java.io.IOException;

/**
 * Programme de test pour les exercices potentiels
 * 
 * COMPILATION :
 *   javac -source 1.8 -target 1.8 *.java
 * 
 * EXÉCUTION :
 *   java TestExercices
 * 
 * NOTE : Ce fichier suppose que les méthodes des exercices ont été
 *        ajoutées dans RQuadtree.java et AVL.java
 */
public class TestExercices {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   TESTS DES EXERCICES POTENTIELS");
        System.out.println("========================================\n");
        
        try {
            testRQuadtree();
            testAVL();
            System.out.println("\n✓ Tous les tests passés !");
        } catch (Exception e) {
            System.out.println("\n✗ Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // ========================================
    // TESTS RQUADTREE
    // ========================================
    
    private static void testRQuadtree() throws IOException {
        System.out.println("=== TESTS RQUADTREE ===\n");
        
        // Charger une image de test
        ImagePNG img = new ImagePNG("../images/i.png");
        RQuadtree quad = new RQuadtree(img);
        
        System.out.println("Image chargée : " + img.width() + "x" + img.height());
        System.out.println("Nombre de feuilles : " + quad.getLeafCount());
        
        // Test getHeight (si implémenté)
        try {
            // int height = quad.getHeight();
            // System.out.println("Hauteur : " + height);
            System.out.println("[ ] getHeight() - À implémenter");
        } catch (Exception e) {
            System.out.println("[X] getHeight() - Non implémenté");
        }
        
        // Test getNodeCount (si implémenté)
        try {
            // int nodes = quad.getNodeCount();
            // System.out.println("Nœuds internes : " + nodes);
            System.out.println("[ ] getNodeCount() - À implémenter");
        } catch (Exception e) {
            System.out.println("[X] getNodeCount() - Non implémenté");
        }
        
        // Test getAllColors (si implémenté)
        try {
            // java.util.List<Color> colors = quad.getAllColors();
            // System.out.println("Couleurs uniques : " + colors.size());
            System.out.println("[ ] getAllColors() - À implémenter");
        } catch (Exception e) {
            System.out.println("[X] getAllColors() - Non implémenté");
        }
        
        // Test toStr existant
        System.out.println("\ntoStr() : " + quad.toStr());
        
        System.out.println();
    }
    
    // ========================================
    // TESTS AVL
    // ========================================
    
    private static void testAVL() throws IOException {
        System.out.println("=== TESTS AVL ===\n");
        
        // Créer un AVL depuis l'image
        ImagePNG img = new ImagePNG("../images/i.png");
        AVL avl = new AVL(img);
        
        System.out.println("Nombre de couleurs : " + avl.size());
        
        // Test toStr existant
        System.out.println("toStr() : " + avl.toStr());
        
        // Test search existant
        System.out.println("search(ffffff) : " + avl.search("ffffff"));
        System.out.println("search(000000) : " + avl.search("000000"));
        System.out.println("search(123456) : " + avl.search("123456"));
        
        // Test getMin (si implémenté)
        try {
            // String min = avl.getMin();
            // System.out.println("Min : " + min);
            System.out.println("[ ] getMin() - À implémenter");
        } catch (Exception e) {
            System.out.println("[X] getMin() - Non implémenté");
        }
        
        // Test getMax (si implémenté)
        try {
            // String max = avl.getMax();
            // System.out.println("Max : " + max);
            System.out.println("[ ] getMax() - À implémenter");
        } catch (Exception e) {
            System.out.println("[X] getMax() - Non implémenté");
        }
        
        // Test isBalanced (si implémenté)
        try {
            // boolean balanced = avl.isBalanced();
            // System.out.println("Équilibré : " + balanced);
            System.out.println("[ ] isBalanced() - À implémenter");
        } catch (Exception e) {
            System.out.println("[X] isBalanced() - Non implémenté");
        }
        
        System.out.println();
    }
}
