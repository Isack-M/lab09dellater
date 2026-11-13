package dbUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Json {

    public static String toJson(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writer().writeValueAsString(obj);
        } catch (Exception e) {
            String msg = "Cannot convert object to JSON. Exception: " +
            e.getMessage() + ". object.toString: "+obj.toString();
            System.out.println("***** "+msg);
            return msg; 
        }
    }
}