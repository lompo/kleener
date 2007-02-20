package com.sixlegs.kleener;

import java.util.*;

class LoopExp extends Expression
{
    private final Expression e1;

    public LoopExp(Expression e1) {
        this.e1 = e1;
    }

    protected void split(List<CharSet> csets) {
        e1.split(csets);
    }

    protected NFA getNFA(List<CharSet> csets) {
        NFA n1 = e1.getNFA(csets);
        State s2 = new State();
        n1.getStop().addEdge(new Edge(s2));
        n1.getStop().addEdge(new Edge(n1.getStart()));
        return new NFA(csets, n1.getStart(), s2);
    }

    public String toString() {
        if (e1 instanceof CharClassExp)
            return e1 + "+";
        return wrap(e1) + "+";
    }
}

