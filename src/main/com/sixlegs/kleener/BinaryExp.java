package com.sixlegs.kleener;

import java.util.*;

abstract class BinaryExp extends Expression
{
    private final Expression left;
    private final Expression right;

    public BinaryExp(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    protected void split(List<CharSet> csets) {
        left.split(csets);
        right.split(csets);
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }
}
