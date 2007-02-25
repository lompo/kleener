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
        Sub[][] clist = new Sub[stateCount][];
        Sub[][] nlist = new Sub[stateCount][];
        startSet(p, clist);
        DState d = dstate(clist);
        DState next;
        for (int len = chars.length(); p < len; p++) { // TODO: short circuit
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

    private DState dstate(Sub[][] threads) {
        Object key = new DeepKey(threads);
        DState d = dstates.get(key);
        if (d == null) {
            threads = threads.clone(); // TODO: deep clone necessary?
            dstates.put(new DeepKey(threads, key.hashCode()),
                        d = new DState(threads, equiv.size()));
        }
        return d;
    }

    private static class DState
    {
        final Sub[][] threads;
        final DState[] next;

        public DState(Sub[][] threads, int size) {
            this.threads = threads;
            this.next = new DState[size];
        }
    }

    private static class DeepKey
    {
        private final Object[] a;
        private final int hashCode;

        public DeepKey(Object[] a) {
            this(a, Arrays.deepHashCode(a));
        }

        public DeepKey(Object[] a, int hashCode) {
            this.a = a;
            this.hashCode = hashCode;
        }
        
        @Override public int hashCode() {
            return hashCode;
        }

        @Override public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof DeepKey))
                return false;
            return Arrays.deepEquals(a, ((DeepKey)o).a);
        }

        @Override public String toString() {
            return Arrays.deepToString(a);
        }
    }
}
