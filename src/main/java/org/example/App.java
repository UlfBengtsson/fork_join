package org.example;

import org.example.models.Company;
import org.example.models.Employee;
import org.example.models.SpeedResult;
import org.json.simple.*;
import org.json.simple.parser.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Use the pre-made getCompanies & getEmployees.
 * Replace all the todos in App and FindEmployeeWithFirstNameTask with your own code.
 */
public class App 
{
    private static long simulatedLoad = 1L; //Make this bigger to simulate a heavier workload (larger files with more employees)

    public static void main( String[] args )
    {
        List<SpeedResult<Employee>> speedResults = new ArrayList<>();

        //speedResults.add(doSeqFindAllWithFirstName());
        //speedResults.add(doForkJoinFindAllWithFirstName());
        //speedResults.add(doStreamParallelFindAllWithFirstName());

        for (SpeedResult result : speedResults) {
            System.out.println(result.getName());
            System.out.println("Found: " + result.getCollected().size());
            System.out.println(result.getDuration() + " milliseconds");
        }

    }

    private static SpeedResult doSeqFindAllWithFirstName() {
        long startTime = System.nanoTime();

        //Todo - Implement your none threaded sequel code

        long endTime = System.nanoTime();

        long duration = (endTime - startTime) / 1000000;  //divide by 1000000 to get milliseconds.

        return new SpeedResult("Seq All With FirstName", duration, result);
    }

    private static SpeedResult doForkJoinFindAllWithFirstName() {
        long startTime = System.nanoTime();

        //Todo - Implement your Fork Join

        long endTime = System.nanoTime();

        long duration = (endTime - startTime) / 1000000;  //divide by 1000000 to get milliseconds.

        return new SpeedResult("ForkJoin All With FirstName", duration, result);
    }

    private static SpeedResult doStreamParallelFindAllWithFirstName() {
        long startTime = System.nanoTime();

        //Todo - Implement your Parallel Stream

        long endTime = System.nanoTime();

        long duration = (endTime - startTime) / 1000000;  //divide by 1000000 to get milliseconds.

        return new SpeedResult("Parallel Stream All With FirstName", duration, result);
    }
//-------------------------------------------------------------------------------
    static List<Company> getCompanies()
    {
        List<Company> companies = new ArrayList();

        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("src/resources/companies.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray companyList = (JSONArray) obj;

            //Iterate over companies array
            companyList.forEach( comp -> companies.add(parseJsonToCompany( (JSONObject) comp ) ) );

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return companies;
    }

    static List<Employee> getEmployees(long companyId)
    {

        try {
            Thread.sleep(simulatedLoad);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Employee> employees = new ArrayList();

        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("src/resources/" + companyId +".json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray employeesList = (JSONArray) obj;

            //Iterate over employees array
            employeesList.forEach( emp -> employees.add(parseJsonToEmployee( (JSONObject) emp ) ) );

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return employees;
    }

    private static Company parseJsonToCompany(JSONObject company)
    {
        //Get id
        long id = (long) company.get("id");

        //Get name
        String name = (String) company.get("name");

        //Get amount of employees
        long employees = (long) company.get("employees");

        return new Company(id, name, employees);
    }
    private static Employee parseJsonToEmployee(JSONObject employee)
    {
        //Get id
        String id = (String) employee.get("id");

        //Get first name
        String firstName = (String) employee.get("firstName");

        //Get last name
        String lastName = (String) employee.get("lastName");

        //Get city name
        String city = (String) employee.get("city");

        return new Employee(id, firstName, lastName, city);
    }

//--------------------------------------------------------------------------------
    private static void simpleMaxNumberEx() {
        int[] data = new int[300_000_000]; //1G

        for (int i = 0; i < data.length; i++) {
            data[i] = ThreadLocalRandom.current().nextInt();
        }

        doSeqMaxNumber(data);

        doForkJoinMaxNumber(data);

        doParallelStreamMaxNumber(data);
    }

    private static void doSeqMaxNumber(int[] data) {
        long startTime = System.nanoTime();

        int max = Integer.MIN_VALUE;
        for (int value : data) {
            if (value > max) {
                max = value;
            }
        }

        long endTime = System.nanoTime();

        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.

        System.out.println("Max value found:" + max);
        System.out.println("Time nano: " + duration);
    }

    private static void doForkJoinMaxNumber(int[] data) {
        long startTimeFJ = System.nanoTime();

        ForkJoinPool pool = new ForkJoinPool();

        FindMaxTask task = new FindMaxTask(data, 0, data.length - 1, data.length / 6);

        int maxFJ = pool.invoke(task);

        long endTimeFJ = System.nanoTime();

        long durationFJ = (endTimeFJ - startTimeFJ);  //divide by 1000000 to get milliseconds.

        System.out.println("Max value found:" + maxFJ);
        System.out.println("Time nano: " + durationFJ);
    }

    private static void doParallelStreamMaxNumber(int[] data) {

        long startTime = System.nanoTime();

        IntStream streamData = Arrays.stream(data);

        OptionalInt result = streamData.parallel().max();

        long endTime = System.nanoTime();

        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.

        if (result.isPresent()) {
            System.out.println("Max: " +  result.getAsInt());
            System.out.println("Time nano: " + duration);
        }
    }
}
