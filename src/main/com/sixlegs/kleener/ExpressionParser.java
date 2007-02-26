package com.sixlegs.kleener;

import static com.sixlegs.kleener.Expression.*;

class ExpressionParser
{
    public Expression parse(String regex) {
        // TODO
        return paren(literal(regex), 0);
    }
}
