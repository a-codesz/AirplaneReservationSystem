import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Class representing the main application window
public class AirplaneReserve extends JFrame {
    private PassengerDetailsWindow passengerDetailsWindow;
    private BookingDetailsWindow bookingDetailsWindow;
    private FlightList flightList;

    public AirplaneReserve() {
        super("Airplane Reservation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());

        flightList = new FlightList();
        initializeFlights();

        // Create passenger details window
        passengerDetailsWindow = new PassengerDetailsWindow(this);

        // Create booking details window
        bookingDetailsWindow = new BookingDetailsWindow(this);
        
        // Initially show passenger details window
        passengerDetailsWindow.setVisible(true);
    }

    // Method to initialize flights
    private void initializeFlights() {
        flightList.addFlight(new Flight("ABC123", "City A", "City B", "10am to 12am", 500.0));
        flightList.addFlight(new Flight("DEF456", "City A", "City C", "7am to 9am", 400.0));
        flightList.addFlight(new Flight("GHI789", "City A", "City D", "11am to 6pm", 600.0));
    }

    // Method to display booking details window
    public void displayBookingDetailsWindow(Passenger passenger, String bookingNumber) {
        bookingDetailsWindow.setPassengerAndBooking(passenger, bookingNumber);
        bookingDetailsWindow.setVisible(true);
    }

    // Method to display sorted flights
    public void displaySortedFlights(String criteria) {
        flightList.mergeSort(criteria);
        JOptionPane.showMessageDialog(this, "Flights sorted by " + criteria);
    }

    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AirplaneReserve app = new AirplaneReserve();
            app.setVisible(false);
        });
    }
}

// Class representing the window for entering passenger details
class PassengerDetailsWindow extends JFrame {
    private JTextField nameField;
    private JTextField ageField;
    private JButton bookButton;

    public PassengerDetailsWindow(AirplaneReserve parent) {
        super("Passenger Details");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null); // Center the window
        setLayout(new GridLayout(3, 2));

        nameField = new JTextField();
        ageField = new JTextField();
        bookButton = new JButton("Book Flight");

        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Age:"));
        add(ageField);
        add(bookButton);

        // Action listener for the book button
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());
                Passenger passenger = new Passenger(name, age);
                parent.displayBookingDetailsWindow(passenger, "12345"); // Dummy booking number
                dispose(); // Close passenger details window
            }
        });
    }
}

// Class representing the window for viewing booking details and sorting flights
class BookingDetailsWindow extends JFrame {
    private JLabel bookingLabel;

    public BookingDetailsWindow(AirplaneReserve parent) {
        super("Booking Details");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());

        bookingLabel = new JLabel();

        JButton sortButton = new JButton("Sort Flights");
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String criteria = JOptionPane.showInputDialog("Enter sort criteria (departureTime/price):");
                parent.displaySortedFlights(criteria);
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(bookingLabel);

        add(panel, BorderLayout.CENTER);
        add(sortButton, BorderLayout.SOUTH);
    }

    // Method to set passenger details and booking information
    public void setPassengerAndBooking(Passenger passenger, String bookingNumber) {
        String bookingInfo = "Booking Number: " + bookingNumber + "<br>" +
                "Passenger: " + passenger.getName() + "<br>" +
                "Age: " + passenger.getAge();
        bookingLabel.setText("<html>" + bookingInfo + "</html>");
    }
}

// Class representing a flight
class Flight {
    private String flightNumber; // Flight number
    private String source; // Source location
    private String destination; // Destination location
    private String departureTime; // Departure time
    private double price; // Price of the flight
    private String bookingNumber; // Booking number associated with the flight

    // Constructor to initialize a flight with details
    public Flight(String flightNumber, String source, String destination, String departureTime, double price) {
        this.flightNumber = flightNumber;
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.price = price;
        this.bookingNumber = null; // Initialize booking number to null
    }

