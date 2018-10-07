package com.bbc.automower.service;

import com.bbc.automower.domain.Lawn;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public interface IParserService {

    Validation<Seq<String>, Lawn> parse(final String filePath);

}
