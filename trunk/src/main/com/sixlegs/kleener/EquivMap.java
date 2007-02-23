package com.sixlegs.kleener;

import java.util.*;

class EquivMap
{
    private final List<CharSet> csets;
    
    public EquivMap(State start) {
        csets = getCharSets(getStates(start));
    }

    public int size() {
        return csets.size();
    }

    // TODO: performance! (build data structure)
    public int getIndex(char c) {
        for (int i = 0, len = size(); i < len; i++)
            if (csets.get(i).contains(c))
                return i;
        return -1;
    }

    private static Set<State> getStates(State start) {
        Set<State> states = Generics.newHashSet();
        List<State> q = Generics.newArrayList();
        q.add(start);
        while (!q.isEmpty()) {
            State state = q.remove(q.size() - 1);
            if (state != null && !states.contains(state)) {
                states.add(state);
                q.add(state.getState1());
                q.add(state.getState2());
            }
        }
        return states;
    }

    private static List<CharSet> getCharSets(Set<State> states) {
        List<CharSet> csets = Generics.newArrayList();
        for (State state : states) {
            if (state.getCharSet() == null)
                continue;
            CharSet copy = new CharSet(state.getCharSet());
            List<CharSet> temp = Generics.newArrayList();
            for (Iterator<CharSet> it = csets.iterator(); it.hasNext();) {
                CharSet oldSet = it.next();
                CharSet newSet = oldSet.intersect(copy);
                if (oldSet.isEmpty())
                    it.remove();
                if (!newSet.isEmpty())
                    temp.add(newSet);
            }
            if (!copy.isEmpty())
                temp.add(copy);
            csets.addAll(temp);
        }
        return csets;
    }
}
