package com.sixlegs.kleener;

import java.util.*;
import java.util.regex.MatchResult;

abstract public class Pattern
{
    public enum CompileType {
        NFA,
        DFA,
    };

    private final String pattern;

    protected Pattern(String pattern) {
        this.pattern = pattern;
    }

    public static Pattern compile(String regex) {
        return compile(regex, CompileType.DFA);
    }

    public static Pattern compile(String regex, CompileType type) {
        return new ExpressionParser().parse(regex).compile(regex, type);
    }
    
    public static boolean matches(String regex, CharSequence input) {
        return compile(regex).matcher(input).matches();
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
        List<String> parts = new ArrayList<String>((limit == 0) ? 10 : limit);
        Matcher m = matcher(input);
        int p = 0, count = 0;
        while (++count < limit && m.find(p))
            parts.add(input.subSequence(p, p = m.end()).toString());
        parts.add(input.subSequence(p, input.length()).toString());
        return parts.toArray(new String[parts.size()]);
    }
}
