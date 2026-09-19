package com.oliver.lib2

class Wizard(var name: String) {
    var maxHp: Int = 50
    var currentHp: Int = 50
    var maxMana: Int = 30
    var currentMana: Int = 30

    var hpPotions: Int = 5
    var mpPotions: Int = 5

    var killsCount: Int = 0
    val killsNeeded: Int = 5
    var isStrongWizard: Boolean = false
    var lifesteal: Int = 0

    // Potion Logic
    fun drinkHpPotion(): Boolean {
        if (hpPotions <= 0) return false
        hpPotions--
        currentHp += 25
        if (currentHp > maxHp) currentHp = maxHp // Mencegah HP melebihi batas maks
        return true
    }

    fun drinkMpPotion(): Boolean {
        if (mpPotions <= 0) return false
        mpPotions--
        currentMana += 15
        if (currentMana > maxMana) currentMana = maxMana
        return true
    }

    // Logic Evolusi saat membunuh musuh
    fun checkEvolution(): Boolean {
        if (!isStrongWizard && killsCount >= killsNeeded) {
            isStrongWizard = true
            maxHp = (maxHp * 1.5).toInt()     // 75 HP
            currentHp = maxHp                 // Auto heal saat evolusi
            maxMana = (maxMana * 1.5).toInt() // 45 MP
            currentMana = maxMana
            lifesteal = 1
            return true
        }
        return false
    }

    fun addKill() {
        killsCount++
        if (isStrongWizard) {
            lifesteal++ // Setiap kill menambah +1 lifesteal
        }
    }
}