package bugbusters;

import org.jooq.impl.DSL;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

class TrieNode {
    HashMap<Character, TrieNode> children;
    boolean endOfWord;
    char value;

    public TrieNode(char value) {
        children = new HashMap<>();
        endOfWord = false;
        this.value = value;
    }
}

class DictionaryTrie {
    TrieNode root;

    public DictionaryTrie() { root = new TrieNode(' ');}

    public void insertWord(String word) {
        word = word.toLowerCase();
        TrieNode currentNode = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            currentNode.children.computeIfAbsent(c, k -> new TrieNode(c));
            currentNode = currentNode.children.get(c);
        }
        currentNode.endOfWord = true;
    }

    public boolean searchWord(String word) {
        word = word.toLowerCase();
        TrieNode currentNode = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (currentNode.children.get(c) == null) {
                return false;
            }
            currentNode = currentNode.children.get(c);
        }
        return currentNode.endOfWord;
    }
}


public class Spellcheck {
    private File DICTIONARY;

    // only public for testing purposes
    public DictionaryTrie referenceTrie;

    public Spellcheck(String filepath) {
        referenceTrie = new DictionaryTrie();
        DICTIONARY = new File(filepath);
    }


    /**
     * Loops through a dictionary file for each word in the query and finds the top NUM_SUGGESTIONS most similar words.
     * If the word is spelled correctly (i.e. already in the dictionary), we skip it as soon as we figure that out.
     * @param query the words to be checked
     * @return an ArrayList containing String arrays, each containing the top NUM_SUGGESTIONS suggestions for a word
     */
    public String check(String query) throws FileNotFoundException {
        query = query.toLowerCase();
        // before we do anything else, search the trie to see if the word is spelled correctly
        if (referenceTrie.searchWord(query)) {
            return "";
        }
        // else it's misspelled, and we need to find a good replacement
        boolean shouldAdd = true;
        int MEM_SIZE = 5;

        // matches maps a string to two integer values:
        // [0] - the number of time we've seen this string in the file (frequency)
        // [1] - the DamLev distance from this string to our query
        HashMap<String, int[]> wordData = new HashMap<>();
        // we also keep an arraylist of strings, so we can eventually sort it and return the best option rather than iterating through the map to find it
        ArrayList<String> words = new ArrayList<>();

        Scanner dict = new Scanner(DICTIONARY);

        while (dict.hasNext()) {

            String candidate = dict.next();
            int lev = DamerauLevenshtein(query, candidate);

            // we don't need to consider strings that are more than 3 edits from our query - they're extremely unlikely to be correct
            // unless the string is really short (i.e. dynamic search updates, in which case we have to consider more options)
            if (lev <= 3 || query.length() < 3) {
                // if we already have this word, increment its frequency
                // otherwise, add it to the map and save its dam-lev distance
                if (wordData.containsKey(candidate)) {
                    int[] tmp = wordData.get(candidate);
                    tmp[0]++;
                    wordData.put(candidate, tmp);
                } else {
                    int[] tmp = new int[]{1, lev};
                    wordData.put(candidate, tmp);
                    words.add(candidate);
                }
            }
        }

        // now we want to find the element in our hashmap with the lowest Dam-Lev distance
        // in the event of a tie, we return the element with the highest frequency
        // to do this, we use mergeSort to sort our arraylist of strings and use the map for fast lookup of relevant values
        if (words.size() > 1) {
            mergeSort(words, wordData);
            return words.getFirst();
        } else if (words.size() == 1){
            return words.getFirst();
        }
        return "";
    }

