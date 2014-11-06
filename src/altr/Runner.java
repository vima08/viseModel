/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package altr;

import altr.entity.Person;
import altr.experiment.Stage;
import altr.experiment.Experiment;
import altr.strategies.SimpleEgoisticStrategy;
import altr.distributions.NormDistr;
import altr.managers.GroupManager;
import altr.managers.PersonManager;
import altr.managers.SimpleExperimentManager;
import altr.strategies.SimpleGroupStrategy;
import java.util.Collection;
import javenue.csv.Csv;

/**
 *
 * @author Vitaly
 */
public class Runner {  
    public static Csv.Writer writer = new Csv.Writer("result.csv").delimiter(';');
    
    static int ITERATION_NUMBER = 100;
    static int STEP_NUMBER = 20;
    static int PEOPLE_NUM = 500;
    
    public static void main(String[] args) throws CloneNotSupportedException {         

        Person groupMan = new Person(10, true, 0, 1, 0, "name", null);
        Person egoist = new Person(10, true, 1, 0, 0, "name", null);

        Experiment exp = new Experiment(1, ITERATION_NUMBER, "", "test1");        
        Stage stage = new Stage(exp.getId(), STEP_NUMBER, new NormDistr(-.8, 30), 1);        
        exp.addStage(stage);
        System.out.println(exp); 
        
        for(int i = 1; i < PEOPLE_NUM; i++){
            System.out.println("group size = " + i);
            Environment env = new Environment("test");
            SimpleExperimentManager eM = new SimpleExperimentManager(exp, env, groupMan, i, egoist, PEOPLE_NUM - i);
            eM.carryOut();
        }
        
        writer.close();
    }
}
