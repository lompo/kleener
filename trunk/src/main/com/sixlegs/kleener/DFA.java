package com.sixlegs.kleener;

import java.util.*;

class DFA extends AbstractPattern
{
    private final Map<Set<State>,DState> dstates = Generics.newHashMap();
    private final EquivMap equiv;
    
    public DFA(Expression e) {
        super(e);
        this.equiv = new EquivMap(start);
    }

    public boolean matches(CharSequence chars) {
        Set<State> newSet = Generics.newHashSet();
        DState d = dstate(startSet(start, new HashSet<State>()));
        DState next;
        for (int i = 0, len = chars.length(); i < len; i++) { // TODO: short circuit?
            char c = chars.charAt(i);
            int index = equiv.getIndex(c);
            if ((next = d.next[index]) == null) {
                step(d.states, c, newSet);
                next = d.next[index] = dstate(newSet);
            }
            d = next;
        }
        return isMatch(d.states);
    }

    private DState dstate(Set<State> set) {
        DState d = dstates.get(set);
        if (d == null)
            dstates.put(set, d = new DState(set, equiv.size()));
        return d;
    }

    private static class DState
    {
        final Set<State> states;
        final DState[] next;

        public DState(Set<State> states, int size) {
            this.states = new HashSet<State>(states);
            this.next = new DState[size];
        }
    }
}
