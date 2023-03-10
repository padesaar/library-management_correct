package entities;

import db.Database;
import lombok.Data;
import lombok.NoArgsConstructor;
import menu.Menu;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.*;
import javax.print.attribute.standard.DateTimeAtCompleted;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static db.ErrorMessage.errorMessage;

@Entity(name = "book")
@Data
@NoArgsConstructor
public class Book {
    static Session session = Database.getHibSesh();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int b_id;

    @Column(name = "book_title")
    private String title;

    @Column(name = "category")
    private String category;

    @Column(name = "qty_in_library")
    private int qtyInLibrary;


    @OneToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @Column(name = "isAvailable")
    private boolean isAvailable;

    public Book(String title, String category, int qtyInLibrary, int authorId, boolean isAvailable) {
        this.title = title;
        this.category = category;
        this.qtyInLibrary = qtyInLibrary;
        this.author.setA_id(authorId);
        this.isAvailable = isAvailable;
    }


    public static void addBook() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hi, you wonderful admin! You are here to add a book");

        System.out.println("Enter the title of the book:");
        String title = scanner.nextLine();


        System.out.println("Enter the category of the book:");
        scanner.next();
        String category = scanner.nextLine();


        System.out.println("Enter the author of the new book:");
        scanner.next();
        String authorName = scanner.nextLine();


        System.out.println("Enter the quantity of this book:");
        int qty = scanner.nextInt();

        session.beginTransaction();
        Transaction trans = session.getTransaction();

