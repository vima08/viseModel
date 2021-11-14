package altr;

import altr.distributions.api.Distribution;
import altr.entity.Person;
import altr.experiment.Stage;
import altr.experiment.Experiment;
import altr.managers.experiment.ExperimentManager;
import javenue.csv.Csv;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static altr.Reader.*;

/**
 *
 * @author Vitaly
 */
public class Runner {
    public static Csv.Writer writer;
    private static final boolean logEnable = true;

    static {

    }
    
//    static int ITERATION_NUMBER = 1000;
//    static int STEP_NUMBER = 40;
//    static int PEOPLE_NUM = 100;

    static int ITERATION_NUMBER = 50;
    static int STEP_NUMBER = 500;
    static int PEOPLE_NUM = 201;

    private static List<Person> people = new ArrayList<>();
    
    public static void main(String[] args) throws CloneNotSupportedException {
        long startTime = System.nanoTime();
        String inputFile = args[0];

        DateFormat df = new SimpleDateFormat("yy.MM.dd_HH.mm.ss");
        Date now = Calendar.getInstance().getTime();
        String reportDate = df.format(now);

        File folder = new File(String.format("out/%s", reportDate));
        if (!folder.exists()) {
            boolean created = folder.mkdir();
            if (logEnable) System.out.printf("Result folder created: %s \n", created);
            try {
                Files.copy(Paths.get(inputFile), Paths.get(String.format("out/%s/%s", reportDate,inputFile)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (logEnable) System.out.println(folder.getAbsolutePath());
        writer = new Csv.Writer(String.format("out/%s/result.csv", reportDate)).delimiter(';');

        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(inputFile), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int reader = -1;
        reader = readComment(lines, reader);

        String val = lines.get(reader);
        double money;
        while (!"q".equals(val)) {
            reader++;
            val = lines.get(reader);
            System.out.printf("%d) %s \n", reader, val);
            if ("q".equals(val)) {
                System.out.println("people added!");
                break;
            }
            money = Double.valueOf(val); //TODO

            people.add(new Person(money, true, 0, 0, 0, "name", null));
            System.out.printf("Person added %f \n", money);
        }

        //---------------------------------------------------

        reader = readComment(lines, reader);

        Integer start = (Integer) readConstant(lines, reader, "start", true);
        reader++;

        Integer finish = (Integer) readConstant(lines, reader, "finish", true);
        reader++;

        Integer step = (Integer) readConstant(lines, reader, "step", true);
        reader++;

        //---------------------------------------------------

        reader = readComment(lines, reader);

        String distributionName = readClassName(lines, reader, "distributionName");
        reader++;

        Variable mu = readVariable(lines, reader, "a", false);
        reader = postReadVariable(reader, mu);

        Variable sigma = readVariable(lines, reader, "b", false);
        reader = postReadVariable(reader, sigma);

      //  Variable k = readVariable(lines, reader, "k", false);
      //  reader = postReadVariable(reader, k);

        //---------------------------------------------------


        reader = readComment(lines, reader);

        Variable iterationNumber = readVariable(lines, reader, "iterationNumber", true);
        reader = postReadVariable(reader, iterationNumber);

        Variable stepNumber = readVariable(lines, reader, "stepNumber", true);
        reader = postReadVariable(reader, stepNumber);

        //---------------------------------------------------

        reader = readComment(lines, reader);

        String experimentManagerName = readClassName(lines, reader, "experimentManagerName");
        reader++;

        Variable personNumber = readVariable(lines, reader, "personNumber", true);
        reader = postReadVariable(reader, personNumber);

        Variable peopleCount = readVariable(lines, reader, "peopleCount", true);
        reader = postReadVariable(reader, peopleCount);

        Variable alpha = readVariable(lines, reader, "alpha", false);
        reader = postReadVariable(reader, alpha);

        //---------------------------------------------------
        Class distribution = null;
        Class experimentManager = null;
        try {
            distribution = Class.forName(String.format("altr.distributions.impl.%s", distributionName));
            experimentManager = Class.forName(String.format("altr.managers.experiment.%s", experimentManagerName));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Constructor distConstructor = null;
        Constructor experimentManagerConstructor = null;
        distConstructor = distribution.getDeclaredConstructors()[1];
        experimentManagerConstructor = experimentManager.getDeclaredConstructors()[0];

        try {
            for(int i = start; i <= finish; i += step) {
                Experiment exp = new Experiment(1, (Integer)iterationNumber.getValue(i), "", "test1");
                Distribution dist = (Distribution) distConstructor.newInstance(mu.getValue(i), sigma.getValue(i),0);//, k.getValue(i));
                Stage stage = new Stage(exp.getId(), (Integer)stepNumber.getValue(i), dist , 1);
                System.out.println(dist.getName());
                exp.addStage(stage);
                Environment env = new Environment("test", (Double) alpha.getValue(i));
                ExperimentManager eM = (ExperimentManager)experimentManagerConstructor.newInstance(exp, env,
                        people.get(0), peopleCount.getValue(i));
                                //(Integer)personNumber.getValue(i)-1), peopleCount.getValue(i));
                eM.carryOut();
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

//        Scanner in = new Scanner(System.in);
//        System.out.println("Enter person 'money' or 'q' ");
//        String val = "start";
//        double money;
//        while (!"q".equals(val)) {
//            val = in.next();
//            if ("q".equals(val)) break;
//            try {
//                money = Double.valueOf(val);
//            } catch (NumberFormatException e) {
//                System.out.println("Enter number");
//                continue;
//            }
//            people.add(new Person(money, true, 0, 0, 0, "name", null));
//        }
//
//        System.out.println("Enter start");

        writer.close();
        long finishTime = System.nanoTime();
        System.out.println(Double.valueOf(Long.toString(finishTime - startTime))/1000000000);
    }

}
