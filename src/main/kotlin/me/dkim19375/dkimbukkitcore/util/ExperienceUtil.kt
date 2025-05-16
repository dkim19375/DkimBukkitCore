/*
 * MIT License
 *
 * Copyright (c) 2021 dkim19375
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.dkim19375.dkimbukkitcore.util

import kotlin.math.roundToInt
import kotlin.math.sqrt
import me.dkim19375.dkimcore.annotation.API
import org.bukkit.entity.Player

/**
 * Utility class for XP calculations
 *
 * Credit goes to
 * [https://gist.github.com/Jikoo/30ec040443a4701b8980](https://gist.github.com/Jikoo/30ec040443a4701b8980)
 * for the majority of this code
 */
@API
object ExperienceUtil {
    /**
     * Calculate a player's total experience based on level and progress to next.
     *
     * @param player the Player
     * @return the amount of experience the Player has
     */
    @API
    fun getTotalExperience(player: Player): Int =
        (getExpFromLevel(player.level) + (getExpToNext(player.level) * player.exp).roundToInt())

    /**
     * Calculate total experience based on level.
     *
     * @param level the level
     * @return the total experience calculated
     */
    @API
    fun getExpFromLevel(level: Int): Int =
        when {
            level > 30 -> (4.5 * level * level - 162.5 * level + 2220).toInt()
            level > 15 -> (2.5 * level * level - 40.5 * level + 360).toInt()
            else -> level * level + 6 * level
        }

    /**
     * Calculate level (including progress to next level) based on total experience.
     *
     * @param exp the total experience
     * @return the level calculated
     */
    @API
    fun getLevelFromExp(exp: Int): Double {
        val level = getIntLevelFromExp(exp)

        // Get remaining exp progressing towards next level. Cast to float for next bit of math.
        val remainder = exp - getExpFromLevel(level).toFloat()

        // Get level progress with float precision.
        val progress = remainder / getExpToNext(level)

        // Slap both numbers together and call it a day. While it shouldn't be possible for progress
        // to be an invalid value (value < 0 || 1 <= value)
        return level.toDouble() + progress
    }

    /**
     * Calculate level based on total experience.
     *
     * @param exp the total experience
     * @return the level calculated
     */
    @API
    fun getIntLevelFromExp(exp: Int): Int {
        if (exp > 1395) {
            return ((sqrt(72 * exp - 54215.0) + 325) / 18).toInt()
        }
        if (exp > 315) {
            return (sqrt(40 * exp - 7839.0) / 10 + 8.1).toInt()
        }
        return if (exp > 0) (sqrt(exp + 9.0) - 3).toInt() else 0
    }

    /**
     * Get the total amount of experience required to progress to the next level.
     *
     * @param level the current level
     */
    @API
    fun getExpToNext(level: Int): Int =
        when {
            level >= 30 -> level * 9 - 158
            level >= 15 -> level * 5 - 38
            else -> level * 2 + 7
        }

    /**
     * Change a Player's experience.
     *
     * This method is preferred over [Player.giveExp]. <br></br>In older versions the method does
     * not take differences in exp per level into account. This leads to overlevelling when granting
     * players large amounts of experience. <br></br>In modern versions, while differing amounts of
     * experience per level are accounted for, the approach used is loop-heavy and requires an
     * excessive number of calculations, which makes it quite slow.
     *
     * @param player the Player affected
     * @param exp the amount of experience to add or remove
     */
    @API
    fun changeExp(player: Player, exp: Int) =
        setExp(player, (getTotalExperience(player) + exp).coerceAtLeast(0))

    @API
    fun setExp(player: Player, exp: Int) {
        require(exp >= 0) { "Experience cannot be negative" }
        val levelAndExp = getLevelFromExp(exp)
        val level = levelAndExp.toInt()
        player.level = level
        player.exp = (levelAndExp - level).toFloat()
    }
}

@API fun Player.getActualTotalExp(): Int = ExperienceUtil.getTotalExperience(this)

@API fun Player.changeExp(exp: Int) = ExperienceUtil.changeExp(this, exp)

@API fun Player.setActualTotalExp(exp: Int) = ExperienceUtil.setExp(this, exp)
