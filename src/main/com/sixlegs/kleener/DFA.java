package com.sixlegs.kleener;

import java.util.*;

class DFA extends AbstractPattern
{
    // TODO: make dstates reclaimable by GC
    private final Map<Object,DState> dstates = Generics.newHashMap();
    private final EquivMap equiv;
    
    public DFA(Expression e) {
        super(e);
        this.equiv = new EquivMap(start);
    }

    public MatchResult matches(CharSequence chars) {
        Sub[] match = new Sub[parenCount];
        int p = 0;
        Map<State,Sub[]> clist = Generics.newHashMap();
        Map<State,Sub[]> nlist = Generics.newHashMap();
        startSet(p, clist);
        DState d = dstate(clist);
        DState next;
        for (int len = chars.length(); p < len; p++) { // TODO: short circuit?
            char c = chars.charAt(p);
            int index = equiv.getIndex(c);
            if ((next = d.next[index]) == null) {
                step(d.threads, c, p + 1, nlist, match);
                next = d.next[index] = dstate(nlist);
            }
            d = next;
        }
        step(d.threads, 0, p, nlist, match);
        return getResult(chars, match);
    }

    private DState dstate(Map<State,Sub[]> threads) {
        Object key = getKey(threads);
        DState d = dstates.get(key);
        if (d == null)
            dstates.put(key, d = new DState(threads, equiv.size()));
        return d;
    }

    private static Object getKey(Map<State,Sub[]> threads) {
        Map<State,List<Sub>> key = Generics.newHashMap();
        for (State state : threads.keySet())
            key.put(state, Arrays.asList(threads.get(state)));
        return key;
    }

    private static class DState
    {
        final Map<State,Sub[]> threads;
        final DState[] next;

        public DState(Map<State,Sub[]> threads, int size) {
            this.threads = new HashMap<State,Sub[]>(threads);
            this.next = new DState[size];
        }
    }
}
