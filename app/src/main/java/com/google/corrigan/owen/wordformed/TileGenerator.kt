package com.google.corrigan.owen.wordformed

import java.util.Random

class TileGenerator(seed: Long) {
    
    companion object {
        private val tiles = intArrayOf(19, 12, 11, 11, 11, 12, 13, 5, 9, 4, 6, 3, 1, 5, 4, 4, 4, 5, 1, 2, 3, 1, 1, 1, 1, 1)
        private val dist = charArrayOf('E', 'A', 'I', 'O', 'N', 'R', 'T', 'L', 'S', 'U', 'D', 'G', 'B', 'C', 'M', 'P', 'F', 'H', 'V', 'W', 'Y', 'K', 'J', 'X', 'Q', 'Z')
        private val values = intArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 8, 8, 10, 10)
        private val rand = Random(System.currentTimeMillis())
        private val bag = ArrayList<Char>()

        @JvmStatic
        fun nextTile(): Char {
            if (bag.isEmpty()) {
                // Fallback or re-init if bag is empty
                return dist[rand.nextInt(dist.size)]
            }
            val ran = rand.nextInt(bag.size)
            return bag[ran]
        }

        @JvmStatic
        fun getValue(letter: Char): Int {
            for (i in dist.indices) {
                if (dist[i] == letter.uppercaseChar()) {
                    return values[i]
                }
            }
            return -1
        }
        
        fun initBag() {
            if (bag.isNotEmpty()) return
            for (j in dist.indices) {
                for (i in 0 until tiles[j]) {
                    bag.add(dist[j])
                }
            }
        }
    }

    init {
        initBag()
    }
}
