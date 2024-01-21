package DI;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class DependencyContainer {

    //singleton pattern
    private static DependencyContainer instance;

    DIEngine diEngine;

    private Map<String, Map.Entry<String, Object>> implementationMap;

    public DependencyContainer() {
        implementationMap = new HashMap<>();
        diEngine = DIEngine.getInstance();
    }

    public void registerClass(String interfaceName, String className) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (!implementationMap.containsKey(className)) {
            Map.Entry<String, Object> interfaceClassPair = createInterfaceClassPair(className,interfaceName);
            implementationMap.put(className, interfaceClassPair);
        }

        if (implementationMap.containsKey(className) && !implementationMap.get(className).getKey().equals(interfaceName)){
            throw new RuntimeException("Class already registered to another interface");
        }
    }

    private Map.Entry<String, Object> createInterfaceClassPair(String className,String interfaceName) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Map.Entry<String, Object> interfaceClassPair = implementationMap.get(className);
        if (interfaceClassPair == null) {
            Object dependency = diEngine.getDependency(className);
            if (dependency == null) {
                dependency = diEngine.resolveDependencies(className,className);
            }
            interfaceClassPair = new AbstractMap.SimpleEntry<>(interfaceName, dependency);
        }
        return interfaceClassPair;
    }

    public Object getImplementation(String className){
        if(!implementationMap.containsKey(className)){
            for (Map.Entry<String, Map.Entry<String, Object>> outerEntry : implementationMap.entrySet()) {
                String outerKey = outerEntry.getKey();
                Map.Entry<String, Object> innerEntry = outerEntry.getValue();
                String innerKey = innerEntry.getKey();
                Object innerValue = innerEntry.getValue();
                System.out.println("Outer Key: " + outerKey);
                System.out.println("Inner Key: " + innerKey);
                System.out.println("Inner Value: " + innerValue);
                System.out.println();
            }
            return null;
        }
        return implementationMap.get(className).getValue();
    }
}
