package altr;

import altr.distributions.api.Distribution;
import altr.entity.Person;
import altr.experiment.Experiment;
import altr.experiment.Stage;
import altr.managers.experiment.CombinedExperimentManager;
import altr.managers.experiment.ExperimentManager;
import altr.managers.experiment.SimpleEgo;
import javenue.csv.Csv;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static altr.Reader.*;

/**
 *
 * @author Vitaly
 */
public class BackEnd {
    private static List<Person> people = new ArrayList<>();
    
    public static void main(String[] args) throws CloneNotSupportedException {
        List<double[]> result = runSimpleEgo(10, "ParetoDistribution", 0.1, 3,
                20, 150000, 10, 11,
                0.5, -150, 50, 10);
        System.out.println(result);

        List<double[]> result1 = runCombined(new double[] {10}, "ParetoDistribution", 0.1, 3,
                20, 150000, 10, new int[] {11},
                0.5, -150, 50, 10, new double[] {0}, new double[] {1}, new double[] {0}, 1);
        System.out.println(result1);
    }

    public static List<double[]> runSimpleEgo(double capital, String distrName,
                                        double mu, double sigma, double k, int iterNum,
                                        int stepNum, int peopleCount, double alpha,
                                        int start, int finish, int step) {
        List<double[]> result = new ArrayList<>();
        int count = (finish - start) / step;
        count++;
        System.out.println(count);
        result.add(new double[count]);
        result.add(new double[count]);
        int j = 0;

        people.add(new Person(capital, true, 0, 0, 0, "name", null));
        Class distribution = null;
        Class experimentManager = null;
        try {
            distribution = Class.forName(String.format("altr.distributions.impl.%s", distrName));
            experimentManager = Class.forName(String.format("altr.managers.experiment.%s", "SimpleEgo"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Constructor distConstructor = null;
        Constructor experimentManagerConstructor = null;
        distConstructor = distribution.getDeclaredConstructors()[0];
        experimentManagerConstructor = experimentManager.getDeclaredConstructors()[0];

        try {
            for(int i = start; i <= finish; i += step) {
                Experiment exp = new Experiment(1, iterNum, "", "test1");
                Distribution dist = (Distribution) distConstructor.newInstance(mu * i, sigma, k);
                Stage stage = new Stage(exp.getId(), stepNum, dist , 1);
                System.out.println(dist.getName());
                exp.addStage(stage);
                Environment env = new Environment("test", alpha);
                SimpleEgo eM = new SimpleEgo(exp, env, people.get(0), peopleCount);
                eM.carryOut();
                result.get(0)[j] = mu * i;
                result.get(1)[j] = eM.getResult();
                j++;
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static List<double[]> runCombined(double[] capital, String distrName,
                                              double mu, double sigma, double k, int iterNum,
                                              int stepNum, int peopleCount[], double alpha,
                                              int start, int finish, int step,
                                              double[] a, double[] e, double[] g, Integer groupNum) {
        Map<Person, Integer> groups = new HashMap<>();

        List<double[]> result = new ArrayList<>();
        int count = (finish - start) / step;
        count++;
        System.out.println(count);
        result.add(new double[count]);
        result.add(new double[count]);
        int j = 0;

        for(int l = 0; l < groupNum; l++) {
            groups.put(new Person(capital[l], true, e[l], g[l], a[l], "name", null), peopleCount[l]);
        }
        Class distribution = null;
        Class experimentManager = null;
        try {
            distribution = Class.forName(String.format("altr.distributions.impl.%s", distrName));
            experimentManager = Class.forName(String.format("altr.managers.experiment.%s", "SimpleEgo"));
        } catch (ClassNotFoundException exc) {
            exc.printStackTrace();
        }

        Constructor distConstructor = null;
        Constructor experimentManagerConstructor = null;
        distConstructor = distribution.getDeclaredConstructors()[0];
        experimentManagerConstructor = experimentManager.getDeclaredConstructors()[0];

        try {
            for(int i = start; i <= finish; i += step) {
                Experiment exp = new Experiment(1, iterNum, "", "test1");
                Distribution dist = (Distribution) distConstructor.newInstance(mu * i, sigma, k);
                Stage stage = new Stage(exp.getId(), stepNum, dist , 1);
                System.out.println(dist.getName());
                exp.addStage(stage);
                Environment env = new Environment("test", alpha);
                CombinedExperimentManager eM = new CombinedExperimentManager(exp, env, groupNum, groups);
                eM.carryOut();
                result.get(0)[j] = mu * i;
                result.get(1)[j] = eM.getResult();
                j++;
            }
        } catch (InstantiationException exc) {
            exc.printStackTrace();
        } catch (IllegalAccessException exc) {
            exc.printStackTrace();
        } catch (InvocationTargetException exc) {
            exc.printStackTrace();
        } catch (CloneNotSupportedException exc) {
            exc.printStackTrace();
        }

        return result;
    }

}
