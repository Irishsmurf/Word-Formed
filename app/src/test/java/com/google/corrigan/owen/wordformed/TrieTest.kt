package com.google.corrigan.owen.wordformed

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TrieTest {
    private lateinit var trie: Trie

    @Before
    fun setUp() {
        trie = Trie()
        trie.add("apple")
        trie.add("apply")
        trie.add("ball")
        trie.add("bat")
    }

    @Test
    fun testIsEntry() {
        assertTrue(trie.isEntry("apple"))
        assertTrue(trie.isEntry("apply"))
        assertTrue(trie.isEntry("ball"))
        assertTrue(trie.isEntry("bat"))
        
        assertFalse(trie.isEntry("app")) // app is a prefix but not an entry
        assertFalse(trie.isEntry("cat"))
    }

    @Test
    fun testSuggest() {
        val appSuggestions = trie.suggest("app")
        assertEquals(2, appSuggestions.size)
        assertTrue(appSuggestions.contains("apple"))
        assertTrue(appSuggestions.contains("apply"))

        val bSuggestions = trie.suggest("ba")
        assertEquals(2, bSuggestions.size)
        assertTrue(bSuggestions.contains("ball"))
        assertTrue(bSuggestions.contains("bat"))
        
        val none = trie.suggest("xyz")
        assertEquals(1, none.size)
        assertEquals("xyz", none[0])
    }

    @Test
    fun testCensor() {
        val input = "I have an apple and a bat"
        val expected = "I have an ***** and a ***"
        assertEquals(expected, trie.censor(input))
    }

    @Test
    fun testSize() {
        assertEquals(4, trie.size())
    }
}
