package org.miod.tests;
import java.io.IOException;
import java.nio.file.FileSystems;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ProgramTestSuite {
    @Test
    public void t1Test() throws IOException {
        System.out.println("test1");
    }
}