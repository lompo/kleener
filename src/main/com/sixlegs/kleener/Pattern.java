package com.sixlegs.kleener;

import java.util.List;

public interface Pattern
{
    public enum CompileType {
        NFA,
        DFA,
    };

    MatchResult matches(CharSequence chars);
}
