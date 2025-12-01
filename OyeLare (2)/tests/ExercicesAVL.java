/**
 * EXERCICES POTENTIELS - AVL
 * Méthodes supplémentaires à ajouter dans AVL.java
 * 
 * Pour tester : copier ces méthodes dans AVL.java
 */

// ============================================================
// EXERCICE 1 : Retourner la couleur minimale (plus petite)
// Complexité : O(log n)
// ============================================================

/**
 * Retourne le code hexadécimal de la couleur minimale
 * (la plus petite selon l'ordre lexicographique RGB)
 * Complexité : O(log n) - descente à gauche
 */
public String getMin() {
    if (root == null) return null;
    
    Node current = root;
    while (current.left != null) {
        current = current.left;
    }
    return current.hexCode;
}

/**
 * Version avec la couleur Color
 */
public Color getMinColor() {
    String hex = getMin();
    return hex == null ? null : ImagePNG.hexToColor(hex);
}


// ============================================================
// EXERCICE 2 : Retourner la couleur maximale (plus grande)
// Complexité : O(log n)
// ============================================================

/**
 * Retourne le code hexadécimal de la couleur maximale
 * Complexité : O(log n) - descente à droite
 */
public String getMax() {
    if (root == null) return null;
    
    Node current = root;
    while (current.right != null) {
        current = current.right;
    }
    return current.hexCode;
}

/**
 * Version avec la couleur Color
 */
public Color getMaxColor() {
    String hex = getMax();
    return hex == null ? null : ImagePNG.hexToColor(hex);
}


// ============================================================
// EXERCICE 3 : Vérifier si l'arbre est équilibré
// Complexité : O(n)
// ============================================================

/**
 * Vérifie si l'AVL est correctement équilibré
 * (facteur d'équilibre entre -1 et 1 pour tous les nœuds)
 * Complexité : O(n)
 */
public boolean isBalanced() {
    return isBalancedRecursive(root);
}

private boolean isBalancedRecursive(Node node) {
    if (node == null) return true;
    
    int balance = getBalance(node);
    
    // Le facteur doit être entre -1 et 1
    if (balance < -1 || balance > 1) {
        return false;
    }
    
    // Vérifier récursivement les sous-arbres
    return isBalancedRecursive(node.left) && isBalancedRecursive(node.right);
}


// ============================================================
// EXERCICE 4 : Parcours PRÉFIXE (racine, gauche, droite)
// Complexité : O(n)
// ============================================================

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
    
    // PRÉFIXE : Racine d'abord
    sb.append("(").append(node.hexCode).append(") ");
    toStrPrefixRecursive(node.left, sb);
    toStrPrefixRecursive(node.right, sb);
}


// ============================================================
// EXERCICE 5 : Parcours SUFFIXE (gauche, droite, racine)
// Complexité : O(n)
// ============================================================

/**
 * Représentation en parcours suffixe (postfixe)
 * Complexité : O(n)
 */
public String toStrSuffix() {
    StringBuilder sb = new StringBuilder();
    toStrSuffixRecursive(root, sb);
    return sb.toString().trim();
}

private void toStrSuffixRecursive(Node node, StringBuilder sb) {
    if (node == null) return;
    
    // SUFFIXE : Racine à la fin
    toStrSuffixRecursive(node.left, sb);
    toStrSuffixRecursive(node.right, sb);
    sb.append("(").append(node.hexCode).append(") ");
}


// ============================================================
// EXERCICE 6 : Calculer la hauteur de l'arbre
// Complexité : O(n)
// ============================================================

/**
 * Retourne la hauteur de l'arbre
 * Complexité : O(1) si on utilise le champ height, O(n) sinon
 */
public int getHeight() {
    return height(root);
}

// Note: height(Node) existe déjà dans l'implémentation de base


// ============================================================
// EXERCICE 7 : Compter le nombre total de nœuds
// Complexité : O(n)
// ============================================================

/**
 * Retourne le nombre total de nœuds dans l'arbre
 * Complexité : O(n)
 */
public int countNodes() {
    return countNodesRecursive(root);
}

private int countNodesRecursive(Node node) {
    if (node == null) return 0;
    return 1 + countNodesRecursive(node.left) + countNodesRecursive(node.right);
}


// ============================================================
// EXERCICE 8 : Compter les feuilles (nœuds sans enfants)
// Complexité : O(n)
// ============================================================

/**
 * Compte le nombre de feuilles dans l'AVL
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


// ============================================================
// EXERCICE 9 : Vérifier si une couleur existe (par composantes RGB)
// Complexité : O(log n)
// ============================================================

/**
 * Vérifie si une couleur avec les composantes RGB données existe
 * Complexité : O(log n)
 */
public boolean contains(int r, int g, int b) {
    String hexCode = String.format("%02x%02x%02x", r, g, b);
    return search(hexCode);
}


