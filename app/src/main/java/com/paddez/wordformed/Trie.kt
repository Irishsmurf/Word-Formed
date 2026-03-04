package com.paddez.wordformed

import java.util.ArrayList

class Trie {
    companion object {
        const val DELIMITER = '\u0001'
    }

    private var root: Node = Node('r'.code)
    private var size = 0
    private var maxDepth = 0

    fun add(word: String): Boolean {
        if (add(root, word + DELIMITER, 0)) {
            size++
            val n = word.length
            if (n > maxDepth) maxDepth = n
            return true
        }
        return false
    }

    private fun add(root: Node, word: String, offset: Int): Boolean {
        if (offset == word.length) return false
        val c = word[offset].code

        var last: Node? = null
        var next = root.firstChild
        while (next != null) {
            if (next.value < c) {
                last = next
                next = next.nextSibling
            } else if (next.value == c) {
                return add(next, word, offset + 1)
            } else break
        }

        val node = Node(c)
        if (last == null) {
            root.firstChild = node
            node.nextSibling = next
        } else {
            last.nextSibling = node
            node.nextSibling = next
        }

        var current = node
        for (i in offset + 1 until word.length) {
            current.firstChild = Node(word[i].code)
            current = current.firstChild!!
        }
        return true
    }

    fun isEntry(word: String): Boolean {
        if (word.isEmpty()) throw IllegalArgumentException("Word can't be empty")
        return isEntry(root, word + DELIMITER, 0)
    }

    private fun isEntry(root: Node, word: String, offset: Int): Boolean {
        if (offset == word.length) return true
        val c = word[offset].code

        var next = root.firstChild
        while (next != null) {
            if (next.value < c) next = next.nextSibling
            else if (next.value == c) return isEntry(next, word, offset + 1)
            else return false
        }
        return false
    }

    fun size(): Int = size

    fun suggest(prefix: String): Array<String> {
        return suggest(root, prefix, 0)
    }

    private fun suggest(root: Node, word: String, offset: Int): Array<String> {
        if (offset == word.length) {
            val words = ArrayList<String>(size)
            val chars = CharArray(maxDepth + 1)
            for (i in 0 until offset) chars[i] = word[i]
            getAll(root, words, chars, offset)
            return words.toTypedArray()
        }
        val c = word[offset].code

        var next = root.firstChild
        while (next != null) {
            if (next.value < c) next = next.nextSibling
            else if (next.value == c) return suggest(next, word, offset + 1)
            else break
        }
        return arrayOf(word)
    }

    private fun getAll(root: Node, words: MutableList<String>, chars: CharArray, depth: Int) {
        var next = root.firstChild
        while (next != null) {
            if (next.value.toChar() == DELIMITER) {
                words.add(String(chars, 0, depth))
            } else {
                chars[depth] = next.value.toChar()
                getAll(next, words, chars, depth + 1)
            }
            next = next.nextSibling
        }
    }

    fun censor(s: String): String {
        if (size == 0) return s
        val z = s.lowercase()
        val n = z.length
        val buffer = StringBuilder(n)
        var i = 0
        while (i < n) {
            val match = longestMatch(root, z, i, 0, 0)
            if (match > 0) {
                for (j in 0 until match) {
                    buffer.append('*')
                    i++
                }
            } else {
                buffer.append(s[i++])
            }
        }
        return buffer.toString()
    }

    private fun longestMatch(root: Node, word: String, offset: Int, depth: Int, maxFound: Int): Int {
        var currentMax = maxFound
        val next = root.firstChild ?: return currentMax
        
        if (next.value.toChar() == DELIMITER) {
            currentMax = depth
        }
        
        if (offset == word.length) {
            return currentMax
        }
        
        val c = word[offset].code
        var current: Node? = next
        while (current != null) {
            if (current.value < c) {
                current = current.nextSibling
            } else if (current.value == c) {
                return longestMatch(current, word, offset + 1, depth + 1, currentMax)
            } else {
                return currentMax
            }
        }
        return currentMax
    }

    private class Node(var value: Int) {
        var firstChild: Node? = null
        var nextSibling: Node? = null
    }
}
