/*
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package basic;

import online.nostrium.utils.deployment.BoMconverter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 
 * This is a fake junit test so that we can automate
 * the BoM generation inside the docs folder.
 * 
 * @author Brito
 * @date: 2024-09-28
 * @location: Germany
 */
public class GenerateBoMTest {
    
    public GenerateBoMTest() {
    }

    @Test
    public void generateBoM() {
        BoMconverter.main(null);
    }
}