// ============================================================
// EXERCICE 10 : Retourner toutes les couleurs dans une liste
// Complexité : O(n)
// ============================================================

/**
 * Retourne toutes les couleurs de l'AVL dans une liste ordonnée
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


// ============================================================
// EXERCICE 11 : Trouver le successeur d'une couleur
// Complexité : O(log n)
// ============================================================

/**
 * Retourne le successeur (couleur suivante) d'une couleur donnée
 * Retourne null si pas de successeur
 * Complexité : O(log n)
 */
public String getSuccessor(String hexCode) {
    Node successor = null;
    Node current = root;
    
    while (current != null) {
        int cmp = compareColors(hexCode, current.hexCode);
        
        if (cmp < 0) {
            // hexCode < current : current pourrait être le successeur
            successor = current;
            current = current.left;
        } else {
            // hexCode >= current : chercher à droite
            current = current.right;
        }
    }
    
    return successor == null ? null : successor.hexCode;
}


// ============================================================
// EXERCICE 12 : Trouver le prédécesseur d'une couleur
// Complexité : O(log n)
// ============================================================

/**
 * Retourne le prédécesseur (couleur précédente) d'une couleur donnée
 * Retourne null si pas de prédécesseur
 * Complexité : O(log n)
 */
public String getPredecessor(String hexCode) {
    Node predecessor = null;
    Node current = root;
    
    while (current != null) {
        int cmp = compareColors(hexCode, current.hexCode);
        
        if (cmp > 0) {
            // hexCode > current : current pourrait être le prédécesseur
            predecessor = current;
            current = current.right;
        } else {
            // hexCode <= current : chercher à gauche
            current = current.left;
        }
    }
    
    return predecessor == null ? null : predecessor.hexCode;
}


// ============================================================
// EXERCICE 13 : Afficher l'arbre avec indentation (debug)
// Complexité : O(n)
// ============================================================

/**
 * Affiche l'arbre avec indentation pour visualisation
 * Complexité : O(n)
 */
public void printTree() {
    printTreeRecursive(root, "", true);
}

private void printTreeRecursive(Node node, String prefix, boolean isLeft) {
    if (node == null) return;
    
    System.out.println(prefix + (isLeft ? "├── " : "└── ") + node.hexCode);
    
    printTreeRecursive(node.left, prefix + (isLeft ? "│   " : "    "), true);
    printTreeRecursive(node.right, prefix + (isLeft ? "│   " : "    "), false);
}


// ============================================================
// EXERCICE 14 : Vider l'arbre
// Complexité : O(1)
// ============================================================

/**
 * Vide l'arbre (supprime tous les éléments)
 * Complexité : O(1)
 */
public void clear() {
    root = null;
}


// ============================================================
// EXERCICE 15 : Compter les couleurs dans une plage
// Complexité : O(n) dans le pire cas
// ============================================================

/**
 * Compte les couleurs entre minHex et maxHex (inclus)
 * Complexité : O(n) dans le pire cas, O(log n + k) en moyenne
 */
public int countInRange(String minHex, String maxHex) {
    return countInRangeRecursive(root, minHex, maxHex);
}

private int countInRangeRecursive(Node node, String minHex, String maxHex) {
    if (node == null) return 0;
    
    int cmpMin = compareColors(node.hexCode, minHex);
    int cmpMax = compareColors(node.hexCode, maxHex);
    
    int count = 0;
    
    // Si le nœud est dans la plage
    if (cmpMin >= 0 && cmpMax <= 0) {
        count = 1;
    }
    
    // Explorer à gauche si minHex pourrait être là
    if (cmpMin > 0) {
        count += countInRangeRecursive(node.left, minHex, maxHex);
    }
    
    // Explorer à droite si maxHex pourrait être là
    if (cmpMax < 0) {
        count += countInRangeRecursive(node.right, minHex, maxHex);
    }
    
    return count;
}


// ============================================================
// EXERCICE 16 : Calculer la somme des composantes RGB
// Complexité : O(n)
// ============================================================

/**
 * Calcule la somme totale des composantes R, G, B de toutes les couleurs
 * Retourne un tableau [sumR, sumG, sumB]
 * Complexité : O(n)
 */
public int[] sumRGB() {
    int[] sum = new int[3];
    sumRGBRecursive(root, sum);
    return sum;
}

private void sumRGBRecursive(Node node, int[] sum) {
    if (node == null) return;
    
    Color c = node.color;
    sum[0] += c.getRed();
    sum[1] += c.getGreen();
    sum[2] += c.getBlue();
    
    sumRGBRecursive(node.left, sum);
    sumRGBRecursive(node.right, sum);
}


// ============================================================
// EXERCICE 17 : Trouver la couleur la plus proche
// Complexité : O(n)
// ============================================================

/**
 * Trouve la couleur la plus proche d'une couleur cible (distance euclidienne RGB)
 * Complexité : O(n) - doit parcourir tout l'arbre
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
