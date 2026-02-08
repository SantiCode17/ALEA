# ALEA - Social Competitive Gaming App 🎯

## Descripción
ALEA es una aplicación Android de competitividad social gamificada que permite a usuarios crear retos, competir con amigos, gestionar apuestas amistosas con moneda virtual (Alea Coins), y escalar en rankings.

## Características Principales
- 🎮 **Retos (Challenges)**: Crea y acepta retos con tus amigos
- 💰 **Alea Coins**: Sistema de moneda virtual para apuestas amistosas
- 🏆 **Rankings**: Compite globalmente y semanalmente
- 👥 **Social**: Agrega amigos y chatea en tiempo real
- 🎨 **Diseño Premium**: UI oscura con glassmorphism y gradientes neón

## Arquitectura
- **Patrón**: MVVM + Repository Pattern
- **Inyección de Dependencias**: Hilt
- **Navegación**: Navigation Component (Single Activity)
- **Base de Datos**: Firebase Firestore
- **Autenticación**: Firebase Auth

## Requisitos Técnicos
- Min SDK: 26 (Android 8.0 Oreo)
- Target SDK: 36
- Kotlin 2.0.21
- Gradle 8.13.2

## Configuración del Proyecto

### 1. Firebase Setup
1. Ve a [Firebase Console](https://console.firebase.google.com/)
2. Crea un nuevo proyecto llamado "ALEA"
3. Agrega una aplicación Android con el package name: `com.example.alea`
4. Descarga el archivo `google-services.json`
5. Colócalo en la carpeta `app/`

### 2. Configuración de Firebase Auth
En Firebase Console:
1. Authentication → Sign-in method
2. Habilita "Email/Password"
3. (Opcional) Habilita "Google" para OAuth

### 3. Configuración de Firestore
En Firebase Console:
1. Firestore Database → Create database
2. Selecciona "Start in test mode" (para desarrollo)
3. Selecciona la región más cercana

### 4. Reglas de Firestore (Producción)
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
  }
}
```

## Estructura del Proyecto
```
app/src/main/java/com/example/alea/
├── AleaApplication.kt          # Application class con @HiltAndroidApp
├── MainActivity.kt             # Single Activity con Navigation
├── data/
│   ├── model/                  # Data classes (User, Challenge, Message, etc.)
│   └── repository/             # Repositorios para Firebase
├── di/
│   └── FirebaseModule.kt       # Módulo Hilt para Firebase
└── ui/
    ├── auth/                   # Login y Register
    ├── challenge/              # Create Challenge, Detail
    ├── chat/                   # Chat en tiempo real
    ├── friends/                # Lista de amigos, Add Friend
    ├── home/                   # Dashboard principal
    ├── notifications/          # Centro de notificaciones
    ├── onboarding/             # Pantalla de bienvenida
    ├── profile/                # Perfil del usuario
    ├── ranking/                # Leaderboard
    └── settings/               # Configuración
