package org.example;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class RuleGen {

    public static void main(String[] args) throws Exception {

        StringBuilder builder = new StringBuilder();

        builder.append("package org.example;\n\n");
        builder.append("import org.example.FactA;\n");
        builder.append("import org.example.FactB;\n");
        builder.append("import org.example.FactC;\n");
        builder.append("import org.example.FactD;\n");
        builder.append("import org.example.FactE;\n\n");
        builder.append("global java.util.List resultList;\n\n");

        int ruleNum = 50000;

        for (int i = 0; i < ruleNum; i++) {
            String value = "ABCDEFG" + i;
            builder.append("rule \"rule" + i + "\"\n");
            builder.append("  when\n");
            builder.append("    $a : FactA( value1 == \"" + value + "\" )\n");
            builder.append("    $b : FactB( value1 == \"" + value + "\", value2 == $a.value2, value3 == $a.value3, value4 == $a.value4, value5 == $a.value5, " +
                                   "value6 == $a.value6, value7 == $a.value7, value8 == $a.value8, value9 == $a.value9, value10 == $a.value10 )\n");
            builder.append("    $c : FactC( value1 == \"" + value + "\", value2 == $b.value2, value3 == $b.value3, value4 == $b.value4, value5 == $b.value5, " +
                                   "value6 == $b.value6, value7 == $b.value7, value8 == $b.value8, value9 == $b.value9, value10 == $b.value10 )\n");
            builder.append("    $d : FactD( value1 == \"" + value + "\", value2 == $c.value2, value3 == $c.value3, value4 == $c.value4, value5 == $c.value5, " +
                                   "value6 == $c.value6, value7 == $c.value7, value8 == $c.value8, value9 == $c.value9, value10 == $c.value10 )\n");
            builder.append("    $e : FactE( value1 == \"" + value + "\", value2 == $d.value2, value3 == $d.value3, value4 == $d.value4, value5 == $d.value5, " +
                                   "value6 == $d.value6, value7 == $d.value7, value8 == $d.value8, value9 == $d.value9, value10 == $d.value10 )\n");
            builder.append("  then\n");
            builder.append("    resultList.add( \"rule" + i + " fired : \" + $a );\n");
            builder.append("end\n");
            builder.append("\n");
        }

        Path path = Paths.get("drl/rules_" + ruleNum + ".drl");
        Files.write(path, builder.toString().getBytes(), StandardOpenOption.CREATE);

        System.out.println("finish");
    }
}
