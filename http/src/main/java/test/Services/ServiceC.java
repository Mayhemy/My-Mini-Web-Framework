package test.Services;

import framework.annotations.Autowired;
import framework.annotations.Qualifier;
import framework.annotations.Service;

import java.io.File;
import java.io.IOException;

@Service
@Qualifier("ServiceC")
public class ServiceC implements ServiceInter {

    @Autowired(verbose = true)
    @Qualifier("ServiceD")
    private ServiceInter serviceD;

    public void methodC1(){
        // Specify the file name
        String fileName = "yourFileServiceCMethodC1.txt";

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
        ((ServiceD)serviceD).methodD1();
    }

    public ServiceC() {
    }
}
