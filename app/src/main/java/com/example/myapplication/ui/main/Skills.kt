package com.example.myapplication.ui.main


enum class AbilityType(val speed: Int, val staminaCost: Int) {
    SLASH(2,3),
    HEAVYSLASH(6,5)
}

class Skills(private val type: AbilityType) {
    val speed: Int = type.speed

    //Takes an optional parameter
    fun use(enemy: Enemy? = null, player: Player? = null) {
        when (type) {
            AbilityType.SLASH -> {
                // Logic for using the slash ability
                enemy?.takeDamage(1)
            }
            AbilityType.HEAVYSLASH -> {
                //Logic for the heavy slash
                enemy?.takeDamage(3)
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
