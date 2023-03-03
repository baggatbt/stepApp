package com.example.myapplication.ui.main


enum class AbilityType(var damage: Int, var speed: Int, var staminaCost: Int) {
    SLASH(2,2,3),
    HEAVYSLASH(5,6,5)
}

class Skills(private val type: AbilityType) {
    var speed: Int = type.speed
    var damage: Int = type.damage
    var staminaCost: Int = type.staminaCost

    //Takes an optional parameter
    fun use(enemy: Enemy? = null, player: Player? = null) {
        when (type) {
            AbilityType.SLASH -> {
                // Logic for using the slash ability
                enemy?.takeDamage(AbilityType.SLASH.damage)

            }
            AbilityType.HEAVYSLASH -> {
                //Logic for the heavy slash
                enemy?.takeDamage(AbilityType.HEAVYSLASH.damage)
                }
            }
        }
    }



/**
 * To create a new skill you can:

val slashSkill = Skills(AbilityType.SLASH)
val blockCard = Skills(AbilityType.BLOCK)

Then, to use a card's ability, you can simply call the use() method:

slashSkill.use()
blockSkill.use()
 */
