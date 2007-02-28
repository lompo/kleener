package com.sixlegs.kleener;

import java.util.*;
import java.util.regex.MatchResult;

abstract public class Pattern
{
    /*
    public static final int CANON_EQ = java.util.regex.Pattern.CANON_EQ;
    public static final int CASE_INSENSITIVE = java.util.regex.Pattern.CASE_INSENSITIVE;
    public static final int COMMENTS = java.util.regex.Pattern.COMMENTS;
    public static final int DOTALL = java.util.regex.Pattern.DOTALL;
    public static final int LITERAL = java.util.regex.Pattern.LITERAL;
    public static final int MULTILINE = java.util.regex.Pattern.MULTILINE;
    public static final int UNICODE_CASE = java.util.regex.Pattern.UNICODE_CASE;
    public static final int UNIX_LINES = java.util.regex.Pattern.UNIX_LINES;
    */

    private final String pattern;
    private final int flags;

    protected Pattern(String pattern, int flags) {
        this.pattern = pattern;
        this.flags = flags;
    }

    public static Pattern compile(String regex) {
        return compile(regex, 0);
    }

    public static Pattern compile(String regex, int flags) {
        return new DFA(new ExpressionParser().parse(regex, flags), regex, flags);
    }

    public static boolean matches(String regex, CharSequence input) {
        return compile(regex).matcher(input).matches();
    }

    public static String quote(String s) {
        // TODO
        throw new UnsupportedOperationException("implement me");
    }

    public int flags() {
        return flags;
    }

    abstract public Matcher matcher(CharSequence input);

    public String pattern() {
        return pattern;
    }

    @Override public String toString() {
        return pattern();
    }

    public String[] split(CharSequence input) {
        return split(input, 0);
    }

    public String[] split(CharSequence input, int limit) {
        List<String> parts = new ArrayList<String>((limit <= 0) ? 10 : limit);
        Matcher m = matcher(input);
        int p = 0;
        for (int count = 0; ++count != limit && m.find(p); p = m.end())
            parts.add(input.subSequence(p, m.start()).toString());
        parts.add(input.subSequence(p, input.length()).toString());
        if (limit == 0) {
            int size = parts.size();
            while ("".equals(parts.get(size - 1)))
                parts.remove(--size);
        }
        return parts.toArray(new String[parts.size()]);
    }
}
