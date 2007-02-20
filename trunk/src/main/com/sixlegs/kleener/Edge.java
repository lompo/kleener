package com.sixlegs.kleener;

import java.util.*;

class Edge
{
    private final State next;
    private final Set<CharSet> csets;
    private final boolean negate;

    public Edge(State next) {
        this(next, null, false);
    }

    public Edge(State next, Set<CharSet> csets) {
        this(next, csets, false);
    }

    public Edge(State next, Set<CharSet> csets, boolean negate) {
        this.next = next;
        this.csets = csets;
        this.negate = negate;
    }

    public State getNext() {
        return next;
    }

    public boolean isNegated() {
        return negate;
    }
    
    public boolean isEmpty() {
        return csets == null;
    }

    public State accept(CharSet cset) {
        return (csets != null && (csets.contains(cset) ^ negate)) ? next : null;
    }

    public State accept(char c) {
        if (csets != null) {
            for (CharSet cset : csets)
                if (cset.contains(c))
                    return next;
        }
        return null;
    }   

    public String toString() {
        return "-" + Misc.format(csets) + "->" + next;
    }
}
