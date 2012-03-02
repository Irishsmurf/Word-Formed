public class Trie
{
    /**
     * The delimiter used in this word to tell where words end. Without a proper delimiter either A.
     * a lookup for 'win' would return false if the list also contained 'windows', or B. a lookup
     * for 'mag' would return true if the only word in the list was 'magnolia'
     *
     * The delimiter should never occur in a word added to the trie.
     */
    public final static char DELIMITER = '\u0001';

    /**
     * Creates a new Trie.
     */
    public Trie()
    {
        root = new Node('r');
        size = 0;
    }

    /**
     * Adds a word to the list.
     * @param word The word to add.
     * @return True if the word wasn't in the list yet
     */
    public boolean add(String word)
    {
        if (add(root, w + DELIMITER, 0))
        {
            size++;
            int n = word.length();
            if (n > maxDepth) maxDepth = n;
            return true;
        }
        return false;
    }

    /*
     * Does the real work of adding a word to the trie
     */
    private boolean add(Node root, String word, int offset)
    {
        if (offset == word.length()) return false;
        int c = word.charAt(offset);

        // Search for node to add to
        Node last = null, next = root.firstChild;
        while (next != null)
        {
            if (next.value < c)
            {
                // Not found yet, continue searching
                last = next;
                next = next.nextSibling;
            }
            else if (next.value == c)
            {
                // Match found, add remaining word to this node
                return add(next, word, offset + 1);
            }
            // Because of the ordering of the list getting here means we won't
            // find a match
            else break;
        }

        // No match found, create a new node and insert
        Node node = new Node(c);
        if (last == null)
        {
            // Insert node at the beginning of the list (Works for next == null
            // too)
            root.firstChild = node;
            node.nextSibling = next;
        }
        else
        {
            // Insert between last and next
            last.nextSibling = node;
            node.nextSibling = next;
        }

        // Add remaining letters
        for (int i = offset + 1; i < word.length(); i++)
        {
            node.firstChild = new Node(word.charAt(i));
            node = node.firstChild;
        }
        return true;
    }

    /**
     * Searches for a word in the list.
     *
     * @param word The word to search for.
     * @return True if the word was found.
     */
    public boolean isEntry(String word)
    {
        if (word.length() == 0)
            throw new IllegalArgumentException("Word can't be empty");
        return isEntry(root, w + DELIMITER, 0);
    }

    /*
     * Does the real work of determining if a word is in the list
     */
    private boolean isEntry(Node root, String word, int offset)
    {
        if (offset == word.length()) return true;
        int c = word.charAt(offset);

        // Search for node to add to
        Node next = root.firstChild;
        while (next != null)
        {
            if (next.value < c) next = next.nextSibling;
            else if (next.value == c) return isEntry(next, word, offset + 1);
            else return false;
        }
        return false;
    }

    /**
     * Returns the size of this list;
     */
    public int size()
    {
        return size;
    }

    /**
     * Returns all words in this list starting with the given prefix
     *
     * @param prefix The prefix to search for.
     * @return All words in this list starting with the given prefix, or if no such words are found,
     *         an array containing only the suggested prefix.
     */
    public String[] suggest(String prefix)
    {
        return suggest(root, prefix, 0);
    }

    /*
     * Recursive function for finding all words starting with the given prefix
     */
    private String[] suggest(Node root, String word, int offset)
    {
        if (offset == word.length())
        {
            ArrayList<String> words = new ArrayList<String>(size);
            char[] chars = new char[maxDepth];
            for (int i = 0; i < offset; i++)
                chars[i] = word.charAt(i);
            getAll(root, words, chars, offset);
            return words.toArray(new String[words.size()]);
        }
        int c = word.charAt(offset);

        // Search for node to add to
        Node next = root.firstChild;
        while (next != null)
        {
            if (next.value < c) next = next.nextSibling;
            else if (next.value == c) return suggest(next, word, offset + 1);
            else break;
        }
        return new String[] { word };
    }

    /**
     * Searches a string for words present in the trie and replaces them with stars (asterixes).
     * @param z The string to censor
     */
    public String censor(String s)
    {
        if (size == 0) return s;       
        String z = s.toLowerCase();    
        int n = z.length();
        StringBuilder buffer = new StringBuilder(n);
        int match;
        char star = '*';
        for (int i = 0; i < n;)
        {
            match = longestMatch(root, z, i, 0, 0);
            if (match > 0)
            {
                for (int j = 0; j < match; j++)
                {
                    buffer.append(star);
                    i++;
                }
            }
            else
            {
                buffer.append(s.charAt(i++));
            }
        }
        return buffer.toString();
    }

    /*
     * Finds the longest matching word in the trie that starts at the given offset...
     */
    private int longestMatch(Node root, String word, int offset, int depth, int maxFound)
    {
        // Uses delimiter = first in the list!
        Node next = root.firstChild;
        if (next.value == DELIMITER) maxFound = depth;
        if (offset == word.length()) return maxFound;
        int c = word.charAt(offset);

        while (next != null)
        {
            if (next.value < c) next = next.nextSibling;
            else if (next.value == c) return longestMatch(next, word,
                offset + 1, depth + 1, maxFound);
            else return maxFound;
        }
        return maxFound;
    }

    /*
     * Represents a node in the trie. Because a node's children are stored in a linked list this
     * data structure takes the odd structure of node with a firstChild and a nextSibling.
     */
    private class Node
    {
        public int value;
        public Node firstChild;
        public Node nextSibling;

        public Node(int value)
        {
            this.value = value;
            firstChild = null;
            nextSibling = null;
        }
    }


    private Node root;
    private int size;
    private int maxDepth; // Not exact, but bounding for the maximum
}
