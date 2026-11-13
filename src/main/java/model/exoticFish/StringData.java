package model.exoticFish;

public class StringData {
    public String exotic_fish_id = "";
    public String fishspecies_name = "";
    public String image = "";
    public String species_population = ""; 
    public String depth_found = "";
    public String habitat = "";
    public String interesting_fact = "";
    public String web_user_id = "";
    public String user_email = ""; 
    public String user_image = "";  
    public String errorMsg = "";

    // default constructor leaves all data members with empty string (Nothing null).
    public StringData() {
    }

    public int characterCount() {
        String s = this.exotic_fish_id + this.fishspecies_name + this.image +
                this.species_population + this.depth_found + this.habitat +
                this.interesting_fact + this.web_user_id + this.user_email + this.user_image;
        return s.length();
    }
}