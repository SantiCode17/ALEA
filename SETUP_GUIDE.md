# 🛠️ ALEA — Guía de Configuración del Entorno de Desarrollo

Esta guía te ayudará a configurar el entorno completo para desarrollar y ejecutar ALEA en tu máquina local.

---

## 📋 Requisitos Previos

### Software Necesario

| Software | Versión Mínima | Descarga |
|---|---|---|
| **Android Studio** | Ladybug 2024.2.1 | [developer.android.com](https://developer.android.com/studio) |
| **JDK** | 17 | Incluido en Android Studio |
| **Git** | 2.30+ | [git-scm.com](https://git-scm.com/) |

### Hardware Recomendado

- **RAM**: 8 GB mínimo (16 GB recomendado)
- **Disco**: 10 GB libres (Android Studio + SDK + proyecto)
- **CPU**: Procesador con soporte de virtualización (Intel VT-x / AMD-V) para emulador

---

## 🚀 Paso 1: Clonar el Repositorio

```bash
git clone https://github.com/SantiCode17/ALEA.git
cd ALEA
```

---

## 📦 Paso 2: Abrir en Android Studio

1. Abre **Android Studio**
2. Selecciona **File → Open**
3. Navega hasta la carpeta `ALEA` y haz clic en **OK**
4. Espera a que Gradle sincronice el proyecto (puede tardar 2–5 minutos la primera vez)

### Resolución de Problemas con Gradle

Si la sincronización falla:

```
File → Invalidate Caches → Invalidate and Restart
```

O desde terminal:

```bash
./gradlew clean
./gradlew --refresh-dependencies
```

---

## 📱 Paso 3: Configurar Dispositivo

### Opción A: Dispositivo Físico (Recomendado)

1. En tu teléfono Android, ve a **Ajustes → Acerca del teléfono**
2. Toca **Número de compilación** 7 veces para activar "Opciones de desarrollador"
3. En **Opciones de desarrollador**, activa:
   - ✅ Depuración USB
   - ✅ Instalación vía USB
4. Conecta el teléfono por USB
5. Acepta el diálogo "¿Permitir depuración USB?" en el teléfono

Verifica la conexión:

```bash
adb devices
# Debería mostrar tu dispositivo
```

### Opción B: Emulador

1. En Android Studio: **Tools → Device Manager**
2. Click en **Create Device**
3. Selecciona **Pixel 7** (o similar)
4. Descarga imagen del sistema **API 34** (Android 14)
5. Finaliza y lanza el emulador

---

## ▶️ Paso 4: Ejecutar la App

### Desde Android Studio

1. Selecciona tu dispositivo en la barra superior
2. Haz clic en el botón **▶ Run** (o `Shift+F10`)
3. Espera la compilación e instalación

### Desde Terminal

```bash
# Compilar
./gradlew assembleDebug

# Instalar en dispositivo conectado
./gradlew installDebug

# O directamente con ADB
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Lanzar la app
adb shell am start -n com.example.alea/.MainActivity
```

---

## 🔥 Paso 5: Configurar Firebase (Opcional)

> **Nota**: La app funciona perfectamente en **modo demo** sin Firebase. Este paso es solo si quieres conectar un backend real.

### 5.1 Crear Proyecto Firebase

1. Ve a [Firebase Console](https://console.firebase.google.com/)
2. Clic en **Añadir proyecto** → Nombre: "ALEA"
3. Desactiva Google Analytics (o actívalo si quieres)
4. Espera a que se cree

### 5.2 Registrar App Android

1. En el proyecto, clic en el icono de **Android**
2. Package name: `com.example.alea`
3. Apodo: "ALEA Android"
4. SHA-1 (para Google Sign-In):

```bash
# Desde la raíz del proyecto:
./gradlew signingReport
# Copia el SHA-1 del variant "debug"
```

5. Descarga `google-services.json`
6. Cópialo a `app/google-services.json` (reemplazando el existente)

### 5.3 Habilitar Autenticación

1. En Firebase Console → **Authentication** → **Sign-in method**
2. Habilita **Correo electrónico/Contraseña**
3. (Opcional) Habilita **Google** para OAuth

### 5.4 Crear Base de Datos Firestore

1. En Firebase Console → **Firestore Database**
2. Clic en **Crear base de datos**
3. Selecciona **Modo de prueba** (para desarrollo)
4. Elige la región más cercana (ej: `europe-west1`)

### 5.5 Reglas de Seguridad (Producción)

Cuando estés listo para producción, aplica estas reglas en Firestore:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId} {
      allow read: if request.auth != null;
      allow write: if request.auth != null && request.auth.uid == userId;
    }
    match /challenges/{challengeId} {
      allow read: if request.auth != null;
      allow create: if request.auth != null;
      allow update: if request.auth != null && 
        (request.auth.uid == resource.data.creatorId || 
         request.auth.uid == resource.data.opponentId);
    }
    match /friendships/{friendshipId} {
      allow read: if request.auth != null;
      allow write: if request.auth != null;
    }
    match /messages/{messageId} {
      allow read: if request.auth != null;
      allow create: if request.auth != null;
    }
    match /notifications/{notificationId} {
      allow read: if request.auth != null;
      allow write: if request.auth != null;
    }
  }
}
```

---

## 🏗️ Estructura de Dependencias

El proyecto usa un **catálogo de versiones** centralizado en `gradle/libs.versions.toml`:

| Categoría | Dependencias Principales |
|---|---|
| **Android Core** | core-ktx, appcompat, material3, constraintlayout |
| **Navigation** | navigation-fragment-ktx, navigation-ui-ktx, navigation-safe-args |
| **Hilt** | hilt-android, hilt-compiler (KSP) |
| **Firebase** | firebase-bom, firestore, auth |
| **UI** | lottie, mpandroidchart, gridlayout |
| **Testing** | junit, espresso, ext-junit |

---

## 🧪 Ejecutar Tests

```bash
# Tests unitarios
./gradlew testDebugUnitTest

