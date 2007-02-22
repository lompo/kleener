package com.sixlegs.kleener;

import java.util.*;

class NFACompiler
{
    private final NFA nfa;
    private final Set<State> states;
    private final List<CharSet> csets;
    private final Map<State,Set<State>> closures = Generics.newHashMap();
    private final Map<Set<State>,Set<State>> closuresPrime = Generics.newHashMap();
    private final Map<Set<State>,State> mapping = Generics.newHashMap();

    public NFACompiler(NFA nfa) {
        this.nfa = nfa;
        states = Misc.getStates(nfa);
        csets = getCharSets(states);
        System.err.println(Misc.format(csets));
    }
    
    public DFA getDFA() {
        System.err.printf("%d NFA states\n", states.size());
        Set<Set<State>> seen = new HashSet<Set<State>>();
        Set<State> startSet = getClosure(nfa.getStart());
        LinkedList<Set<State>> q = new LinkedList<Set<State>>();
        q.add(startSet);
        while (!q.isEmpty()) {
            Set<State> T = q.removeFirst();
            State T_map = getSetState(T);
            for (CharSet cset : csets) {
                Set<State> U = getClosure(move(T, cset));
                if (U.isEmpty())
                    continue;
                if (seen.add(U))
                    q.add(U);
                State U_map = getSetState(U);
                T_map.addEdge(new Edge(U_map, cset));
            }
        }
        DFA dfa = new DFA(getSetState(startSet));
        System.err.printf("%d DFA states\n", Misc.getStates(dfa).size());
        return dfa;
    }

    private Set<State> getClosure(Set<State> states) {
        Set<State> closure = closuresPrime.get(states);
        if (closure == null) {
            closure = Generics.newHashSet();
            for (State state : states)
                closure.addAll(getClosure(state));
            closuresPrime.put(states, closure);
        }
        return closure;
    }

    private Set<State> getClosure(State state) {
        Set<State> closure = closures.get(state);
        if (closure == null) {
            closure = Generics.newHashSet();
            closure.add(state);
            for (Edge edge : state.getEdges())
                if (edge.isEmpty())
                    closure.addAll(getClosure(edge.getNext()));
            closures.put(state, closure);
        }
        return closure;
    }

    private State getSetState(Set<State> set) {
        State state = mapping.get(set);
        if (state == null)
            mapping.put(set, state = new State());
        return state;
    }

    private static List<CharSet> getCharSets(Set<State> states) {
        List<CharSet> csets = new ArrayList<CharSet>();
        for (State state : states) {
            for (Edge edge : state.getEdges()) {
                if (edge.isEmpty())
                    continue;
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
        }
        return csets;
    }

    private static Set<State> move(Set<State> states, CharSet cset) {
        Set<State> result = new HashSet<State>();
        for (State state : states)
            for (Edge edge : state.getEdges())
                if (!edge.isEmpty() && edge.getCharSet().containsAny(cset))
                    result.add(edge.getNext());
        return result;
    }
}
