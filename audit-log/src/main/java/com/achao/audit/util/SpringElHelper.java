package com.achao.audit.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author achao
 */
public class SpringElHelper implements BeanFactoryAware {

    private static final TemplateParserContext PARSER_CONTEXT = new TemplateParserContext();

    private static final SpelExpressionParser PARSER = new SpelExpressionParser();

    private final StandardEvaluationContext evaluationContext = new StandardEvaluationContext();

    private BeanFactory beanFactory;
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        //填充evaluationContext对象的`BeanFactoryResolver`。
        this.evaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
    }

    public String spElRender(String springEl, Object[] args) {
        String value = springEl;
        if (this.beanFactory != null && this.beanFactory instanceof ConfigurableBeanFactory) {
            value = ((ConfigurableBeanFactory) this.beanFactory).resolveEmbeddedValue(springEl);
        }
        evaluationContext.setVariable("args", args);
        Expression expression = PARSER.parseExpression(value, PARSER_CONTEXT);
        return expression.getValue(evaluationContext, String.class);
    }
}
