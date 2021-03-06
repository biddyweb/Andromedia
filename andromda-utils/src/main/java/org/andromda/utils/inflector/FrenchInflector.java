package org.andromda.utils.inflector;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Language utility for transforming French words
 *
 * @author Cédric Vidal
 */
public class FrenchInflector
{
    private static final List<Inflection> plurals = new ArrayList<Inflection>();

    private static final List<Pattern> uncountables = new ArrayList<Pattern>();

    /**
     *
     */
    static class Inflection
    {
        private final Pattern pattern;

        private final String replace;

        /**
         * @param pattern
         * @param replace
         */
        public Inflection(Pattern pattern, String replace)
        {
            super();
            this.pattern = pattern;
            this.replace = replace;
        }

        /**
         * @param regexp
         * @param replace
         */
        public Inflection(String regexp, String replace)
        {
            super();
            this.pattern = Pattern.compile(regexp);
            this.replace = replace;
        }

        /**
         * @return pattern
         */
        public Pattern getPattern()
        {
            return pattern;
        }

        /**
         * @return replace
         */
        public String getReplace()
        {
            return replace;
        }
    }

    static
    {
        /*
         * NON COMPOSED WORDS
         */

        uncountable(endsWith("(s|x|z)"));

        /*
         * -al words such as cheval -> chevaux (horse)
         */
        takeAnS(new String[]{"bal", "carnaval", "c?r?monial", "chacal",
            "choral", "festival", "nopal", "pal", "r?cital", "r?gal",
            "santal"});
        plural(endsWith("(al)"), "$1aux");

        /*
         * -eau words such as rideau -> rideaux (curtain) -eu words such as ?meu ->
         * ?meux (the bird)
         */
        takeAnS(new String[]{"landau", "sarrau", "bleu", "?meu", "emposieu",
            "enfeu", "feu" /* adj */, "lieu" /* le poisson */, "pneu",
            "richelieu", "schleu"});
        plural(endsWith("(au|eu|eau)"), "$1$2x");

        /*
         * -ou words take an s except the following exceptions
         */
        takeAnX(new String[]{"bijou", "caillou", "chou", "genou", "hibou",
            "joujou", "pou"});

        /*
         * -ail words take an S suche as portail -> portails except the
         * following exceptions
         */
        ailIrregulars(new String[]{"b", "cor", "?m", "soupir", "trav",
            "vant", "vitr"});

        /*
         * Exceptions that doesn't fall in any category
         */
        plural(endsWith("a?eul"), "$1a?eux");
        plural(endsWith("ciel"), "$1cieux");
        plural(endsWith("oeil"), "$1yeux");

        /*
         * COMPOSED WORDS Remains to be done. Requires type information only
         * available from a dictionary
         */
    }

    /**
     * Add a pattern that if matched return the original word
     *
     * @param pattern
     */
    private static void uncountable(String pattern)
    {
        uncountables.add(Pattern.compile(pattern));
    }

    /**
     * -ail words that don't follow the rule
     *
     * @param strings
     */
    private static void ailIrregulars(String[] strings)
    {
        for (String string : strings)
        {
            plural("(\\S*)" + string + "ail$", "$1" + string + "aux");
        }
    }

    /**
     * Words that take an S
     *
     * @param patterns
     */
    private static void takeAnS(String[] patterns)
    {
        for (String string : patterns)
        {
            plural(endsWith(string), "$1" + string + 's');
        }
    }

    /**
     * Words that take an X
     *
     * @param patterns
     */
    private static void takeAnX(String[] patterns)
    {
        for (String string : patterns)
        {
            plural(endsWith(string), "$1" + string + 'x');
        }
    }

    /**
     * More complex pluralizations
     *
     * @param pattern
     * @param replace
     */
    private static void plural(String pattern, String replace)
    {
        plurals.add(new Inflection(pattern, replace));
    }

    /**
     * @param end
     * @return a pattern that matches words ending with the given parameter
     */
    private static String endsWith(String end)
    {
        return "(\\S*)" + end + '$';
    }

    /**
     * Converts an French word to plural form
     *
     * @param str
     * @return plural form
     */
    public static String pluralize(String str)
    {

        for (Pattern pattern : uncountables)
        {
            Matcher matcher = pattern.matcher(str);
            if (matcher.matches())
            {
                return str;
            }
        }

        List<Inflection> rules = FrenchInflector.getPluralRules();
        for (Inflection inflection : rules)
        {
            Pattern pattern = inflection.getPattern();
            String replace = inflection.getReplace();
            Matcher matcher = pattern.matcher(str);
            if (matcher.matches())
            {
                return matcher.replaceFirst(replace);
            }
        }
        return str.replaceFirst("([\\w]+)([^s])$", "$1$2s");
    }

    /**
     * Returns map of plural patterns
     */
    private static List<Inflection> getPluralRules()
    {
        return plurals;
    }
}
