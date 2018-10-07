package com.bbc.automower.main;

import com.bbc.automower.service.IParserService;
import com.bbc.automower.service.implementation.ParserService;
import com.google.inject.AbstractModule;

public class AppInjector extends AbstractModule {
    
    @Override 
    protected void configure() {
        bind(IParserService.class).to(ParserService.class);
    }

}
