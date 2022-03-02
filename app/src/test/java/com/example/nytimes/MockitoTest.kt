package com.example.nytimes

import org.junit.Before
import org.mockito.MockitoAnnotations

/**
 * Base class to support mocking
 * in tests.
 */
abstract class MockitoTest {

    @Before
    open fun setup() {
        MockitoAnnotations.initMocks(this)
    }
}