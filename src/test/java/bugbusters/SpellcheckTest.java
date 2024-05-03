package bugbusters;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class SpellcheckTest {

    @Test
    void check() {
        Registrar reg = new Registrar("schemaBugbuster", "u111111", "p111111");
        Spellcheck spellcheck = new Spellcheck("spellcheck_dictionary.txt");
        spellcheck.readToFile(reg.getConn());
        try {
            long start = System.nanoTime();
            assertEquals("hutchins", spellcheck.check("htchinss"));
            long stop = System.nanoTime();
            System.out.println("Time to check 'hutchins': " + TimeUnit.MILLISECONDS.convert(stop-start, TimeUnit.NANOSECONDS) + " milliseconds");
            start = System.nanoTime();
            assertEquals("programming", spellcheck.check("programming"));
            stop = System.nanoTime();
            System.out.println("Time to check 'programming': " + TimeUnit.MILLISECONDS.convert(stop-start, TimeUnit.NANOSECONDS) + " milliseconds");
            start = System.nanoTime();
            assertEquals("biology", spellcheck.check("bology"));
            stop = System.nanoTime();
            System.out.println("Time to check 'biology': " + TimeUnit.MILLISECONDS.convert(stop-start, TimeUnit.NANOSECONDS) + " milliseconds");
            start = System.nanoTime();
            assertEquals("biddle", spellcheck.check("biddel"));
            stop = System.nanoTime();
            System.out.println("Time to check 'biddle': " + TimeUnit.MILLISECONDS.convert(stop-start, TimeUnit.NANOSECONDS) + " milliseconds");
            start = System.nanoTime();
            assertEquals("genetics", spellcheck.check("jenetiks"));
            stop = System.nanoTime();
            System.out.println("Time to check 'genetics': " + TimeUnit.MILLISECONDS.convert(stop-start, TimeUnit.NANOSECONDS) + " milliseconds");
            start = System.nanoTime();
            assertEquals("software", spellcheck.check("softwear"));
            stop = System.nanoTime();
            System.out.println("Time to check 'software': " + TimeUnit.MILLISECONDS.convert(stop-start, TimeUnit.NANOSECONDS) + " milliseconds");
            start = System.nanoTime();
            assertEquals("calculus", spellcheck.check("calculas"));
            stop = System.nanoTime();
            System.out.println("Time to check 'calculus': " + TimeUnit.MILLISECONDS.convert(stop-start, TimeUnit.NANOSECONDS) + " milliseconds");
        } catch (FileNotFoundException f) {
            System.out.println(f.getMessage());
        }
    }

    @Test
    void readToFile() {
        Registrar reg = new Registrar("schemaBugbuster", "u111111", "p111111");
        Spellcheck spellcheck = new Spellcheck("spellcheck_dictionary.txt");
        spellcheck.readToFile(reg.getConn());

    }
}