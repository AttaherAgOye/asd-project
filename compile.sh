#!/bin/bash
# Script de compilation du projet

echo "=== Compilation du projet ==="

# Créer le dossier target/classes s'il n'existe pas
mkdir -p target/classes

# Compiler tous les fichiers Java
javac -source 1.8 -target 1.8 -d target/classes -sourcepath src/main/java src/main/java/*.java

if [ $? -eq 0 ]; then
    echo "✓ Compilation réussie !"
    echo "Les fichiers .class sont dans target/classes/"
else
    echo "✗ Erreur de compilation"
    exit 1
fi
