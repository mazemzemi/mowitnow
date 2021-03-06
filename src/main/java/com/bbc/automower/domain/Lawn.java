package com.bbc.automower.domain;

import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.control.Validation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Predicate;

import static com.bbc.automower.error.Error.OCCUPIED_POSITION;
import static com.bbc.automower.error.Error.OUTSIDE_POSITION;
import static io.vavr.API.*;


@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class Lawn {

    int width;
    int height;
    List<Mower> mowers;

    public static Lawn of(final int width, final int height) {
        return new Lawn(width, height);
    }

    private Lawn(final int width, final int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Arguments must be >= 0");
        }

        this.width = width;
        this.height = height;
        this.mowers = List();
    }

    public Lawn initialize(final List<Mower> mowers) {
        return Lawn.builder()
                .width(width)
                .height(height)
                .mowers(this.mowers.appendAll(mowers))
                .build();
    }

    public Lawn execute() {
        return mowers.foldLeft(
                this,
                (l,m) ->  l.executeInstructions(m)
                        .map2(Mower::printPosition)
                        ._1
        );
    }

    private Tuple2<Lawn, Mower> executeInstructions(final Mower mower) {
        return mower.executeInstruction()
                .map(mower1 -> validatePosition(mower1).fold(
                        err -> {
                            log.warn(err);
                            return Tuple(this, mower.removeInstruction());
                        },
                        mower2 -> Tuple(move(mower2), mower2)
                ))
                .map(lawnAndMower -> lawnAndMower._1.executeInstructions(lawnAndMower._2))
                .getOrElse(() -> Tuple(this, mower));
    }

    private Lawn move(final Mower mower) {
        return Lawn.builder()
                .width(width)
                .height(height)
                .mowers(mowers.replace(mower, mower))
                .build();
    }

    private Validation<String, Mower> validatePosition(final Mower mower) {
        return insideGrillPosition(mower)
                .flatMap(this::availablePosition);
    }

    private Validation<String, Mower> insideGrillPosition(final Mower mower) {
        Position position = mower.getPosition();
        return position.getX() >= 0 && position.getX() <= width
                && position.getY() >= 0 && position.getY() <= height ?
                Valid(mower) :
                Invalid(OUTSIDE_POSITION.text(mower.getUuid(), mower.getPosition()));
    }

    private Validation<String, Mower> availablePosition(final Mower mower) {
        Predicate<Mower> equalsToMower = mower::equals;

        return mowers
                .filter(equalsToMower.negate()) //Java 11 : use Predicate.not(mower::equals)
                .map(Mower::getPosition)
                .contains(mower.getPosition()) ?
                Invalid(OCCUPIED_POSITION.text(mower.getUuid(), mower.getPosition())) :
                Valid(mower);
    }

}
