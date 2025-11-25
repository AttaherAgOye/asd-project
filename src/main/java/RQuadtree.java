import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un R-quadtree pour la compression d'images
 * Avec compressions Lambda (qualité) et Phi (poids)
 */
public class RQuadtree {
    
    // Nœud interne du R-quadtree
    private static class Node {
        Color color;      // Couleur moyenne du nœud
        int x, y;         // Coordonnées du coin supérieur gauche
        int size;         // Taille du carré (largeur = hauteur)
        double luminance; // Luminance du nœud
        Node NO, NE, SE, SO; // 4 fils (Nord-Ouest, Nord-Est, Sud-Est, Sud-Ouest)
        
        Node(int x, int y, int size) {
            this.x = x;
            this.y = y;
            this.size = size;
        }
        
        boolean isLeaf() {
            return NO == null && NE == null && SE == null && SO == null;
        }
        
        // Calcule la luminance selon la formule donnée
        void calculateLuminance() {
            if (color != null) {
                double R = color.getRed() / 255.0;
                double G = color.getGreen() / 255.0;
                double B = color.getBlue() / 255.0;
                this.luminance = 0.2126 * R + 0.7152 * G + 0.0722 * B;
            }
        }
    }
    
    private Node root;
    private int width, height;
    
    /**
     * Constructeur : construit le R-quadtree à partir d'une image PNG
     */
    public RQuadtree(ImagePNG img) {
        this.width = img.width();
        this.height = img.height();
        
        // Calculer la taille nécessaire (puissance de 2)
        int maxDim = Math.max(width, height);
        int size = 1;
        while (size < maxDim) {
            size *= 2;
        }
        
        this.root = buildTree(img, 0, 0, size);
    }
    
    /**
     * Construction récursive du R-quadtree
     */
    private Node buildTree(ImagePNG img, int x, int y, int size) {
        Node node = new Node(x, y, size);
        
        // Si la taille est 1, c'est une feuille
        if (size == 1) {
            if (x < width && y < height) {
                node.color = img.getPixel(x, y);
                node.calculateLuminance();
            } else {
                // Pixel hors de l'image -> couleur par défaut (blanc)
                node.color = Color.WHITE;
                node.luminance = 1.0;
            }
            return node;
        }
        
        // Sinon, diviser en 4 sous-quadrants
        int halfSize = size / 2;
        node.NO = buildTree(img, x, y, halfSize);
        node.NE = buildTree(img, x + halfSize, y, halfSize);
        node.SE = buildTree(img, x + halfSize, y + halfSize, halfSize);
        node.SO = buildTree(img, x, y + halfSize, halfSize);
        
        // Calculer la couleur moyenne et la luminance
        if (areChildrenHomogeneous(node)) {
            mergeNode(node);
        } else {
            calculateAverageColor(node);
        }
        
        return node;
    }
    
    /**
     * Vérifie si les 4 fils ont la même couleur
     */
    private boolean areChildrenHomogeneous(Node node) {
        if (!node.NO.isLeaf() || !node.NE.isLeaf() || !node.SE.isLeaf() || !node.SO.isLeaf()) {
            return false;
        }
        
        Color c = node.NO.color;
        return c.equals(node.NE.color) && c.equals(node.SE.color) && c.equals(node.SO.color);
    }
    
    /**
     * Fusionne un nœud en retirant ses fils
     */
    private void mergeNode(Node node) {
        // Calculer la couleur moyenne AVANT de supprimer les fils
        calculateAverageColor(node);
        // Supprimer les fils
        node.NO = node.NE = node.SE = node.SO = null;
    }
    
    /**
     * Calcule la couleur moyenne d'un nœud à partir de ses fils
     */
    private void calculateAverageColor(Node node) {
        int r = 0, g = 0, b = 0;
        double lum = 0;
        
        if (node.NO != null) { r += node.NO.color.getRed(); g += node.NO.color.getGreen(); b += node.NO.color.getBlue(); lum += node.NO.luminance; }
        if (node.NE != null) { r += node.NE.color.getRed(); g += node.NE.color.getGreen(); b += node.NE.color.getBlue(); lum += node.NE.luminance; }
        if (node.SE != null) { r += node.SE.color.getRed(); g += node.SE.color.getGreen(); b += node.SE.color.getBlue(); lum += node.SE.luminance; }
        if (node.SO != null) { r += node.SO.color.getRed(); g += node.SO.color.getGreen(); b += node.SO.color.getBlue(); lum += node.SO.luminance; }
        
        node.color = new Color(r / 4, g / 4, b / 4);
        node.luminance = lum / 4;
    }
    
    /**
     * Compression à qualité contrôlée (Lambda)
     * Lambda ∈ [0, 255] fixe la dégradation de luminance maximale autorisée
     */
    public void compressLambda(double lambda) {
        if (lambda < 0 || lambda > 255) {
            throw new IllegalArgumentException("Lambda doit être entre 0 et 255");
        }
        compressLambdaRecursive(root, lambda / 255.0); // Normaliser lambda
    }
    
    private void compressLambdaRecursive(Node node, double lambda) {
        if (node == null || node.isLeaf()) {
            return;
        }
        
        // Récursion sur les fils
        compressLambdaRecursive(node.NO, lambda);
        compressLambdaRecursive(node.NE, lambda);
        compressLambdaRecursive(node.SE, lambda);
        compressLambdaRecursive(node.SO, lambda);
        
        // Vérifier si on peut fusionner (tous les fils sont des feuilles)
        if (node.NO.isLeaf() && node.NE.isLeaf() && node.SE.isLeaf() && node.SO.isLeaf()) {
            double maxDiff = 0;
            double avgLum = node.luminance;
            
            maxDiff = Math.max(maxDiff, Math.abs(node.NO.luminance - avgLum));
            maxDiff = Math.max(maxDiff, Math.abs(node.NE.luminance - avgLum));
            maxDiff = Math.max(maxDiff, Math.abs(node.SE.luminance - avgLum));
            maxDiff = Math.max(maxDiff, Math.abs(node.SO.luminance - avgLum));
            
            // Si la différence est acceptable, fusionner
            if (maxDiff <= lambda) {
                mergeNode(node);
            }
        }
    }
    
