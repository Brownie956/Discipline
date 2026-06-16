package com.cbmedia.discipline

import com.cbmedia.discipline.model.CardRules
import com.cbmedia.discipline.model.CardType
import com.cbmedia.discipline.model.DrawResult
import com.cbmedia.discipline.model.Game
import com.cbmedia.discipline.model.GameState
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

object GameEngine {

    @OptIn(ExperimentalTime::class)
    fun startGame(
        baseTimerMinutes: Long,
        cardCounts: Map<CardType, Int>
    ): GameState {
        val deck = cardCounts.flatMap { (type, count) ->
            List(count) { type }
        }.shuffled()

        return GameState(
            remainingMinutes = baseTimerMinutes,
            deck = deck,
            discardPile = emptyList()
        )
    }

    @OptIn(ExperimentalTime::class)
    fun canDraw(
        game: Game,
        now: Instant = Clock.System.now()
    ): Boolean {
        val state = game.state
        val isFrozen = isGameFrozen(state.freezeEndsAt)
        val intervalHasPassed = state.lastDrawTime?.let { lastDraw ->
            now >= lastDraw + game.drawInterval
        } ?: true

        return state.deck.isNotEmpty() &&
                state.lastDrawTime != now &&
                !isFrozen &&
                intervalHasPassed
    }

    @OptIn(ExperimentalTime::class)
    fun drawCard(
        game: Game,
        now: Instant = Clock.System.now()
    ): DrawResult {
        require(canDraw(game, now)) {
            "Cannot draw a card today."
        }

        val state = game.state
        val drawnCard = state.deck.first()
        val remainingDeck = state.deck.drop(1)

        return when (drawnCard) {
            CardType.GREEN -> DrawResult(
                drawnCard = drawnCard,
                newState = state.copy(
                    remainingMinutes = state.remainingMinutes - CardRules.GREEN_DAY_SUBTRACT,
                    deck = remainingDeck,
                    discardPile = state.discardPile + drawnCard,
                    lastDrawnCard = drawnCard,
                    lastDrawTime = now
                )
            )

            CardType.RED -> DrawResult(
                drawnCard = drawnCard,
                newState = state.copy(
                    remainingMinutes = state.remainingMinutes + CardRules.RED_DAYS_ADDED,
                    deck = remainingDeck,
                    discardPile = state.discardPile + drawnCard,
                    lastDrawnCard = drawnCard,
                    lastDrawTime = now
                )
            )

            CardType.STICKY -> DrawResult(
                drawnCard = drawnCard,
                newState = state.copy(
                    remainingMinutes = state.remainingMinutes + CardRules.RED_DAYS_ADDED,
                    deck = (remainingDeck + drawnCard).shuffled(),
                    lastDrawnCard = drawnCard,
                    lastDrawTime = now
                )
            )

            CardType.YELLOW -> DrawResult(
                drawnCard = drawnCard,
                newState = state.copy(
                    deck = (remainingDeck + List(CardRules.YELLOW_REDS_ADDED) { CardType.RED }).shuffled(),
                    discardPile = state.discardPile + drawnCard,
                    lastDrawnCard = drawnCard,
                    lastDrawTime = now
                )
            )

            CardType.RESET -> DrawResult(
                drawnCard = drawnCard,
                newState = state.copy(
                    deck = (remainingDeck + state.discardPile).shuffled(),
                    discardPile = emptyList(),
                    lastDrawnCard = drawnCard,
                    lastDrawTime = now
                )
            )

            CardType.DOUBLE -> {
                val redsToAdd = remainingDeck.count { it == CardType.RED }
                val yellowsToAdd = remainingDeck.count { it == CardType.YELLOW }

                DrawResult(
                    drawnCard = drawnCard,
                    newState = state.copy(
                        deck = (
                                remainingDeck +
                                        List(redsToAdd) { CardType.RED } +
                                        List(yellowsToAdd) { CardType.YELLOW }
                                ).shuffled(),
                        discardPile = state.discardPile + drawnCard,
                        lastDrawnCard = drawnCard,
                        lastDrawTime = now
                    )
                )
            }

            CardType.FREEZE -> {
                val freezeDays = CardRules.randomFreezeDays()

                DrawResult(
                    drawnCard = drawnCard,
                    freezeDays = freezeDays,
                    newState = state.copy(
                        deck = remainingDeck,
                        discardPile = state.discardPile + drawnCard,
                        freezeEndsAt = now + freezeDays.days,
                        lastDrawnCard = drawnCard,
                        lastDrawTime = now
                    )
                )
            }

            CardType.ARCTIC -> {
                val freezeDays = CardRules.randomFreezeDays()

                DrawResult(
                    drawnCard = drawnCard,
                    freezeDays = freezeDays,
                    newState = state.copy(
                        deck = (remainingDeck + drawnCard).shuffled(),
                        freezeEndsAt = now + freezeDays.days,
                        lastDrawnCard = drawnCard,
                        lastDrawTime = now
                    )
                )
            }
        }
    }
}
