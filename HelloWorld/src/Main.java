import java.sql.*;
import java.util.Random;



public class Main
{
  static final int NUM_NUT= 29;
  
  public static void main( String args[] )
  {
    Connection c = null;
    Statement stmt = null;
    try {
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:sr26.db");
      c.setAutoCommit(false);
      System.out.println("Opened database successfully");
         

      DatabaseMetaData md = c.getMetaData();
      ResultSet rs1 = md.getTables(null, null, "%", null);
      
      while (rs1.next()) {
      //  System.out.println("table1" + rs1.getString(3));
      }
      
      rs1.close();
      
      int[] nutri_id_tab = new int[] {
    		  301,   0, 312,   0,   0, 303, 304, 315,   0, 305, 317, 309, 306, 307, 0,
    		  320, 401, 326, 323, 430, 404, 405, 406, 415, 432, 418, 410,   0, 421,
    		  255, 205, 291, 203,
    		  269, 431, 601, 621, 629, 631};
      double[] female_19_50 = new double[] {
    		  1000, 25, 0.9, 3, 150, 18, 320, 1.8, 45, 700, 55, 8, 4700, 1500, 2.3, 
    		  700, 75, 15, 15, 90, 1.1, 1.1, 14, 1.3, 400, 2.4, 5, 30, 425} ; //NUM_NUT=29
        
      String[][] food_ids = new String[][] {
    		  	{"01001", "01082", "01117", "01123"}, //diary
    		  	{"05027", "05029", "05141", "05167"}, //poutry
    		  	{"09202", "09218", "09226", "09252"}, // "09504"}, //fruits
    		  	{"10005", "10211"}, //pork
    		  	{"11052", "11090", "11124", "11135"}, //"11333", "11352"}, //veg
    		  	{"23307", "23293"}, //beef
    		  	{"17300"}}; //lamb
      
      double[] nutr_sum_output = new double[NUM_NUT];
      
      //select diary
      int i, j, k, food_id;
      String sql;
      stmt = c.createStatement();
      
     
      for (i = 0; i < food_ids.length; i++)
    	  for (j = 0; j < food_ids[i].length; j++) 
    		  if (food_ids[i][j].length() != 0) {
    			  for (k = 0; k < NUM_NUT; k++) {
    				  if (nutri_id_tab[k]!=0) {
    					  sql = String.format("SELECT t.* FROM NUT_DATA t WHERE t.NDB_No = \"%s\" AND t.Nutr_No = %d", 
    							  food_ids[i][j].toString(), nutri_id_tab[k]);
    					  //System.out.println("test:" + i + " " + j + " " + k +" " + sql);
    					  ResultSet rs = stmt.executeQuery(sql);
    					  if (rs.next()) {
    						  //System.out.println("NDB_No: " + rs.getInt("NDB_No") + " Nutr_No:" + rs.getInt("Nutr_No") 
    							//	  + " Nutr_Val; " + rs.getDouble("Nutr_Val"));
    						  nutr_sum_output[k] += rs.getDouble("Nutr_Val");
    					  }
    					  rs.close();
    				  }
    			  }
    		  }
      
     
      for (i = 0; i < NUM_NUT; i++) {
    	  if (nutri_id_tab[i] != 0 ) {
    		  sql = String.format("SELECT t.* FROM NUTR_DEF t WHERE t.Nutr_No = %d", nutri_id_tab[i]);
    		  //System.out.println(sql);
    		  ResultSet rs = stmt.executeQuery(sql);
    		  System.out.println(i + " " + nutri_id_tab[i] + " " + rs.getString("NutrDesc") + " units:" + rs.getString("Units") + " " + 
    			  "std:" + female_19_50[i] + " " + String.format("%.2f", nutr_sum_output[i]*3/7));
    		  rs.close();
    	  }
      }
      
      System.out.println("food list");
      
      for (i = 0; i < food_ids.length; i++)
    	  for (j = 0; j < food_ids[i].length; j++) 
    		  if (food_ids[i][j].length() != 0) {
    			  sql = String.format("SELECT t.* FROM FOOD_DES t WHERE t.NDB_No = \"%s\"", food_ids[i][j].toString());
        		  //System.out.println(sql);
        		  ResultSet rs_1 = stmt.executeQuery(sql);
        		  String food_desc = rs_1.getString("Long_Desc"); 
        		  
        		  System.out.println(food_desc);
        		  rs_1.close();
        		         				  
    		  }
      
      stmt.close();
      c.close();
    } catch ( Exception e ) {
      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      System.exit(0);
    }
    System.out.println("Operation done successfully");
  }
}