```

## Pantallas Implementadas
1. ✅ Onboarding
2. ✅ Login
3. ✅ Register  
4. ✅ Home Dashboard
5. ✅ Ranking
6. ✅ Create Challenge (Wizard 3 pasos)
7. ✅ Challenge Detail
8. ✅ Friends
9. ✅ Add Friend (Bottom Sheet)
10. ✅ Chat
11. ✅ Profile
12. ✅ Settings
13. ✅ Notifications

## Sistema de Diseño
### Colores
- **Background**: #121212 (negro profundo)
- **Surface**: #1E1E2C (glass effect)
- **Primary Gradient**: #FF8C42 → #FF4B6A (naranja a rosa)
- **Secondary Gradient**: #4E54C8 → #8F94FB (violeta a azul)

# ALEA — Especificación completa de la aplicación Android (Kotlin)

![ALEA](app/src/main/res/drawable/ic_launcher_foreground.png)

Última versión: 1.0 • Febrero 2026

Tabla de contenidos
- [Visión general](#visión-general)
- [Quick Start](#quick-start)
- [Características destacadas](#características-destacadas)
- [Sistema de diseño — IMMERSIVE NEON GLASS](#sistema-de-diseño---immersive-neon-glass)
- [Navegación y pantallas principales](#navegación-y-pantallas-principales)
- [Componentes reutilizables](#componentes-reutilizables)
- [Modelos de datos (resumen)](#modelos-de-datos-resumen)
- [Arquitectura y decisiones técnicas](#arquitectura-y-decisiones-técnicas)
- [Dependencias clave](#dependencias-clave)
- [Estructura de carpetas](#estructura-de-carpetas)
- [Roadmap y Fases](#roadmap-y-fases)
- [Contribuir](#contribuir)
- [Contacto y licencia](#contacto-y-licencia)

## Visión general

ALEA es una plataforma de gamificación social competitiva para retos y apuestas amistosas entre amigos. Actúa como árbitro social, facilita la creación de retos, rastrea resultados y gestiona una economía de reputación con "Alea Coins".

Target: Generación Z y Millennials (18–35 años).

Plataforma: Android (Kotlin, Jetpack Compose) • Arquitectura: MVVM + Hilt • Min SDK: 26 • Target SDK: 34

## Quick Start

Requisitos previos: Android Studio (Arctic Fox o superior), JDK 17.

1. Clona el repositorio:

```bash
git clone https://github.com/SantiCode17/ALEA.git
cd ALEA
```

2. Añade tu `google-services.json` en `app/` (Firebase).

3. Sincroniza y ejecuta en Android Studio o desde terminal:

```bash
./gradlew clean assembleDebug
./gradlew installDebug
```

4. Crea un proyecto en Firebase y habilita Authentication y Realtime/Firestore según prefieras (ver sección de configuración).

## Características destacadas

- Registro y autenticación (Email/Password, Google, Apple, Facebook)
- Creación de retos mediante wizard (6 pasos)
- Economía virtual (Alea Coins)
- Sistema de amigos y chat 1:1 en tiempo real
- Ranking / Leaderboard con Podium view
- Perfil gamificado (niveles, XP, badges)
- Notificaciones push (FCM)
- Offline-first: Room + sincronización con Firebase
- Animaciones y UI avanzadas (glassmorphism, neon glow, Lottie)

## Sistema de diseño — IMMERSIVE NEON GLASS

Fundamentos críticos para el diseño (resumen):

- Deep Dark Mode: background `#121212`, superficies `#1E1E2C` con opacidades.
- Glassmorphism: backdrop blur (RenderEffect API 31+ / Blurry para API 26-30).
- Gradientes exactos:
  - Gradiente primario (Solar Flare): `#FF8C42` → `#FF4B6A` (CTAs, FAB)
  - Gradiente secundario (Aurora): `#4E54C8` → `#8F94FB` (cards, badges)
- Tipografía: Poppins (pesos: 900, 700, 600, 500, 400, 300)
- Escalas de texto: Display 32sp, H1 28sp, H2 24sp, H3 20sp, Body 15sp, Caption 13sp, Small 11sp
- Sombras: glow effect para botones principales con colores del gradiente
- Animaciones: 300ms estándar, FastOutSlowIn; micro-interacciones obligatorias (pulse, scale, shimmer, confetti)

Colores semánticos:

- Success: `#4ADE80`
- Error: `#EF4444`
- Warning: `#FBBF24`
- Info: `#60A5FA`

### Notas de implementación visual

- Usar RenderEffect para blur en Android 12+ (API 31+). Para API 26-30 usar procesamiento de Bitmap o la librería Blurry.
- Importar Poppins desde Google Fonts (Compose Google Fonts) y declarar todos los pesos en el tema.

## Navegación y pantallas principales

La app usa una Single Activity con NavHost (Navigation Compose). Estructura de tabs (Bottom Navigation flotante):

Orden de pestañas:
1. Home (house)
2. Ranking (trophy)
3. FAB central — Create Challenge
4. Friends (users)
5. Profile (user_circle)

FAB: diámetro 56dp, gradiente Solar Flare, icono plus_bold 24dp, rotación y scale al presionar.

Pantallas principales (resumen):

- Splash & Onboarding (animaciones Lottie)
- Login / Registro (social logins)
- Home Dashboard (balance, últimos retos, quick actions)
- Ranking & Podium (custom Canvas)
- Friends (lista, add friend modal)
- Chat (burbujas, sticky input)
- Create Challenge (wizard 6 pasos)
- Profile (parallax header, stats)
- Settings

Cada pantalla incluye variantes para tablets y ajustes responsivos.

## Componentes reutilizables

Incluye componentes Compose listos: PrimaryButton (gradiente), SecondaryButton, ChipButton, GlassTextField, GlassCard, PulseClickable, ShimmerEffect.

