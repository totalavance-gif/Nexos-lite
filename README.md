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

## Releases firmados

Los tags `v*` disparan el workflow **Release APK**: compila el release,
lo firma con el keystore de los secrets, verifica la firma y lo publica en
**Releases** con descarga directa. La app avisa de nuevas versiones sola.

```sh
git tag v1.0.0
git push origin v1.0.0
```

Secrets necesarios (`Settings → Secrets → Actions`): `NEXOS_KEYSTORE_BASE64`,
`NEXOS_KEYSTORE_PASSWORD`, `NEXOS_KEY_ALIAS`, `NEXOS_KEY_PASSWORD`.
El keystore vive fuera del repo, con copia de seguridad.

## Compilar en local

Requisitos: JDK 21 + Android SDK con `compileSdk 37`.

```sh
echo "sdk.dir=/ruta/a/tu/Android-Sdk" > local.properties
./gradlew :app:assembleDebug
```

## Upstream (NewPipe)

El fork vive de dos fuentes upstream:

- **NewPipeExtractor** (librería, versión en `gradle/libs.versions.toml`): es
  lo que habla con YouTube. Si YouTube cambia algo y los videos fallan, lo
  normal es subir esta versión y soltar release. Urgente cuando rompe.
- **NewPipe app** (`upstream/dev`): features y fixes. Se trae con merge cada
  cierto tiempo; los conflictos suelen estar en archivos de marca.

```sh
git fetch upstream
git merge upstream/dev
```

El workflow **Upstream check** (lunes + manual) abre un issue cuando hay
extractor nuevo o commits pendientes, y lo cierra al ponerse al día.
