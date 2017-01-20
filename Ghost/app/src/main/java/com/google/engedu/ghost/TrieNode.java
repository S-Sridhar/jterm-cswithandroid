/* Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.StringTokenizer;

public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;
    private Random mRandom;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
        mRandom = new Random();
    }

    public void add(String s) {
        if(s.length() == 0) { /** base case, finished adding the word */
            isWord = true;
            return;
        }
        String firstChar = s.substring(0,1);
        String remaining = s.substring(1); // Substring with one param automatically goes to the end
        if(children.containsKey(firstChar)) {
            //IF the character is already in the children map
            // Then I can ask the child at that character to add the remaining string suffix
            children.get(firstChar).add(remaining);
        } else {
            // IT doesn't exist in the children, so need to insert it to the character map
            TrieNode newNode = new TrieNode();
            children.put(firstChar, newNode);
        }
    }

    public boolean isWord(String s) {
        if(s.length() == 0){
            return isWord;
        }
        // Find out if we have a child with s's prefix and that child.isWords(s's suffix)
        String firstChar = s.substring(0,1);
        String remaining = s.substring(1);
        if(children.containsKey(firstChar)){
            return children.get(firstChar).isWord(remaining);
        }
        // The prefix is not in the children list, so this is no a walid word.
        return false;
    }

    public String getAnyWordStartingWith(String s) {
        if(s== null) {
            // We have no prefix at all- s is null
            if (children.size() >0) {
                String nextChar = pickRandomChildChar();
                String nextSuffix = children.get(nextChar).getAnyWordStartingWith(null);
                return nextChar + nextSuffix;
            } else {
                return "";
            }
        }
        if(s.length() == 0){
            // If there is no prefix, return a random word!
            // if we have no children, we can return the empty string if we are a word, or null
            if(children.size() ==0){
                if(isWord){
                    // We are a word!
                    return "";
                } else {
                    return null;
                }
            } else if (children.size() >0) {
                String childKey = pickRandomChildChar();
                String nextRemaining = children.get(childKey).getAnyWordStartingWith("");
                return  childKey + nextRemaining;
            }
        } else  {
            // s is not a emty string
            // retrun a word that starst with the first letter of s
            String firstChar = s.substring(0,1);
            String remaining = s.substring(1);
            if(children.containsKey(firstChar)){
                // We can delegate the problem to our children
                String word = children.get(firstChar).getAnyWordStartingWith(remaining);
                if(word == null){
                    // our children don't have any words starting with remaining
                    return null;
                } else {
                    return firstChar += word;
                }
            } else {
                // We are looking for a prefix that is not in our dictionary
                return null;
            }
        }
        return null;

    }

    private String pickRandomChildChar() {
        //Find a random key in our hashmap
        int index = mRandom.nextInt(children.size());
        int reached = 0;
        //Assume the children's order doesn't change after creation in their set
        for(String s: children.keySet()){
            if(index == reached) {
             return s;
            }
            reached++;
        }
        return null;
    }

    public String getGoodWordStartingWith(String s) {
        return null;
    }
}