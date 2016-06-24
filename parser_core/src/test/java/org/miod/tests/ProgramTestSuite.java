package org.miod.tests;
import org.miod.parser.ErrorReporter;
import org.miod.program.GlobalSymbolTable;
import org.miod.program.SymItem;
import org.miod.program.SymKind;
import org.miod.program.SymVisibility;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ProgramTestSuite {
    @Test
    public void globalSymbolTable() {
        ErrorReporter reporter = new ErrorReporter();
        final String unitSystemName = "System";
        final String unitMyName = "my::My";
        final String unitMy2Name = "my::My2";
        final String unitMy3Name = "my::My3";
        GlobalSymbolTable systemTab = new GlobalSymbolTable(unitSystemName,
            reporter);
        GlobalSymbolTable myTab1 = new GlobalSymbolTable(unitMyName, reporter);
        GlobalSymbolTable myTab2 = new GlobalSymbolTable(unitMy2Name, reporter);
        GlobalSymbolTable myTab3 = new GlobalSymbolTable(unitMy3Name, reporter);
        myTab1.addImport(systemTab, false);
        myTab1.addImport(myTab2, true);
        myTab2.addImport(systemTab, false);
        myTab2.addImport(myTab3, false);
        SymItem sym1 = new SymItem("sym1", SymKind.Var, SymVisibility.Public, null);
        // the same name, to check name collision
        SymItem sym2 = new SymItem("sym1", SymKind.Var, SymVisibility.Public, null);
        SymItem sym3 = new SymItem("sym3", SymKind.Var, SymVisibility.Public, null);
        SymItem symSystem = new SymItem("system", SymKind.Var, SymVisibility.Public, null);
        systemTab.put(symSystem);
        myTab1.put(sym1);
        myTab2.put(sym2);
        myTab3.put(sym3);
        
        assertNotNull(myTab3.resolve("sym3"));
        assertNull(myTab1.resolve("sym3"));
        assertNotNull(myTab1.resolve("sym1"));
        assertTrue(sym1 == myTab1.resolve("sym1"));
        assertTrue(sym2 == myTab1.resolve(unitMy2Name + "::sym1"));
        assertNotNull(myTab1.resolve(unitMy2Name + "::sym1"));
        assertNotNull(myTab2.resolve("system"));
        assertNotNull(myTab2.resolve("System::system"));
        assertEquals(GlobalSymbolTable.getParentNamespace("pak::sub::unit"),
                "pak::sub");
        assertEquals(GlobalSymbolTable.getParentNamespace("System"),
                "");
    }
}