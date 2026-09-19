package com.oliver.lib2

import java.util.Scanner

class GameFlow {
    private val scanner = Scanner(System.`in`)
    private lateinit var player: Wizard

    fun start() {
        println("WIZARD ADVENTURE")
        print("What's your name? ")
        val nameInput = scanner.nextLine().trim()
        val name = if (nameInput.isBlank()) "Unknown Wizard" else nameInput

        player = Wizard(name)
        println("Good luck, ${player.name}! You're gonna need it!\n")

        mainMenu()
    }

    private fun mainMenu() {
        var isRunning = true
        while (isRunning) {
            println("What're you going to do?")
            println("1. View Stats")
            println("2. Enter battle")
            print("Choose: ")

            when (scanner.nextLine().trim()) {
                "1" -> viewStats()
                "2" -> {
                    val survived = enterBattle()
                    if (!survived) {
                        println("   YOU DIED! GAME RESTARTING...  ")
                        start()
                        return
                    }
                }
                else -> println("Invalid choice!")
            }
        }
    }

    private fun viewStats() {
        var inStatsMenu = true
        while (inStatsMenu) {
            println("${player.name}'s STATS")
            println("HP: ${player.currentHp}/${player.maxHp}")
            println("Mana: ${player.currentMana}/${player.maxMana}")

            if (player.isStrongWizard) {
                println("Status: Strong Wizard (Lifesteal: ${player.lifesteal})")
            } else {
                println("Kills needed to evolve: ${player.killsCount}/${player.killsNeeded}")
            }

            println("Mana Potions held: ${player.mpPotions}")
            println("Health Potions held: ${player.hpPotions}")
            println("a. Drink Mana Potion")
            println("b. Drink Health Potion")
            println("c. Rename self")
            println("d. Back")
            print("Choose: ")

            when (scanner.nextLine().trim().lowercase()) {
                "a" -> {
                    if (player.drinkMpPotion()) {
                        println("Drank Mana Potion (+15 MP)!")
                    } else {
                        println("No Mana Potions left!")
                    }
                }
                "b" -> {
                    if (player.drinkHpPotion()) {
                        println("Drank Health Potion (+25 HP)!")
                    } else {
                        println("No Health Potions left!")
                    }
                }
                "c" -> {
                    print("Enter new name: ")
                    val newName = scanner.nextLine().trim()
                    if (newName.isNotBlank()) {
                        player.name = newName
                        println("Name updated to ${player.name}")
                    } else {
                        println("Name cannot be empty!")
                    }
                }
                "d" -> inStatsMenu = false
                else -> println("Invalid input!")
            }
        }
        println()
    }

    private fun enterBattle(): Boolean {
        // Generate monster acak: Fire, Water, atau Grass
        val types = arrayOf("Fire", "Water", "Grass")
        val randomType = types[(0..2).random()]
        val enemy = Enemy(randomType)

        println("BATTLE")
        println("A wild ${enemy.name} appeared!")

        while (enemy.currentHp > 0 && player.currentHp > 0) {
            println("\n${player.name}")
            println("HP: ${player.currentHp}/${player.maxHp}")
            println("Mana: ${player.currentMana}/${player.maxMana}")
            println("HP Potions: ${player.hpPotions} /vert{} MP Potions:${player.mpPotions}")
            println("----------------------------------------")
            println("${enemy.name}")
            println("HP: ${enemy.currentHp}/${enemy.maxHp}")
            println("Type: ${enemy.type}")
            println("----------------------------------------")
            println("a. Fire Attack")
            println("b. Water Attack")
            println("c. Grass Attack")
            println("d. Drink potion")
            println("e. Run")
            print("Action: ")

            when (scanner.nextLine().trim().lowercase()) {
                "a" -> if (!playerAttack("Fire", enemy)) continue
                "b" -> if (!playerAttack("Water", enemy)) continue
                "c" -> if (!playerAttack("Grass", enemy)) continue
                "d" -> {
                    drinkPotionInBattle()
                    continue
                }
                "e" -> {
                    println("Cupu bet")
                    return true
                }
                else -> {
                    println("Invalid action!")
                    continue
                }
            }

            if (enemy.currentHp <= 0) {
                println("\n[VICTORY] You defeated ${enemy.name}!")
                player.addKill()

                if (player.checkEvolution()) {
                    println("Congratulations! You evolved into a STRONG WIZARD!")
                    println("Stats boosted: 1.5x HP & Mana! You gained Lifesteal!")
                }
                return true
            }

            println("\n${enemy.name} attacks you for${enemy.attackDamage} damage!")
            player.currentHp -= enemy.attackDamage

            if (player.currentHp <= 0) {
                return false
            }
        }
        return true
    }

    private fun playerAttack(spellType: String, enemy: Enemy): Boolean {
        val manaCost = 10
        if (player.currentMana < manaCost) {
            println("Not enough Mana! Need $manaCost Mana.")
            return false
        }

        player.currentMana -= manaCost

        var baseDamage = if (player.isStrongWizard) 15 else 10

        val isEffective = (spellType == "Fire" && enemy.type == "Grass") ||
                (spellType == "Water" && enemy.type == "Fire") ||
                (spellType == "Grass" && enemy.type == "Water")

        if (isEffective) {
            baseDamage *= 2
            println("$spellType attack deals$baseDamage damage!")
        } else {
            println("$spellType attack deals$baseDamage damage.")
        }

        enemy.currentHp -= baseDamage

        if (player.isStrongWizard && player.lifesteal > 0) {
            player.currentHp += player.lifesteal
            if (player.currentHp > player.maxHp) player.currentHp = player.maxHp
            println("Restored ${player.lifesteal} HP from attack!")
        }

        return true
    }

    private fun drinkPotionInBattle() {
        println("Which potion? (1. Health | 2. Mana): ")
        when (scanner.nextLine().trim()) {
            "1" -> {
                if (player.drinkHpPotion()) println("Restored 25 HP!")
                else println("No HP Potions!")
            }
            "2" -> {
                if (player.drinkMpPotion()) println("Restored 15 MP!")
                else println("No MP Potions!")
            }
            else -> println("Invalid choice!")
        }
    }
}