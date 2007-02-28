package com.sixlegs.kleener;

import static com.sixlegs.kleener.Expression.*;

class ExpressionParser
{
    public Expression parse(String regex, int flags) {
        // TODO
        return paren(literal(regex), 0);
    }
}
