package DI;

import DI.DependencyContainer;
import framework.annotations.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DIEngine {


    private static DIEngine instance;
    DependencyContainer dependencyContainer;
    HashMap<String, Object> dependenciesMap;

    private DIEngine() {
    }

    public static DIEngine getInstance() {
        if (instance == null) {
            instance = new DIEngine();
            instance.dependenciesMap = new HashMap<>();
            instance.dependencyContainer = new DependencyContainer();
        }
        return instance;
    }


    public boolean initializeController(String controllerName) {
        if (resolveDependencies(controllerName, controllerName) != null) {
            return true;
        } else {
            return false;
        }
    }

    public Object makeQualifierInstance(Class cl) {
        if (cl.isAnnotationPresent(Qualifier.class)) {
            Qualifier qualifier = (Qualifier) cl.getAnnotation(Qualifier.class);
            return resolveDependencies(cl.getName(), qualifier.value());
        }
        throw new RuntimeException("Bean must have qualifier");
    }

    //vraca  true ako se izvrsi kako treba
    public Object resolveDependencies(String parentName, String className) {
        Object instance = null;
        try {
//            System.out.println("Resolving dependencies for class " + className);
            Class cl = Class.forName(className);
            Constructor constructor = cl.getDeclaredConstructor();

            if (!cl.isAnnotationPresent(Qualifier.class) && dependenciesMap.containsKey(className)) {
                return dependenciesMap.get(className);
            }
            if(cl.isAnnotationPresent(Service.class) && dependenciesMap.containsKey(className)){
                return dependenciesMap.get(className);
            }
//            if(!cl.isAnnotationPresent(Bean.class) && !cl.isAnnotationPresent(Service.class)){
//                Object instance = makeQualifierInstance(cl);
//            } // OVDE SAMO PUSTIM SAM CE SE NAPRAVITI NA DNU FUNKCIJE OVE

            if (cl.isAnnotationPresent(Bean.class)) {
                Bean bean = (Bean) cl.getAnnotation(Bean.class);
                String scope = bean.scope();
                if (!scope.equals("singleton") && !scope.equals("prototype")) {
                    throw new RuntimeException("Scope must be singleton or prototype");
                }
                if(scope.equals("singleton") && dependenciesMap.containsKey(className)){
                    return dependenciesMap.get(className);
                }
            }

            constructor.setAccessible(true);
            instance = constructor.newInstance();


            Field[] fields = cl.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    field.setAccessible(true);
                    String dependencyName = field.getType().getName();
                    boolean verbose = false;
                    if(field.isAnnotationPresent(Qualifier.class) && field.getType().isInterface()) {
                        String fullClassName = field.getType().getPackage().getName() + "." + field.getAnnotation(Qualifier.class).value();;
                        verbose = !dependenciesMap.containsKey(fullClassName) || getClassAndTestIfItsComponent(fullClassName);
                        dependencyContainer.registerClass(field.getType().getName(), fullClassName);
                        Object implementationOfInterface = dependencyContainer.getImplementation(fullClassName);
//                        System.out.println("EVO ME INTERFACE " + field.getType().getName()+ " PUNO IME KLASE" + fullClassName);

                        if(implementationOfInterface == null){
                            throw new RuntimeException("Something went terribly wrong");
                        }
                        field.setAccessible(true);
                        field.set(instance, implementationOfInterface);
                    }else if(!field.isAnnotationPresent(Qualifier.class) && field.getType().isInterface()){
                        return new RuntimeException("Interface must have qualifier");
                    }else{
                        verbose = !dependenciesMap.containsKey(dependencyName) || getClassAndTestIfItsComponent(dependencyName);
                        Object dependency = resolveDependencies(className,  dependencyName);
                        field.set(instance, dependency);
                    }
                    if (autowired.verbose() && verbose) {
                        String resolutionMessage = String.format(
                                "Initialized %s %s in %s on %s with %d",
                                field.getType().getName(), field.getName(), className,
                                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                                instance.hashCode()
                        ); // ovde za parentClassName moze i parentClass nisam siguran koje je od ta dva, zato ova methoda ima 2 argumenta
                        System.out.println(resolutionMessage);
                    }
                }
            }
            dependenciesMap.put(className, instance);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                 InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return instance;
    }

    //check if certain class in the map and return it if it does, else return null
    public Object getDependency(String className){
        if(dependenciesMap.containsKey(className)){
            return dependenciesMap.get(className);
        }
        return null;
    }
    public void invokeMethod(String className, String methodName, Object[] parameters) throws ClassNotFoundException, NoSuchMethodException {
        Class cl = Class.forName(className);
        Method method;

        // Determine the parameter types and then call the method with those types if empty call empty method
        Class[] parameterTypes = new Class[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            parameterTypes[i] = parameters[i].getClass();
        }
        if(parameters.length == 0){
            method = cl.getDeclaredMethod(methodName);
        }else{
            method = cl.getDeclaredMethod(methodName, parameterTypes);
        }
        try {
            method.invoke(dependenciesMap.get(className), parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public boolean getClassAndTestIfItsComponent(String className){
        try {
            Class cl = Class.forName(className);
            if(cl.isAnnotationPresent(Component.class) || (cl.isAnnotationPresent(Bean.class) &&  ((Bean)cl.getAnnotation(Bean.class)).scope().equals("prototype"))){
                return true;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}