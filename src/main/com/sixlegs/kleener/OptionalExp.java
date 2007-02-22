package com.sixlegs.kleener;

import java.util.*;

class OptionalExp extends Expression
{
    private final Expression exp;

    public OptionalExp(Expression exp) {
        this.exp = exp;
    }

    protected NFA getNFA() {
        State s1 = new State();
        NFA n1 = exp.getNFA();
        s1.addEdge(new Edge(n1.getStart()));
        s1.addEdge(new Edge(n1.getStop()));
        return new NFA(s1, n1.getStop());
    }

    public String toString() {
        if (exp instanceof LoopExp) {
            StringBuilder sb = new StringBuilder(exp.toString());
            sb.setCharAt(sb.length() - 1, '*');
            return sb.toString();
        }
        if (exp instanceof CharClassExp)
            return exp + "?";
        return wrap(exp) + "?";
    }        
}
