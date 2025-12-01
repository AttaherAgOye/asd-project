================================================================================
                    DOSSIER DE PRÉPARATION AU CONTRÔLE
================================================================================

Ce dossier contient des exercices potentiels pour le contrôle pratique.

FICHIERS
--------

1. ExercicesRQuadtree.java
   - Toutes les méthodes supplémentaires possibles pour RQuadtree
   - 15 exercices avec implémentations complètes
   - Chaque méthode inclut sa complexité

2. ExercicesAVL.java
   - Toutes les méthodes supplémentaires possibles pour AVL
   - 17 exercices avec implémentations complètes
   - Chaque méthode inclut sa complexité

3. QuestionsTheoriques.txt
   - Questions sur les complexités
   - Questions sur les structures
   - Questions de débogage
   - Conseils pour l'examen

4. TestExercices.java
   - Programme de test pour vérifier les implémentations

5. ExercicesComplets.java
   - Version fusionnée avec TOUT le code prêt à copier


COMMENT UTILISER
----------------

1. Lire QuestionsTheoriques.txt pour réviser la théorie

2. Pour chaque exercice dans ExercicesRQuadtree.java et ExercicesAVL.java :
   a. Essayer d'implémenter la méthode vous-même
   b. Comparer avec la solution fournie
   c. Comprendre la complexité

3. Copier les méthodes dans les vrais fichiers pour tester


EXERCICES LES PLUS PROBABLES
----------------------------

RQUADTREE :
  ★★★ getHeight()           - Hauteur de l'arbre
  ★★★ getNodeCount()        - Nombre de nœuds internes
  ★★★ getAllColors()        - Lister les couleurs uniques
  ★★☆ toGrayscale()         - Convertir en niveaux de gris
  ★★☆ getColorAt(x, y)      - Couleur à une position
  ★★☆ toStrWithSize()       - Format avec tailles
  ★☆☆ mirrorHorizontal()    - Miroir horizontal

AVL :
  ★★★ getMin() / getMax()   - Couleur min/max
  ★★★ isBalanced()          - Vérifier l'équilibre
  ★★☆ toStrPrefix()         - Parcours préfixe
  ★★☆ countNodes()          - Compter les nœuds
  ★☆☆ findClosestColor()    - Couleur la plus proche


RAPPEL DES COMPLEXITÉS
----------------------

| Méthode              | Complexité  | Justification                    |
|----------------------|-------------|----------------------------------|
| Construction RQuad   | O(n)        | n = pixels, 1 visite par pixel   |
| compressLambda       | O(m)        | m = nœuds, 1 visite par nœud     |
| compressPhi          | O(m × k)    | k = nombre de fusions            |
| toPNG                | O(n)        | n = pixels                       |
| toStr (RQuad)        | O(m)        | m = nœuds                        |
| AVL search           | O(log n)    | arbre équilibré, hauteur log n   |
| AVL insert           | O(log n)    | descente + rééquilibrage O(1)    |
| AVL remove           | O(log n)    | descente + rééquilibrage O(1)    |
| getHeight            | O(m)        | parcours complet                 |
| getMin/getMax        | O(log n)    | descente simple                  |


BONNE CHANCE ! 🍀
================================================================================
