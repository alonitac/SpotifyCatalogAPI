package com.example.catalog;

import com.example.catalog.utils.SpotifyUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.example.catalog.utils.SpotifyUtils.*;
import static org.junit.jupiter.api.Assertions.*;


public class SpotifyUtilsTest {

    @Test
    public void testValidId() {
        assertTrue(isValidId("6rqhFgbbKwnb9MLmUQDhG6")); // valid Spotify ID
        assertTrue(isValidId("1a2B3c4D5e6F7g8H9iJkL0mN")); // valid 22 character ID
        assertTrue(isValidId("a1b2C3d4E5f6G7h8I9jK0L1m2N")); // valid 30 character ID
    }

    @Test
    public void testInvalidId() {
        assertFalse(isValidId(null)); // null ID
        assertFalse(isValidId("")); // empty ID
        assertFalse(isValidId("shortID")); // too short ID (less than 15 characters)
        assertFalse(isValidId("AAAAAAAthisIDiswaytoolongtobevalid")); // too long ID (more than 30 characters)
        assertFalse(isValidId("!@#$$%^&*()_+")); // invalid characters
        assertFalse(isValidId("1234567890abcdefGHIJKLMNO!@#")); // includes invalid characters
    }

    @Test
    public void testInValidURI() {
        assertFalse(isValidURI(null));; // URI
        assertFalse(isValidURI("")); // empty URI
        assertFalse(isValidURI("spotify:playlist:1234")); // ID to short
    }

    @Test
    public void testValidURI() {
        assertTrue(isValidURI("spotify:track:1234567890abcdef1234"));; // True
        assertTrue(isValidURI("spotify:album:abcdefghij1234567890")); // True

    }

    @Test
    public void InValidGetSpotifyClientParams() {
        assertThrows(IllegalArgumentException.class,() ->getSpotifyClient(null, "11554592"),"Illegal client id - null");
        assertThrows(IllegalArgumentException.class,() ->getSpotifyClient("", "11554592"),"Illegal client id - empty");
        assertThrows(IllegalArgumentException.class,() ->getSpotifyClient("ClientId1123", null),"Illegal client secret - null");
        assertThrows(IllegalArgumentException.class,() ->getSpotifyClient("ClientId1123", ""),"Illegal client secret - empty");

    }

}