Ejemplos de contrato (Inputs / Outputs):

- PrimaryButton: (text:String, onClick:()->Unit, enabled:Boolean) → botón estilizado con gradiente y feedback táctil.
- GlassTextField: (value:String, onValueChange:(String)->Unit, label:String) → TextField con estilo glass + validaciones.

Edge cases a cubrir cuando se implemente: campos vacíos, conexión intermitente, timeouts en autenticación social, conflicto de sincronización de coins.

## Modelos de datos (resumen)

- User: id, email, username, displayName, avatarUrl, coins, level, xp, winRate, totalChallenges, completedChallenges, ranking, isOnline
- Challenge: id, creatorId, title, description, category, difficulty, stake, deadline, participants, status, winnerId
- Message: id, senderId, receiverId, text, timestamp, isRead, type
- Achievement: id, name, description, emoji, requirement, unlockedAt

Las entidades tienen contrapartida Room (@Entity) para cache local y DTOs para Firebase.

## Arquitectura y decisiones técnicas

- Patrón: MVVM + Repositories + UseCases
- DI: Hilt
- State: StateFlow / LiveData según caso
- Almacenamiento local: Room (offline-first)
- Sincronización: Firebase Realtime Database o Firestore (según tu preferencia). WorkManager para sincronización en background.
- Notificaciones: FCM
- Ofuscación: ProGuard/R8 en release

Seguridad: reglas de Firebase, validación server-side (Cloud Functions) para transacciones de coins, EncryptedSharedPreferences para tokens.

## Dependencias clave

- Kotlin 1.9.x
- Android Gradle Plugin 8.2.x
- Jetpack Compose (BOM)
- Hilt (2.48+)
- Firebase (Auth, Database/Firestore, Storage, Messaging)
- Room 2.6.x
- Coil (image loading)
- Lottie (animaciones)
- MPAndroidChart (gráficas)
- Blurry (blur fallback en API < 31)

Consulta `app/build.gradle.kts` para la lista completa y versiones pinneadas.

## Estructura de carpetas

Resumen (ruta principal):

```
app/src/main/java/com/alea/app/
├── data/
│   ├── local/          # Room (dao, entities)
│   ├── remote/         # Firebase services
│   └── repository/     # Repositorios
├── domain/             # UseCases
├── ui/                 # Screens, components, theme
├── di/                 # Hilt modules
└── AleaApplication.kt
```

## Roadmap y fases

- Fase 1 — MVP (4 semanas): Autenticación Email/Google, diseño theme, home básico, crear retos básico, perfil, monedas locales.
- Fase 2 — Social (3 semanas): amigos, chat 1:1, ranking, FCM, sincronización Firebase.
- Fase 3 — Gamificación (2 semanas): niveles, achievements, estadísticas avanzadas.
- Fase 4 — Polish (2 semanas): animaciones avanzadas, onboarding interactivo, tests y optimizaciones.

## Contribuir

Si quieres contribuir:

1. Fork y crea una rama feature/mi-feature
2. Abre un PR con descripción y screenshots
3. Sigue la guía de estilo (Kotlin + Compose lint)

Checklist mínima para PR:

- Código probado en debug
- Tests unitarios para lógica crítica
- No revelar secretos en el repo

## Build & ejecución (rápido)

```bash
# Build y ejecutar desde la raíz
./gradlew clean assembleDebug
./gradlew installDebug
```

Para desarrollo en Android Studio: abrir el proyecto, sincronizar Gradle y ejecutar en un emulador o dispositivo.

## Archivos importantes a añadir (antes de ejecutar)

- `app/google-services.json` — configuración Firebase
- Variables secretas para OAuth providers (en Gradle properties o en CI secreto)

## Contacto y licencia

Autor: SantiCode17
Repositorio: https://github.com/SantiCode17/ALEA

Licencia: MIT (colocar LICENSE en la raíz si quieres abrir el proyecto)

---

Si quieres, puedo:

1. Ajustar el README para incluir badges automáticos (build/coverage)
2. Generar un `CONTRIBUTING.md` y `CODE_OF_CONDUCT`
3. Añadir una sección de `How to run tests` con ejemplos de unit/UI tests

Indícame si prefieres que deje una versión más corta para la vista principal del repo o que mantenga este README como la única fuente completa.

**Archivos modificados:**
- `README.md` — Versión definitiva y detallada del proyecto ALEA (guía, diseño, roadmap, dependencias)
