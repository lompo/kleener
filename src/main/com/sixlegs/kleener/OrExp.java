package com.sixlegs.kleener;

import java.util.*;

class OrExp extends BinaryExp
{
    public OrExp(Expression left, Expression right) {
        super(left, right);
    }

    public OrExp(Expression... e) {
        this(0, e);
    }

    private OrExp(int index, Expression[] e) {
        this(e[index], (index + 2 == e.length) ? e[index + 1] : new OrExp(index + 1, e));
    }

    protected NFA getNFA(List<CharSet> csets) {
        State s1 = new State();
        State s2 = new State();
        NFA n1 = getLeft().getNFA(csets);
        NFA n2 = getRight().getNFA(csets);
        s1.addEdge(new Edge(n1.getStart()));
        s1.addEdge(new Edge(n2.getStart()));
        n1.getStop().addEdge(new Edge(s2));
        n2.getStop().addEdge(new Edge(s2));
        return new NFA(csets, s1, s2);
    }

    public String toString() {
        return getLeft() + "|" + getRight();
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

