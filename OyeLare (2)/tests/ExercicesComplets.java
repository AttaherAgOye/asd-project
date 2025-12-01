/**
 * ================================================================================
 *                    EXERCICES COMPLETS - COPIER-COLLER PRÊT
 * ================================================================================
 * 
 * Ce fichier contient TOUTES les méthodes prêtes à être copiées directement
 * dans RQuadtree.java ou AVL.java
 */


// ================================================================================
//                              POUR RQUADTREE.java
// ================================================================================

/*
 * COPIER CES MÉTHODES DANS RQuadtree.java (avant la dernière accolade })
 */

    // ----- EXERCICE : getHeight -----
    /**
     * Retourne la hauteur maximale de l'arbre
     * Complexité : O(m) où m = nombre de nœuds
     */
    public int getHeight() {
        return computeHeight(root);
    }
    
    private int computeHeight(Node node) {
        if (node == null) return 0;
        if (node.isLeaf()) return 1;
        int hNO = computeHeight(node.NO);
        int hNE = computeHeight(node.NE);
        int hSE = computeHeight(node.SE);
        int hSO = computeHeight(node.SO);
        return 1 + Math.max(Math.max(hNO, hNE), Math.max(hSE, hSO));
    }

    // ----- EXERCICE : getNodeCount -----
    /**
     * Compte le nombre de nœuds internes (non-feuilles)
     * Complexité : O(m) où m = nombre de nœuds
     */
    public int getNodeCount() {
        return countNodes(root);
    }
    
    private int countNodes(Node node) {
        if (node == null || node.isLeaf()) return 0;
        return 1 + countNodes(node.NO) + countNodes(node.NE) 
                 + countNodes(node.SE) + countNodes(node.SO);
    }

    // ----- EXERCICE : getAllColors -----
    /**
     * Retourne la liste de toutes les couleurs uniques
     * Complexité : O(m × k) où k = couleurs uniques
     */
    public java.util.List<Color> getAllColors() {
        java.util.List<Color> colors = new java.util.ArrayList<>();
        collectUniqueColors(root, colors);
        return colors;
    }
    
    private void collectUniqueColors(Node node, java.util.List<Color> colors) {
        if (node == null) return;
        if (node.isLeaf()) {
            boolean found = false;
            for (Color c : colors) {
                if (c.equals(node.color)) { found = true; break; }
            }
            if (!found) colors.add(node.color);
        } else {
            collectUniqueColors(node.NO, colors);
            collectUniqueColors(node.NE, colors);
            collectUniqueColors(node.SE, colors);
            collectUniqueColors(node.SO, colors);
        }
    }

    // ----- EXERCICE : toGrayscale -----
    /**
     * Convertit toutes les couleurs en niveaux de gris
     * Complexité : O(m)
     */
    public void toGrayscale() {
        toGrayscaleRecursive(root);
    }
    
    private void toGrayscaleRecursive(Node node) {
        if (node == null) return;
        if (node.isLeaf()) {
            int gray = (int)(node.luminance * 255);
            gray = Math.max(0, Math.min(255, gray));
            node.color = new Color(gray, gray, gray);
        } else {
            toGrayscaleRecursive(node.NO);
            toGrayscaleRecursive(node.NE);
            toGrayscaleRecursive(node.SE);
            toGrayscaleRecursive(node.SO);
            calculateAverageColor(node);
        }
    }

    // ----- EXERCICE : toNegative -----
    /**
     * Applique un filtre négatif
     * Complexité : O(m)
     */
    public void toNegative() {
        toNegativeRecursive(root);
    }
    
    private void toNegativeRecursive(Node node) {
        if (node == null) return;
        if (node.isLeaf()) {
            int r = 255 - node.color.getRed();
            int g = 255 - node.color.getGreen();
            int b = 255 - node.color.getBlue();
            node.color = new Color(r, g, b);
            node.calculateLuminance();
        } else {
            toNegativeRecursive(node.NO);
            toNegativeRecursive(node.NE);
            toNegativeRecursive(node.SE);
            toNegativeRecursive(node.SO);
            calculateAverageColor(node);
        }
    }

    // ----- EXERCICE : getColorAt -----
    /**
     * Retourne la couleur du pixel à la position (x, y)
     * Complexité : O(log n)
     */
    public Color getColorAt(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException("Coordonnées hors limites");
        }
        return getColorAtRecursive(root, x, y);
    }
    
    private Color getColorAtRecursive(Node node, int x, int y) {
        if (node == null) return null;
        if (node.isLeaf()) return node.color;
        
        int midX = node.x + node.size / 2;
        int midY = node.y + node.size / 2;
        
        if (x < midX) {
            if (y < midY) return getColorAtRecursive(node.NO, x, y);
            else return getColorAtRecursive(node.SO, x, y);
        } else {
            if (y < midY) return getColorAtRecursive(node.NE, x, y);
            else return getColorAtRecursive(node.SE, x, y);
        }
    }

    // ----- EXERCICE : toStrWithSize -----
    /**
     * Représentation textuelle avec la taille de chaque région
     * Complexité : O(m)
     */
    public String toStrWithSize() {
        StringBuilder sb = new StringBuilder();
        toStrWithSizeRecursive(root, sb);
        return sb.toString().trim();
    }
    
    private void toStrWithSizeRecursive(Node node, StringBuilder sb) {
        if (node == null) return;
        if (node.isLeaf()) {
            sb.append(ImagePNG.colorToHex(node.color)).append(":").append(node.size);
        } else {
            sb.append("(");
            toStrWithSizeRecursive(node.NO, sb);
            sb.append(" ");
            toStrWithSizeRecursive(node.NE, sb);
            sb.append(" ");
            toStrWithSizeRecursive(node.SE, sb);
            sb.append(" ");
            toStrWithSizeRecursive(node.SO, sb);
            sb.append(")");
        }
    }

    // ----- EXERCICE : mirrorHorizontal -----
    /**
     * Applique un miroir horizontal (gauche-droite)
     * Complexité : O(m)
     */
    public void mirrorHorizontal() {
        mirrorHorizontalRecursive(root);
    }
    
    private void mirrorHorizontalRecursive(Node node) {
        if (node == null || node.isLeaf()) return;
        
        // Échanger NO <-> NE et SO <-> SE
        Node temp = node.NO;
        node.NO = node.NE;
        node.NE = temp;
        
        temp = node.SO;
        node.SO = node.SE;
        node.SE = temp;
        
        mirrorHorizontalRecursive(node.NO);
        mirrorHorizontalRecursive(node.NE);
        mirrorHorizontalRecursive(node.SE);
        mirrorHorizontalRecursive(node.SO);
    }

    // ----- EXERCICE : mirrorVertical -----
    /**
     * Applique un miroir vertical (haut-bas)
     * Complexité : O(m)
     */
    public void mirrorVertical() {
        mirrorVerticalRecursive(root);
    }
    
    private void mirrorVerticalRecursive(Node node) {
        if (node == null || node.isLeaf()) return;
        
        // Échanger NO <-> SO et NE <-> SE
        Node temp = node.NO;
        node.NO = node.SO;
        node.SO = temp;
        
        temp = node.NE;
        node.NE = node.SE;
        node.SE = temp;
        
        mirrorVerticalRecursive(node.NO);
        mirrorVerticalRecursive(node.NE);
        mirrorVerticalRecursive(node.SE);
        mirrorVerticalRecursive(node.SO);
    }

    // ----- EXERCICE : countColorOccurrences -----
    /**
     * Compte le nombre de feuilles ayant une couleur spécifique
     * Complexité : O(m)
     */
    public int countColorOccurrences(Color color) {
        return countColorRecursive(root, color);
    }
    
    private int countColorRecursive(Node node, Color target) {
        if (node == null) return 0;
        if (node.isLeaf()) return node.color.equals(target) ? 1 : 0;
        return countColorRecursive(node.NO, target) +
               countColorRecursive(node.NE, target) +
               countColorRecursive(node.SE, target) +
               countColorRecursive(node.SO, target);
    }

    // ----- EXERCICE : equals -----
    /**
     * Vérifie si ce quadtree est identique à un autre
     * Complexité : O(m)
     */
    public boolean equals(RQuadtree other) {
        if (other == null) return false;
        if (this.width != other.width || this.height != other.height) return false;
        return equalsRecursive(this.root, other.root);
    }
    
    private boolean equalsRecursive(Node n1, Node n2) {
        if (n1 == null && n2 == null) return true;
        if (n1 == null || n2 == null) return false;
        if (n1.isLeaf() != n2.isLeaf()) return false;
        if (n1.isLeaf()) return n1.color.equals(n2.color);
        return equalsRecursive(n1.NO, n2.NO) &&
               equalsRecursive(n1.NE, n2.NE) &&
               equalsRecursive(n1.SE, n2.SE) &&
               equalsRecursive(n1.SO, n2.SO);
    }


