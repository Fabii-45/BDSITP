package application;
import java.sql.*;

public class ConnexionSQL {
    public static void main(String args[]){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection connexion=DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/MUSEE","root","password");
            //musee est ici le nom de notre base de données
            Statement stmt=connexion.createStatement();
            ResultSet rs=stmt.executeQuery("select * from SALLE");
            // stmt.executeAndUpdate("Insert ...") pour l'ajout de données
            while(rs.next())
                System.out.println(rs.getInt(1)+"  "+rs.getString(2));
            connexion.close();
        }
        catch(Exception e){ System.err.println(e);}
    }


}

