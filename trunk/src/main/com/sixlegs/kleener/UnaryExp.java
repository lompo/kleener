package com.sixlegs.kleener;

import java.util.*;

abstract class UnaryExp extends Expression
{
    private final Expression child;

    public UnaryExp(Expression child) {
        this.child = child;
    }

    public Expression getChild() {
        return child;
    }

    protected void split(List<CharSet> csets) {
        child.split(csets);
    }
}