    // Getter methods to access flight details
    public String getFlightNumber() {
        return flightNumber;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public double getPrice() {
        return price;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    // Method to book the flight with a booking number
    public void book(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    // Override toString method to provide a string representation of the flight
    @Override
    public String toString() {
        return "Flight Number: " + getFlightNumber() +
                "\nSource: " + getSource() +
                "\nDestination: " + getDestination() +
                "\nDeparture Time: " + getDepartureTime() +
                "\nPrice: " + getPrice();
    }
}

// Class representing a passenger
class Passenger {
    private String name; // Passenger name
    private int age; // Passenger age

    // Constructor to initialize a passenger with name and age
    public Passenger(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Getter methods to access passenger details
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}

// Class representing a reservation
abstract class Reservation {
    protected Flight flight; // Flight object associated with the reservation
    protected Passenger passenger; // Passenger object associated with the reservation

    // Constructor to initialize a reservation with a flight and passenger
    public Reservation(Flight flight, Passenger passenger) {
        this.flight = flight;
        this.passenger = passenger;
    }

    // Abstract method to get reservation details
    public abstract String getReservationDetails();

    // Getter method to get the flight associated with the reservation
    public Flight getFlight() {
        return flight;
    }

    // Getter method to get the passenger associated with the reservation
    public Passenger getPassenger() {
        return passenger;
    }
}

// Class representing a booking
class Booking extends Reservation implements Bookable {
    public String bookingNumber; // Booking number associated with the booking

    // Constructor to initialize a booking with flight, passenger, and booking number
    public Booking(Flight flight, Passenger passenger, String bookingNumber) {
        super(flight, passenger);
        this.bookingNumber = bookingNumber;
    }

    // Getter method to get the booking number
    public String getBookingNumber() {
        return bookingNumber;
    }

    // Setter method to set the booking number
    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    // Method to book the reservation with a booking number
    @Override
    public void book(String bookingNumber) {
        setBookingNumber(bookingNumber);
    }

    // Method to get reservation details
    @Override
    public String getReservationDetails() {
        return "Booking Number: " + bookingNumber +
                "\nFlight Number: " + getFlight().getFlightNumber() +
                "\nSource: " + getFlight().getSource() +
                "\nDestination: " + getFlight().getDestination() +
                "\nPassenger: " + getPassenger().getName() +
                "\nPassenger Age: " + getPassenger().getAge();
    }
}

// Class representing a list of flights
class FlightList {
    private FlightNode head; // Reference to the first FlightNode in the list

    // Method to add a new flight to the list
    public void addFlight(Flight flight) {
        FlightNode newNode = new FlightNode(flight);
        if (head == null) {
            head = newNode; // If the list is empty, set the new node as the head
        } else {
            FlightNode current = head;
            while (current.next != null) {
                current = current.next; // Traverse to the end of the list
            }
            current.next = newNode; // Append the new node to the end of the list
        }
    }

    // Method to display all flights in the list
    public void displayFlights() {
        FlightNode current = head;
        while (current != null) {
            System.out.println(current.flight); // Print the flight details
            current = current.next; // Move to the next node
        }
    }

    // Method to search for a flight by its flight number
    public FlightNode searchFlight(String flightNumber) {
        FlightNode current = head;
        while (current != null) {
            if (current.flight.getFlightNumber().equals(flightNumber)) {
                return current; // Return the node if flight number matches
            }
            current = current.next; // Move to the next node
        }
        return null; // Return null if flight not found
    }

    // Method to perform merge sort on the list based on a specified criteria
    public void mergeSort(String criteria) {
        head = mergeSort(head, criteria);
    }

    // Recursive method to perform merge sort on a list
    private FlightNode mergeSort(FlightNode head, String criteria) {
        if (head == null || head.next == null) {
            return head; // Base case: return head if list is empty or has only one node
        }
        FlightNode middle = getMiddle(head); // Get the middle node of the list
        FlightNode nextOfMiddle = middle.next;
        middle.next = null; // Split the list into two halves
        FlightNode left = mergeSort(head, criteria); // Recursively sort the left half
        FlightNode right = mergeSort(nextOfMiddle, criteria); // Recursively sort the right half
        return merge(left, right, criteria); // Merge the sorted halves
    }

    // Method to merge two sorted lists based on a specified criteria
    private FlightNode merge(FlightNode left, FlightNode right, String criteria) {
        FlightNode result = null;
        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }
        if (criteria.equals("departureTime")) {
            if (left.flight.getDepartureTime().compareTo(right.flight.getDepartureTime()) <= 0) {
                result = left;
                result.next = merge(left.next, right, criteria);
            } else {
                result = right;
                result.next = merge(left, right.next, criteria);
            }
        } else if (criteria.equals("price")) {
            if (left.flight.getPrice() <= right.flight.getPrice()) {
                result = left;
                result.next = merge(left.next, right, criteria);
            } else {
                result = right;
                result.next = merge(left, right.next, criteria);
            }
        }
        return result;
    }

    // Method to find the middle node of a list
    private FlightNode getMiddle(FlightNode head) {
        if (head == null) {
            return head;
        }
        FlightNode slow = head;
        FlightNode fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }
}

// Node class to hold Flight object in a linked list
class FlightNode {
    Flight flight; // Flight object
    FlightNode next; // Reference to the next FlightNode

    // Constructor to initialize FlightNode with a Flight object
    public FlightNode(Flight flight) {
        this.flight = flight;
        this.next = null;
    }
}

// Interface representing bookable entities
interface Bookable {
    void book(String bookingNumber); // Method to book an entity with a booking number
}

// Interface representing seat availability
interface SeatAvailability {
    int getAvailableSeats(); // Method to get the number of available seats
    int getTotalSeats(); // Method to get the total number of seats
}

// Class representing seat availability
class Seats implements SeatAvailability {
    private int totalSeats; // Total number of seats
    private int availableSeats; // Number of available seats

    // Constructor to initialize seats with total and available seats
    public Seats(int totalSeats, int availableSeats) {
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
    }

    // Method to get the number of available seats
    @Override
    public int getAvailableSeats() {
        return availableSeats;
    }

    // Method to get the total number of seats
    @Override
    public int getTotalSeats() {
        return totalSeats;
    }
}