// ================================================================================
//                              POUR AVL.java
// ================================================================================

/*
 * COPIER CES MÉTHODES DANS AVL.java (avant la dernière accolade })
 */

    // ----- EXERCICE : getMin -----
    /**
     * Retourne le code hexadécimal de la couleur minimale
     * Complexité : O(log n)
     */
    public String getMin() {
        if (root == null) return null;
        Node current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.hexCode;
    }

    // ----- EXERCICE : getMax -----
    /**
     * Retourne le code hexadécimal de la couleur maximale
     * Complexité : O(log n)
     */
    public String getMax() {
        if (root == null) return null;
        Node current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.hexCode;
    }

    // ----- EXERCICE : isBalanced -----
    /**
     * Vérifie si l'AVL est correctement équilibré
     * Complexité : O(n)
     */
    public boolean isBalanced() {
        return isBalancedRecursive(root);
    }
    
    private boolean isBalancedRecursive(Node node) {
        if (node == null) return true;
        int balance = getBalance(node);
        if (balance < -1 || balance > 1) return false;
        return isBalancedRecursive(node.left) && isBalancedRecursive(node.right);
    }

    // ----- EXERCICE : toStrPrefix -----
    /**
     * Représentation en parcours préfixe
     * Complexité : O(n)
     */
    public String toStrPrefix() {
        StringBuilder sb = new StringBuilder();
        toStrPrefixRecursive(root, sb);
        return sb.toString().trim();
    }
    
    private void toStrPrefixRecursive(Node node, StringBuilder sb) {
        if (node == null) return;
        sb.append("(").append(node.hexCode).append(") ");
        toStrPrefixRecursive(node.left, sb);
        toStrPrefixRecursive(node.right, sb);
    }

    // ----- EXERCICE : toStrSuffix -----
    /**
     * Représentation en parcours suffixe
     * Complexité : O(n)
     */
    public String toStrSuffix() {
        StringBuilder sb = new StringBuilder();
        toStrSuffixRecursive(root, sb);
        return sb.toString().trim();
    }
    
    private void toStrSuffixRecursive(Node node, StringBuilder sb) {
        if (node == null) return;
        toStrSuffixRecursive(node.left, sb);
        toStrSuffixRecursive(node.right, sb);
        sb.append("(").append(node.hexCode).append(") ");
    }

    // ----- EXERCICE : getHeight (pour AVL) -----
    /**
     * Retourne la hauteur de l'arbre AVL
     * Complexité : O(1) car on utilise le champ height
     */
    public int getAVLHeight() {
        return height(root);
    }

    // ----- EXERCICE : countNodes -----
    /**
     * Retourne le nombre total de nœuds
     * Complexité : O(n)
     */
    public int countNodes() {
        return countNodesRecursive(root);
    }
    
    private int countNodesRecursive(Node node) {
        if (node == null) return 0;
        return 1 + countNodesRecursive(node.left) + countNodesRecursive(node.right);
    }

    // ----- EXERCICE : countLeaves -----
    /**
     * Compte le nombre de feuilles
     * Complexité : O(n)
     */
    public int countLeaves() {
        return countLeavesRecursive(root);
    }
    
    private int countLeavesRecursive(Node node) {
        if (node == null) return 0;
        if (node.left == null && node.right == null) return 1;
        return countLeavesRecursive(node.left) + countLeavesRecursive(node.right);
    }

    // ----- EXERCICE : contains (par RGB) -----
    /**
     * Vérifie si une couleur RGB existe
     * Complexité : O(log n)
     */
    public boolean contains(int r, int g, int b) {
        String hexCode = String.format("%02x%02x%02x", r, g, b);
        return search(hexCode);
    }

    // ----- EXERCICE : toList -----
    /**
     * Retourne toutes les couleurs dans une liste ordonnée
     * Complexité : O(n)
     */
    public java.util.List<String> toList() {
        java.util.List<String> list = new java.util.ArrayList<>();
        toListRecursive(root, list);
        return list;
    }
    
    private void toListRecursive(Node node, java.util.List<String> list) {
        if (node == null) return;
        toListRecursive(node.left, list);
        list.add(node.hexCode);
        toListRecursive(node.right, list);
    }

    // ----- EXERCICE : getSuccessor -----
    /**
     * Retourne le successeur d'une couleur
     * Complexité : O(log n)
     */
    public String getSuccessor(String hexCode) {
        Node successor = null;
        Node current = root;
        
        while (current != null) {
            int cmp = compareColors(hexCode, current.hexCode);
            if (cmp < 0) {
                successor = current;
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return successor == null ? null : successor.hexCode;
    }

    // ----- EXERCICE : getPredecessor -----
    /**
     * Retourne le prédécesseur d'une couleur
     * Complexité : O(log n)
     */
    public String getPredecessor(String hexCode) {
        Node predecessor = null;
        Node current = root;
        
        while (current != null) {
            int cmp = compareColors(hexCode, current.hexCode);
            if (cmp > 0) {
                predecessor = current;
                current = current.right;
            } else {
                current = current.left;
            }
        }
        return predecessor == null ? null : predecessor.hexCode;
    }

    // ----- EXERCICE : clear -----
    /**
     * Vide l'arbre
     * Complexité : O(1)
     */
    public void clear() {
        root = null;
    }

    // ----- EXERCICE : findClosestColor -----
    /**
     * Trouve la couleur la plus proche (distance euclidienne)
     * Complexité : O(n)
     */
    public String findClosestColor(int targetR, int targetG, int targetB) {
        if (root == null) return null;
        String[] closest = new String[1];
        double[] minDist = {Double.MAX_VALUE};
        findClosestRecursive(root, targetR, targetG, targetB, closest, minDist);
        return closest[0];
    }
    
    private void findClosestRecursive(Node node, int tR, int tG, int tB, 
                                       String[] closest, double[] minDist) {
        if (node == null) return;
        Color c = node.color;
        double dist = Math.sqrt(
            Math.pow(c.getRed() - tR, 2) +
            Math.pow(c.getGreen() - tG, 2) +
            Math.pow(c.getBlue() - tB, 2)
        );
        if (dist < minDist[0]) {
            minDist[0] = dist;
            closest[0] = node.hexCode;
        }
        findClosestRecursive(node.left, tR, tG, tB, closest, minDist);
        findClosestRecursive(node.right, tR, tG, tB, closest, minDist);
    }
