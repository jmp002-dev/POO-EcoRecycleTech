
```text
src/main/java/modelo        Lógica del dominio y persistencia
src/main/java/vista         Interfaz gráfica Swing
src/main/java/controlador   Coordinación entre modelo y vista
docs                        Documentación Javadoc
capturas                    Imágenes usadas en el manual
```

## Compilación y ejecución

### Linux/macOS

```bash
mkdir -p out
javac -encoding UTF-8 -d out $(find src/main/java -name "*.java")
java -cp out controlador.Main
```

### Windows PowerShell

```powershell
New-Item -ItemType Directory -Force out
$archivos = Get-ChildItem -Recurse src/main/java -Filter *.java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d out $archivos
java -cp out controlador.Main
```

## Funcionamiento básico

1. Pulsar **Simular entrada de residuo** para añadir un elemento a la cinta.
2. Pulsar **Procesar primer residuo** para clasificarlo.
3. Revisar los porcentajes de llenado de los cuatro depósitos.
4. Seleccionar un depósito y pulsar **Vaciar contenedor seleccionado** cuando sea necesario.
5. Al cerrar la ventana se guarda el estado en `estado_planta.json`.

## Git propuesto

```bash
git init
git branch -M main
git switch -c develop
git add .gitignore README.md src
git commit -m "feat: estructura inicial de carpetas MVC"
git add .
git commit -m "feat: modelo de residuos y contenedores"
git add .
git commit -m "feat: interfaz Swing y controlador"
git add .
git commit -m "feat: persistencia, logs y documentación"
```