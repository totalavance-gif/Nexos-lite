# Nexos Tube

Fork personal de [NewPipe](https://github.com/TeamNewPipe/NewPipe)
(GPL-3.0-or-later, ver `LICENSE`) con marca propia: `Nexos Tube`,
paquete `com.nexos.tube`, icono y banner propios. El código Java/Kotlin
conserva sus paquetes `org.schabi.newpipe` para facilitar merges con upstream.

## Descargar el APK

Cada push a `main` compila y publica el APK debug:

1. Abre la pestaña **Actions** del repo.
2. Entra al último run verde de **Build APK**.
3. Descarga el artifact **app-debug** e instálalo (sideload).

Si un build falla, el workflow abre un issue con el extracto del log.

## Compilar en local

Requisitos: JDK 21 + Android SDK con `compileSdk 37`.

```sh
echo "sdk.dir=/ruta/a/tu/Android-Sdk" > local.properties
./gradlew :app:assembleDebug
```

## Traer cambios de upstream

```sh
git fetch upstream
git merge upstream/dev
```
