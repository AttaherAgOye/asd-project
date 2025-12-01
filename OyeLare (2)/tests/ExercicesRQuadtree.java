/**
 * EXERCICES POTENTIELS - RQUADTREE
 * Méthodes supplémentaires à ajouter dans RQuadtree.java
 * 
 * Pour tester : copier ces méthodes dans RQuadtree.java
 */

// ============================================================
// EXERCICE 1 : Compter le nombre de nœuds INTERNES
// Complexité : O(m) où m = nombre de nœuds
// ============================================================

/**
 * Compte le nombre de nœuds internes (non-feuilles) dans l'arbre
 * Complexité : O(m) où m = nombre de nœuds
 */
public int getNodeCount() {
    return countNodes(root);
}

private int countNodes(Node node) {
    if (node == null || node.isLeaf()) {
        return 0;
    }
    // Ce nœud est interne (1) + récursion sur les 4 fils
    return 1 + countNodes(node.NO) + countNodes(node.NE) 
             + countNodes(node.SE) + countNodes(node.SO);
}


// ============================================================
// EXERCICE 2 : Calculer la hauteur/profondeur de l'arbre
// Complexité : O(m) où m = nombre de nœuds
// ============================================================

/**
 * Retourne la hauteur maximale de l'arbre
 * Une feuille a une hauteur de 1
 * Complexité : O(m) où m = nombre de nœuds
 */
public int getHeight() {
    return computeHeight(root);
}

private int computeHeight(Node node) {
    if (node == null) {
        return 0;
    }
    if (node.isLeaf()) {
        return 1;
    }
    // 1 + max des hauteurs des fils
    int hNO = computeHeight(node.NO);
    int hNE = computeHeight(node.NE);
    int hSE = computeHeight(node.SE);
    int hSO = computeHeight(node.SO);
    return 1 + Math.max(Math.max(hNO, hNE), Math.max(hSE, hSO));
}


// ============================================================
// EXERCICE 3 : Trouver la couleur la plus fréquente
// Complexité : O(m) où m = nombre de nœuds
// ============================================================

/**
 * Retourne la couleur la plus fréquente parmi les feuilles
 * Complexité : O(m) où m = nombre de nœuds
 */
public Color getMostFrequentColor() {
    // Utiliser une liste pour stocker les couleurs (pas de HashMap !)
    java.util.List<Color> colors = new java.util.ArrayList<>();
    java.util.List<Integer> counts = new java.util.ArrayList<>();
    
    collectColorFrequencies(root, colors, counts);
    
    if (colors.isEmpty()) return null;
    
    // Trouver le max
    int maxIndex = 0;
    for (int i = 1; i < counts.size(); i++) {
        if (counts.get(i) > counts.get(maxIndex)) {
            maxIndex = i;
        }
    }
    return colors.get(maxIndex);
}

private void collectColorFrequencies(Node node, java.util.List<Color> colors, 
                                      java.util.List<Integer> counts) {
    if (node == null) return;
    
    if (node.isLeaf()) {
        // Chercher si la couleur existe déjà
        int index = -1;
        for (int i = 0; i < colors.size(); i++) {
            if (colors.get(i).equals(node.color)) {
                index = i;
                break;
            }
        }
        if (index >= 0) {
            counts.set(index, counts.get(index) + 1);
        } else {
            colors.add(node.color);
            counts.add(1);
        }
    } else {
        collectColorFrequencies(node.NO, colors, counts);
        collectColorFrequencies(node.NE, colors, counts);
        collectColorFrequencies(node.SE, colors, counts);
        collectColorFrequencies(node.SO, colors, counts);
    }
}


// ============================================================
// EXERCICE 4 : Parcours PRÉFIXE (nœud avant les fils)
// Complexité : O(m) où m = nombre de nœuds
// ============================================================

/**
 * Représentation textuelle en parcours préfixe
 * Le nœud parent est affiché AVANT ses fils
 * Complexité : O(m)
 */
public String toStrPrefix() {
    StringBuilder sb = new StringBuilder();
    toStrPrefixRecursive(root, sb);
    return sb.toString().trim();
}

private void toStrPrefixRecursive(Node node, StringBuilder sb) {
    if (node == null) return;
    
    if (node.isLeaf()) {
        sb.append(ImagePNG.colorToHex(node.color)).append(" ");
    } else {
        // PRÉFIXE : d'abord le nœud (représenté par '['), puis les fils
        sb.append("[ ");
        toStrPrefixRecursive(node.NO, sb);
        toStrPrefixRecursive(node.NE, sb);
        toStrPrefixRecursive(node.SE, sb);
        toStrPrefixRecursive(node.SO, sb);
        sb.append("] ");
    }
}


// ============================================================
// EXERCICE 5 : Parcours SUFFIXE (fils avant le nœud)
// Complexité : O(m) où m = nombre de nœuds
// ============================================================

