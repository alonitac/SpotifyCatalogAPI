package com.example.catalog;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.example.catalog.utils.SpotifyUtils.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;



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
        assertFalse(isValidId("thisIDiswaytoolongtobevalidfmkhfkmh")); // too long ID (more than 30 characters)
        assertFalse(isValidId("!@#$$%^&*()_+")); // invalid characters
        assertFalse(isValidId("1234567890abcdefGHIJKLMNO!@#")); // includes invalid characters
    }
    @Test
    public void testValidURI() {
        // Valid Spotify URIs
        assertTrue(isValidURI("spotify:track:6rqhFgbbKwnb9MLmUQDhG6"));
        assertTrue(isValidURI("spotify:album:1a2B3c4D5e6F7g8H9iJkL0mN"));
        assertTrue(isValidURI("spotify:artist:a1b2C3d4E5f6G7h8I9jK0L1m2N"));
        assertTrue(isValidURI("spotify:playlist:6rqhFgbbKwnb9MLmUQDhG6"));
    }

    // Test case for invalid URIs
    @Test
    public void testInvalidURI() {
        // Invalid URIs
        assertFalse(isValidURI("spotify:track:")); // Empty identifier after "track"
        assertFalse(isValidURI("spotify:track:123")); // Too short ID
        assertFalse(isValidURI("spotify:album:123456789012345678901234567890123")); // Too long ID (over 30 characters)
        assertFalse(isValidURI("spotify:podcast:123456789012345")); // Invalid resource type
        assertFalse(isValidURI("track:6rqhFgbbKwnb9MLmUQDhG6")); // Missing "spotify:" prefix
        assertFalse(isValidURI("spotify:track:invalid#ID!")); // Invalid characters in ID
    }
    @Test
    public void testGetSpotifyClient_InvalidClientIdAndSecret() {
        // Invalid clientId and clientSecret (both null)
        assertThrows(IllegalArgumentException.class, () -> getSpotifyClient(null, null), "Invalid client ID or secret.");

        // Invalid clientId (null) and valid clientSecret
        assertThrows(IllegalArgumentException.class, () -> getSpotifyClient(null, "validSecret"), "Invalid client ID or secret.");

        // Invalid clientId (empty string) and valid clientSecret
        assertThrows(IllegalArgumentException.class, () -> getSpotifyClient("", "validSecret"), "Invalid client ID or secret.");

        // Valid clientId and invalid clientSecret (null)
        assertThrows(IllegalArgumentException.class, () -> getSpotifyClient("validClientId", null), "Invalid client ID or secret.");

        // Valid clientId and invalid clientSecret (empty string)
        assertThrows(IllegalArgumentException.class, () -> getSpotifyClient("validClientId", ""), "Invalid client ID or secret.");
    }

}
