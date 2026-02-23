package com.example.alea.data

import com.example.alea.R
import com.example.alea.data.model.*

object MockDataProvider {

    val currentUser = User(
        id = "user_001",
        name = "Santiago Sánchez",
        username = "@santisanchez",
        email = "santiago@gmail.com",
        bio = "Competidor nato. Reto a todos 🔥",
        aleaCoins = 13450,
        level = 12,
        totalChallenges = 224,
        weeklyPoints = 590,
        avatarRes = R.drawable.oso_alea_normal_fondo_azul
    )

    val friends = listOf(
        Friend("Alberto García", "@alberto_garcia", "Online", true, 1, 0xFF4E54C8.toInt()),
        Friend("Nuria Rodríguez", "@nuria_rod", "Hace 5 min", true, 1, 0xFFFF4B6A.toInt()),
        Friend("Michael Tony", "@mtony", "Hace 2 horas", false, 0, 0xFF8F94FB.toInt()),
        Friend("Joseph Ray", "@jray", "Online", false, 0, 0xFF4CAF50.toInt()),
        Friend("Thomas Adison", "@tadison", "Hace 1 día", false, 0, 0xFFFFC107.toInt()),
        Friend("Jira López", "@jiralopez", "Hace 3 días", false, 0, 0xFFFF5252.toInt())
    )

    val weeklyRanking = listOf(
        RankUser("#1", "Davis Curtis", "@daviscurtis", 2569, false, 0xFF4E54C8.toInt()),
        RankUser("#2", "Alena Donin", "@alenadonin", 1469, false, 0xFFFF4B6A.toInt()),
        RankUser("#3", "Craig Gouse", "@craigg", 1053, false, 0xFF8F94FB.toInt()),
        RankUser("#4", "Santiago Sánchez", "@santisanchez", 590, true, 0xFFFF8C42.toInt()),
        RankUser("#5", "Zain Vaccaro", "@zainv", 448, false, 0xFF4CAF50.toInt()),
        RankUser("#6", "Madelyn Dias", "@madelyndias", 390, false, 0xFFFFC107.toInt()),
        RankUser("#7", "Michael Tony", "@mtony", 312, false, 0xFF8F94FB.toInt()),
        RankUser("#8", "Joseph Ray", "@jray", 280, false, 0xFF4CAF50.toInt()),
        RankUser("#9", "Jira López", "@jiralopez", 175, false, 0xFFFF5252.toInt()),
        RankUser("#10", "Thomas Adison", "@tadison", 144, false, 0xFFFFC107.toInt())
    )

    val challenges = listOf(
        Challenge(1, "HACER 30 FLEXIONES SEGUIDAS", "Alberto García", 500, ChallengeStatus.PENDING,
            R.drawable.oso_alea_haciendo_ejercicio_sinfondo, "Resistencia", "25 Sep 2024",
            "Demuestra que puedes con las 30. Tienes 3 días para completarlo."),
        Challenge(2, "APROBAR CON UN 10 EL PRÓXIMO EXAMEN", "Nuria Rodríguez", 300, ChallengeStatus.ACTIVE,
            R.drawable.oso_alea_mujer_estudiando_sinfondo, "Estudio", "30 Oct 2024",
            "Demuestra que eres el mejor estudiante."),
        Challenge(3, "CORRER 5KM EN MENOS DE 30 MIN", "Joseph Ray", 200, ChallengeStatus.COMPLETED,
            R.drawable.oso_alea_deporte_aire_libre, "Deporte", "", "", 180),
        Challenge(4, "APRENDER UNA CANCIÓN EN GUITARRA", "Thomas Adison", 150, ChallengeStatus.REJECTED,
            R.drawable.oso_alea_mujerpelo_estudiando, "Habilidad"),
        Challenge(5, "COMER MÁS PICANTE QUE YO", "Jira López", 100, ChallengeStatus.ACTIVE,
            R.drawable.oso_alea_haciendo_ejercicio_sinfondo, "Humor"),
        Challenge(6, "NO USAR EL MÓVIL EN 24H", "Thomas Adison", 500, ChallengeStatus.COMPLETED,
            R.drawable.oso_alea_sentado_sofa_calendario_sinfondo, "Resistencia", "", "", 500)
    )

    val chatWithNuria = listOf(
        Message(false, "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", "14:20", "Sábado, 14:20"),
        Message(true, "Lorem El Mejor, El Uber..., El Magna Ut Labore Et Dolore", "14:21"),
        Message(false, "Lorem ipsum dolor Sit Amet, Consectetur Adipiscing.", "14:22"),
        Message(true, "Oh? Is Lorem Ipsum For Consequat", "14:23"),
        Message(false, "Lorem ipsum dolor Sit Amet, Consectetur Adipiscing.", "14:45"),
        Message(true, "Oh? Is Lorem Ipsum For Consequat", "15:00"),
        Message(false, "¡Buenos días! ¿Aceptas mi reto?", "09:00", "Hoy, 09:00"),
        Message(true, "Claro, lo acepto! Vamos a ver quién gana 😄", "09:01"),
        Message(false, "Recuerda que si pierdes me debes 500 monedas 😏", "09:02"),
        Message(true, "No problem, tengo mucha confianza en mí mismo", "09:03")
    )