/**
 * Représentation textuelle en parcours suffixe
 * Les fils sont affichés AVANT le nœud parent
 * Complexité : O(m)
 */
public String toStrSuffix() {
    StringBuilder sb = new StringBuilder();
    toStrSuffixRecursive(root, sb);
    return sb.toString().trim();
}

private void toStrSuffixRecursive(Node node, StringBuilder sb) {
    if (node == null) return;
    
    if (node.isLeaf()) {
        sb.append(ImagePNG.colorToHex(node.color)).append(" ");
    } else {
        // SUFFIXE : d'abord les fils, puis le nœud (représenté par '>')
        toStrSuffixRecursive(node.NO, sb);
        toStrSuffixRecursive(node.NE, sb);
        toStrSuffixRecursive(node.SE, sb);
        toStrSuffixRecursive(node.SO, sb);
        sb.append("> ");
    }
}


// ============================================================
// EXERCICE 6 : Lister toutes les couleurs uniques
// Complexité : O(m × k) où k = nombre de couleurs uniques
// ============================================================

/**
 * Retourne la liste de toutes les couleurs uniques dans le quadtree
 * Complexité : O(m × k) où m = nœuds, k = couleurs uniques
 */
public java.util.List<Color> getAllColors() {
    java.util.List<Color> colors = new java.util.ArrayList<>();
    collectUniqueColors(root, colors);
    return colors;
}

private void collectUniqueColors(Node node, java.util.List<Color> colors) {
    if (node == null) return;
    
    if (node.isLeaf()) {
        // Vérifier si la couleur n'est pas déjà dans la liste
        boolean found = false;
        for (Color c : colors) {
            if (c.equals(node.color)) {
                found = true;
                break;
            }
        }
        if (!found) {
            colors.add(node.color);
        }
    } else {
        collectUniqueColors(node.NO, colors);
        collectUniqueColors(node.NE, colors);
        collectUniqueColors(node.SE, colors);
        collectUniqueColors(node.SO, colors);
    }
}


// ============================================================
// EXERCICE 7 : Compression par différence de COULEUR (pas luminance)
// Complexité : O(m) où m = nombre de nœuds
// ============================================================

/**
 * Compression basée sur la différence de couleur RGB
 * Fusionne si la différence max entre les composantes RGB est <= threshold
 * Complexité : O(m)
 */
public void compressColor(int threshold) {
    if (threshold < 0 || threshold > 255) {
        throw new IllegalArgumentException("Threshold doit être entre 0 et 255");
    }
    compressColorRecursive(root, threshold);
}

private void compressColorRecursive(Node node, int threshold) {
    if (node == null || node.isLeaf()) {
        return;
    }
    
    // Récursion d'abord
    compressColorRecursive(node.NO, threshold);
    compressColorRecursive(node.NE, threshold);
    compressColorRecursive(node.SE, threshold);
    compressColorRecursive(node.SO, threshold);
    
    // Vérifier si on peut fusionner
    if (node.NO.isLeaf() && node.NE.isLeaf() && 
        node.SE.isLeaf() && node.SO.isLeaf()) {
        
        // Calculer la différence max entre toutes les paires
        int maxDiff = 0;
        Color[] colors = {node.NO.color, node.NE.color, node.SE.color, node.SO.color};
        
        for (int i = 0; i < 4; i++) {
            for (int j = i + 1; j < 4; j++) {
                int dr = Math.abs(colors[i].getRed() - colors[j].getRed());
                int dg = Math.abs(colors[i].getGreen() - colors[j].getGreen());
                int db = Math.abs(colors[i].getBlue() - colors[j].getBlue());
                maxDiff = Math.max(maxDiff, Math.max(dr, Math.max(dg, db)));
            }
        }
        
        if (maxDiff <= threshold) {
            mergeNode(node);
        }
    }
}


// ============================================================
// EXERCICE 8 : Convertir en niveaux de gris
// Complexité : O(m) où m = nombre de nœuds
// ============================================================

/**
 * Convertit toutes les couleurs en niveaux de gris
 * Utilise la luminance déjà calculée
 * Complexité : O(m)
 */
public void toGrayscale() {
    toGrayscaleRecursive(root);
}

private void toGrayscaleRecursive(Node node) {
    if (node == null) return;
    
    if (node.isLeaf()) {
        // Utiliser la luminance pour calculer le niveau de gris
        int gray = (int)(node.luminance * 255);
        gray = Math.max(0, Math.min(255, gray)); // Borner entre 0 et 255
        node.color = new Color(gray, gray, gray);
    } else {
        toGrayscaleRecursive(node.NO);
        toGrayscaleRecursive(node.NE);
        toGrayscaleRecursive(node.SE);
        toGrayscaleRecursive(node.SO);
        // Recalculer la couleur moyenne du nœud interne
        calculateAverageColor(node);
    }
}


// ============================================================
// EXERCICE 9 : Appliquer un filtre négatif
// Complexité : O(m) où m = nombre de nœuds
// ============================================================

