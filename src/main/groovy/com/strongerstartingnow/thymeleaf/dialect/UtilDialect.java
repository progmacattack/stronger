package com.strongerstartingnow.thymeleaf.dialect;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.processor.IProcessor;

public class UtilDialect extends AbstractDialect {

	@Override
	public String getPrefix() {
		return "util";
	}
	
	@Override
	public Set<IProcessor> getProcessors() {
		final Set<IProcessor> processors = new HashSet<>();
		processors.add(new RoundToFive());
		processors.add(new BlogDate());
		return processors;
	}

}
