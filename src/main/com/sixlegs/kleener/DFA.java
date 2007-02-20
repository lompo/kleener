package com.sixlegs.kleener;

import java.util.*;

final public class DFA extends FA implements Pattern
{
    DFA(State start) {
        super(start);
    }

    public boolean matches(CharSequence chars) {
        int index = 0;
        int length = chars.length();
        State state = getStart();
        BIGLOOP:
        while (index < length) {
            char c = chars.charAt(index++);
            for (Edge edge : state.getEdges()) {
                State next = edge.accept(c);
                if (next != null) {
                    state = next;
                    continue BIGLOOP;
                }
            }
            return false;
        }
        return true;
    }

    public DFA getMinimizedDFA() {
        // TODO
        return this;
    }

    public Pattern compile() {
        // TODO
        return this;
    }

    /*
      char -> equiv (lookup switch?)
      cur state + equiv -> new state (nested switch)
    */
}
