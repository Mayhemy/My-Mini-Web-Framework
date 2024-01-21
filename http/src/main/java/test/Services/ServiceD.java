package test.Services;

import framework.annotations.Bean;
import framework.annotations.Qualifier;

import java.io.File;
import java.io.IOException;

@Bean()
@Qualifier("ServiceD")
public class ServiceD implements ServiceInter {


    public void methodD1(){
        // Specify the file name
        String fileName = "yourFileServiceDMethodD1.txt";

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
    public ServiceD() {
    }
}
