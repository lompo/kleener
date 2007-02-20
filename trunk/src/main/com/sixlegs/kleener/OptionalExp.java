package com.sixlegs.kleener;

import java.util.*;

class OptionalExp extends Expression
{
    private final Expression e1;

    public OptionalExp(Expression e1) {
        this.e1 = e1;
    }

    protected void split(List<CharSet> csets) {
        e1.split(csets);
    }

    protected NFA getNFA(List<CharSet> csets) {
        State s1 = new State();
        NFA n1 = e1.getNFA(csets);
        s1.addEdge(new Edge(n1.getStart()));
        s1.addEdge(new Edge(n1.getStop()));
        return new NFA(csets, s1, n1.getStop());
    }

    public String toString() {
        if (e1 instanceof LoopExp) {
            StringBuilder sb = new StringBuilder(e1.toString());
            sb.setCharAt(sb.length() - 1, '*');
            return sb.toString();
        }
        if (e1 instanceof CharClassExp)
            return e1 + "?";
        return wrap(e1) + "?";
    }        
}
