package org.miod.tests;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
import org.miod.parser.DefaultUnitsPathsResolver;
import org.miod.parser.ErrorReporter;
import org.miod.parser.UnitParser;
import org.miod.program.symbol_table.GlobalSymbolTable;
import org.miod.program.SymItem;
import org.miod.program.symbol_table.SymbolKind;
import org.miod.program.symbol_table.SymbolVisibility;

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
        SymItem sym1 = new SymItem(unitMyName + "::sym1", SymbolKind.Var, SymbolVisibility.Public, null);
        // the same name, to check name collision
        SymItem sym2 = new SymItem(unitMy2Name + "::sym1", SymbolKind.Var, SymbolVisibility.Public, null);
        SymItem sym3 = new SymItem(unitMy3Name + "::sym3", SymbolKind.Var, SymbolVisibility.Public, null);
        SymItem symSystem = new SymItem(unitSystemName + "::system", SymbolKind.Var, SymbolVisibility.Public, null);
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
    
    @Test
    public void unitNameFromPath() {        
        List<String> stringPaths = new ArrayList<>();
        stringPaths.add("abc");
        stringPaths.add("/root/bb");
        stringPaths.add("koko/k/qwe/");
                
        DefaultUnitsPathsResolver resolver = new DefaultUnitsPathsResolver();
        resolver.addPaths(stringPaths);
        String unit1 = resolver.unitNameFromPath(FileSystems.getDefault().getPath("abc/my.miod"));
        assertEquals(unit1, "my");
        String unit2 = resolver.unitNameFromPath(FileSystems.getDefault().getPath("abc/mypkg/my.miod"));
        assertEquals(unit2, "mypkg::my");
        /*
        Set<Path> paths = new HashSet<>();
        for(String s : stringPaths) {
            paths.add(FileSystems.getDefault().getPath(s).toAbsolutePath());
        }
        */
        resolver.unitNameToPath("mypkg::my");
    }
}