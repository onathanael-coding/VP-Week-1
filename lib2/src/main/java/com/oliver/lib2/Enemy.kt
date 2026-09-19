package com.oliver.lib2

class Enemy(val type: String) {
    val name: String = type + "mon"
    val maxHp: Int = 30
    var currentHp: Int = maxHp
    val attackDamage: Int = 10
}