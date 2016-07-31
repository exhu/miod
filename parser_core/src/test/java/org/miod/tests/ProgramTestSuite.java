package org.miod.tests;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
import org.miod.parser.DefaultUnitsPathsResolver;
import org.miod.parser.ErrorReporter;
import org.miod.program.symbol_table.DefaultSymbolTable;
import org.miod.program.symbol_table.GlobalSymbolTable;
import org.miod.program.symbol_table.SymbolDesc;
import org.miod.program.symbol_table.SymbolKind;
import org.miod.program.symbol_table.SymbolTableItem;
import org.miod.program.symbol_table.SymbolVisibility;
import org.miod.program.symbol_table.symbols.VarSymbol;
import org.miod.program.types.BuiltinType;
import org.miod.program.types.MiodType;
import org.miod.program.types.ValueTypeId;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ProgramTestSuite {
    private SymbolTableItem newVar(String name, MiodType type) {
        return new VarSymbol(new SymbolDesc(name, null, type, SymbolVisibility.Public));
    }
    @Test
    public void globalSymbolTable() {
        ErrorReporter reporter = new ErrorReporter();
        final String unitSystemName = "System";
        final String unitMyName = "my::My";
        final String unitMy2Name = "my::My2";
        final String unitMy3Name = "my::My3";
        DefaultSymbolTable defTable = new DefaultSymbolTable(reporter);
        GlobalSymbolTable systemTab = new GlobalSymbolTable(defTable, unitSystemName,
            reporter);
        GlobalSymbolTable myTab1 = new GlobalSymbolTable(defTable, unitMyName, reporter);
        GlobalSymbolTable myTab2 = new GlobalSymbolTable(defTable, unitMy2Name, reporter);
        GlobalSymbolTable myTab3 = new GlobalSymbolTable(defTable, unitMy3Name, reporter);
        myTab1.addImport(systemTab, false);
        myTab1.addImport(myTab2, true);
        myTab2.addImport(systemTab, false);
        myTab2.addImport(myTab3, false);
        BuiltinType btype = new BuiltinType(ValueTypeId.INT32);
        SymbolTableItem sym1 = newVar("sym1", btype);
        // the same name, to check name collision
        SymbolTableItem sym2 = newVar("sym1", btype);
        SymbolTableItem sym3 = newVar("sym3", btype);
        SymbolTableItem symSystem = newVar("system", btype);
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