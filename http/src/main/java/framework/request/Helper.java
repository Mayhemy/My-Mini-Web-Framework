package framework.request;

import java.util.HashMap;

public class Helper {

    public static HashMap<String, String> getParametersFromRoute(String route) {
        String[] splittedRoute = route.split("\\?");

        if(splittedRoute.length == 1) {
            return new HashMap<String, String>();
        }

        return Helper.getParametersFromString(splittedRoute[1]);
    }

    public static HashMap<String, String> getParametersFromString(String parametersString) {
        if(parametersString.equals("") || !parametersString.matches("[a-zA-Z0-9=&\\s]+")) { //added check so that it doesn't crash when there are no parameters, params p1=WORKS&p2=WORKS
            return new HashMap<String, String>();
        }
        HashMap<String, String> parameters = new HashMap<String, String>();
        String[] pairs = parametersString.split("&");
        for (String pair:pairs) {
            String[] keyPair = pair.split("=");
            if(keyPair.length > 1){ //added this for handling empty spaces in request via postman
                keyPair[1] = keyPair[1].trim();
            }else{
                keyPair[1] = "";
            }
            parameters.put(keyPair[0], keyPair[1]);
        }

        return parameters;
    }

}
