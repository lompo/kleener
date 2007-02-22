package com.sixlegs.kleener;

import java.io.*;
import java.util.*;

class StringExp extends Expression
{
    private final char[] chars;

    public StringExp(String value) {
        chars = value.toCharArray();
    }

    @Override protected Collection<CharSet> getCharSets() {
        List<CharSet> csets = new ArrayList<CharSet>();
        for (char c : chars)
            csets.add(new CharSet(c));
        return csets;
    }

    protected NFA getNFA() {
        State a = new State();
        State b = a;
        for (char c : chars)
            b.addEdge(new Edge(b = new State(), new CharSet(c)));
        return new NFA(a, b);
    }

    private static final CharSet ESCAPE = new CharSet("()[]?*+|");

    public String toString() {
        return Misc.escapeChars(Misc.escapeStringLiteral(new String(chars)), ESCAPE);
    }
}
