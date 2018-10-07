package com.bbc.automower.domain;

import com.bbc.automower.enumeration.Instruction;
import com.bbc.automower.enumeration.Orientation;
import io.vavr.collection.List;
import lombok.*;
import lombok.EqualsAndHashCode.Include;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import static com.bbc.automower.enumeration.Orientation.*;
import static io.vavr.API.*;
import static java.util.UUID.randomUUID;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Slf4j
public class Mower implements Movable<Mower> {

    @Include
    UUID uuid;
    Position position;
    Orientation orientation;
    List<Instruction> instructions;

    public static Mower of(final int x, final int y, final Orientation orientation) {
        return new Mower(x, y, orientation);
    }

    private Mower(final int x, final int y, final Orientation orientation) {
        uuid = randomUUID();
        position = Position.of(x, y);
        this.orientation = orientation;
        instructions = List();
    }

    public Mower instructions(final List<Instruction> instructions) {
        return Mower.builder()
                .uuid(uuid)
                .orientation(orientation)
                .position(position)
                .instructions(this.instructions.appendAll(instructions))
                .build();
    }

    public Mower executeNextInstruction() {
        Mower mower = instructions.head()
                .apply(this);

        return Mower.builder()
                .uuid(mower.uuid)
                .orientation(mower.orientation)
                .position(mower.position)
                .instructions(mower.instructions.pop())
                .build();
    }

    @Override
    public Mower turnRight() {
        return Mower.builder()
                .uuid(uuid)
                .orientation(orientation.right())
                .position(position)
                .instructions(instructions)
                .build();
    }

    @Override
    public Mower turnLeft() {
        return Mower.builder()
                .uuid(uuid)
                .orientation(orientation.left())
                .position(position)
                .instructions(instructions)
                .build();
    }

    @Override
    public Mower goForward() {
        return Mower.builder()
                .uuid(uuid)
                .orientation(orientation)
                .instructions(instructions)
                .position(
                        Match(orientation).of(
                                Case($(EAST), position::incrementX),
                                Case($(SOUTH), position::decrementY),
                                Case($(NORTH), position::incrementY),
                                Case($(WEST), position::decrementX),
                                Case($(), position)))
                .build();
    }

    public Mower printPosition() {
        log.info("{} {} {}", position.getX(), position.getY(), orientation.getLabel());
        return this;
    }

}
