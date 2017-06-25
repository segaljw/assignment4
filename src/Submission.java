import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Jake on 4/30/2017.
 */
public class Submission {
    public static void main(String args[]){
        List<String> personList = new ArrayList<>();
        List<String> countyList = new ArrayList<>();
        Map<String,List<Person>> income = new HashMap<>();

        try {
            Stream<String> stream = Files.lines(Paths.get(args[0]));
            countyList = stream.collect(Collectors.toList());
            stream = Files.lines(Paths.get(args[1]));
            personList = stream.collect(Collectors.toList());

        }catch (IOException e){
            e.printStackTrace();
        }

        List<Person> peoples = new ArrayList<>();
        personList.forEach(entry->{
            String[] values = entry.split(",");
            Person p = new Person();
            p.setFirstName(values[0]);
            p.setLastName(values[1]);
            p.setIncome(Integer.parseInt(values[2]));
            p.setZipcode(Integer.parseInt(values[3]));
            peoples.add(p);
        });


        List<County> counties = new ArrayList<>();
        countyList.forEach(entry->{
            String[] values = entry.split(",");
            County c = new County();
            c.setCounty(values[0]);
            c.setState(values[1]);
            c.setZipcode(Integer.parseInt(values[2]));
            peoples.forEach(p->{
                if(p.getZipcode()==c.getZipcode()){
                    List<Person> something = income.get(c.getCounty());
                    if(something == null){
                        something = new ArrayList<>();
                        income.put(c.getCounty(),something);
                    }
                    something.add(p);
                }
            });
            counties.add(c);
        });

        income.forEach((co,pe)->{
            pe.forEach(inc->{
                double sum = pe.stream().mapToDouble(p->p.getIncome()).sum();
                int count = (int)pe.stream().count();
                double avg = sum/count;
                counties.forEach(c->{
                   if(c.getCounty().equals(co)){
                       c.setAverageIncome(avg);
                   }
                });
            });
        });



        counties.forEach(county->{
            List<Person> person = peoples.stream().filter(p->p.getZipcode() == county.getZipcode()).collect(Collectors.toList());
            peoples.removeAll(person);
            person.forEach(prsn->{
                prsn.setIncome(prsn.getIncome()-county.getAverageIncome());
                prsn.setCounty(county.getCounty());
                prsn.setState(county.getState());
                if(prsn.getState().equals("VA")){
                    peoples.add(prsn);
                }
            });
        });


        Comparator<Person> comparator = Comparator.comparing(Person::getIncome).reversed().thenComparing(Person::getLastName).thenComparing(Person::getFirstName).thenComparing(Person::getCounty);
        Collections.sort(peoples,comparator);
        peoples.forEach(person -> {
            System.out.println(person.getFirstName()+" "+person.getLastName()+" "+person.getIncome()+" "+person.getCounty());
        });



    }
    private static class Person{
        private String firstName, lastName, county, state;
        private int zipcode;
        private double income;

        public Person(String firstName, String lastName, int income, int zipcode, String county, String state){
            super();
            this.firstName = firstName;
            this.lastName = lastName;
            this.income = income;
            this.zipcode = zipcode;
            this.county = county;
        }

        public Person(){

        }

        public String getFirstName(){
            return firstName;
        }

        public void setFirstName(String firstName){
            this.firstName = firstName;
        }

        public String getLastName(){
            return lastName;
        }

        public void setLastName(String lastName){
            this.lastName = lastName;
        }

        public double getIncome(){ return income; }

        public void setIncome(double income){
            this.income = income;
        }

        public int getZipcode(){ return zipcode; }

        public void setZipcode(int zipcode){
            this.zipcode = zipcode;
        }

        public String getCounty(){ return county; }

        public void setCounty(String county){
            this.county = county;
        }

        public String getState(){ return state; }

        public void setState(String state){ this.state = state; }

    }

    private static class County{
        private String county, state;
        private int zipcode;
        private double averageIncome;

        public County(String county, String state, int zipcode, int averageIncome){
            super();
            this.county = county;
            this.state = state;
            this.zipcode = zipcode;
            this.averageIncome = averageIncome;
        }

        public County(){

        }

        public String getCounty(){ return county; }

        public void setCounty(String county){
            this.county = county;
        }

        public String getState(){ return state; }

        public void setState(String state){
            this.state = state;
        }

        public int getZipcode(){ return zipcode; }

        public void setZipcode(int zipcode){
            this.zipcode = zipcode;
        }

        public double getAverageIncome(){ return averageIncome; }

        public void setAverageIncome(double averageIncome){
            this.averageIncome = averageIncome;
        }
    }
}




