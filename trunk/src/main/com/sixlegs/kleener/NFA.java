package com.sixlegs.kleener;

import java.util.*;

class NFA extends AbstractPattern
{
    public NFA(Expression e) {
        super(e);
    }

    public MatchResult matches(CharSequence chars) {
        Sub[] match = new Sub[parenCount];
        int p = 0;
        Map<State,Sub[]> clist = Generics.newHashMap();
        Map<State,Sub[]> nlist = Generics.newHashMap();
        Map<State,Sub[]> t;
        startSet(p, clist);
        for (int len = chars.length(); p < len && !clist.isEmpty(); p++) {
            step(clist, chars.charAt(p), p + 1, nlist, match);
            t = clist; clist = nlist; nlist = t; // swap
        }
        step(clist, 0, p, nlist, match);
        return getResult(chars, match);
    }
}
