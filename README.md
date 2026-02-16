# 🎲 ALEA — Plataforma de Retos Sociales Gamificada

<p align="center">
  <strong>Compite. Desafía. Conquista.</strong><br/>
  Aplicación Android nativa de gamificación social competitiva
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Android-8.0%2B-brightgreen?logo=android" alt="Android 8.0+"/>
  <img src="https://img.shields.io/badge/Kotlin-2.0.21-7F52FF?logo=kotlin" alt="Kotlin 2.0.21"/>
  <img src="https://img.shields.io/badge/API-26%E2%80%9336-blue" alt="API 26–36"/>
  <img src="https://img.shields.io/badge/Firebase-Firestore-orange?logo=firebase" alt="Firebase"/>
  <img src="https://img.shields.io/badge/Hilt-2.53.1-green" alt="Hilt 2.53.1"/>
</p>

---

## 📖 Descripción

**ALEA** es una aplicación Android nativa que transforma la competición amistosa en una experiencia gamificada completa. Los usuarios pueden crear retos personalizados, apostar monedas virtuales (₳), subir en el ranking global y desbloquear logros mientras compiten con sus amigos.

Diseñada con un sistema visual **Neon Glass Dark Theme**, la app ofrece una experiencia premium con animaciones fluidas, transiciones cinematográficas y una interfaz moderna que prioriza la accesibilidad.

> 🎲 *Alea iacta est* — La suerte está echada

---

## ✨ Características Principales

### 🎯 Sistema de Retos
- **Creación de retos** en 3 pasos con wizard interactivo
- **8 categorías**: Fitness, Gaming, Estudio, Creatividad, Cocina, Música, Deportes, Otro
- **4 niveles de dificultad** con multiplicadores de apuesta (1x–5x)
- **Retos públicos y privados** entre amigos
- **Seguimiento de estado**: Activo, Pendiente, Completado, Cancelado

### 💰 Economía Virtual
- **Monedas Alea (₳)** como divisa principal
- **Sistema de apuestas** con multiplicadores por dificultad
- **Recompensas** por victorias, logros y rachas
- **Balance en tiempo real** con gráfico de evolución semanal

### 🏆 Gamificación
- **Sistema de niveles** (1–99) con títulos progresivos (Novato → Leyenda)
- **16+ logros desbloqueables** en 4 categorías (General, Social, Competitivo, Legendario)
- **Ranking global** con podio animado (Semanal / Histórico)
- **Barra de XP** con progreso visual hacia el siguiente nivel

### 👥 Social
- **Sistema de amigos** con búsqueda y solicitudes
- **Chat en tiempo real** entre amigos
- **Perfiles detallados** con estadísticas, gráficos de rendimiento y logros
- **Indicadores de estado** online/offline

### 🔔 Notificaciones
- **Centro de notificaciones** con tipos diferenciados (retos, amigos, sistema)
- **Navegación contextual** desde cada notificación
- **Marcar todo como leído** de un toque

---

## 🏗️ Arquitectura y Stack Técnico

| Componente | Tecnología | Versión |
|---|---|---|
| **Lenguaje** | Kotlin | 2.0.21 |
| **Build System** | Gradle KTS + AGP | 8.13.2 |
| **Min SDK** | Android 8.0 Oreo | API 26 |
| **Target/Compile SDK** | Android 15 | API 36 |
| **UI** | Fragments + ViewBinding + XML | AndroidX |
| **Navegación** | Navigation Component + SafeArgs | 2.9.0 |
| **Inyección de Dependencias** | Hilt (Dagger) | 2.53.1 |
| **Backend** | Firebase Firestore | BOM 33.12.0 |
| **Autenticación** | Firebase Authentication | — |
| **Gráficos** | MPAndroidChart | 3.1.0 |
| **Animaciones** | Lottie | 6.6.2 |
| **Procesamiento** | KSP | 2.0.21-1.0.28 |

### Patrón Arquitectónico — MVVM

```
┌──────────────────────────────────────────────┐
│  View Layer (Fragments + ViewBinding + XML)  │
├──────────────────────────────────────────────┤
│  ViewModel Layer (StateFlow + Hilt)          │
├──────────────────────────────────────────────┤
│  Repository Layer (Firebase / Demo Mode)     │
├──────────────────────────────────────────────┤
│  Model Layer (Data classes + Enums)          │
└──────────────────────────────────────────────┘
```

---

## 📂 Estructura del Proyecto

```
app/src/main/java/com/example/alea/
├── data/
│   ├── model/              # User, Challenge, Achievement, Notification, Friendship, Message
│   └── repository/         # AuthRepository, UserRepository, ChallengeRepository,
│                           # FriendsRepository, NotificationRepository, ChatRepository
├── di/
│   └── AppModule.kt        # Módulo Hilt con bindings de Firebase
├── ui/
│   ├── auth/               # LoginFragment, RegisterFragment
│   ├── challenge/          # CreateChallengeFragment (wizard 3 pasos), ChallengeDetailFragment,
│   │                       # VictoryFragment, CreateChallengeStepsAdapter
│   ├── chat/               # ChatFragment con mensajes en tiempo real
│   ├── components/         # PerformanceChartView (Custom View)
│   ├── friends/            # FriendsFragment, AddFriendBottomSheet
│   ├── home/               # HomeFragment, ChallengesAdapter, HomeViewModel
│   ├── notifications/      # NotificationsFragment, NotificationsAdapter
│   ├── onboarding/         # OnboardingFragment con animación Lottie
│   ├── profile/            # ProfileFragment, AchievementsAdapter, ProfileViewModel
│   ├── ranking/            # RankingFragment con podio Top 3, RankingAdapter
│   └── settings/           # SettingsFragment
├── AleaApplication.kt      # @HiltAndroidApp
└── MainActivity.kt          # Single Activity + Navigation + Bottom Navigation
```

