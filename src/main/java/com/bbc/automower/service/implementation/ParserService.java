package com.bbc.automower.service.implementation;

import com.bbc.automower.domain.Lawn;
import com.bbc.automower.domain.Mower;
import com.bbc.automower.enumeration.Instruction;
import com.bbc.automower.enumeration.Orientation;
import com.bbc.automower.service.IParserService;
import com.google.inject.Singleton;
import io.vavr.API;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.Value;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import io.vavr.control.Validation;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.bbc.automower.error.Error.*;
import static io.vavr.API.*;
import static io.vavr.control.Validation.combine;
import static io.vavr.control.Validation.sequence;
import static java.lang.Integer.parseInt;
import static java.util.function.Function.identity;
import static org.apache.commons.io.IOUtils.LINE_SEPARATOR;

@Singleton
@Slf4j
public class ParserService implements IParserService {

    private static final Validation<Seq<String>, Lawn> INVALID_EMPTY_FILE = Invalid(Seq(EMPTY_FILE.text()));

    @Override
    public Validation<Seq<String>, Lawn> parse(@NonNull final String filePath) {
        log.debug("Parsing file {}......", filePath);
        return readFile(filePath)
                .mapError(API::Seq)
                .flatMap(this::parseLines);
    }

    private Validation<Seq<String>, Lawn> parseLines(final List<String> lines) {
        return lines.isEmpty() ?
                INVALID_EMPTY_FILE :
                lawn(1, lines.get(0))
                        .flatMap(lawn -> initialize(lawn, lines));
    }

    private Validation<Seq<String>, Lawn> initialize(final Lawn lawn, final List<String> lines) {
        return sequence(lines
                .zipWithIndex()
                .drop(1)
                .grouped(2)
                .map(this::mowerWithInstructions))
                .map(Value::toList)
                .map(lawn::initialize);
    }

    private Validation<Seq<String>, Lawn> lawn(final int lineNumber, final String line) {
        return validateLength(lineNumber, line, 2)
                .mapError(API::Seq)
                .flatMap(elts -> combine(
                        validateInt(lineNumber, elts[0]),
                        validateInt(lineNumber, elts[1])
                ).ap(Lawn::of));
    }

    private Validation<Seq<String>, Mower> mowerWithInstructions(final List<Tuple2<String, Integer>> tuples) {
        return assertSize(tuples, 2)
                .mapError(API::Seq)
                .flatMap(t ->
                        combine(
                                mower(t._1, t._2),
                                instructions(t._1 + 1, t._3)
                        ).ap(Mower::instructions)
                                .fold(
                                        errors -> Invalid(errors.flatMap(identity())),
                                        API::Valid
                                ));
    }

    private Validation<Seq<String>, Mower> mower(final int lineNumber, final String line) {
        return validateLength(lineNumber, line, 3)
                .mapError(API::Seq)
                .flatMap(elts -> combine(
                        validateInt(lineNumber, elts[0]),
                        validateInt(lineNumber, elts[1]),
                        orientation(lineNumber, elts[2])
                ).ap(Mower::of));
    }

    private Validation<Seq<String>, List<Instruction>> instructions(final int lineNumber, final String s) {
        return sequence(CharSeq(s)
                .map(c ->
                        instruction(lineNumber, c)
                                .mapError(API::Seq)))
                .map(Value::toList);
    }

    private Validation<String, Instruction> instruction(final int lineNumber, final char c) {
        return Instruction.getByLabel(c)
                .toValid(() -> INVALID_INSTRUCTION.text(lineNumber, c));
    }

    private Validation<String, Orientation> orientation(int lineNumber, String s) {
        return Orientation.getByLabel(s)
                .toValid(() -> INVALID_ORIENTATION.text(lineNumber, s));
    }

    private Validation<String, Tuple3<Integer, String, String>> assertSize(final List<Tuple2<String, Integer>> list, final int size) {
        return list.size() == size ?
                Valid(Tuple(list.get(0)._2 + 1, list.get(0)._1, list.get(1)._1)) :
                Invalid(INVALID_SIZE_LIST.text(list, size));
    }

    private Validation<String, String[]> validateLength(final int lineNumber, final String line, final int expected) {
        String[] elements = line.split("\\s+");
        return elements.length == expected ?
                Valid(elements) :
                Invalid(INVALID_LENGTH.text(lineNumber, elements.length, expected));
    }

    private Validation<String, Integer> validateInt(final int lineNumber, final String s) {
        return Try(() -> parseInt(s))
                .toValid(() -> INVALID_INT.text(lineNumber, s));
    }

    private Validation<String, List<String>> readFile(final String filePath) {
        return Try(() -> getInputStream(filePath))
                .mapTry(inputStream -> List(
                        IOUtils.toString(inputStream)
                                .split(LINE_SEPARATOR)))
                .toValid(() -> FILE_NOT_FOUND.text(filePath));
    }

    private InputStream getInputStream(final String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        return file.exists() ?
                new FileInputStream(file) :
                Option.of(ParserService.class
                        .getClassLoader()
                        .getResourceAsStream(filePath))
                        .getOrElseThrow(() -> new FileNotFoundException("File " + filePath + " not found"));
    }

}
