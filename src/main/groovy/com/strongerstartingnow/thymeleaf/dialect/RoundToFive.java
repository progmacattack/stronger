package com.strongerstartingnow.thymeleaf.dialect;

import org.thymeleaf.Arguments;
import org.thymeleaf.Configuration;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.attr.AbstractTextChildModifierAttrProcessor;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;

/** Round a number to the nearest five and place the result as a text node of the subject element
 * Call from thymeleaf template using dialectName: roundtofive-text **/
public class RoundToFive extends AbstractTextChildModifierAttrProcessor {

	private Double numberToRound;
	private Integer numberRounded;
	
	protected RoundToFive() {
		super("roundtofive-text");
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
        final Double parsedExpression = (Double)expression.execute(configuration, arguments);
        this.numberToRound = parsedExpression;
        
        roundIt();
        return numberRounded.toString();
	}

	//round the member variable to nearest five
	private void roundIt() {
		Long rounded = Math.round(numberToRound / 5) * 5;
		this.numberRounded = Integer.valueOf(rounded.intValue());
	}


	@Override
	public int getPrecedence() {
		return 0;
	}
	

}
