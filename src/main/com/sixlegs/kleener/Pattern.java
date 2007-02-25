package com.sixlegs.kleener;

import java.util.List;

public interface Pattern
{
    public enum MatchType {
        LeftmostBiased,
        LeftmostLongest,
    };

    public enum CompileType {
        NFA,
        DFA,
    };

    MatchResult matches(CharSequence chars);
}
