package com.achao.audit.metedata.expression;

/**
 * @author achao
 */
public class SpElMetadata implements IExpressionMetadata{

    private String springEl;

    private Object[] args;

    public String getSpringEl() {
        return springEl;
    }

    public void setSpringEl(String springEl) {
        this.springEl = springEl;
    }

    @Override
    public String getExpression() {
        return springEl;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
