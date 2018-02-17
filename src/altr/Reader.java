package altr;

import java.util.List;

/**
 * Created by scholar on 11.02.2018.
 */
public class Reader {

    public static Variable readVariable(List<String> lines, int reader, String varName, boolean isInt) {
        Variable var;

        reader++;
        String val = lines.get(reader);
        System.out.printf("%d) %s \n", reader, val);
        if ("*".equals(val)) {
            reader++;
            val = lines.get(reader);
            System.out.printf("%d) %s \n", reader, val);
            var = new Variable(true, Double.valueOf(val));
        } else if ("q".equals(val)) {
            return null;
        } else {
            if (isInt) {
                var = new Variable(false, Integer.valueOf(val));
            } else {
                var = new Variable(false, Double.valueOf(val));
            }
            System.out.printf("%s %s \n", varName, var.getValue());
        }

//            try {
//            } catch (NumberFormatException e) {
//                System.out.println("Enter number");
//                continue;
//            }

        return var;
    }

    public static int postReadVariable(int reader, Variable var) {
        if (var.isVariable()) {
            reader += 2;
        } else {
            reader++;
        }
        return reader;
    }

    public static Object readConstant(List<String> lines, int reader, String constantName, boolean isInt) {
        Object constant;

        reader++;
        String val = lines.get(reader);
        System.out.printf("%d) %s \n", reader, val);

        if (isInt) {
            constant = Integer.valueOf(val);
        } else {
            constant = Double.valueOf(val);
        }
        System.out.printf("%s %s \n", constantName, constant);

        return constant;
    }

    public static String readClassName(List<String> lines, int reader, String name) {
        reader++;
        String val = lines.get(reader);
        System.out.printf("%d) %s \n", reader, val);
        System.out.printf("%s %s \n", name, val);
        return val;
    }

    public static int readComment(List<String> lines, int reader) {
        reader++;
        String val = lines.get(reader);
        System.out.printf("%d) %s \n", reader, val);
        return reader;
    }
}
