package com.sixlegs.kleener;

import java.util.*;

class CatExp extends BinaryExp
{
    public CatExp(Expression left, Expression right) {
        super(left, right);
    }

    public CatExp(Expression... e) {
        this(0, e);
    }

    private CatExp(int index, Expression[] e) {
        this(e[index], (index + 2 == e.length) ? e[index + 1] : new CatExp(index + 1, e));
    }
    
    protected NFA getNFA(List<CharSet> csets) {
        NFA n1 = getLeft().getNFA(csets);
        NFA n2 = getRight().getNFA(csets);
        State.merge(n1.getStop(), n2.getStart());
        return new NFA(csets, n1.getStart(), n2.getStop());
    }

    public String toString() {
        return getLeft().toString() + getRight().toString();
    }
}

