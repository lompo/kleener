package com.sixlegs.kleener;

import java.util.*;

final public class NFA extends FA
{
    private final State stop;
    private final List<CharSet> csets;

    // TODO: remove csets, collect in getDFA instead
    NFA(List<CharSet> csets, State start, State stop) {
        super(start);
        this.csets = csets;
        this.stop = stop;
    }

    State getStop() {
        return stop;
    }
    
    public DFA getDFA() {
        Set<Set<State>> seen = new HashSet<Set<State>>();
        Map<Set<State>,State> mapping = new HashMap<Set<State>,State>();
        Map<Object,Set<State>> closureCache = new HashMap<Object,Set<State>>();
        Set<State> startSet = getClosure(getStart(), closureCache);
        LinkedList<Set<State>> queue = new LinkedList<Set<State>>();
        queue.add(startSet);
        while (!queue.isEmpty()) {
            Set<State> T = queue.removeFirst();
            State T_map = getSetState(T, mapping);
            for (CharSet cset : csets) {
                Set<State> U = getClosure(move(T, cset), closureCache);
                if (U.isEmpty())
                    continue;
                if (seen.add(U))
                    queue.add(U);
                State U_map = getSetState(U, mapping);
                T_map.addEdge(new Edge(U_map, Collections.singleton(cset)));
            }
        }
        // System.err.println(mapping);
        return new DFA(mapping.get(startSet));
    }


    private static Set<State> move(Set<State> states, CharSet cset) {
        Set<State> result = new HashSet<State>();
        for (State state : states) {
            for (Edge edge : state.getEdges()) {
                State next = edge.accept(cset);
                if (next != null)
                    result.add(next);
            }
        }
        return result;
    }

    private static State getSetState(Set<State> set, Map<Set<State>,State> mapping) {
        State state = mapping.get(set);
        if (state == null)
            mapping.put(set, state = new State());
        return state;
    }

    private static Set<State> getClosure(Set<State> states, Map<Object,Set<State>> cache) {
        Set<State> closure = cache.get(states);
        if (closure == null) {
            closure = new HashSet<State>();
            for (State state : states)
                closure.addAll(getClosure(state, cache));
            cache.put(states, closure);
        }
        return closure;
    }

    private static Set<State> getClosure(State state, Map<Object,Set<State>> cache) {
        Set<State> closure = cache.get(state);
        if (closure == null) {
            computeClosure(state, closure = new HashSet<State>());
            cache.put(state, closure);
        }
        return closure;
    }

    private static void computeClosure(State state, Set<State> closure) {
        if (!closure.contains(state)) {
            closure.add(state);
            for (Edge edge : state.getEdges()) {
                if (edge.isEmpty())
                    computeClosure(edge.getNext(), closure);
            }
        }
    }
}
