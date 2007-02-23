package com.sixlegs.kleener;

import java.util.*;

class NFA extends AbstractPattern
{
    public NFA(Expression e) {
        super(e);
    }

    public boolean matches(CharSequence chars) {
        Set<State> curSet = startSet(start, new HashSet<State>());
        Set<State> newSet = Generics.newHashSet();
        Set<State> t;
        for (int i = 0, len = chars.length(); i < len && !curSet.isEmpty(); i++) {
            step(curSet, chars.charAt(i), newSet);
            t = curSet; curSet = newSet; newSet = t; // swap
        }
        return isMatch(curSet);
    }
}
