package org.example;

import db.Database;
import entities.Author;
import entities.Book;
import entities.Client;
import entities.Rent;
import org.hibernate.Session;

import java.sql.Timestamp;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {


        Scanner scanner = new Scanner(System.in);
        //  Session session = Database.getHibSesh();

        //    Book.listBook();

        // Client nick = new Client("Barry Tooms", "barry@gmail.com");
        // Client.addClient(nick);


        //Client client1 = new Client ("Daisy Duck", "daisy@gmail.com");
        //Client.addClient(client1);
        // Client client2 = new Client ("Donald Duck", "donald@gmail.com");
        // Client.addClient(client2);
        /// Client client3 = new Client ("Hans Goose", "hans@gmail.com");
        // Client.addClient(client3);
        // Client client4 = new Client ("Mickey Mouse", "mickey@gmail.com");
        // Client.addClient(client4);
        // Client client5 = new Client ("Minnie Mouse", "minnie@gmail.com");
        // Client.addClient(client5);
        // Client client6 = new Client ("Goofy Gooferson", "goofy@gmail.com");
        // Client.addClient(client6);
        // Client client7 = new Client ("Robert The Millionaire Duck", "robert@gmail.com");
        // Client.addClient(client7);
        // Client client8 = new Client ("Pluto The Dog", "pluto@gmail.com");
        // Client.addClient(client8);
        //Client.listClient();

        //Rent.listRent();

        //Rent rent = new Rent(15, "Timestamp.valueOf('2023-01-17 12:02:12'"), Timestamp.valueOf("'2023-01-17 12:02:12'"), 5, true);
        // Rent.createNewRent(rent);

        // Author jhonny = new Author("Jhonny Depp", "Autobiography");
        // Author.addAuthor(jhonny);
        //Author.listAuthor();

        //Book.checkOutBook();
        //Rent.listRent();
        //Book.listBook();

        // Client client9 = new Client("Hubbie Horse", "hubbie@gmail.com");
        // Client.addClient(client9);


        //Author.listAuthor(); OK

        // Client.addNewClientByClient(new Client()); OK
        // Client.deleteClient(9);
        // Client.deleteClient(10);
        //  Client.listClient();
        // Client.deleteClientByAdmin(); //Delete by admin OK
        // Client.listClient();

        // Rent rent = new Rent(14, Timestamp.valueOf("2023-01-17 12:02:12"), Timestamp.valueOf("2023-01-17 12:02:12"), 4, true);
        // Rent.createNewRent(rent);
        Rent.listRent();

        // Book.checkBook();
        // Client.updateClientByAdmin();
        // Client.addNewClientByAdmin();



    }
}