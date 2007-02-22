package com.sixlegs.kleener;

import java.util.*;

class Edge
{
    private final State next;
    private final CharSet cset;
    private final boolean negate;

    public Edge(State next) {
        this(next, null, false);
    }

    public Edge(State next, CharSet cset) {
        this(next, cset, false);
    }

    public Edge(State next, CharSet cset, boolean negate) {
        this.next = next;
        this.cset = cset;
        this.negate = negate;
    }

    public State getNext() {
        return next;
    }

    public boolean isNegated() {
        return negate;
    }
    
    public boolean isEmpty() {
        return cset == null;
    }

    public CharSet getCharSet() {
        return cset;
    }

    public String toString() {
        return "-" + Misc.format(Collections.singleton(cset)) + "->" + next;
    }
}