# Tests de instrumentación (requiere dispositivo/emulador)
./gradlew connectedDebugAndroidTest
```

---

## 📦 Generar APK de Release

```bash
# APK debug (sin firmar)
./gradlew assembleDebug
# → app/build/outputs/apk/debug/app-debug.apk

# APK release (requiere keystore)
./gradlew assembleRelease
```

Para firmar el APK de release, crea un archivo `keystore.properties` en la raíz:

```properties
storePassword=tu_password
keyPassword=tu_key_password
keyAlias=tu_alias
storeFile=ruta/a/tu/keystore.jks
```

---

## 🐛 Solución de Problemas Comunes

### Error: "SDK location not found"
Crea un archivo `local.properties` en la raíz con:
```properties
sdk.dir=/ruta/a/tu/Android/Sdk
```

### Error: "Failed to find target android-36"
Abre **SDK Manager** en Android Studio e instala **Android API 36**.

### Error: "Gradle sync failed"
```bash
./gradlew clean
./gradlew --refresh-dependencies
```

### La app muestra datos demo en vez de Firebase
Verifica que:
1. `google-services.json` está en `app/`
2. El package name coincide: `com.example.alea`
3. Firebase Auth y Firestore están habilitados en la consola

### Error al instalar en dispositivo
```bash
# Desinstalar versión anterior
adb uninstall com.example.alea

# Reinstalar
./gradlew installDebug
```

---

## 📚 Recursos Adicionales

- [Documentación oficial de Android](https://developer.android.com/docs)
- [Firebase Documentation](https://firebase.google.com/docs)
- [Hilt Dependency Injection](https://developer.android.com/training/dependency-injection/hilt-android)
- [Navigation Component](https://developer.android.com/guide/navigation)
- [Material Design 3](https://m3.material.io/)

---

## 👤 Autor

**Santiago** — [@SantiCode17](https://github.com/SantiCode17)
