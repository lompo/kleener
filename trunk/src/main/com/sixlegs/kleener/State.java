package com.sixlegs.kleener;

final class State
{
    public static final State MATCH = new State(null, null, null);
    
    volatile private static int ids;
    private final int id = ids++;

    private final CharSet cset;
    private final Edge edge1;
    private final Edge edge2;

    public State(CharSet cset, Edge edge1) {
        this(cset, edge1, null);
        assert cset != null && edge1 != null;
    }

    public State(Edge edge1, Edge edge2) {
        this(null, edge1, edge2);
        assert edge1 != null && edge2 != null;
    }
    
    private State(CharSet cset, Edge edge1, Edge edge2) {
        this.cset = cset;
        this.edge1 = edge1;
        this.edge2 = edge2;
    }

    public CharSet getCharSet() {
        return cset;
    }

    public State getState1() {
        return (edge1 != null) ? edge1.getState() : null;
    }

    public State getState2() {
        return (edge2 != null) ? edge2.getState() : null;
    }
    
    public String toString() {
        return String.valueOf(id);
    }
}
