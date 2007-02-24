package com.sixlegs.kleener;

public interface Pattern
{
    public enum Type {
        NFA,
        DFA,
        DFA_PRECOMPUTE,
        DFA_OPTIMIZE,
        DFA_COMPILE
    };

    // TODO: support surrogates
    boolean matches(CharSequence chars);
}
