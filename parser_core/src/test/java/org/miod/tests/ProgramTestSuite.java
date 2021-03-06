package org.miod.tests;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
import org.miod.parser.DefaultUnitsPathsResolver;
import org.miod.parser.ErrorReporter;
import org.miod.parser.ErrorReporterException;
import org.miod.parser.UnitParser;
import org.miod.program.CompilationUnit;
import org.miod.program.symbol_table.DefaultSymbolTable;
import org.miod.program.symbol_table.GlobalSymbolTable;
import org.miod.program.symbol_table.SymbolDesc;
import org.miod.program.symbol_table.SymbolTableItem;
import org.miod.program.symbol_table.SymbolVisibility;
import org.miod.program.symbol_table.symbols.ConstSymbol;
import org.miod.program.symbol_table.symbols.TypeDefSymbol;
import org.miod.program.symbol_table.symbols.VarSymbol;
import org.miod.program.types.ArrayType;
import org.miod.program.types.FloatType;
import org.miod.program.types.IntegerType;
import org.miod.program.types.MiodType;
import org.miod.program.values.IntegerValue;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ProgramTestSuite {
    private SymbolTableItem newVar(String name, MiodType type) {
        return new VarSymbol(new SymbolDesc(name, null, type, SymbolVisibility.PUBLIC));
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
        IntegerType btype = IntegerType.INT32;
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
        resolver.pathFromUnitName("mypkg::my");
    }

    @Test
    public void staticIfTest() {
        ErrorReporter reporter = new ErrorReporter();
        reporter.setStopAtFirstError(true);
        
        List<String> stringPaths = new ArrayList<>();
        stringPaths.add("test_data");
        UnitParser parser = new UnitParser(stringPaths, reporter);
        parser.parseFileFromPathString("test_data/pkg1/test0001.miod", false);
        System.out.println(parser.getContext().getOrParseUnit("pkg1::test0001", null).symTable.getItemsAsString());
    }

    @Test
    public void arithmConstTest() {
        ErrorReporter reporter = new ErrorReporter();
        reporter.setStopAtFirstError(true);

        List<String> stringPaths = new ArrayList<>();
        stringPaths.add("test_data");
        UnitParser parser = new UnitParser(stringPaths, reporter);
        parser.parseFileFromPathString("test_data/pkg1/test0002.miod", false);
        CompilationUnit unit = parser.getContext().getOrParseUnit("pkg1::test0002", null);
        System.out.println(unit.symTable.getItemsAsString());
        assertEquals(((IntegerValue)((ConstSymbol)unit.symTable.resolve("b")).value).value, 28);
    }
    
    @Test
    public void integerTest() {
        assertEquals(IntegerType.INT8.maxValue, Byte.MAX_VALUE);
        assertEquals(IntegerType.INT8.minValue, Byte.MIN_VALUE);
        assertEquals(IntegerType.UINT8.maxValue, 255);
        assertEquals(IntegerType.UINT8.minValue, 0);
        assertEquals(IntegerType.UINT32.maxValue, 0xFFFFFFFFL);
        assertEquals(IntegerType.UINT32.minValue, 0);
        assertEquals(IntegerType.INT32.maxValue, Integer.MAX_VALUE);
        assertEquals(IntegerType.INT32.minValue, Integer.MIN_VALUE);
        assertEquals(IntegerType.INT64.maxValue, Long.MAX_VALUE);
        assertEquals(IntegerType.INT64.minValue, Long.MIN_VALUE);
        assertEquals(IntegerType.CARDINAL.maxValue, Integer.MAX_VALUE);
        assertEquals(IntegerType.CARDINAL.minValue, 0);

        assertEquals(IntegerType.CARDINAL.isCardinal, true);
        assertEquals(IntegerType.INT32.isCardinal, false);

        assertEquals(IntegerType.fromLiteral(0), IntegerType.CARDINAL);
        assertEquals(IntegerType.fromLiteral(-1), IntegerType.INT32);
        assertEquals(IntegerType.fromLiteral(Integer.MIN_VALUE), IntegerType.INT32);
        assertEquals(IntegerType.fromLiteral(Integer.MAX_VALUE), IntegerType.CARDINAL);
        assertEquals(IntegerType.fromLiteral((long)(Integer.MAX_VALUE)+1), IntegerType.INT64);
        assertEquals(IntegerType.fromLiteral((long)(Integer.MIN_VALUE)-1), IntegerType.INT64);
        assertEquals(IntegerType.fromLiteral(Long.MIN_VALUE), IntegerType.INT64);
        assertEquals(IntegerType.fromLiteral(Long.MAX_VALUE), IntegerType.INT64);
        assertEquals(IntegerType.fromLiteral(0xFFFFFFFFL), IntegerType.INT64);
    }

    @Test
    public void promotionTest() {
        // check int, float promotion
        assertEquals(IntegerType.CARDINAL.promote(IntegerType.INT8), IntegerType.INT32);
        assertEquals(IntegerType.CARDINAL.promote(IntegerType.INT32), IntegerType.INT32);
        assertEquals(IntegerType.CARDINAL.promote(IntegerType.UINT32), IntegerType.UINT32);
        assertEquals(IntegerType.CARDINAL.promote(IntegerType.INT64), IntegerType.INT64);
        assertEquals(FloatType.FLOAT.promote(FloatType.FLOAT), FloatType.FLOAT);
        assertEquals(FloatType.FLOAT.promote(FloatType.DOUBLE), FloatType.DOUBLE);
        assertEquals(FloatType.DOUBLE.promote(FloatType.DOUBLE), FloatType.DOUBLE);
    }

    // TODO write type resolving tests
    @Test
    public void constExpressionEvalTest() {
        // TODO comparable, int/uint/cardinal tests etc.
    }

    @Test
    public void arrayTypeTest() {
        ErrorReporter reporter = new ErrorReporter();
        reporter.setStopAtFirstError(true);

        List<String> stringPaths = new ArrayList<>();
        stringPaths.add("test_data");
        UnitParser parser = new UnitParser(stringPaths, reporter);
        parser.parseFileFromPathString("test_data/pkg1/test_array0001.miod", false);
        CompilationUnit unit = parser.getContext().getOrParseUnit("pkg1::test_array0001", null);
        System.out.println(unit.symTable.getItemsAsString());
        assertTrue(unit.symTable.resolve("openA") instanceof TypeDefSymbol);
        
        assertTrue(unit.symTable.resolve("sizedA") instanceof TypeDefSymbol);        
        assertTrue(unit.symTable.resolve("sizedB") instanceof TypeDefSymbol);
        ArrayType aType = (ArrayType)(((TypeDefSymbol)unit.symTable.resolve("sizedA")).desc.type);
        assertEquals(aType.size, 3);
        ArrayType bType = (ArrayType)(((TypeDefSymbol)unit.symTable.resolve("sizedB")).desc.type);
        assertEquals(bType.size, 4);
        ArrayType initedArray = (ArrayType)(((ConstSymbol)unit.symTable.resolve("initArray")).value.getType());
        assertEquals(initedArray.size, 3);
    }

    @Test
    public void intCastTest() {
        ErrorReporter reporter = new ErrorReporter();
        reporter.setStopAtFirstError(true);

        List<String> stringPaths = new ArrayList<>();
        stringPaths.add("test_data");
        UnitParser parser = new UnitParser(stringPaths, reporter);
        try {
            parser.parseFileFromPathString("test_data/pkg1/test_int_cast0001.miod", false);
        } catch(ErrorReporterException e) {
            System.out.println(reporter.errorsAsText());
        }
        CompilationUnit unit = parser.getContext().getOrParseUnit("pkg1::test_int_cast0001", null);
        System.out.println(unit.symTable.getItemsAsString());

        ConstSymbol bSym = (ConstSymbol)unit.symTable.resolve("b");
        IntegerValue bValue = (IntegerValue)bSym.value;
        assertEquals(bValue.value, 0);

        ConstSymbol aSym = (ConstSymbol)unit.symTable.resolve("a");
        IntegerValue aValue = (IntegerValue)aSym.value;
        assertEquals(aValue.value, 127);
    }

    @Test
    public void cattrAnnotationTest() {
        ErrorReporter reporter = new ErrorReporter();
        reporter.setStopAtFirstError(true);

        List<String> stringPaths = new ArrayList<>();
        stringPaths.add("test_data");
        UnitParser parser = new UnitParser(stringPaths, reporter);
        try {
        parser.parseFileFromPathString("test_data/pkg1/hello_world01.miod", false);
        } catch(ErrorReporterException e) {
            System.err.println(reporter.errorsAsText());
            assertTrue(false);
        }
        CompilationUnit unit = parser.getContext().getOrParseUnit("pkg1::hello_world01", null);
        System.out.println(unit.symTable.getItemsAsString());
    }
}