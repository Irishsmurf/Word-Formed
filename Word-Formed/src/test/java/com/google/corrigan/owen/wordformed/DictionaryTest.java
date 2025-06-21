package com.google.corrigan.owen.wordformed;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import android.content.Context;
import android.content.res.Resources;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class DictionaryTest {

    @Mock
    private Context mockContext;

    @Mock
    private Resources mockResources;

    private Dictionary dictionary;

    // This is the content of our test word_list.txt
    // TEST, HELLO, WORLD, CAR, CART, CARD, A, APPLE, SPACES, ANOTHER, LOWERCASE
    // Filtered out: ÆTHER (if not A-Z), 123NUMBER, WORD!, EMPTYLINE
    private final String testWordListData =
            "TEST\n" +
            "HELLO\n" +
            "WORLD\n" +
            "CAR\n" +
            "CART\n" +
            "CARD\n" +
            "A\n" +
            "APPLE\n" +
            "  SPACES  \n" + // Will be trimmed to SPACES
            "LoweRCASe\n" +  // Will be normalized to LOWERCASE
            "ANOTHER\n" +
            "ÆTHER\n" +      // May be filtered by [A-Z]+
            "123NUMBER\n" +  // Filtered
            "WORD!\n" +      // Filtered
            "\n";            // Empty line, filtered

    private final String emptyWordListData = "";
    private final String malformedWordListData = "word1\nword123\nword-symbol";


    @Before
    public void setUp() throws IOException {
        // Reset static state in Dictionary before each test
        // This is important because Trie is static in Dictionary
        Dictionary.resetAndLoad(null); // Pass null to reset, actual loading needs context

        when(mockContext.getResources()).thenReturn(mockResources);
    }

    private void setupMockStream(String data) throws IOException {
        InputStream mockInputStream = new ByteArrayInputStream(data.getBytes());
        // R.raw.word_list is an int, so we use any int here.
        // The actual value doesn't matter as getResources() is mocked.
        when(mockResources.openRawResource(R.raw.word_list)).thenReturn(mockInputStream);
    }

    @Test
    public void testDictionaryInitialization_LoadsWordsCorrectly() throws IOException {
        setupMockStream(testWordListData);
        dictionary = new Dictionary(mockContext); // Initialize

        assertTrue(Dictionary.isWord("TEST"));
        assertTrue(Dictionary.isWord("HELLO"));
        assertTrue(Dictionary.isWord("WORLD"));
        assertTrue(Dictionary.isWord("CAR"));
        assertTrue(Dictionary.isWord("CART"));
        assertTrue(Dictionary.isWord("CARD"));
        assertTrue(Dictionary.isWord("A"));
        assertTrue(Dictionary.isWord("APPLE"));
        assertTrue(Dictionary.isWord("SPACES")); // From "  SPACES  "
        assertTrue(Dictionary.isWord("LOWERCASE")); // From "LoweRCASe"
        assertTrue(Dictionary.isWord("ANOTHER"));

        assertFalse(Dictionary.isWord("ÆTHER")); // Filtered by [A-Z]+
        assertFalse(Dictionary.isWord("123NUMBER")); // Filtered
        assertFalse(Dictionary.isWord("WORD!"));    // Filtered
        assertFalse(Dictionary.isWord("")); // Empty string
        assertFalse(Dictionary.isWord("NOTINLIST"));

        // Expected count: TEST, HELLO, WORLD, CAR, CART, CARD, A, APPLE, SPACES, LOWERCASE, ANOTHER = 11 words
        assertEquals(11, Dictionary.getWordCount());
    }

    @Test
    public void testIsWord_CaseInsensitivity() throws IOException {
        setupMockStream("TestWord\n");
        dictionary = new Dictionary(mockContext);

        assertTrue(Dictionary.isWord("TESTWORD"));
        assertTrue(Dictionary.isWord("testword"));
        assertTrue(Dictionary.isWord("TestWord"));
        assertTrue(Dictionary.isWord("tEsTwOrD"));
    }

    @Test
    public void testIsWord_WordNotInDictionary() throws IOException {
        setupMockStream("EXISTING\n");
        dictionary = new Dictionary(mockContext);
        assertFalse(Dictionary.isWord("NONEXISTENT"));
    }

    @Test
    public void testIsWord_EmptyAndNull() throws IOException {
        setupMockStream("ANYTHING\n");
        dictionary = new Dictionary(mockContext);
        assertFalse(Dictionary.isWord(""));
        assertFalse(Dictionary.isWord(null));
    }

    @Test
    public void testDictionary_EmptyWordListFile() throws IOException {
        setupMockStream(emptyWordListData);
        dictionary = new Dictionary(mockContext);

        assertEquals(0, Dictionary.getWordCount());
        assertFalse(Dictionary.isWord("ANYWORD"));
    }

    @Test
    public void testDictionary_MalformedWordListFile() throws IOException {
        setupMockStream(malformedWordListData); // "word1\nword123\nword-symbol"
        dictionary = new Dictionary(mockContext);
        // Only "word1" should be loaded if it matches [A-Z]+, assuming it's WORDONE
        // Current Dictionary logic converts to uppercase. "word1" -> "WORD1"
        // "word123" -> "WORD123" (filtered out)
        // "word-symbol" -> "WORD-SYMBOL" (filtered out)
        // So, if "WORDONE" was intended, it should be in the file as such.
        // The current test file has "word1", which becomes "WORD1" and is filtered.
        // Let's adjust test file for clarity or ensure Dictionary handles this as expected.
        // For now, assume "word1" becomes "WORD1" and is valid.
        // Re-evaluating Dictionary's loadWords: `word.matches("^[A-Z]+$")`
        // "WORD1" is valid. "WORD123" is not. "WORD-SYMBOL" is not.

        // Adjusting test data for clarity:
        setupMockStream("VALIDWORD\nword123\nword-symbol\n");
        dictionary = new Dictionary(mockContext);

        assertTrue(Dictionary.isWord("VALIDWORD"));
        assertFalse(Dictionary.isWord("WORD123"));
        assertFalse(Dictionary.isWord("WORD-SYMBOL"));
        assertEquals(1, Dictionary.getWordCount());
    }


    @Test
    public void testDictionary_InitializationWithNullContext() {
        // Dictionary constructor has a null check for context
        // We call resetAndLoad(null) in setUp to clear static state.
        // Then, new Dictionary(null) should not throw, but log an error.
        // isWord and getWordCount should reflect uninitialized state.
        Dictionary.resetAndLoad(null); // Ensure it's reset
        dictionary = new Dictionary(null); // Pass null context

        assertFalse(Dictionary.isWord("ANYTHING")); // Should be safe, return false
        assertEquals(0, Dictionary.getWordCount()); // Should be safe, return 0
        // To verify logs, we'd need a logging framework test rule.
    }

    @Test
    public void testDictionary_IOExceptionDuringLoad() throws IOException {
        // Mock resources to throw IOException
        when(mockResources.openRawResource(R.raw.word_list)).thenThrow(new IOException("Test IO Exception"));
        when(mockContext.getResources()).thenReturn(mockResources); // Ensure this mock is used

        Dictionary.resetAndLoad(null); // Reset static state
        dictionary = new Dictionary(mockContext); // Initialize, this should catch IOException

        assertFalse(Dictionary.isWord("ANYTHING")); // Dictionary should be uninitialized or empty
        assertEquals(0, Dictionary.getWordCount());
        // Logs should indicate an error.
    }
}
