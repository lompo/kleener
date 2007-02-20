package com.sixlegs.kleener;

import java.util.*;

class OrExp extends Expression
{
    private final Expression e1;
    private final Expression e2;

    public OrExp(Expression e1, Expression e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public OrExp(Expression... e) {
        this(0, e);
    }

    private OrExp(int index, Expression[] e) {
        this(e[index], (index + 2 == e.length) ? e[index + 1] : new OrExp(index + 1, e));
    }

    protected void split(List<CharSet> csets) {
        e1.split(csets);
        e2.split(csets);
    }

    protected NFA getNFA(List<CharSet> csets) {
        State s1 = new State();
        State s2 = new State();
        NFA n1 = e1.getNFA(csets);
        NFA n2 = e2.getNFA(csets);
        s1.addEdge(new Edge(n1.getStart()));
        s1.addEdge(new Edge(n2.getStart()));
        n1.getStop().addEdge(new Edge(s2));
        n2.getStop().addEdge(new Edge(s2));
        return new NFA(csets, s1, s2);
    }

    public String toString() {
        return e1 + "|" + e2;
    }

//     protected int getLength()
//     {
//         return e1.getLength();
//     }

//     protected boolean isFixedLength()
//     {
//         return (e1.isFixedLength() && e2.isFixedLength() &&
//                 e1.getLength() == e2.getLength());
//     }
}

