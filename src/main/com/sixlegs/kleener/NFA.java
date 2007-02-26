package com.sixlegs.kleener;

import java.util.*;
import java.util.regex.MatchResult;

class NFA extends PatternHelper
{
    public NFA(String regex, Expression e) {
        super(regex, e);
    }

    protected Matcher createMatcher() {
        return new NFAMatcher(this);
    }

    private static class NFAMatcher extends Matcher
    {
        private Sub[][] clist;
        private Sub[][] nlist;
        private Sub[][] t;

        public NFAMatcher(NFA pattern) {
            super(pattern);
            clist = new Sub[pattern.stateCount][];
            nlist = new Sub[pattern.stateCount][];            
        }

        protected boolean match(int p) {
            pattern.startSet(p, clist);
            for (int len = input.length(); p < len; p++) { // TODO: short circuit
                pattern.step(clist, input.charAt(p), p + 1, nlist, match);
                t = clist; clist = nlist; nlist = t; // swap
            }
            pattern.step(clist, 0, p, nlist, match);
            return updateMatch();
        }
    }
}
