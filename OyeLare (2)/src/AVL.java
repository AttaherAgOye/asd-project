import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un AVL pour stocker les couleurs d'un R-quadtree
 * Chaque couleur est identifiée par son triplet (R, V, B)
 * 
 * Complexités :
 * - Recherche : O(log n)
 * - Insertion : O(log n)
 * - Suppression : O(log n)
 * - Construction depuis quadtree : O(m log m) où m = nombre de couleurs
 */
public class AVL {
    
    // Nœud de l'AVL
    private static class Node {
        String hexCode;      // Code hexadécimal de la couleur
        Color color;         // Couleur RGB
        Node left, right;    // Fils gauche et droit
        int height;          // Hauteur du sous-arbre
        
        Node(String hexCode, Color color) {
            this.hexCode = hexCode;
            this.color = color;
            this.height = 1;
        }
    }
    
    private Node root;
    private int size;
    
    /**
     * Constructeur par défaut
     */
    public AVL() {
        this.root = null;
        this.size = 0;
    }
    
    /**
     * Constructeur à partir d'un R-quadtree
     * Complexité : O(m log m) où m = nombre de feuilles du quadtree
     */
    public AVL(RQuadtree quad) {
        this();
        String repr = quad.toStr();
        // Parser la représentation textuelle et insérer les couleurs
        parseAndInsert(repr);
    }
    
    /**
     * Constructeur à partir d'une image PNG
     * Complexité : O(n log n) où n = nombre de pixels
     */
    public AVL(ImagePNG img) {
        this();
        // Parcourir tous les pixels et insérer les couleurs uniques
        for (int x = 0; x < img.width(); x++) {
            for (int y = 0; y < img.height(); y++) {
                Color c = img.getPixel(x, y);
                String hexCode = ImagePNG.colorToHex(c);
                insert(hexCode, c);
            }
        }
    }
    
    /**
     * Parse la représentation textuelle d'un quadtree et insère les couleurs
     */
    private void parseAndInsert(String repr) {
        // Format hiérarchique : (fils1 fils2 fils3 fils4) pour nœuds, hexcode pour feuilles
        // On extrait uniquement les codes hexadécimaux (pas les parenthèses)
        String[] tokens = repr.split("\\s+");
        for (String token : tokens) {
            // Ignorer les parenthèses vides ou tokens vides
            if (token.isEmpty() || token.equals("(") || token.equals(")")) {
                continue;
            }
            // Nettoyer les parenthèses autour du token
            token = token.replaceAll("[()]", "");
            if (!token.isEmpty() && token.matches("[0-9a-fA-F]{6}")) {
                Color color = ImagePNG.hexToColor(token);
                insert(token, color);
            }
        }
    }
    
    /**
     * Recherche une couleur dans l'AVL par son code hexadécimal
     * Complexité : O(log n)
     */
    public Color search(String hexCode) {
        Node node = searchNode(root, hexCode);
        return node != null ? node.color : null;
    }
    
    /**
     * Recherche une couleur dans l'AVL par son triplet (R, V, B)
     * Complexité : O(log n)
     */
    public Color search(int r, int g, int b) {
        String hexCode = String.format("%02x%02x%02x", r, g, b);
        return search(hexCode);
    }
    
    /**
     * Recherche récursive dans l'arbre
     * Complexité : O(log n)
     */
    private Node searchNode(Node node, String hexCode) {
        if (node == null) {
            return null;
        }
        
        int cmp = compareColors(hexCode, node.hexCode);
        if (cmp < 0) {
            return searchNode(node.left, hexCode);
        } else if (cmp > 0) {
            return searchNode(node.right, hexCode);
        } else {
            return node;
        }
    }
    
    /**
     * Compare deux couleurs selon l'ordre lexicographique sur (R, V, B)
     * C1 < C2 ssi R1 < R2, ou (R1 = R2 et V1 < V2), ou (R1 = R2 et V1 = V2 et B1 < B2)
     * Complexité : O(1)
     */
    private int compareColors(String hex1, String hex2) {
        int r1 = Integer.parseInt(hex1.substring(0, 2), 16);
        int g1 = Integer.parseInt(hex1.substring(2, 4), 16);
        int b1 = Integer.parseInt(hex1.substring(4, 6), 16);
        
        int r2 = Integer.parseInt(hex2.substring(0, 2), 16);
        int g2 = Integer.parseInt(hex2.substring(2, 4), 16);
        int b2 = Integer.parseInt(hex2.substring(4, 6), 16);
        
        if (r1 != r2) return r1 - r2;
        if (g1 != g2) return g1 - g2;
        return b1 - b2;
    }
    
    /**
     * Ajoute ou met à jour une couleur dans l'AVL
     * Complexité : O(log n)
     */
    public void insert(String hexCode, Color color) {
        root = insertNode(root, hexCode, color);
    }
    
    /**
     * Ajoute une couleur dans l'AVL par son triplet (R, V, B)
     * Complexité : O(log n)
     */
    public void add(int r, int g, int b) {
        String hexCode = String.format("%02x%02x%02x", r, g, b);
        Color color = new Color(r, g, b);
        insert(hexCode, color);
    }
    
