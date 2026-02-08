package com.example.alea.data.model

/**
 * Achievement categories
 */
enum class AchievementCategory {
    GENERAL,
    SOCIAL,
    COMPETITIVE,
    LEGENDARY
}

/**
 * Achievement data class
 */
data class Achievement(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val emoji: String = "",
    val category: AchievementCategory = AchievementCategory.GENERAL,
    val requirement: Int = 0,
    val xpReward: Int = 50,
    val coinsReward: Long = 0,
    val unlockedAt: Long? = null
) {
    val isUnlocked: Boolean
        get() = unlockedAt != null
}

/**
 * Predefined achievements for ALEA
 */
object Achievements {
    val FIRST_STEPS = Achievement(
        id = "first_steps",
        name = "First Steps",
        description = "Create your first challenge",
        emoji = "🎯",
        category = AchievementCategory.GENERAL,
        requirement = 1,
        xpReward = 50,
        coinsReward = 100
    )

    val FIRST_WIN = Achievement(
        id = "first_win",
        name = "Winner!",
        description = "Win your first challenge",
        emoji = "🏆",
        category = AchievementCategory.COMPETITIVE,
        requirement = 1,
        xpReward = 100,
        coinsReward = 200
    )

    val FIVE_WINS = Achievement(
        id = "five_wins",
        name = "Getting Hot",
        description = "Win 5 challenges",
        emoji = "🔥",
        category = AchievementCategory.COMPETITIVE,
        requirement = 5,
        xpReward = 200,
        coinsReward = 500
    )

    val TEN_WINS = Achievement(
        id = "ten_wins",
        name = "Champion",
        description = "Win 10 challenges",
        emoji = "⭐",
        category = AchievementCategory.COMPETITIVE,
        requirement = 10,
        xpReward = 500,
        coinsReward = 1000
    )

    val TWENTY_FIVE_WINS = Achievement(
        id = "twenty_five_wins",
        name = "Unstoppable",
        description = "Win 25 challenges",
        emoji = "💪",
        category = AchievementCategory.COMPETITIVE,
        requirement = 25,
        xpReward = 1000,
        coinsReward = 2500
    )

    val FIFTY_WINS = Achievement(
        id = "fifty_wins",
        name = "Legend",
        description = "Win 50 challenges",
        emoji = "👑",
        category = AchievementCategory.LEGENDARY,
        requirement = 50,
        xpReward = 2500,
        coinsReward = 5000
    )

    val SOCIAL_BUTTERFLY = Achievement(
        id = "social_butterfly",
        name = "Social Butterfly",
        description = "Add 10 friends",
        emoji = "🦋",
        category = AchievementCategory.SOCIAL,
        requirement = 10,
        xpReward = 150,
        coinsReward = 300
    )

    val POPULAR = Achievement(
        id = "popular",
        name = "Popular",
        description = "Add 25 friends",
        emoji = "🌟",
        category = AchievementCategory.SOCIAL,
        requirement = 25,
        xpReward = 300,
        coinsReward = 600
    )

    val COIN_COLLECTOR = Achievement(
        id = "coin_collector",
        name = "Coin Collector",
        description = "Accumulate 10,000 coins",
        emoji = "💰",
        category = AchievementCategory.GENERAL,
        requirement = 10000,
        xpReward = 500,
        coinsReward = 0
    )

    val WEALTHY = Achievement(
        id = "wealthy",
        name = "Wealthy",
        description = "Accumulate 50,000 coins",
        emoji = "💎",
        category = AchievementCategory.LEGENDARY,
        requirement = 50000,
        xpReward = 1500,
        coinsReward = 0
    )

    val STREAK_3 = Achievement(
        id = "streak_3",
        name = "On Fire",
        description = "Win 3 challenges in a row",
        emoji = "🔥",
        category = AchievementCategory.COMPETITIVE,
        requirement = 3,
        xpReward = 200,
        coinsReward = 300
    )

    val STREAK_5 = Achievement(
        id = "streak_5",
        name = "Unstoppable Streak",
        description = "Win 5 challenges in a row",
        emoji = "⚡",
        category = AchievementCategory.COMPETITIVE,
        requirement = 5,
        xpReward = 500,
        coinsReward = 750
    )

    val TOP_10 = Achievement(
        id = "top_10",
        name = "Elite",
        description = "Reach the Top 10 ranking",
        emoji = "🏅",
        category = AchievementCategory.COMPETITIVE,
        requirement = 10,
        xpReward = 750,
        coinsReward = 1500
    )

    val TOP_1 = Achievement(
        id = "top_1",
        name = "The Best",
        description = "Reach #1 in ranking",
        emoji = "🥇",
        category = AchievementCategory.LEGENDARY,
        requirement = 1,
        xpReward = 2000,
        coinsReward = 5000
    )

    val FIRST_CHAT = Achievement(
        id = "first_chat",
        name = "Chatty",
        description = "Send your first message",
        emoji = "💬",
        category = AchievementCategory.SOCIAL,
        requirement = 1,
        xpReward = 25,
        coinsReward = 50
    )

    val EARLY_ADOPTER = Achievement(
        id = "early_adopter",
        name = "Early Adopter",
        description = "Join ALEA in its first year",
        emoji = "🚀",
        category = AchievementCategory.LEGENDARY,
        requirement = 1,
        xpReward = 500,
        coinsReward = 1000
    )

    /**
     * Get all available achievements
     */
    fun getAllAchievements(): List<Achievement> = listOf(
        FIRST_STEPS, FIRST_WIN, FIVE_WINS, TEN_WINS, TWENTY_FIVE_WINS, FIFTY_WINS,
        SOCIAL_BUTTERFLY, POPULAR, COIN_COLLECTOR, WEALTHY,
        STREAK_3, STREAK_5, TOP_10, TOP_1, FIRST_CHAT, EARLY_ADOPTER
    )

    /**
     * Get achievements by category
     */
    fun getByCategory(category: AchievementCategory): List<Achievement> =
        getAllAchievements().filter { it.category == category }
}
