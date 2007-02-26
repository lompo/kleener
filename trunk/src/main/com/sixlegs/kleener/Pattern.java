package com.sixlegs.kleener;

import java.util.List;
import java.util.regex.MatchResult;

public interface Pattern
{
    public enum CompileType {
        NFA,
        DFA,
    };

    MatchResult matches(CharSequence chars);
}
