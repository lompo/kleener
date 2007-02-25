package com.sixlegs.kleener;

import java.util.*;

abstract class AbstractPattern implements Pattern
{
    private static final Sub EMPTY_SUB = new Sub(-1, -1);
    private static final Map<State,Sub[]> EMPTY_THREADS = Collections.emptyMap();

    protected final State start;
    protected final int parenCount;

    public AbstractPattern(Expression e) {
        if (e.getStart().getOp() != State.Op.LParen)
            throw new IllegalArgumentException("Outer expression must be paren");
        this.start = e.getStart();
        this.parenCount = count(start, new HashSet<State>());
        e.patch(State.MATCH);
    }

    private static int count(State state, Set<State> mark) {
        if (state == null || mark.contains(state))
            return 0;
        mark.add(state);
        return ((state.getOp() == State.Op.LParen) ? 1 : 0) +
            count(state.getState1(), mark) +
            count(state.getState2(), mark);
    }

    protected void startSet(int p, Map<State,Sub[]> threads) {
        step(EMPTY_THREADS, 0, p, threads, new Sub[parenCount]);
    }

    protected void step(Map<State,Sub[]> clist, int c, int p, Map<State,Sub[]> nlist, Sub[] match) {
        // System.err.println("p=" + p + " c='" + (char)c + "' match=" + match + " clist=" + clist);
        nlist.clear();
        for (State state : clist.keySet()) {
            Sub[] tmatch = clist.get(state);
            switch (state.getOp()) {
            case CharSet:
                if (state.getCharSet().contains(c))
                    addState(nlist, state.getState1(), tmatch, p);
                break;
            case Match:
                System.arraycopy(tmatch, 0, match, 0, parenCount);
                return;
            }
        }
        if (match[0] == null || match[0].sp < 0)
            addState(nlist, start, new Sub[parenCount], p);
    }

    private void addState(Map<State,Sub[]> threads, State state, Sub[] match, int p) {
        if (state == null)
            return;
        if (threads.containsKey(state))
            return;

        Sub[] tmatch = new Sub[parenCount];
        System.arraycopy(match, 0, tmatch, 0, parenCount);
        threads.put(state, tmatch);
        
        switch (state.getOp()) {
        case Match:
        case CharSet:
            break;
        case Split:
            addState(threads, state.getState1(), match, p);
            addState(threads, state.getState2(), match, p);
            break;
        default:
            int data = state.getData();
            Sub save = match[data];
            match[data] = (state.getOp() == State.Op.LParen) ?
                new Sub(p, -1) :
                new Sub(save.sp, p);
            addState(threads, state.getState1(), match, p);
            match[data] = save;
        }
    }

    protected static MatchResult getResult(final CharSequence chars, final Sub[] match) {
        // System.err.println(Arrays.asList(match));
        if (match[0] == null || match[0].sp < 0)
            return null;
        return new MatchResult() {
            public String group() {
                return group(0);
            }
            public int groupCount() {
                return match.length - 1;
            }
            public String group(int group) {
                return chars.subSequence(match[group].sp, match[group].ep).toString();
            }
        };
    }
}
