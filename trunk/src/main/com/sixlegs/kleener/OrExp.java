package com.sixlegs.kleener;

import java.util.*;

class OrExp extends Expression
{
    private final Expression left;
    private final Expression right;
    
    public OrExp(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public OrExp(Expression... e) {
        this(0, e);
    }

    private OrExp(int index, Expression[] e) {
        this(e[index], (index + 2 == e.length) ? e[index + 1] : new OrExp(index + 1, e));
    }

    protected NFA getNFA() {
        State s1 = new State();
        State s2 = new State();
        NFA n1 = left.getNFA();
        NFA n2 = right.getNFA();
        s1.addEdge(new Edge(n1.getStart()));
        s1.addEdge(new Edge(n2.getStart()));
        n1.getStop().addEdge(new Edge(s2));
        n2.getStop().addEdge(new Edge(s2));
        return new NFA(s1, s2);
    }

    public String toString() {
        return left + "|" + right;
    }
}

