package com.sixlegs.kleener;

public interface Pattern
{
    public enum Type { NFA, DFA };

    boolean matches(CharSequence chars);
}

