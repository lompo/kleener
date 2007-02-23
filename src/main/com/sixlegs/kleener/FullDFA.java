package com.sixlegs.kleener;

import java.util.*;

class FullDFA extends AbstractPattern
{
    private final boolean optimize;

    public FullDFA(Expression e, boolean optimize) {
        super(e);
        this.optimize = optimize;
    }

    public boolean matches(CharSequence chars) {
        // TODO
        return false;
    }

    public Pattern compile() {
        // TODO
        return this;
    }
}

