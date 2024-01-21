package test.Services;

import framework.annotations.Autowired;
import framework.annotations.Bean;
import framework.annotations.Qualifier;

import java.io.File;
import java.io.IOException;

@Bean()
@Qualifier("ServiceA")
public class ServiceA implements ServiceInter {

    @Autowired(verbose = true)
    private ComponentA componentA;


    public void methodA1(String testParam){
        // Specify the file name
        String fileName = "yourFileServiceAMethodA1" + testParam + ".txt";

        // Create a File object in the current working directory
        File file = new File(fileName);

        try {
            // Create an empty file
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getAbsolutePath());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.err.println("Error creating the file: " + e.getMessage());
        }
    }
    public ServiceA() {
    }
}
