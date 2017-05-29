package com.strongerstartingnow.thymeleaf.dialect;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.thymeleaf.Arguments;
import org.thymeleaf.Configuration;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.attr.AbstractTextChildModifierAttrProcessor;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;

/** Format dateTime for blog **/
public class BlogDate extends AbstractTextChildModifierAttrProcessor {

	private String dateTime;
	
	protected BlogDate() {
		super("blogdate");
	}
	
	@Override
	protected String getText(Arguments arguments, Element element, String attributeName) {
		String val = element.getAttributeValue(attributeName);
		 
		final Configuration configuration = arguments.getConfiguration();
		/*
         * Obtain the Thymeleaf Standard Expression parser
         */
        final IStandardExpressionParser parser =
                StandardExpressions.getExpressionParser(configuration);

        /*
         * Parse the attribute value as a Thymeleaf Standard Expression
         */
        final IStandardExpression expression =
                parser.parseExpression(configuration, arguments, val);

        /*
         * Execute the expression just parsed
         */
        final String parsedExpression = (String)expression.execute(configuration, arguments);
        this.dateTime = parsedExpression;
        
        process();
        return "Posted " + dateTime;
	}

	/** Format date	 * */
	private void process() {
		LocalDateTime dt = LocalDateTime.parse(dateTime);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy 'at' h':'mm' 'a");
		dateTime = dt.format(formatter);
	}


	@Override
	public int getPrecedence() {
		return 0;
	}
	

}
