package com.bbc.automower.main;

import com.bbc.automower.service.implementation.ParserServiceTest;
import org.junit.Before;
import org.junit.Test;

import static com.bbc.automower.main.Main.main;
import static com.bbc.automower.main.TestAppender.clear;
import static com.bbc.automower.main.TestAppender.messages;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MainTest {

    @Before
    public void setUp() {
        clear();
    }

    @Test
    public void should_execute_automower_program() {
        // Given
        String[] args = new String[]{};
        
        // Action
        main(args);
        
        // Asserts
        assertThatMowersHavePrintedTheirPositions();
    }
    
    @Test
    public void should_execute_automower_program_with_file() {
        // Given
        String[] args = new String[] {ParserServiceTest.GOOD_FILE_PATH};
        
        // Action
        main(args);
        
        // Asserts
        assertThatMowersHavePrintedTheirPositions();
    }

    private void assertThatMowersHavePrintedTheirPositions() {
        assertNotNull(messages);
        assertEquals(messages.size(), 2);
        assertEquals(messages.get(0), "1 3 N");
        assertEquals(messages.get(1), "5 1 E");
    }

}