    // Because the number of words could potentially be very large, we will use mergesort to sort the final list of strings
    private void mergeSort(ArrayList<String> words, HashMap<String, int[]> wordMap) {
        sort(words, wordMap, 0, words.size()-1);
    }
    private void sort(ArrayList<String> words, HashMap<String, int[]> wordMap, int l, int r) {
        if (l < r) {
            // Find the middle of the array
            int m = (l + r)/2;
            // Sort both halves
            sort(words, wordMap, l, m);
            sort(words, wordMap, m+1, r);

            // Cast the magic merging spell
            merge(words, wordMap, l, m, r);
        }
    }
    private void merge(ArrayList<String> words, HashMap<String, int[]> wordMap, int l, int m, int r) {
        int n1 = m-l+1;
        int n2 = r-m;

        ArrayList<String> left = new ArrayList<>();
        ArrayList<String> right = new ArrayList<>();

        for (int i = 0; i < n1; i++) {
            left.add(words.get(l+i));
        }
        for (int i = 0; i < n2; i++) {
            right.add(words.get(i+m+1));
        }

        // Merge the temp arraylists
        int i = 0, j = 0;
        int k = l;
        while (i < n1 && j < n2) {
            int[] wordA = wordMap.get(left.get(i));
            int[] wordB = wordMap.get(right.get(j));
            if (wordA[1] < wordB[1]) {
                words.set(k, left.get(i));
                i++;
            } else if (wordA[1] == wordB[1]) {
                // if the two words have the same lev distance, we compare their frequency in the text file
                // and choose the one that appears more
                int wordAFreq = wordA[0];
                int wordBFreq = wordB[0];
                if (wordAFreq > wordBFreq) {
                    words.set(k, left.get(i));
                    i++;
                } else {
                    words.set(k, right.get(j));
                    j++;
                }
            } else {
                words.set(k, right.get(j));
                j++;
            }
            k++;
        }

        // Copy remaining elements
        while (i < n1) {
            words.set(k, left.get(i));
            i++;
            k++;
        }
        while (j < n2) {
            words.set(k, right.get(j));
            j++;
            k++;
        }
    }

    /**
     * Implements a variation of the Wagner-Fischer algorithm to calculate the Damerau-Levenshtein distance between input and comparison.
     * This is an improvement over simple Levenshtein distance because it accounts for transposition as a single action.
     * i.e., "negineering" to "engineering" is a 1-cost move over a 2-cost move (one transposition vs two replacements)
     * This algorithm is O(mn), where m and n are the lengths of the strings.
     * @param input string A
     * @param comparison string B
     * @return the Damerau-Levenshtein distance between the parameters
     */
    private int DamerauLevenshtein(String input, String comparison) {
        char[] s = stringToArr(input);
        char[] t = stringToArr(comparison);

        int[][] d = new int[s.length][t.length];

        for (int i = 1; i <= input.length(); i++) {
            d[i][0] = i;
        }

        for (int j = 1; j <= comparison.length(); j++) {
            d[0][j] = j;
        }

        for (int i = 1; i <= input.length(); i++) {
            for (int j = 1; j <= comparison.length(); j++) {
                int substitutionCost = 1;
                if (s[i] == t[j]) {
                    substitutionCost = 0;
                }
                d[i][j] = Math.min(d[i-1][j]+1, Math.min(d[i][j-1]+1, d[i-1][j-1]+substitutionCost));
                if (i > 1 && j > 1 && s[i] == t[j-1] && s[i-1] == t[j]) {
                    d[i][j] = Math.min(d[i][j], d[i-2][j-2]+1);
                }
            }
        }

        return d[input.length()][comparison.length()];
    }

    /**
     * Reads relevant course information from the database and saves it to a text file.
     * This method also saves the words in a trie for quick verification of correct spelling.
     *
     * @param conn a database connection
     */
    public void readToFile(Connection conn) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT Dept, CourseName, LNameInstructor, FNameInstructor from course");
            ResultSet rs = ps.executeQuery();

            JSONObject result = new JSONObject(DSL.using(conn).fetch(rs).formatJSON());
            String resultString = result.toString();
            String[] data = resultString.split("[{}\":,\\[\\]\\s]+");
            ArrayList<String> cleanData = new ArrayList<>(Arrays.asList(data));

            cleanData.removeIf(String::isEmpty);
            cleanData.removeAll(Collections.singleton("I"));
            cleanData.removeAll(Collections.singleton("II"));
            cleanData.removeAll(Collections.singleton("III"));
            cleanData.removeAll(Collections.singleton("IV"));
            cleanData.removeAll(Collections.singleton("-"));
            cleanData.removeAll(Collections.singleton("&"));

            PrintWriter pw = new PrintWriter(DICTIONARY);
            // write all our strings to the text file and to our reference trie
            for (String s : cleanData) {
                pw.println(s.toLowerCase());
                referenceTrie.insertWord(s);
            }
            pw.close();


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // converts String s into a 1-indexed array of chars for the Levenshtein algorithm
    private char[] stringToArr(String s) {
        char[] string = new char[s.length()+1];
        for (int i = 1; i < string.length; i++) {
            string[i] = s.charAt(i-1);
        }
        return string;
    }
}
