package bugbusters;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class DictionaryTrieTest {
    public static void main(String[] args) {
        Registrar reg = new Registrar("schemaBugbuster", "u111111", "p111111");
        Spellcheck spellcheck = new Spellcheck("spellcheck_dictionary.txt");
        long start = System.nanoTime();
        spellcheck.readToFile(reg.getConn());
        long stop = System.nanoTime();
        System.out.println("Time to create text file: " + TimeUnit.MILLISECONDS.convert(stop-start, TimeUnit.NANOSECONDS) + " milliseconds");

        start = System.nanoTime();
        spellcheck.referenceTrie.searchWord("Biddle");
        stop = System.nanoTime();
        System.out.println("Time to search word \"Biddle\": " + TimeUnit.MICROSECONDS.convert(stop-start, TimeUnit.NANOSECONDS) + " microseconds");


    }
    @Test
    void searchWord() {
        Registrar reg = new Registrar("schemaBugbuster", "u111111", "p111111");
        Spellcheck spellcheck = new Spellcheck("spellcheck_dictionary.txt");
        spellcheck.readToFile(reg.getConn());
        boolean isFound = spellcheck.referenceTrie.searchWord("Biddle");
        boolean notFound = spellcheck.referenceTrie.searchWord("Biddl");
        boolean found2 = spellcheck.referenceTrie.searchWord("Civ/biblical");

        assertTrue(isFound);
        assertFalse(notFound);
        assertTrue(found2);
    }
}