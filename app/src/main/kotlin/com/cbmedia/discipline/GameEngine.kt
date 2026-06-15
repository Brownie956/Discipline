package com.cbmedia.discipline

import com.cbmedia.discipline.model.CardRules
import com.cbmedia.discipline.model.CardType
import com.cbmedia.discipline.model.DrawResult
import com.cbmedia.discipline.model.GameState
import java.time.LocalDate

object GameEngine {

    fun startGame(
        baseDays: Int,
        cardCounts: Map<CardType, Int>
    ): GameState {
        val deck = cardCounts.flatMap { (type, count) ->
            List(count) { type }
        }.shuffled()

        return GameState(
            remainingDays = baseDays,
            deck = deck,
            discardPile = emptyList()
        )
    }

    fun canDrawToday(
        state: GameState,
        today: LocalDate = LocalDate.now()
    ): Boolean {
        val isFrozen = isGameFrozen(state.freezeEndsOn)

        return state.deck.isNotEmpty() &&
                state.lastDrawDate != today &&
                !isFrozen
    }

    fun drawCard(
        state: GameState,
        today: LocalDate = LocalDate.now()
    ): DrawResult {
        require(canDrawToday(state, today)) {
            "Cannot draw a card today."
        }

        val drawnCard = state.deck.first()
        val remainingDeck = state.deck.drop(1)

        return when (drawnCard) {
            CardType.GREEN -> DrawResult(
                drawnCard = drawnCard,
                newState = state.copy(
                    remainingDays = state.remainingDays - CardRules.GREEN_DAY_SUBTRACT,
                    deck = remainingDeck,
                    discardPile = state.discardPile + drawnCard,
                    lastDrawnCard = drawnCard,
                    lastDrawDate = today
                )
            )

            CardType.RED -> DrawResult(
                drawnCard = drawnCard,
                newState = state.copy(
                    remainingDays = state.remainingDays + CardRules.RED_DAYS_ADDED,
                    deck = remainingDeck,
                    discardPile = state.discardPile + drawnCard,
                    lastDrawnCard = drawnCard,
                    lastDrawDate = today
                )
            )

            CardType.STICKY -> DrawResult(
                drawnCard = drawnCard,
                newState = state.copy(
                    remainingDays = state.remainingDays + CardRules.RED_DAYS_ADDED,
                    deck = (remainingDeck + drawnCard).shuffled(),
                    lastDrawnCard = drawnCard,
                    lastDrawDate = today
                )
            )

            CardType.YELLOW -> DrawResult(
                drawnCard = drawnCard,
                newState = state.copy(
                    deck = (remainingDeck + List(CardRules.YELLOW_REDS_ADDED) { CardType.RED }).shuffled(),
                    discardPile = state.discardPile + drawnCard,
                    lastDrawnCard = drawnCard,
                    lastDrawDate = today
                )
            )

            CardType.RESET -> DrawResult(
                drawnCard = drawnCard,
                newState = state.copy(
                    deck = (remainingDeck + state.discardPile).shuffled(),
                    discardPile = emptyList(),
                    lastDrawnCard = drawnCard,
                    lastDrawDate = today
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
                        lastDrawDate = today
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
                        freezeEndsOn = today.plusDays(freezeDays.toLong()),
                        lastDrawnCard = drawnCard,
                        lastDrawDate = today
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
                        freezeEndsOn = today.plusDays(freezeDays.toLong()),
                        lastDrawnCard = drawnCard,
                        lastDrawDate = today
                    )
                )
            }
        }
    }
}
