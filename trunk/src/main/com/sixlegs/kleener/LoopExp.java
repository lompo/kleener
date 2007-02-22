package com.sixlegs.kleener;

import java.util.*;

class LoopExp extends Expression
{
    private final Expression exp;

    public LoopExp(Expression exp) {
        this.exp = exp;
    }

    protected NFA getNFA() {
        NFA n1 = exp.getNFA();
        State s2 = new State();
        n1.getStop().addEdge(new Edge(s2));
        n1.getStop().addEdge(new Edge(n1.getStart()));
        return new NFA(n1.getStart(), s2);
    }

    public String toString() {
        if (exp instanceof CharClassExp)
            return exp + "+";
        return wrap(exp) + "+";
    }
}