/**
 * Applique un filtre négatif (inverse les couleurs)
 * Complexité : O(m)
 */
public void toNegative() {
    toNegativeRecursive(root);
}

private void toNegativeRecursive(Node node) {
    if (node == null) return;
    
    if (node.isLeaf()) {
        // Inverser chaque composante : nouveau = 255 - ancien
        int r = 255 - node.color.getRed();
        int g = 255 - node.color.getGreen();
        int b = 255 - node.color.getBlue();
        node.color = new Color(r, g, b);
        node.calculateLuminance(); // Recalculer la luminance
    } else {
        toNegativeRecursive(node.NO);
        toNegativeRecursive(node.NE);
        toNegativeRecursive(node.SE);
        toNegativeRecursive(node.SO);
        // Recalculer la couleur moyenne
        calculateAverageColor(node);
    }
}


// ============================================================
// EXERCICE 10 : Obtenir la couleur à une coordonnée (x, y)
// Complexité : O(log n) où n = taille de l'image
// ============================================================

/**
 * Retourne la couleur du pixel à la position (x, y)
 * Complexité : O(log n) - descente dans l'arbre
 */
public Color getColorAt(int x, int y) {
    if (x < 0 || x >= width || y < 0 || y >= height) {
        throw new IllegalArgumentException("Coordonnées hors limites");
    }
    return getColorAtRecursive(root, x, y);
}

private Color getColorAtRecursive(Node node, int x, int y) {
    if (node == null) return null;
    
    if (node.isLeaf()) {
        return node.color;
    }
    
    // Déterminer dans quel quadrant se trouve le point
    int midX = node.x + node.size / 2;
    int midY = node.y + node.size / 2;
    
    if (x < midX) {
        if (y < midY) {
            return getColorAtRecursive(node.NO, x, y); // Nord-Ouest
        } else {
            return getColorAtRecursive(node.SO, x, y); // Sud-Ouest
        }
    } else {
        if (y < midY) {
            return getColorAtRecursive(node.NE, x, y); // Nord-Est
        } else {
            return getColorAtRecursive(node.SE, x, y); // Sud-Est
        }
    }
}


// ============================================================
// EXERCICE 11 : Vérifier si deux quadtrees sont identiques
// Complexité : O(m) où m = nombre de nœuds
// ============================================================

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
    // Cas de base
    if (n1 == null && n2 == null) return true;
    if (n1 == null || n2 == null) return false;
    
    // Vérifier si les deux sont des feuilles ou des nœuds internes
    if (n1.isLeaf() != n2.isLeaf()) return false;
    
    if (n1.isLeaf()) {
        // Comparer les couleurs
        return n1.color.equals(n2.color);
    } else {
        // Comparer récursivement les 4 fils
        return equalsRecursive(n1.NO, n2.NO) &&
               equalsRecursive(n1.NE, n2.NE) &&
               equalsRecursive(n1.SE, n2.SE) &&
               equalsRecursive(n1.SO, n2.SO);
    }
}


// ============================================================
// EXERCICE 12 : Représentation avec tailles
// Complexité : O(m) où m = nombre de nœuds
// ============================================================

/**
 * Représentation textuelle avec la taille de chaque région
 * Format: code:taille pour les feuilles
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
        sb.append(ImagePNG.colorToHex(node.color));
        sb.append(":").append(node.size);
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


// ============================================================
// EXERCICE 13 : Compter les feuilles d'une couleur spécifique
// Complexité : O(m) où m = nombre de nœuds
// ============================================================

/**
 * Compte le nombre de feuilles ayant une couleur spécifique
 * Complexité : O(m)
 */
public int countColorOccurrences(Color color) {
    return countColorRecursive(root, color);
}

private int countColorRecursive(Node node, Color target) {
    if (node == null) return 0;
    
    if (node.isLeaf()) {
        return node.color.equals(target) ? 1 : 0;
    }
    
    return countColorRecursive(node.NO, target) +
           countColorRecursive(node.NE, target) +
           countColorRecursive(node.SE, target) +
           countColorRecursive(node.SO, target);
}


// ============================================================
// EXERCICE 14 : Miroir horizontal
// Complexité : O(m) où m = nombre de nœuds
// ============================================================

/**
 * Applique un miroir horizontal (échange gauche-droite)
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
    
    // Récursion
    mirrorHorizontalRecursive(node.NO);
    mirrorHorizontalRecursive(node.NE);
    mirrorHorizontalRecursive(node.SE);
    mirrorHorizontalRecursive(node.SO);
}


// ============================================================
// EXERCICE 15 : Miroir vertical
// Complexité : O(m) où m = nombre de nœuds
// ============================================================

/**
 * Applique un miroir vertical (échange haut-bas)
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
    
    // Récursion
    mirrorVerticalRecursive(node.NO);
    mirrorVerticalRecursive(node.NE);
    mirrorVerticalRecursive(node.SE);
    mirrorVerticalRecursive(node.SO);
}
