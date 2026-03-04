package com.paddez.wordformed

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TileGeneratorTest {

    @Before
    fun setUp() {
        TileGenerator.initBag()
    }

    @Test
    fun testGetValue() {
        assertEquals(1, TileGenerator.getValue('E'))
        assertEquals(1, TileGenerator.getValue('A'))
        assertEquals(10, TileGenerator.getValue('Z'))
        assertEquals(10, TileGenerator.getValue('Q'))
        assertEquals(8, TileGenerator.getValue('J'))
        assertEquals(2, TileGenerator.getValue('D'))
        assertEquals(-1, TileGenerator.getValue(' '))
    }

    @Test
    fun testNextTile() {
        val tile = TileGenerator.nextTile()
        assertTrue(tile in 'A'..'Z')
    }

    @Test
    fun testCaseInsensitiveValue() {
        assertEquals(1, TileGenerator.getValue('e'))
        assertEquals(10, TileGenerator.getValue('z'))
    }
}
