package com.sixlegs.kleener;

import java.util.*;

final public class NFA extends FA
{
    private final State stop;

    NFA(State start, State stop) {
        super(start);
        this.stop = stop;
    }

    State getStop() {
        return stop;
    }
    
    public DFA getDFA() {
        List<CharSet> csets = getCharSets(getStart());
        System.err.println(Misc.format(csets));
        
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
                T_map.addEdge(new Edge(U_map, cset));
            }
        }
        return new DFA(mapping.get(startSet));
    }

    private static List<CharSet> getCharSets(State start) {
        final List<CharSet> csets = new ArrayList<CharSet>();
        getCharSets(start, csets, new HashSet<State>());
        return csets;
    }

    private static void getCharSets(State state, List<CharSet> csets, Set<State> seen) {
        for (Edge edge : state.getEdges()) {
            if (!edge.isEmpty()) {
                CharSet copy = new CharSet(edge.getCharSet());
                List<CharSet> temp = new ArrayList<CharSet>();
                for (Iterator<CharSet> it = csets.iterator(); it.hasNext();) {
                    CharSet oldSet = it.next();
                    CharSet newSet = oldSet.intersect(copy);
                    if (oldSet.isEmpty())
                        it.remove();
                    if (!newSet.isEmpty())
                        temp.add(newSet);
                }
                if (!copy.isEmpty())
                    temp.add(copy);
                csets.addAll(temp);
            }

            State next = edge.getNext();
            if (next != null && !seen.contains(next)) {
                seen.add(state);
                getCharSets(next, csets, seen);
            }
        }
    }

    private static Set<State> move(Set<State> states, CharSet cset) {
        Set<State> result = new HashSet<State>();
        for (State state : states)
            for (Edge edge : state.getEdges())
                if (!edge.isEmpty() && edge.getCharSet().containsAny(cset))
                    result.add(edge.getNext());
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