    val coinHistory = listOf(
        CoinTransaction("Victoria en reto vs Alberto", 250, "10:23", "Hoy"),
        CoinTransaction("Reto completado: Flexiones", 150, "08:45", "Hoy"),
        CoinTransaction("Reto creado: Karaoke challenge", -100, "19:12", "Ayer"),
        CoinTransaction("Posición #4 en Ranking Semanal", 50, "12:00", "Ayer"),
        CoinTransaction("Bonus de racha x3", 300, "", "Esta semana"),
        CoinTransaction("Reto perdido vs Nuria", -200, "", "Esta semana"),
        CoinTransaction("Registro en ALEA", 500, "", "Hace 30 días")
    )

    val unlockedAchievements = listOf(
        Achievement("Primera Victoria", "Gana tu primer reto", "🥇"),
        Achievement("Imparable", "Completa 5 retos seguidos", "💪"),
        Achievement("Velocista", "Acepta un reto en menos de 1 min", "⚡"),
        Achievement("Top 5", "Entra en el Top 5 del ranking", "🏆"),
        Achievement("Rico en Alea", "Acumula 10,000★", "💰"),
        Achievement("Popular", "Ten 5 amigos en ALEA", "👥"),
        Achievement("Perfeccionista", "Completa un reto al 100%", "🎯"),
        Achievement("En Racha", "3 victorias seguidas", "🔥"),
        Achievement("Estudioso", "Completa 3 retos de estudio", "📚"),
        Achievement("Deportista", "Completa 3 retos de deporte", "🏃"),
        Achievement("Humorista", "Crea un reto de humor", "😂"),
        Achievement("Veterano", "Lleva 30 días en ALEA", "🌟")
    )

    val lockedAchievements = listOf(
        Achievement("Leyenda", "Llega al puesto #1", "👑", false),
        Achievement("Millonario", "Acumula 100,000★", "💎", false),
        Achievement("Invicto", "10 victorias sin perder", "🛡️", false),
        Achievement("Socialite", "Ten 20 amigos", "🎭", false),
        Achievement("Madrugador", "Completa un reto antes de las 7AM", "🌅", false),
        Achievement("Noctámbulo", "Completa un reto después de medianoche", "🌙", false),
        Achievement("Retador", "Crea 10 retos", "📢", false),
        Achievement("Coleccionista", "Gana 5 tipos de logros", "🏅", false),
        Achievement("Mentor", "Ayuda a un amigo a ganar su primer reto", "🎓", false),
        Achievement("Constante", "7 días seguidos activo", "📆", false),
        Achievement("Estratega", "Gana 3 retos con apuesta máxima", "🧠", false),
        Achievement("Multitarea", "Ten 3 retos activos a la vez", "⚡", false),
        Achievement("Resistente", "Pierde 5 retos y sigue activo", "💪", false),
        Achievement("Creativo", "Crea un reto personalizado", "🎨", false),
        Achievement("Amistoso", "Envía 50 mensajes", "💬", false),
        Achievement("Explorador", "Completa retos en 5 categorías", "🗺️", false),
        Achievement("Campeón Semanal", "Gana el reto semanal", "🏆", false),
        Achievement("Influencer", "Que 10 personas acepten tus retos", "⭐", false)
    )

    val notifications = listOf(
        Notification(1, "Alberto García", "te ha enviado un nuevo reto: HACER 30 FLEXIONES SEGUIDAS", "2 min", false, true),
        Notification(2, "Nuria Rodríguez", "ha completado el reto: APROBAR CON UN 10. ¡Ha ganado 200 Alea Coins!", "15 min", false, false, true),
        Notification(3, "Joseph Ray", "ha aceptado tu reto: CORRER 5KM. El reto comienza ahora.", "1 hora", true),
        Notification(4, "", "¡Has subido al puesto #4 del ranking semanal!", "3 horas", true, false, true),
        Notification(5, "Thomas Adison", "te ha enviado un mensaje.", "Ayer", true),
        Notification(6, "", "¡Has ganado el logro 'Primera Victoria'! +50 Alea Coins", "2 días", true, false, true)
    )

    val contactSuggestions = listOf(
        Friend("Metal Exchange", "@metalexch", "", phone = "+34 123-456-7890"),
        Friend("Michael tony", "@mtony2", "", phone = "+34 123-456-7890"),
        Friend("Joseph ray", "@jray2", "", phone = "+34 123-456-7890"),
        Friend("Thomas adison", "@tadison2", "", phone = "+34 123-456-7890"),
        Friend("Alberto Rodríguez", "@albrodz", "", phone = "+34 123-456-7890")
    )
}