---

## 📱 Pantallas (13 implementadas)

| # | Pantalla | Descripción |
|---|---|---|
| 1 | **Onboarding** | Animación Lottie + presentación de features |
| 2 | **Login** | Auth con email/password + Google Sign-In |
| 3 | **Register** | Registro con validaciones completas |
| 4 | **Home** | Balance ₳, gráfico de evolución, retos recientes, acciones rápidas, XP bar |
| 5 | **Ranking** | Podio Top 3 + lista completa + toggle Semanal/Histórico |
| 6 | **Crear Reto** | Wizard de 3 pasos: categoría → oponente → detalles |
| 7 | **Victoria** | Celebración con monedas y XP ganados |
| 8 | **Detalle del Reto** | Info completa con participantes y acciones |
| 9 | **Amigos** | Lista con estado online/offline + búsqueda |
| 10 | **Chat** | Mensajes en tiempo real estilo burbujas |
| 11 | **Perfil** | Stats, logros, rendimiento semanal, retos filtrados (completados/pendientes) |
| 12 | **Notificaciones** | Lista tipificada con acciones contextuales |
| 13 | **Ajustes** | Cuenta, tema, seguridad, cerrar sesión |

---

## 🎨 Sistema de Diseño — Neon Glass Dark Theme

| Elemento | Valor |
|---|---|
| **Background** | `#0A0E1A` |
| **Surface** | `#1A1E2E` con glass-morphism |
| **Primary Gradient** | `#00F5FF` → `#8B5CF6` (Cyan → Purple) |
| **Secondary Gradient** | `#FF6B9D` → `#C084FC` (Pink → Purple) |
| **Solar Gradient** | `#F59E0B` → `#EF4444` (Amber → Red) |
| **Gold/Silver/Bronze** | `#FFD700` / `#C0C0C0` / `#CD7F32` |
| **Success / Error** | `#4ADE80` / `#EF4444` |
| **Glass Border** | `#FFFFFF` @ 10% opacity |

### Componentes Custom
- `PerformanceChartView` — Gráfico de barras animado (Custom View)
- Glass Cards con bordes semi-transparentes
- FAB con gradiente radial
- Segmented controls con toggle groups
- Badge system (Active, Won, Lost, Trend)

### 6 Animaciones de Navegación
- `fade_in.xml`, `fade_out.xml`, `fade_scale_in.xml`
- `slide_in_right.xml`, `slide_out_left.xml`, `slide_in_left.xml`, `slide_out_right.xml`

---

## 🚀 Demo Mode

ALEA incluye un **modo demo completo** que funciona sin conexión a Firebase:

- **Usuario demo**: Player One — Nivel 12, 12,450 ₳, 4 logros desbloqueados
- **12 usuarios ficticios** con perfiles variados para ranking
- **6 retos de ejemplo** en diferentes categorías y estados
- **Notificaciones simuladas** con timestamps relativos
- **Chat mock** con mensajes de prueba
- **16 logros predefinidos** con recompensas de XP y monedas

> El modo demo se activa automáticamente cuando Firebase no está configurado (instancias `null`).

---

## ⚡ Instalación Rápida

```bash
# 1. Clonar el repositorio
git clone https://github.com/SantiCode17/ALEA.git
cd ALEA

# 2. Abrir en Android Studio
#    File → Open → Seleccionar carpeta ALEA

# 3. Sincronizar Gradle (automático)
#    O: File → Sync Project with Gradle Files

# 4. Ejecutar en dispositivo/emulador
#    Click ▶ Run — o desde terminal:
./gradlew installDebug
```

> 📄 Para instrucciones detalladas, consulta **[SETUP_GUIDE.md](SETUP_GUIDE.md)**

---

## 🔧 Requisitos

- **Android Studio** Ladybug (2024.2.1) o superior
- **JDK 17** o superior
- **Android SDK** 36
- **Dispositivo o emulador** con API 26+ (Android 8.0+)

---

## 🔥 Firebase (Opcional)

La app funciona completamente en **modo demo** sin Firebase. Para conectar Firebase:

1. Crea un proyecto en [Firebase Console](https://console.firebase.google.com/)
2. Añade app Android con package `com.example.alea`
3. Descarga `google-services.json` → carpeta `app/`
4. Habilita **Authentication** (Email/Password)
5. Crea base de datos **Firestore**

---

## 🛡️ Buenas Prácticas

- ✅ Inyección de dependencias completa con Hilt
- ✅ Null-safety en todas las instancias Firebase
- ✅ Validación de entrada en formularios
- ✅ Gestión de errores con fallback a modo demo
- ✅ StateFlow para estado reactivo sin memory leaks
- ✅ ViewBinding tipado (sin `findViewById`)
- ✅ SafeArgs para navegación segura
- ✅ ProGuard configurado para release builds
- ✅ Localización completa en español

---

## 📄 Licencia

Proyecto académico — Módulo **Diseño de Interfaces** · Ciclo Formativo **DAM** (Desarrollo de Aplicaciones Multiplataforma)

## 👤 Autor

**Santiago** — [@SantiCode17](https://github.com/SantiCode17)
