package view;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.exoticFish.*;
import dbUtils.*;

public class ExoticFishView {

    public static StringDataList getAll(DbConn dbc) {

        // sdl will be an empty array and DbError with "" 
        StringDataList sdl = new StringDataList(); 

        sdl.dbError = dbc.getErr(); // returns "" if connection is good, else db error msg.
        if (sdl.dbError.length() > 0) {
            return sdl; // cannot proceed, db error (and that's been recorded in return object).
        }
        
        // sd will have all of it's fields initialized to ""
        StringData sd = new StringData();
        
        try {
            String sql = "SELECT exotic_fish.*, web_user.user_email, web_user.user_image "
                       + "FROM exotic_fish "
                       + "JOIN web_user ON exotic_fish.web_user_id = web_user.web_user_id "
                       + "ORDER BY exotic_fish.exotic_fish_id";
            
            PreparedStatement stmt = dbc.getConn().prepareStatement(sql);
            ResultSet results = stmt.executeQuery();

            while (results.next()) {
                
                sd = new StringData();
                
                // the Format methods do not throw exceptions. If they find illegal data (like you 
                // tried to format a date as an integer), they return an error message (instead of 
                // returning the formatted value). So, you'll see these error messages right in the 
                // API output (JSON data) and/or you'll see it on the page in the UI.

                sd.exotic_fish_id = Format.fmtInteger(results.getObject("exotic_fish_id"));
                sd.fishspecies_name = Format.fmtString(results.getObject("fishspecies_name"));
                sd.image = Format.fmtString(results.getObject("image"));
                sd.species_population = Format.fmtInteger(results.getObject("species_population"));
                sd.depth_found = Format.fmtInteger(results.getObject("depth_found"));
                sd.habitat = Format.fmtString(results.getObject("habitat"));
                sd.interesting_fact = Format.fmtString(results.getObject("interesting_fact"));
                sd.web_user_id = Format.fmtInteger(results.getObject("web_user_id"));
                sd.user_email = Format.fmtString(results.getObject("user_email"));
                sd.user_image = Format.fmtString(results.getObject("user_image"));
                
                sdl.exoticFishList.add(sd);
            }
            results.close();
            stmt.close();
        } catch (Exception e) {
            sd.errorMsg = "Exception thrown in ExoticFishView.getAll(): " + e.getMessage();
            sdl.exoticFishList.add(sd);
        }
        return sdl;
    }
}