package com.sixlegs.kleener;

public class Main
{
    // sample from wikipedia
    // "(public|private|protected|)\s*(\w+)\s+(\w+)\s*\("
    
    public static void main(String[] args) throws Exception {
        Expression space = new CharClassExp(" \t\n\r\f\n", false);
        Expression alnum = CharClassExp.parse("A-Za-z0-9");
        Expression e =
            new CatExp(Expression.repeat(new OrExp(new StringExp("public"),
                                                   new StringExp("private"),
                                                   new StringExp("protected")), 0, 1),
                       Expression.repeat(space, 0, 0),
                       Expression.repeat(alnum, 1, 0),
                       Expression.repeat(space, 1, 0),
                       Expression.repeat(alnum, 1, 0),
                       Expression.repeat(space, 0, 0),
                       new StringExp("("));
        System.err.println(e);
        DFA dfa = e.getNFA().getDFA().getMinimizedDFA();
        System.err.println(dfa.matches("public int main("));
        Pattern compiled = dfa.compile();
        System.err.println(compiled.matches("public int main("));
    }

    // "^(([^:]+)://)?([^:/]+)(:([0-9]+))?(/.*)"
    // "(([^:]+)://)?([^:/]+)(:([0-9]+))?(/.*)"
    // "usd [+-]?[0-9]+.[0-9][0-9]"
    // "\\b(\\w+)(\\s+\\1)+\\b"
}
