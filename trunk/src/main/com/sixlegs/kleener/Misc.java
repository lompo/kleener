package com.sixlegs.kleener;

import java.util.*;

class Misc
{
    public static Set<State> getStates(FA fa) {
        Set<State> states = new HashSet<State>();
        List<State> q = new ArrayList<State>();
        q.add(fa.getStart());
        while (!q.isEmpty()) {
            State state = q.remove(q.size() - 1);
            states.add(state);
            for (Edge edge : state.getEdges())
                if (!states.contains(edge.getNext()))
                    q.add(edge.getNext());
        }
        return states;
    }

    public static String format(Collection<CharSet> csets) {
        if (csets == null)
            return "EMPTY";
        List<String> temp = new ArrayList<String>();
        for (CharSet cset : csets)
            temp.add("\"" + escapeStringLiteral(cset.toString()) + "\"");
        return temp.toString();
    }

    public static String escapeChars(String value, CharSet cset)
    {
        StringBuilder sb = new StringBuilder();
        char[] chars = value.toCharArray();
        for (int i = 0, size = chars.length; i < size; i++) {
            char ch = chars[i];
            if (cset.contains(ch))
                sb.append("\\");
            sb.append(ch);
        }
        return sb.toString();
    }
    
    public static String escapeStringLiteral(String value)
    {
        StringBuilder sb = new StringBuilder();
        char[] chars = value.toCharArray();
        for (int i = 0, size = chars.length; i < size; i++) {
            char ch = chars[i];
            switch (ch) {
            case '\n': sb.append("\\n"); break;
            case '\r': sb.append("\\r"); break;
            case '\b': sb.append("\\b"); break;
            case '\f': sb.append("\\f"); break;
            case '\t': sb.append("\\t"); break;
            case '\"': sb.append("\\\""); break;
            case '\\': sb.append("\\\\"); break;
            default:
                sb.append(ch);
            }
        }
        return sb.toString();
    }
}