        try {
            Author author = new Author();
            author.setAuthorName(authorName);
            session.persist(author);
            Book book = new Book();
            book.setTitle(title);
            book.setCategory(category);
            book.setAuthor(author);
            book.setAvailable(true);
            book.setQtyInLibrary(qty);

            session.persist(book);
            session.flush();
            trans.commit();
        } catch (Exception e) {
            trans.rollback();
            e.printStackTrace();
        }
    }

    public static void updateBook() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello, please enter book's ISBN:");
        int id = scanner.nextInt();

        session.beginTransaction();
        Transaction trans = session.getTransaction();
        Book book = session.get(Book.class, id);
        System.out.println("Book you want to update is:" + session.get(Book.class, book.getB_id()));
        System.out.println("Now there are few thing to update. Stay in line.");

        System.out.println("Enter books's new title:");
        scanner.nextLine();
        String title = scanner.nextLine();

        System.out.println("Enter book's category:");
        String category = scanner.next();

        System.out.println("Enter the quantity of the book:");
        int qty = scanner.nextInt();

        System.out.println("Enter if the book is available in the library or not(true/false)");
        boolean availability = scanner.nextBoolean();

        System.out.println("Book with the inserted id will be updated. Please hold for further information...");


        try {
            book.setTitle(title);
            book.setCategory(category);
            book.setAvailable(true);
            book.setQtyInLibrary(qty);
            session.merge(book);
            session.flush();
            trans.commit();
        } catch (Exception e) {
            trans.rollback();
            e.printStackTrace();
        }
        System.out.println("Thank you for update");
        System.out.println("Updated info is here:");
        System.out.println("Title of the book: " +title);
        System.out.println("Category of the book:" +category);
        System.out.println("Quantity in the library: " +qty);
        System.out.println("And the book is available: " +availability);
    }
    public static void deleteBook() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello, please enter book's ISBN:");
        int isbn = scanner.nextInt();
        System.out.println("Book with the inserted ISBN will be deleted. Please hold for further information...");
        session.beginTransaction();
        Transaction trans = session.getTransaction();
        Book book = session.get(Book.class, isbn);

        try {
            session.delete(book);
            session.flush();
            trans.commit();
        } catch (Exception e) {
            trans.rollback();
            e.printStackTrace();
        }
        System.out.println("Book with the id: " +isbn+ "is deleted. Thank you!");
    }

    // Collections can be used to store the books in the library's collection.
    public static void listBook() {
        System.out.println("Here are the list of books we have in our library:");

        try {
            session.beginTransaction();
            List<Book> books = session.createQuery("from book").list();

            for (Book book : books) {
                System.out.println(book);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static Scanner sc = new Scanner(System.in);

    // Users should be able to search and view books in the library's collection by entering the ISBN
    // or title of the book.


    // Admins should be able to add, update, and delete books from the collection,
    // as well as manage user accounts via the console interface.


    // Users should be able to check out and return books by entering the ISBN of the book,
    // with the option to place a hold on a book if it is currently checked out.


    public static void returnBook(int isbn) {
        Session session = Database.getHibSesh();
        try {
            session.beginTransaction();
            Book book = session.get(Book.class, isbn);
            if (!book.isAvailable) {
                System.out.println("Are you sure you want to return this book? (yes/no)");
                Scanner scanner = new Scanner(System.in);
                String confirm = scanner.nextLine();
                if (confirm.equalsIgnoreCase("yes")) {
                    System.out.println("How many books you want return?");
                    int qty = scanner.nextInt();
                    book.setQtyInLibrary(book.getQtyInLibrary() + qty);
                    book.setAvailable(true);
                    session.merge(book);
                    session.flush();
                    System.out.println("Book returned successfully!");
                } else {
                    System.out.println("Return cancelled.");
                }
            } else {
                System.out.println("Book has not been checked out.");
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void checkoutBookByTitle(String title) {
        Session session = Database.getHibSesh();


        try {
            session.beginTransaction();
            List<Book> books = session.createQuery("from book where title = :title", Book.class)
                    .setParameter("title", title)
                    .getResultList();
            if (books.size() > 0) {
                Book book = books.get(0);
                if (book.isAvailable) {
                    System.out.println("Are you sure you want to checkout this book? (yes/no)");
                    Scanner scanner = new Scanner(System.in);
                    String confirm = scanner.nextLine();
                    if (confirm.equalsIgnoreCase("yes")) {
                        System.out.println("How many books you want checkout?");
                        int qty = scanner.nextInt();
                        book.setQtyInLibrary(book.getQtyInLibrary() - qty);
                        book.setAvailable(true);
                        session.merge(book);
                        session.flush();
                        System.out.println("Checkout was successfully!");
                    } else {
                        System.out.println("Checkout cancelled.");
                    }
                } else {
                    System.out.println("Book has not been checked out.");
                }
            } else {
                System.out.println("No book found with that title.");
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public boolean isBookAvailable(int qty) {

        if (this.qtyInLibrary > 0 || this.qtyInLibrary > qty) {
            return true;
        } else {
            System.out.println("Sorry this book is out of stock at the moment. Come back later!");
            return false;
        }

    }

    public static void issueBookByIsbn() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your id:");
        int id = scanner.nextInt();

        System.out.println("Enter a book isbn to check if it's available:");
        int bk = scanner.nextInt();

        System.out.println("How many books you would like to rent?");
        int qty = scanner.nextInt();

        session.beginTransaction();
        Transaction trans = session.getTransaction();
        Book book = session.get(Book.class, bk);
        Client client = session.get(Client.class, id);

        if (book.isBookAvailable(qty)) {
            try {
                Rent rent = new Rent();
                rent.setBook(book);
                rent.setIssueDate(Timestamp.valueOf(Rent.issueDate()));
                rent.setDueDate(Timestamp.valueOf(Rent.dueDate()));
                rent.setClient(client);
                rent.setReturned(false);
                session.merge(client);
                session.merge(book);
                session.persist(rent);
                session.flush();
                trans.commit();
            } catch (Exception e) {
                System.out.println("Something went wrong. Try again or go back to the main menu");
                System.out.println("Press 1 to start again or 0 to go back to the main menu");
                int option = scanner.nextInt();

                switch (option) {
                    case 0:
                        Menu.mainMenu();
                        break;
                    case 1:
                        Book.issueBookByIsbn();
                        break;
                }
            }
        } else {
            System.out.println("This book is not available at the moment. Come back later.");
            System.out.println("You can press 1 to rent another book or 0 to go back to the main menu");
            int option = scanner.nextInt();

            switch (option) {
                case 0:
                    Menu.mainMenu();
                    break;
                case 1:
                    Book.issueBookByIsbn();
                    break;
            }
        }
        System.out.println("The book you rented is: " + book);
        Menu.clientMenu();
    }

    public static void returnBookByIsbn() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your id:");
        int id = scanner.nextInt();

        System.out.println("Enter a book's isbn to return:");
        int bk = scanner.nextInt();

        System.out.println("How many books you will return?");
        int qty = scanner.nextInt();

        session.beginTransaction();
        Transaction trans = session.getTransaction();
        Book book = session.get(Book.class, bk);
        Rent rent = session.get(Rent.class, bk);
        Client client = session.get(Client.class, id);
            try {
                rent.setClient(client);
                rent.setBook(book);
                rent.setReturned(true);
                book.setQtyInLibrary(book.qtyInLibrary + qty);
                book.setAvailable(true);

                session.merge(client);
                session.merge(book);
                session.persist(rent);
                session.flush();
                trans.commit();
            } catch (Exception e) {
                System.out.println("Something went wrong. Try again or go back to the main menu");
                System.out.println("Press 1 to start again or 0 to go back to the main menu");
                int option = scanner.nextInt();

                switch (option) {
                    case 0:
                        Menu.mainMenu();
                        break;
                    case 1:
                        Book.issueBookByIsbn();
                        break;
                }
            }


        System.out.println("The book you rented is returned! Thank you!");
        System.out.println("Press 0 to return to our main menu");
        int option = scanner.nextInt();
        switch (option) {
            case 0:
                Menu.mainMenu();
                break;
        }
    }

    public static void searchBookByIsbn() {
        Scanner scanner = new Scanner(System.in);

        while (true) {

            try {
                System.out.println("Enter ISBN of the book to search: ");
               int b_id = scanner.nextInt();
                session.beginTransaction();
                List<Book> books = session.createQuery("from book where b_id = :b_id", Book.class)
                        .setParameter("b_id", b_id)
                        .getResultList();
                if (books.isEmpty()) {
                    System.out.println("No books found for the search term: " + b_id);
                } else {
                    for (Book book : books) {
                        System.out.println("ISBN: " + book.getB_id() + ", Title: " + book.getTitle() + ", Author: " + book.getAuthor());
                    }
                }
                System.out.println("Enter 0 to exit or any other key to continue search:");
                int choice = scanner.nextInt();
                if (choice == 0) {
                    System.exit(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void searchBookByTitle() {
        Scanner scanner = new Scanner(System.in);

        while (true) {

            try {
                System.out.println("Enter ISBN of the book to search: ");
                String book_title = scanner.nextLine();
                session.beginTransaction();
                List<Book> books = session.createQuery("from book where book_title = :book_title", Book.class)
                        .setParameter("book_title", book_title)
                        .getResultList();
                if (books.isEmpty()) {
                    System.out.println("No books found for the search term: " + book_title);
                } else {
                    for (Book book : books) {
                        System.out.println("ISBN: " + book.getB_id() + ", Title: " + book.getTitle() + ", Author: " + book.getAuthor());
                    }
                }
                System.out.println("Enter 0 to exit or any other key to continue search:");
                int choice = scanner.nextInt();
                if (choice == 0) {
                    System.exit(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

//       session.update(qtyInLibrary - qty); - if the rent is made