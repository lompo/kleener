package com.sixlegs.kleener;

import java.util.*;

class LoopExp extends UnaryExp
{
    public LoopExp(Expression child) {
        super(child);
    }

    protected NFA getNFA(List<CharSet> csets) {
        NFA n1 = getChild().getNFA(csets);
        State s2 = new State();
        n1.getStop().addEdge(new Edge(s2));
        n1.getStop().addEdge(new Edge(n1.getStart()));
        return new NFA(csets, n1.getStart(), s2);
    }

    public String toString() {
        Expression e1 = getChild();
        if (e1 instanceof CharClassExp)
            return e1 + "+";
        return wrap(e1) + "+";
    }
}

