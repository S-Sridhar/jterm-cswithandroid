/*
 *  Copyright 2016 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.google.engedu.anagrams;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

import android.text.TextUtils;
import android.util.Log;

import java.util.List;


/**
 * Tests for AnagramDictionary
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class AnagramDictionaryTest {
    @Before
    public void beforeEach() {
        PowerMockito.mockStatic(Log.class);
    }

    @Test
    public void testAddition() {
        assertEquals(3, 1 +2);
    }

    @Test
    public void testSortLetters() {
        assertEquals(AnagramDictionary.sortLetters("a"), "a");
        assertEquals("opst", AnagramDictionary.sortLetters("stop"));
        assertEquals("abcdefg", AnagramDictionary.sortLetters("abcdefg"));
        assertEquals("", AnagramDictionary.sortLetters(""));
    }

    @Test
    public void testIsAnagram() {
        assertTrue(AnagramDictionary.isAnagram("a", "a"));
        assertTrue(AnagramDictionary.isAnagram("post", "stop"));
        assertTrue(AnagramDictionary.isAnagram("act", "cat"));
        assertFalse(AnagramDictionary.isAnagram("potato", "potata"));
        assertFalse(AnagramDictionary.isAnagram("cat", "cats"));
    }

    @Test
    public void testIsGoodWord() {
       // TODO: This may need to be in AndroidTest

    }

    @Test
    public void testGetAnagrams() {
        String[] strings = {"act", "cat", "cats"};
        AnagramDictionary dict = new AnagramDictionary(strings);
        List<String> anagrams = dict.getAnagrams("act");
        assertTrue(anagrams.contains("cat"));
    }
}
