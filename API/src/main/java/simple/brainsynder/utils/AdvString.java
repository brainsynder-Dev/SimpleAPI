package simple.brainsynder.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class AdvString {
    /**
     * Retrieves the text that comes after the first instance of 'needle'
     *
     * @param needle
     * @param haystack -- the text that is being scanned
     * @return -- Returns the text that is after 'needle'
     */
    public static String after (String needle, String haystack) {
        return haystack.substring((haystack.indexOf(needle)+needle.length()));
    }

    /**
     * Retrieves the text that comes after the last instance of 'needle'
     *
     * @param needle
     * @param haystack
     * @return -- Returns the text that is after the last 'needle'
     */
    public static String afterLast (String needle, String haystack) {
        return haystack.substring(reversePos(needle, haystack)+needle.length());
    }

    /**
     * Retrieves the text that comes before the first instance of 'needle'
     *
     * @param needle
     * @param haystack -- the text that is being scanned
     * @return -- Returns the text that is before 'needle'
     */
    public static String before (String needle, String haystack) {
        return haystack.substring(0, haystack.indexOf(needle));
    }

    /**
     * Retrieves the text that comes after the last instance of 'needle'
     *
     * @param needle
     * @param haystack -- the text that is being scanned
     * @return -- Returns the text that is before the last instance of 'needle'
     */
    public static String beforeLast (String needle, String haystack) {
        return haystack.substring(0, reversePos(needle, haystack));
    }

    /**
     * Retrieves the text that is between the first instance of 'first' and 'last'
     *
     * @param first
     * @param last
     * @param haystack -- the text that is being scanned
     * @return -- Returns the text that is between the instance of 'first' and 'last'
     */
    public static String between (String first, String last, String haystack) {
        return before(last, after(first, haystack));
    }

    /**
     * Retrieves the text that is between the last instance of 'first' and 'last'
     *
     * @param first
     * @param last
     * @param haystack -- the text that is being scanned
     * @return -- Returns the text that is between the last instance of 'first' and 'last'
     */
    public static String betweenLast (String first, String last, String haystack) {
        return afterLast(first, beforeLast(last, haystack));
    }


    public static int reversePos(String needle, String haystack) {
        int pos = reverse(haystack).indexOf(reverse(needle));
        return haystack.length() - pos - needle.length();
    }

    /**
     * Reverses the 'input' text
     *
     * @param input
     * @return -- Returns the 'input' in reverse
     */
    public static String reverse (String input) {
        char[] chars = input.toCharArray();
        List<Character> characters = new ArrayList<>();

        for (char c: chars)
            characters.add(c);

        Collections.reverse(characters);
        ListIterator iterator = characters.listIterator();
        StringBuilder builder = new StringBuilder();

        while (iterator.hasNext())
            builder.append(iterator.next());
        return builder.toString();
    }

    /**
     * Scrambles the 'input' text in random configurations
     *
     * @param input
     * @return -- Returns the 'input' randomly scambled
     */
    public static String scramble(String input) {
        StringBuilder out = new StringBuilder();
        for (String part : input.split(" ")) {
            List<Character> characters = new ArrayList<>();
            for (char c : part.toCharArray()) {
                characters.add(c);
            }
            StringBuilder output = new StringBuilder(part.length());
            while (characters.size() != 0) {
                int rndm = (int) (Math.random() * characters.size());
                output.append(characters.remove(rndm));
            }
            out.append(output.toString()).append(' ');
        }
        return out.toString().trim();
    }
}
