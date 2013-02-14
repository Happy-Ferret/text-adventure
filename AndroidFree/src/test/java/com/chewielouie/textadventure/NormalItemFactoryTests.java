package com.chewielouie.textadventure;

import static org.junit.Assert.*;

import org.junit.Test;

public class NormalItemFactoryTests {

    @Test
    public void produces_NormalItem_objects() {
        Item item = new NormalItemFactory().create();
        assertTrue( item instanceof NormalItem );
    }
}

