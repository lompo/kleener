package com.sixlegs.kleener;

import java.util.*;
import java.util.regex.MatchResult;

class NFA extends AbstractPattern
{
    public NFA(Expression e) {
        super(e);
    }

    public MatchResult matches(CharSequence chars) {
        Sub[] match = new Sub[parenCount];
        Sub[][] clist = new Sub[stateCount][];
        Sub[][] nlist = new Sub[stateCount][];
        Sub[][] t;
        int p = 0;
        startSet(p, clist);
        for (int len = chars.length(); p < len; p++) { // TODO: short circuit
            step(clist, chars.charAt(p), p + 1, nlist, match);
            t = clist; clist = nlist; nlist = t; // swap
        }
        step(clist, 0, p, nlist, match);
        return getResult(chars, match);
    }
}
