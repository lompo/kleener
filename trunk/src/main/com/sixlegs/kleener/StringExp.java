package com.sixlegs.kleener;

import java.io.*;
import java.util.*;

class StringExp extends Expression
{
    private final char[] chars;

    public StringExp(String value) {
        chars = value.toCharArray();
    }

    protected void split(List<CharSet> csets) {
        for (char c : chars)
            split(csets, new CharSet(c));
    }
        
    protected NFA getNFA(List<CharSet> csets) {
        State a = new State();
        State b = a;
        for (char c : chars)
            b.addEdge(new Edge(b = new State(), getEquivSet(csets, c)));
        return new NFA(csets, a, b);
    }

    private static Set<CharSet> getEquivSet(List<CharSet> csets, char c) {
        for (CharSet cset : csets)
            if (cset.contains(c))
                return Collections.singleton(cset);
        return Collections.emptySet();
    }
    
    private static final CharSet ESCAPE = new CharSet("()[]?*+|");

    public String toString() {
        return Misc.escapeChars(Misc.escapeStringLiteral(new String(chars)), ESCAPE);
    }
}
