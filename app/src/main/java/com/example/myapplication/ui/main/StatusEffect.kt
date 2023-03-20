package com.example.myapplication.ui.main

//Interface all status effects must implement all of these functions
interface StatusEffect {

    //return boolean, false indicates status not applied.
    //if true, then the status effect should be applied
    fun condition_check();

//returns who should be targeted by this status effect
    fun entity_to_target();

    //this defines what happens when a status effect is being applied
    //apply will directly visit the player object, and update his state accordingly.
    fun apply(LivingEntity entity);

    /*
    {
        entity.health -= poision_amount;
        entity.speed -= slug_amount;

        if(entity instanceof Player)
        {
            Player plyr = (Player)entity; //downcast

            plyr.skill_tree.remove(ability_one); //suppress ability 1
        }

        else if(entity instanceof Enemy)
        {

        }
    }*/

    //this function describes what the status effect does
    //can be used to inform the user
    fun String desrcipt()
}

class Poison implements StateEffect
{
    double factors;
    double hit_rate;

    Poison(double hr)
    {
        hit_rate = hr;
    }

    fun condition_check()
    {
        return random() < hit_rate;
    }

    fun apply(LivingEntity entity)
    {
        level * factor;

        if(enemy.type is water)
        {

        }
    }

    fun descript() {

    }
}

class Juggernaut implements StatusEffect
{

}

class Burn implements StatusEffect
{

}

// This is how you implement all status effects.
class Battle
{

    for(Skill s : player.skill_tree)
    {
        StatusEffect status_effect = s.getStatusEffect();

        if(status_effect.condition_check())
        {
            if(status_effect.entity_to_target == yourself) {
                status_effect.apply(player);
            }
            else {
                status_effect.apply(enemy);
            }
        }
    }
}
