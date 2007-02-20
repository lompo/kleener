package com.sixlegs.kleener;

import java.util.*;

final class State
{
    volatile private static int ids;
    private Data data = new Data();

    private static class Data
    {
        int id = ids++;
        List<Edge> edges = new ArrayList<Edge>();
    }

    public static State merge(State a, State b) {
        b.data.edges.addAll(a.data.edges);
        a.data = b.data;
        return a;
    }

    public List<Edge> getEdges() {
        return Collections.unmodifiableList(data.edges);
    }
    
    public void addEdge(Edge edge) {
        data.edges.add(edge);
    }

    public boolean equals(Object o) {
        if (!(o instanceof State))
            return false;
        return data.equals(((State)o).data);
    }

    public int hashCode() {
        return data.hashCode();
    }

    public String toString() {
        return String.valueOf(data.id);
    }
}
