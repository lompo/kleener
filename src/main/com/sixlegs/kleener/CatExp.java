package com.sixlegs.kleener;

import java.util.*;

class CatExp extends Expression
{
    private final Expression e1;
    private final Expression e2;

    public CatExp(Expression e1, Expression e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public CatExp(Expression... e) {
        this(0, e);
    }

    private CatExp(int index, Expression[] e) {
        this(e[index], (index + 2 == e.length) ? e[index + 1] : new CatExp(index + 1, e));
    }
    
    protected void split(List<CharSet> csets) {
        e1.split(csets);
        e2.split(csets);
    }

    protected NFA getNFA(List<CharSet> csets) {
        NFA n1 = e1.getNFA(csets);
        NFA n2 = e2.getNFA(csets);
        State.merge(n1.getStop(), n2.getStart());
        return new NFA(csets, n1.getStart(), n2.getStop());
    }

    public String toString() {
        return e1.toString() + e2.toString();
    }
}

