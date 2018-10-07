package com.bbc.automower.main;

import com.bbc.automower.service.implementation.ParserServiceTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MainTest {
    
    private final Main main = new Main();

    @Before
    public void setUp() {
        TestAppender.clear();
    }

    @Test
    public void should_execute_automower_program() {
        // Given
        String[] args = new String[]{};
        
        // Action
        main.main(args);
        
        // Asserts
        assertThatMowersHavePrintTheirPositions();
    }
    
    @Test
    public void should_execute_automower_program_with_file() {
        // Given
        String[] args = new String[] {ParserServiceTest.GOOD_FILE_PATH};
        
        // Action
        main.main(args);
        
        // Asserts
        assertThatMowersHavePrintTheirPositions();
    }

    private void assertThatMowersHavePrintTheirPositions() {
        assertNotNull(TestAppender.messages);
        assertEquals(TestAppender.messages.size(), 2);
        assertEquals(TestAppender.messages.get(0), "1 3 N");
        assertEquals(TestAppender.messages.get(1), "5 1 E");
    }

}
