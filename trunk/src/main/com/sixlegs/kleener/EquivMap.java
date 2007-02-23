package com.sixlegs.kleener;

import java.util.*;

class EquivMap
{
    private static final Comparator<CharSet> BY_LENGTH = new Comparator<CharSet>(){
        public int compare(CharSet c1, CharSet c2) {
            return c2.length() - c1.length();
        }
    };
    
    private final List<CharSet> csets = Generics.newArrayList();
    private final int size;
    private final int[] ascii = new int[128];

    public EquivMap(State start) {
        Arrays.fill(ascii, -1);
        split(start, new HashSet<State>());
        size = csets.size();
        Collections.sort(csets, BY_LENGTH);

        for (int i = 0; i < size; i++) {
            CharSet cset = csets.get(i);
            for (char c = cset.nextChar((char)0); c >= 0 && c < 128; c = cset.nextChar((char)(c + 1)))
                ascii[c] = i;
        }
    }

    public int size() {
        return size;
    }

    public int getIndex(char c) {
        if (c < 128)
            return ascii[c];
        // TODO: better data structure?
        for (int i = 0; i < size; i++)
            if (csets.get(i).contains(c))
                return i;
        return -1;
    }

    private void split(State state, Set<State> mark) {
        if (state == null || mark.contains(state))
            return;
        mark.add(state);
        if (state.getCharSet() != null) {
            CharSet copy = new CharSet(state.getCharSet());
            for (int i = 0, size = csets.size(); i < size; i++) {
                CharSet oldSet = csets.get(i);
                CharSet newSet = oldSet.intersect(copy);
                if (oldSet.isEmpty()) {
                    csets.set(i, newSet);
                } else if (!newSet.isEmpty()) {
                    csets.add(newSet);
                }
            }
            if (!copy.isEmpty())
                csets.add(copy);
        }
        split(state.getState1(), mark);
        split(state.getState2(), mark);
    }
}
