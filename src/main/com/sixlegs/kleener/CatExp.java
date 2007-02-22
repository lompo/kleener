package com.sixlegs.kleener;

import java.util.*;

class CatExp extends Expression
{
    private final Expression left;
    private final Expression right;

    public CatExp(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public CatExp(Expression... e) {
        this(0, e);
    }

    private CatExp(int index, Expression[] e) {
        this(e[index], (index + 2 == e.length) ? e[index + 1] : new CatExp(index + 1, e));
    }
    
    protected NFA getNFA() {
        NFA n1 = left.getNFA();
        NFA n2 = right.getNFA();
        State.merge(n1.getStop(), n2.getStart());
        return new NFA(n1.getStart(), n2.getStop());
    }

    public String toString() {
        return left.toString() + right;
    }
}

