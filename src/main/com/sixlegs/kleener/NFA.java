package com.sixlegs.kleener;

import java.util.*;

final public class NFA extends FA
{
    private final State stop;

    NFA(State start, State stop) {
        super(start);
        this.stop = stop;
    }

    State getStop() {
        return stop;
    }

    public DFA getDFA() {
        return new NFACompiler(this).getDFA();
    }
}
