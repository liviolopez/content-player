package com.liviolopez.contentplayer.utils

import org.junit.Assert.*
import org.junit.Test

/** Test moved to instrumentation test
 * Code item to search
 */
class StringUtilsTest {
    @Test
    fun `code item to search is only number __ return true`(){
        val result = StringUtils.onlyNumber("123456")
        assertTrue(result)
    }

    @Test
    fun `code item to search is only number _ letter __ return false`(){
        val result = StringUtils.onlyNumber("123A456")

        assertFalse(result)
    }

    @Test
    fun `code item to search is only number _ character __ return false`(){
        val result = StringUtils.onlyNumber("$")

        assertFalse(result)
    }
}