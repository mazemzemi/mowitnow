package com.bbc.automower.enumeration;

import com.bbc.automower.domain.Mower;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.bbc.automower.enumeration.Instruction.*;
import static io.vavr.API.None;
import static io.vavr.API.Some;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InstructionTest {

    @Mock
    private Mower mower;

    @Test
    public void should_get_by_label() {
        assertEquals(getByLabel('F'), Some(FORWARD));
        assertEquals(getByLabel('R'), Some(RIGHT));
        assertEquals(getByLabel('L'), Some(LEFT));
        assertEquals(getByLabel('X'), None());
    }

    @Test
    public void should_execute_go_forward_when_forward() {
        //Action
        FORWARD.apply(mower);

        //Verify
        verify(mower).goForward();
    }

    @Test
    public void should_execute_turn_right_when_right() {
        //Action
        RIGHT.apply(mower);

        //Verify
        verify(mower).turnRight();
    }

    @Test
    public void should_execute_turn_left_when_left() {
        //Action
        LEFT.apply(mower);

        //Verify
        verify(mower).turnLeft();
    }

}