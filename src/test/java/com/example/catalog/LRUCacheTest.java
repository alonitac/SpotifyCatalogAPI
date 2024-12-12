package com.example.catalog;

import com.example.catalog.utils.LRUCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
            assertEquals(0, cache.size(), "Cache should be empty initially");


            // TODO assert cache is empty V
        }

        @Test
        @DisplayName("throws NullPointerException when getting a null key")
        void throwsExceptionWhenGettingNullKey() {
            assertThrows(NullPointerException.class,()-> {cache.get(null);});
            // TODO assert NullPointerException thrown on `cache.get(null)` V
        }

        @Nested
        @DisplayName("after adding 2 elements")
        class AfterAdding2Elements {

            @BeforeEach
            void addElements() {
                cache.set("k1","v1");
                cache.set("k2","v2");

                // TODO add 2 elements V
            }

            @Test
            @DisplayName("cache contains the added elements")
            void containsAddedElements() {
                assertTrue(cache.get("k1")!=null);
                assertTrue(cache.get("k2")!=null);

                // TODO assert the added 2 elements are available V
            }
        }

        @Nested
        @DisplayName("after adding 3 elements")
        class AfterAdding3Elements {

            @BeforeEach
            void addElements() {
                cache.set("k3","v3");
                cache.set("k4","v4");
                cache.set("k5","v5");


                // TODO add 3 elements V
            }

            @Nested
            @DisplayName("when cleared")
            class WhenCleared {
                @Test
                @DisplayName("should be empty after clearing")
                void shouldBeEmptyAfterClearing() {
                    // Assert that the cache is empty after clearing
                    assertTrue(cache.get("k3")==null);  // k3 should be null as it was cleared
                    assertTrue(cache.get("k4")==null);  // k4 should be null as it was cleared
                    assertTrue(cache.get("k5")==null);  // k5 should be null as it was cleared
                }
                @Test
                @DisplayName("should not contain any elements after clearing")
                void shouldNotContainAnyElements() {
                    // Assert that the cache is empty after clearing (even if you add an element after clearing)
                    cache.set("k6", "v6");
                    assertTrue(cache.get("k6")!=null);  // k6 should now be in the cache
                    assertTrue(cache.get("k3")==null);    // k3 should still be null
                }

                // addElements (in AfterAdding3Elements) is executed and then clearCache
                // before EACH test case in WhenCleared


                @BeforeEach
                void clearCache() {
                    cache.clear();
                    // TODO clear the cache after V
                }
            }
        }

    }
}
