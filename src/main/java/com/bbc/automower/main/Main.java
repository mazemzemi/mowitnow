package com.bbc.automower.main;

import com.bbc.automower.domain.Lawn;
import com.bbc.automower.service.IParserService;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import static com.google.inject.Guice.createInjector;
import static java.util.Objects.nonNull;

@Slf4j
public class Main {

    private final static String DEFAULT_FILE_PATH = "META-INF/config/automower.txt";

    @Inject
    private IParserService parserService;

    public static void main(final String[] args) {
        createInjector(new AppInjector())
                .getInstance(Main.class)
                .run(getFilePath(args));
    }

    private void run(final String filename) {
        parserService
                .parse(filename)
                .map(Lawn::execute)
                .swap()
                .forEach(errors -> errors.forEach(log::error));
    }

    private static String getFilePath(final String[] args) {
        return nonNull(args) && args.length == 1 ?
                args[0] :
                DEFAULT_FILE_PATH;
    }

}
