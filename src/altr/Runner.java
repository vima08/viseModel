package altr;

import altr.entity.Person;
import altr.experiment.Stage;
import altr.experiment.Experiment;
import altr.distributions.impl.NormalDistribution;
import altr.managers.experiment.SimpleEgoWithMortalityExperimentManager;
import javenue.csv.Csv;

/**
 *
 * @author Vitaly
 */
public class Runner {  
    public static Csv.Writer writer = new Csv.Writer("result.csv").delimiter(';');
    
//    static int ITERATION_NUMBER = 1000;
//    static int STEP_NUMBER = 40;
//    static int PEOPLE_NUM = 100;
    
    static int ITERATION_NUMBER = 1;
    static int STEP_NUMBER = 500;
    static int PEOPLE_NUM = 100;
    
    public static void main(String[] args) throws CloneNotSupportedException {         

//        Person groupMan = new Person(10, true, 0, 1, 0, "name", null);
        Person egoist = new Person(10, true, 1, 0, 0, "name", null);
//        Person altrMan = new Person(0, true, 0, 0, 1, "name", null);
                
        //for(int i = 1; i < PEOPLE_NUM - 50; i++){
//        for(int i = 1; i < PEOPLE_NUM; i++){
//        //for(int t = 1; t < 400; t++){
//            Experiment exp = new Experiment(1, ITERATION_NUMBER, "", "test1");
//            Stage stage = new Stage(exp.getId(), STEP_NUMBER, new NormalDistribution(-.1, 1), 1);
//            exp.addStage(stage);
//            //System.out.println(exp);
//            System.out.println("group size = " + i);
//            //System.out.println("t = " + t);
//            Environment env = new Environment("test");
//            SimpleExperimentManager eM = new SimpleExperimentManager(exp, env, groupMan, 0, egoist, PEOPLE_NUM - 0);
//            //SimpleTwoGroupExperimentManager eM = new SimpleTwoGroupExperimentManager(exp, env, groupMan, i, egoist, PEOPLE_NUM - i - 50);
//            //AltrExperimentManager eM = new AltrExperimentManager(exp, env, groupMan, i, egoist, PEOPLE_NUM - i);
//            //SimpleThresholdExperimentM eM = new SimpleThresholdExperimentM(exp, env, groupMan, egoist, PEOPLE_NUM, t * 0.001);
//            eM.carryOut();
//        }
  
        /// fdsghdfhd
        
//        for(int i = 1; i < 100; i++){
//            Experiment exp = new Experiment(1, ITERATION_NUMBER, "", "test1");
//            Stage stage = new Stage(exp.getId(), STEP_NUMBER, new NormalDistribution(-.3, 1), 1);
//            exp.addStage(stage);
//            Environment env = new Environment("test");
//            HierarchicalExperimentManager eM = new HierarchicalExperimentManager(exp, env, i * 0.01);
//            eM.carryOut();    
//        }
        
//        for(int i = 1; i < 100; i++){
//            Experiment exp = new Experiment(1, ITERATION_NUMBER, "", "test1");
//            Stage stage = new Stage(exp.getId(), STEP_NUMBER, new NormalDistribution(-5, 10), 1);
//            exp.addStage(stage);            
//            System.out.println("alpha = " + i);            
//            Environment env = new Environment("test");
//            SimpleEgoExperimentManager eM = new SimpleEgoExperimentManager(exp, env, egoist, PEOPLE_NUM, ((double)i )/ 100);
//            eM.carryOut();
//        }
        
//        for(int i = 1; i < 100; i++){
//            Experiment exp = new Experiment(1, ITERATION_NUMBER, "", "test1");
//            Stage stage = new Stage(exp.getId(), STEP_NUMBER, new NormalDistribution(-.3, 7), 1);
//            exp.addStage(stage);            
//            System.out.println("alpha = " + i);            
//            Environment env = new Environment("test");
//            StrangeAltrExperimentManager eM = new StrangeAltrExperimentManager(exp, env, altrMan, i, egoist, PEOPLE_NUM - i, 0.8, 0.25);
//            eM.carryOut();
//        }
        
//        for(int i = 0; i <= 100; i++){
//            Experiment exp = new Experiment(1, ITERATION_NUMBER, "", "test1");
//            double mu = ((double)i - 50) / 50;
//            Stage stage = new Stage(exp.getId(), STEP_NUMBER, new NormalDistribution(mu, 1), 1);
//            exp.addStage(stage);            
//            System.out.println("mu = " + mu);            
//            Environment env = new Environment("test");
//            StrangeAltrExperimentManager eM = new StrangeAltrExperimentManager(exp, env, altrMan, 5, egoist, 100 - 5, 0.65, 0.45);
////            AltrExperimentManager eM = new AltrExperimentManager(exp, env, altrMan, 5, egoist, 30 - 5, 0.45);
//            eM.carryOut();
//        }
        
//        for (int i = 1; i <= 100; i++){
//            Experiment exp = new Experiment(1, ITERATION_NUMBER, "", "test1");
//            Stage stage = new Stage(exp.getId(), STEP_NUMBER, new NormalDistribution(-0.1, 1), 1);
//            exp.addStage(stage);            
//            System.out.println("gm = " + (double)i/100);            
//            Environment env = new Environment("test");
//            GroupPlusAltrAndEgoExperimentManager eM = new GroupPlusAltrAndEgoExperimentManager(exp, env, groupMan, 10, egoist, PEOPLE_NUM - 10, (double)i/100);  
////            SimpleExperimentManager eM = new SimpleExperimentManager(exp, env, groupMan, 10, egoist, PEOPLE_NUM - 10);
////            AltrExperimentManager eM = new AltrExperimentManager(exp, env, groupMan, 10, egoist, PEOPLE_NUM - 10);
//            eM.carryOut();
//        }
        
        
        for(int i = 1; i < 100; i++){
            Experiment exp = new Experiment(1, ITERATION_NUMBER, "", "test1");
            double mu = ((double)i - 50) / 50;
            Stage stage = new Stage(exp.getId(), STEP_NUMBER, new NormalDistribution(mu, 1), 1);
            exp.addStage(stage);            
            System.out.println("mu = " + mu);            
            Environment env = new Environment("test");
            SimpleEgoWithMortalityExperimentManager eM = new SimpleEgoWithMortalityExperimentManager(exp, env, egoist, PEOPLE_NUM, 0.5);            
            eM.carryOut();
        }
        writer.close();
    }
}
