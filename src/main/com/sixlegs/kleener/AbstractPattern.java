package com.sixlegs.kleener;

import java.util.*;

abstract class AbstractPattern implements Pattern
{
    private static final Sub EMPTY_SUB = new Sub(-1, -1);
    private static final Sub START_SUB = new Sub(0, -1);
    private static final Sub[][] EMPTY = {};

    protected final State start;
    protected final int stateCount;
    protected final int parenCount;
    private final State[] states;

    public AbstractPattern(Expression e) {
        if (e.getStart().getOp() != State.Op.LParen)
            throw new IllegalArgumentException("Outer expression must be paren");
        this.start = e.getStart();
        e.patch(State.MATCH);

        Set<State> states = new LinkedHashSet<State>();
        states.add(State.MATCH);
        
        this.parenCount = visit(start, states);
        this.stateCount = states.size();
        this.states = states.toArray(new State[stateCount]);
    }

    private static int visit(State state, Set<State> mark) {
        if (state == null || mark.contains(state))
            return 0;
        state.setId(mark.size());
        mark.add(state);
        return ((state.getOp() == State.Op.LParen) ? 1 : 0) +
            visit(state.getState1(), mark) +
            visit(state.getState2(), mark);
    }

    protected void startSet(int p, Sub[][] threads) {
        step(EMPTY, 0, p, threads, new Sub[parenCount]);
    }

    protected void step(Sub[][] clist, int c, int p, Sub[][] nlist, Sub[] match) {
        Arrays.fill(nlist, null);
        for (int i = 0; i < clist.length; i++) {
            Sub[] tmatch = clist[i];
            if (tmatch == null)
                continue;
            State state = states[i];
            switch (state.getOp()) {
            case CharSet:
                if (state.getCharSet().contains(c))
                    addState(nlist, state.getState1(), tmatch, p);
                break;
            case NotCharSet:
                if (!state.getCharSet().contains(c))
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

    private void addState(Sub[][] threads, State state, Sub[] match, int p) {
        if (state == null)
            return;
        int id = state.getId();
        if (threads[id] != null)
            return;

        threads[id] = new Sub[parenCount];
        System.arraycopy(match, 0, threads[id], 0, parenCount);
        
        switch (state.getOp()) {
        case Match:
        case CharSet:
        case NotCharSet:
            break;
        case Split:
            addState(threads, state.getState1(), match, p);
            addState(threads, state.getState2(), match, p);
            break;
        default:
            int data = state.getData();
            Sub save = match[data];
            match[data] = (state.getOp() == State.Op.LParen) ?
                ((p == 0) ? START_SUB : new Sub(p, -1)) :
                new Sub(save.sp, p);
            addState(threads, state.getState1(), match, p);
            match[data] = save;
        }
    }

    protected static MatchResult getResult(final CharSequence chars, final Sub[] match) {
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
