#!/bin/bash
# Script d'exécution du projet

# Compiler d'abord si nécessaire
if [ ! -d "target/classes" ] || [ ! -f "target/classes/Main.class" ]; then
    echo "Compilation nécessaire..."
    ./compile.sh
    if [ $? -ne 0 ]; then
        exit 1
    fi
fi

echo ""
echo "=== Exécution du programme ==="
echo ""

# Exécuter avec les arguments fournis
if [ $# -eq 0 ]; then
    # Mode interactif
    java -cp target/classes Main
else
    # Mode non-interactif avec arguments
    java -cp target/classes Main "$@"
fi
