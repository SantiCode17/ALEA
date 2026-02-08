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

### Componentes
- Cards con glassmorphism (blur + transparencia)
- Botones con gradiente y efecto ripple
- Bottom Navigation flotante
- Badges para estados de retos

## Próximos Pasos
1. [ ] Agregar `google-services.json` de Firebase
2. [ ] Implementar animaciones Lottie para mascota
3. [ ] Agregar gráficos con MPAndroidChart
4. [ ] Implementar Push Notifications
5. [ ] Agregar tests unitarios y de UI
6. [ ] Optimizar con ProGuard para release

## Build & Run
```bash
# Sincronizar Gradle
./gradlew sync

# Build debug
./gradlew assembleDebug

# Instalar en dispositivo
./gradlew installDebug
```

## Dependencias Principales
- Navigation Component 2.8.5
- Hilt 2.53.1
- Firebase BOM 33.7.0
- Lottie 6.6.2
- Coil 2.7.0
- MPAndroidChart v3.1.0

---

**Nota**: Esta aplicación requiere un archivo `google-services.json` válido de Firebase para funcionar correctamente.
# ALEA
