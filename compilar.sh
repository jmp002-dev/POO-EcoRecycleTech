#!/usr/bin/env bash
set -euo pipefail
rm -rf out
mkdir -p out
javac -encoding UTF-8 -d out $(find src/main/java -name '*.java')
echo "Compilación completada. Ejecuta: java -cp out controlador.Main"
