package com.example.catalog;

import com.example.catalog.utils.LRUCache;
import com.jayway.jsonpath.spi.cache.Cache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LRUCacheTest {

    LRUCache<String, String> cache;

    @Nested
    @DisplayName("when instantiated with capacity 3")
    class WhenInstantiated {

        @BeforeEach
        void createNewCache() {
            cache = new LRUCache<>(3);
        }

        @Test
        @DisplayName("cache is initially empty")
        void isEmpty() {
            assertTrue(cache.isEmpty(), "Cache should be empty initially");
        }

        @Test
        @DisplayName("throws NullPointerException when getting a null key")
        void throwsExceptionWhenGettingNullKey() {

            // Act & Assert: Verify that NullPointerException is thrown
            assertThrows(NullPointerException.class, () -> cache.get(null),
                    "Cache should throw NullPointerException when getting a null key");
        }


        @Nested
        @DisplayName("after adding 2 elements")
        class AfterAdding2Elements {

            @BeforeEach
            void addElements() {
                cache.set("k1","v1");
                cache.set("k2","v2");
            }

            @Test
            @DisplayName("cache contains the added elements")
            void containsAddedElements() {
                cache.get("k1");
                cache.get("k2");
            }
        }

        @Nested
        @DisplayName("after adding 3 elements")
        class AfterAdding3Elements {

            @BeforeEach
            void addElements() {
                cache.set("k1","v1");
                cache.set("k2","v2");
                cache.set("k3","v3");

            }

            @Nested
            @DisplayName("when cleared")
            class WhenCleared {

                // addElements (in AfterAdding3Elements) is executed and then clearCache
                // before EACH test case in WhenCleared


                @BeforeEach
                void clearCache() {
                    cache.clear();
                }
            }
        }

    }
}