    /**
     * Compression à poids contrôlé (Phi)
     * Phi > 0 représente le nombre maximum de feuilles autorisées
     */
    public void compressPhi(int phi) {
        if (phi <= 0) {
            throw new IllegalArgumentException("Phi doit être > 0");
        }
        
        while (countLeaves(root) > phi) {
            // Trouver le nœud avec la plus petite différence de luminance
            Node nodeToMerge = findBestNodeToMerge(root);
            if (nodeToMerge == null) break;
            
            // Fusionner ce nœud (calcule automatiquement la moyenne)
            mergeNode(nodeToMerge);
        }
    }
    
    /**
     * Trouve le meilleur nœud à fusionner (celui avec la plus petite variance de luminance)
     */
    private Node findBestNodeToMerge(Node node) {
        List<Node> candidates = new ArrayList<>();
        collectMergeableCandidates(node, candidates);
        
        if (candidates.isEmpty()) return null;
        
        Node best = candidates.get(0);
        double minVariance = calculateLuminanceVariance(best);
        
        for (Node candidate : candidates) {
            double variance = calculateLuminanceVariance(candidate);
            if (variance < minVariance) {
                minVariance = variance;
                best = candidate;
            }
        }
        
        return best;
    }
    
    /**
     * Collecte les nœuds candidats pour la fusion (avec 4 fils feuilles)
     */
    private void collectMergeableCandidates(Node node, List<Node> candidates) {
        if (node == null || node.isLeaf()) return;
        
        if (node.NO.isLeaf() && node.NE.isLeaf() && node.SE.isLeaf() && node.SO.isLeaf()) {
            candidates.add(node);
        }
        
        collectMergeableCandidates(node.NO, candidates);
        collectMergeableCandidates(node.NE, candidates);
        collectMergeableCandidates(node.SE, candidates);
        collectMergeableCandidates(node.SO, candidates);
    }
    
    /**
     * Calcule la variance de luminance entre un nœud et ses fils
     */
    private double calculateLuminanceVariance(Node node) {
        double avg = node.luminance;
        double sum = 0;
        sum += Math.pow(node.NO.luminance - avg, 2);
        sum += Math.pow(node.NE.luminance - avg, 2);
        sum += Math.pow(node.SE.luminance - avg, 2);
        sum += Math.pow(node.SO.luminance - avg, 2);
        return sum / 4;
    }
    
    /**
     * Convertit le R-quadtree en ImagePNG
     */
    public ImagePNG toPNG() throws IOException {
        // Créer une image temporaire blanche
        java.awt.image.BufferedImage buffImg = new java.awt.image.BufferedImage(
            width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);
        
        // Remplir en blanc
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                buffImg.setRGB(x, y, Color.WHITE.getRGB());
            }
        }
        
        // Sauvegarder temporairement
        String tempFile = "temp_quadtree.png";
        javax.imageio.ImageIO.write(buffImg, "png", new java.io.File(tempFile));
        
        // Charger avec ImagePNG
        ImagePNG result = new ImagePNG(tempFile);
        
        // Remplir l'image à partir du quadtree
        fillImage(root, result);
        
        // Nettoyer le fichier temporaire
        new java.io.File(tempFile).delete();
        
        return result;
    }
    
    /**
     * Remplit récursivement l'image à partir du R-quadtree
     */
    private void fillImage(Node node, ImagePNG img) {
        if (node == null) return;
        
        if (node.isLeaf()) {
            // Remplir tous les pixels de cette région avec la couleur du nœud
            for (int i = 0; i < node.size && (node.x + i) < width; i++) {
                for (int j = 0; j < node.size && (node.y + j) < height; j++) {
                    img.setPixel(node.x + i, node.y + j, node.color);
                }
            }
        } else {
            // Récursion sur les fils
            fillImage(node.NO, img);
            fillImage(node.NE, img);
            fillImage(node.SE, img);
            fillImage(node.SO, img);
        }
    }
    
    /**
     * Produit la représentation textuelle du R-quadtree
     * Format hiérarchique : (fils1 fils2 fils3 fils4) pour nœuds internes, code_hex pour feuilles
     */
    public String toStr() {
        StringBuilder sb = new StringBuilder();
        toStrRecursive(root, sb);
        return sb.toString().trim();
    }
    
    private void toStrRecursive(Node node, StringBuilder sb) {
        if (node == null) return;
        
        if (node.isLeaf()) {
            // Feuille : juste le code hexa (sans parenthèses)
            sb.append(ImagePNG.colorToHex(node.color)).append(" ");
        } else {
            // Nœud interne : parenthèses autour des 4 fils
            sb.append("(");
            toStrRecursive(node.NO, sb);
            toStrRecursive(node.NE, sb);
            toStrRecursive(node.SE, sb);
            toStrRecursive(node.SO, sb);
            sb.append(") ");
        }
    }
    
    /**
     * Compte le nombre de feuilles (pour affichage)
     */
    public int getLeafCount() {
        return countLeaves(root);
    }
    
    /**
     * Compte récursivement le nombre de feuilles dans l'arbre
     */
    private int countLeaves(Node node) {
        if (node == null) return 0;
        if (node.isLeaf()) return 1;
        return countLeaves(node.NO) + countLeaves(node.NE) + countLeaves(node.SE) + countLeaves(node.SO);
    }
}
