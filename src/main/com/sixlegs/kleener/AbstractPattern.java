package com.sixlegs.kleener;

import java.util.*;

abstract class AbstractPattern implements Pattern
{
    protected final State start;

    public AbstractPattern(Expression e) {
        this.start = e.getStart();
        e.patch(State.MATCH);
    }

    protected static Set<State> startSet(State start, Set<State> set) {
        set.clear();
        addState(start, set);
        return set;
    }

    protected static void addState(State state, Set<State> set) {
        if (state == null || set.contains(state))
            return;
        if (state.getCharSet() != null || state == State.MATCH) {
            set.add(state);
        } else {
            addState(state.getState1(), set);
            addState(state.getState2(), set);
        }
    }
    
    protected static void step(Set<State> curSet, char c, Set<State> newSet) {
        newSet.clear();
        for (State state : curSet) {
            CharSet cset = state.getCharSet();
            if (cset != null && cset.contains(c))
                addState(state.getState1(), newSet);
        }
    }
    
    protected static boolean isMatch(Collection<State> states) {
        for (State state : states)
            if (state == State.MATCH)
                return true;
        return false;
    }
}
