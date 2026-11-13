package model.exoticFish;

import dbUtils.*;

public class DbMods {
    /*
     * Returns a "StringData" object that is full of field level validation
     * error messages (or "" for any field that passes validation).
     */
    private static StringData validate(StringData inputData) {

        StringData errorMsgs = new StringData();

        /*
        - Useful to copy field names from StringData as a reference
          public String exotic_fish_id = "";
          public String fishspecies_name = "";
          public String image = "";
          public String species_population = ""; 
          public String depth_found = "";
          public String habitat = "";
          public String interesting_fact = "";
          public String web_user_id = ""; // Foreign Key
          public String user_email = ""; // joined from web_user table
          public String user_image = ""; // joined from web_user table
         */
        // Validation
        errorMsgs.fishspecies_name = Validate.stringMsg(inputData.fishspecies_name, 100, true);
        errorMsgs.image = Validate.stringMsg(inputData.image, 200, false);
        errorMsgs.species_population = Validate.integerMsg(inputData.species_population, false);
        errorMsgs.depth_found = Validate.integerMsg(inputData.depth_found, false);
        errorMsgs.habitat = Validate.stringMsg(inputData.habitat, 100, false);
        errorMsgs.interesting_fact = Validate.stringMsg(inputData.interesting_fact, 500, false);
        errorMsgs.web_user_id = Validate.integerMsg(inputData.web_user_id, true);

        return errorMsgs;
    } // validate

    public static StringData insert(StringData inputData, DbConn dbc) {

        StringData errorMsgs = new StringData();
        errorMsgs = validate(inputData);
        if (errorMsgs.characterCount() > 0) { // at least one field has an error, don't go any further.
            errorMsgs.errorMsg = "Please try again";
            return errorMsgs;

        } else { // all fields passed validation

            // Start preparing SQL statement
            String sql = "INSERT INTO exotic_fish (fishspecies_name, image, species_population, depth_found, habitat, interesting_fact, web_user_id) " +
                    "values (?,?,?,?,?,?,?)";

            // PrepStatement is Sally's wrapper class for java.sql.PreparedStatement
            // Only difference is that Sally's class takes care of encoding null
            // when necessary. And it also System.out.prints exception error messages.
            PrepStatement pStatement = new PrepStatement(dbc, sql);

            // Encode string values into the prepared statement (wrapper class).
            pStatement.setString(1, inputData.fishspecies_name); // string type is simple
            pStatement.setString(2, inputData.image);
            pStatement.setInt(3, Validate.convertInteger(inputData.species_population));
            pStatement.setInt(4, Validate.convertInteger(inputData.depth_found));
            pStatement.setString(5, inputData.habitat);
            pStatement.setString(6, inputData.interesting_fact);
            pStatement.setInt(7, Validate.convertInteger(inputData.web_user_id));

            // here the SQL statement is actually executed
            int numRows = pStatement.executeUpdate();

            // This will return empty string if all went well, else all error messages.
            errorMsgs.errorMsg = pStatement.getErrorMsg();
            if (errorMsgs.errorMsg.length() == 0) {
                if (numRows == 1) {
                    errorMsgs.errorMsg = ""; // This means SUCCESS. Let the user interface decide how to tell this to
                                             // the user.
                } else {
                    // probably never get here unless you forgot your WHERE clause and did a bulk
                    // sql update.
                    errorMsgs.errorMsg = numRows + " records were inserted when exactly 1 was expected.";
                }
            } else if (errorMsgs.errorMsg.contains("foreign key")) {
                errorMsgs.errorMsg = "Invalid Web User Id - " + errorMsgs.errorMsg;
            } else if (errorMsgs.errorMsg.contains("Duplicate entry")) {
                errorMsgs.errorMsg = "That fish species name is already taken - " + errorMsgs.errorMsg;
            }

        } // customerId is not null and not empty string.
        return errorMsgs;
    } // insert

}