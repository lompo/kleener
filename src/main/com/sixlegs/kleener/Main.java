package com.sixlegs.kleener;

import static com.sixlegs.kleener.Expression.*;

public class Main
{
    // sample from wikipedia
    // "(public|private|protected|)\s*(\w+)\s+(\w+)\s*\("

    public static void main(String[] args) throws Exception {
        Expression e =
            concat(repeat(or(literal("public"),
                             literal("private"),
                             literal("protected")), 0, 1),
                   repeat(space(), 0, 0),
                   repeat(alnum(), 1, 0),
                   repeat(space(), 1, 0),
                   repeat(alnum(), 1, 0),
                   repeat(space(), 0, 0),
                   literal("("));

        String str = "public  int main(";
        System.err.println(e.compile(Pattern.Type.NFA).matches(str));
        System.err.println(e.compile(Pattern.Type.DFA).matches(str));
        System.err.println(e.compile(Pattern.Type.DFA_PRECOMPUTE).matches(str));
        System.err.println(e.compile(Pattern.Type.DFA_OPTIMIZE).matches(str));
        System.err.println(e.compile(Pattern.Type.DFA_COMPILE).matches(str));
    }

    private static Expression space() {
        return literal(new CharSet(" \t\n\r\f\n"));
    }
    
    private static Expression alnum() {
        return literal(new CharSet("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"));
    }
}
