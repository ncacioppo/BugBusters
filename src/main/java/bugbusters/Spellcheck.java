package bugbusters;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Spellcheck {
    private final File DICTIONARY;
    private final int NUM_SUGGESTIONS = 5;

    public Spellcheck(String filepath) {
        DICTIONARY = new File(filepath);
    }

    /**
     * Loops through a dictionary file for each word in the query and finds the top NUM_SUGGESTIONS most similar words.
     * If the word is spelled correctly (i.e. already in the dictionary), we skip it as soon as we figure that out.
     * @param query the words to be checked
     * @return an ArrayList containing String arrays, each containing the top NUM_SUGGESTIONS suggestions for a word
     */
    public String check(String query) throws FileNotFoundException {
        // change to take one word at a time - we loop through the words array in keywordSearch and replace the word if necessary
        ArrayList<String[]> results = new ArrayList<>();
        String result = "";
        query = query.toLowerCase();
        boolean shouldAdd = true;
        String[] matches = new String[NUM_SUGGESTIONS];
        int[] levDistances = new int[NUM_SUGGESTIONS];
        Scanner dict = new Scanner(DICTIONARY);
        while (dict.hasNext()) {
            String candidate = dict.next().toLowerCase();
            if (candidate.equals(query)) {
                shouldAdd = false;
                break;
            }
            int lev = TrueDamLev(query, candidate);
            int maxIndex = getMax(levDistances);
            // if the arrays aren't full yet, keep filling
            if (levDistances[levDistances.length-1] == 0) {
                for (int i = 0; i < levDistances.length; i++) {
                    if (levDistances[i] == 0) {
                        levDistances[i] = lev;
                        matches[i] = candidate;
                        break;
                    }
                }
            } else {
                // otherwise move on and see if we replace something that's already there
                if (lev < levDistances[maxIndex]) {
                    levDistances[maxIndex] = lev;
                    matches[maxIndex] = candidate;
                }
            }
            // insertion-sort the arrays so the best option is first
            sort(levDistances, matches);
        }
        // if the word was misspelled, return our top option
        if (shouldAdd) {
            if (matches[0] != null) {
                return matches[0];
            } else {
                for (int i = 0; i < matches.length; i++) {
                    if (matches[i] != null) {
                        return matches[i];
                    }
                }
            }
        // otherwise return the empty string
        }
        return "";
    }

    // get the index of the biggest thing in arr
    // (which corresponds to the least similar string we're still saving)
    private int getMax(int[] arr) {
        int max = arr[0];
        int index = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
                index = i;
            }
        }
        return index;
    }

    // because the arrays being sorted are so short, insertion sort is fine here
    // anything else would be overkill
    private void sort(int[] levDistances, String[] matches) {
        int n = levDistances.length;

        for (int i = 1; i < n; i++) {
            int key = levDistances[i];
            String matchKey = matches[i];
            int j = i-1;

            while (j >= 0 && levDistances[j] > key) {
                levDistances[j+1] = levDistances[j];
                matches[j+1] = matches[j];
                j--;
            }
            levDistances[j+1] = key;
            matches[j+1] = matchKey;
        }
    }



    // calculates the true Damerau-Levenshtein distance between input and comparison
    // this is an improved measure over simple Levenshtein distance, because it accounts for transposition as a single action
    // i.e., "negineering" to "engineering" is a 1-cost move (single transposition) over a 2-cost move (2 replacements)
    // this algorithm is O(mn) in the worst case, where m and n are the lengths of the strings
    private int TrueDamLev(String input, String comparison) {
        char[] s = stringToArr(input);
        char[] t = stringToArr(comparison);
        int[] da = new int[27];


        int[][] d = new int[s.length+1][t.length+1];

        int maxdist = input.length()+comparison.length();
        d[0][0] = maxdist;

        for (int i = 2; i <= input.length()+1; i++) {
            d[i][0] = maxdist;
            d[i][1] = i;
        }

        for (int j = 2; j <= comparison.length()+1; j++) {
            d[0][j] = maxdist;
            d[1][j] = j;
        }

        for (int i = 1; i <= input.length(); i++) {
            int db = 1;
            for (int j = 1; j <= comparison.length(); j++) {
                int k = da[t[j]-96]+1;
                int l = db;
                int substitutionCost;
                if (s[i] == t[j]) {
                    substitutionCost = 0;
                    db = j;
                } else {
                    substitutionCost = 1;
                }
                d[i][j] = Math.min(d[i-1][j-1]+substitutionCost,
                        Math.min(Math.min(d[i][j-1]+1,
                                        d[i-1][j]+1),
                                d[k-1][l-1]+(i-k-1)+1+(j-l-1)));
            }
            da[s[i]-96] = i;
        }

        return d[input.length()][comparison.length()]+1;
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
