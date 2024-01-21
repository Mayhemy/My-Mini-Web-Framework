package discovery;

import framework.annotations.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

//@Aspect
public class DiscoveryMechanism {
    private static DiscoveryMechanism instance = null;
    private HashMap<String,String> controllers;

    private DiscoveryMechanism() {
    }

    public static DiscoveryMechanism getInstance() {
        if (instance == null) {
//            System.out.println("Creating new instance");
            instance = new DiscoveryMechanism();
            instance.controllers = new HashMap<>();
        }
        return instance;
    }

    public String extractPath(Method method)  {
        if (method.isAnnotationPresent(Path.class)) {
            Path path = (Path) method.getAnnotation(Path.class);
            return path.path();
        }
        return null;
    }
//    @Pointcut("@within(framework.annotations.Controller)")
    void controllerAnnotation(){

    }


    public HashMap<String,String> discoverControllers() {
        String basePackage = "test.Controllers"; // Replace with your project's base package

        // Perform classpath scanning
        Set<Class> classes = scanClasses(basePackage);

        // Print the found classes
        for (Class<?> clazz : classes) {
//            System.out.println("TEST"   + clazz.getName());
            Method[] controllerMethods= clazz.getDeclaredMethods();
            for (Method method : controllerMethods) {
                String path = extractPath(method);
                if(method.getAnnotation(Get.class) != null) {
                    controllers.put("GET" + path,clazz.getName()+'-'+method.getName());
                }else if(method.getAnnotation(Post.class) != null){
                    controllers.put("POST" + path,clazz.getName()+'-'+method.getName());
                }
            }

        }

        return controllers;
    }
    private static Set<Class> scanClasses(String basePackage) {
        Reflections reflections = new Reflections(basePackage, new SubTypesScanner(false));
        System.out.println(new HashSet<>(reflections.getSubTypesOf(Object.class)));
        return reflections.getSubTypesOf(Object.class)
                .stream()
                .collect(Collectors.toSet());

    }

    public HashMap<String,String> getControllers(){
        return controllers;
    }
}
