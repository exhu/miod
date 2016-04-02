package org.miod.tests;
import java.io.IOException;
import java.nio.file.FileSystems;
import org.miod.program.GlobalSymbolTable;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ProgramTestSuite {
    @Test
    public void globalSymbolTable() throws IOException {
        final String unitSystemName = "System";
        final String unitMyName = "my::My";
        final String unitMy2Name = "my::My2";
        GlobalSymbolTable systemTab = new GlobalSymbolTable(unitSystemName);
        GlobalSymbolTable myTab = new GlobalSymbolTable(unitMyName);
        GlobalSymbolTable myTab2 = new GlobalSymbolTable(unitMy2Name);
        myTab.addImport(systemTab, false);
        myTab.addImport(myTab2, true);
        
        
    }
}