    /**
     * Insertion récursive dans l'arbre
     * Complexité : O(log n)
     */
    private Node insertNode(Node node, String hexCode, Color color) {
        // Insertion classique BST
        if (node == null) {
            size++;
            return new Node(hexCode, color);
        }
        
        int cmp = compareColors(hexCode, node.hexCode);
        if (cmp < 0) {
            node.left = insertNode(node.left, hexCode, color);
        } else if (cmp > 0) {
            node.right = insertNode(node.right, hexCode, color);
        } else {
            // Le nœud existe déjà, mise à jour
            node.color = color;
            return node;
        }
        
        // Mettre à jour la hauteur
        node.height = 1 + Math.max(height(node.left), height(node.right));
        
        // Rééquilibrer l'arbre
        return balance(node);
    }
    
    /**
     * Retire une couleur de l'AVL par son code hexadécimal
     * Complexité : O(log n)
     */
    public void remove(String hexCode) {
        root = removeNode(root, hexCode);
    }
    
    /**
     * Retire une couleur de l'AVL par son triplet (R, V, B)
     * Complexité : O(log n)
     */
    public void remove(int r, int g, int b) {
        String hexCode = String.format("%02x%02x%02x", r, g, b);
        remove(hexCode);
    }
    
    /**
     * Suppression récursive dans l'arbre
     * Complexité : O(log n)
     */
    private Node removeNode(Node node, String hexCode) {
        if (node == null) {
            return null;
        }
        
        int cmp = compareColors(hexCode, node.hexCode);
        if (cmp < 0) {
            node.left = removeNode(node.left, hexCode);
        } else if (cmp > 0) {
            node.right = removeNode(node.right, hexCode);
        } else {
            // Nœud trouvé
            size--;
            
            // Cas 1 : Feuille ou un seul enfant
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }
            
            // Cas 2 : Deux enfants
            Node successor = findMin(node.right);
            node.hexCode = successor.hexCode;
            node.color = successor.color;
            node.right = removeNode(node.right, successor.hexCode);
            size++; // Compenser la décrémentation
        }
        
        // Mettre à jour la hauteur
        node.height = 1 + Math.max(height(node.left), height(node.right));
        
        // Rééquilibrer l'arbre
        return balance(node);
    }
    
    /**
     * Trouve le nœud minimum dans un sous-arbre
     */
    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }
    
    /**
     * Retourne la hauteur d'un nœud
     */
    private int height(Node node) {
        return node == null ? 0 : node.height;
    }
    
    /**
     * Calcule le facteur d'équilibre d'un nœud
     */
    private int getBalance(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }
    
    /**
     * Rééquilibre un nœud si nécessaire
     */
    private Node balance(Node node) {
        int balance = getBalance(node);
        
        // Cas gauche-gauche
        if (balance > 1 && getBalance(node.left) >= 0) {
            return rotateRight(node);
        }
        
        // Cas gauche-droite
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        
        // Cas droite-droite
        if (balance < -1 && getBalance(node.right) <= 0) {
            return rotateLeft(node);
        }
        
        // Cas droite-gauche
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        
        return node;
    }
    
    /**
     * Rotation à droite
     */
    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;
        
        x.right = y;
        y.left = T2;
        
        y.height = 1 + Math.max(height(y.left), height(y.right));
        x.height = 1 + Math.max(height(x.left), height(x.right));
        
        return x;
    }
    
    /**
     * Rotation à gauche
     */
    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;
        
        y.left = x;
        x.right = T2;
        
        x.height = 1 + Math.max(height(x.left), height(x.right));
        y.height = 1 + Math.max(height(y.left), height(y.right));
        
        return y;
    }
    
    /**
     * Retourne le nombre de couleurs dans l'AVL
     */
    public int size() {
        return size;
    }
    
    /**
     * Vérifie si l'AVL est vide
     */
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * Représentation textuelle de l'AVL (parcours infixe, sous forme parenthésée)
     * Chaque couleur est représentée par son code hexadécimal entre parenthèses
     * Complexité : O(n) où n = nombre de couleurs
     */
    public String toStr() {
        StringBuilder sb = new StringBuilder();
        toStrInOrder(root, sb);
        return sb.toString().trim();
    }
    
    /**
     * Parcours infixe récursif
     * Complexité : O(n)
     */
    private void toStrInOrder(Node node, StringBuilder sb) {
        if (node != null) {
            toStrInOrder(node.left, sb);
            sb.append("(").append(node.hexCode).append(") ");
            toStrInOrder(node.right, sb);
        }
    }
    
    /**
     * Retourne toutes les couleurs de l'AVL sous forme de liste
     */
    public List<String> getAllColors() {
        List<String> colors = new ArrayList<>();
        collectColors(root, colors);
        return colors;
    }
    
    private void collectColors(Node node, List<String> colors) {
        if (node != null) {
            collectColors(node.left, colors);
            colors.add(node.hexCode);
            collectColors(node.right, colors);
        }
    }
}
