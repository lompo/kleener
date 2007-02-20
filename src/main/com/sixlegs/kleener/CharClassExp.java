package com.sixlegs.kleener;

import java.io.*;
import java.util.*;

class CharClassExp extends Expression
{
    private final CharSet cset;
 	private final boolean negate;

    public CharClassExp(CharSequence chars, boolean negate) {
        this(new CharSet(chars), negate);
    }

    private CharClassExp(CharSet cset, boolean negate) {
        this.cset = cset;
        this.negate = negate;
    }

    // TODO: this should become part of Expression.parse
    public static CharClassExp parse(CharSequence chars) throws IOException { // TODO: get rid of exception
        boolean negate = chars.charAt(0) == '^';
        int index = negate ? 1 : 0;
        CharSet cset = new CharSet();
        Unescaper unesc = new Unescaper(new CharSequenceReader(chars, index, chars.length() - index));
        boolean sawDot = false;
        int prev = 0;
        for (;;) {
            int c = unesc.read();
            if (c == -1)
                break;
            if (unesc.wasEscaped()) {
                cset.add((char)c);
            } else if (c == '-' && prev > 0) {
                int next = unesc.read();
                if (next == -1) {
                    cset.add('-');
                    break;
                } else {
                    for (int i = prev + 1; i <= next; i++)
                        cset.add((char)i);
                }
            } else if (c == '.') {
                sawDot = true;
            } else {
                cset.add((char)c);
            }
            prev = c;
        }
        if (sawDot) {
            // negate: chars = endl_chars - chars
            // otherwise: same, but set negate = true
            // TODO
            throw new UnsupportedOperationException("implement me");
        }
        return new CharClassExp(cset, negate);
    }

    protected void split(List<CharSet> csets) {
        split(csets, cset);
    }

    protected NFA getNFA(List<CharSet> csets) {
        Set<CharSet> eset = getEquivSet(csets, cset);
        State a = new State();
        State b = new State();
        a.addEdge(new Edge(b, eset, negate));
        return new NFA(csets, a, b);
    }

    private static Set<CharSet> getEquivSet(List<CharSet> csets, CharSet cset) {
        Set<CharSet> eset = new HashSet<CharSet>();
        for (CharSet check : csets)
            if (check.containsAny(cset))
                eset.add(check);
        return eset;
    }
    
    private static final CharSet ESCAPE = new CharSet("^-");

    public String toString() {
        // TODO: escape hyphen, caret
        return (negate ? "[^" : "[") + Misc.escapeChars(Misc.escapeStringLiteral(cset.toString()), ESCAPE) + "]";
    }
}
