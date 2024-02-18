import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class StartingScreen extends JFrame implements ActionListener {
     private JButton nextButton;

     public StartingScreen() {
         setTitle("Starting Screen");
         setSize(1200, 463); // Set the size to match the image dimensions
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         setLayout(null); // Use absolute layout for positioning

         // Create a panel to hold the content
         JPanel contentPanel = new JPanel();
         contentPanel.setLayout(null); // Use absolute layout for positioning
         contentPanel.setBounds(0, 0, 1200, 463); // Set panel bounds to match frame size

         // Add the background image
         ImageIcon backgroundImage = new ImageIcon(".idea/res/travel41.jpg"); // Replace with your image file path
         JLabel backgroundLabel = new JLabel(backgroundImage);
         backgroundLabel.setBounds(0, 0, 1200, 463); // Set image bounds to match frame size
         contentPanel.add(backgroundLabel);

         // Add the "Next" button to the panel
         nextButton = new JButton("Next");
         nextButton.setBounds(550, 370, 100, 40); // Adjust position to center vertically and near the bottom
         nextButton.addActionListener(this);
         contentPanel.add(nextButton); // Add button to the panel

         // Add the content panel to the frame
         add(contentPanel);

         // Center the frame on the screen
         setLocationRelativeTo(null);
     }

     @Override
     public void actionPerformed(ActionEvent e) {
         if (e.getSource() == nextButton) {
             dispose(); // Close the starting screen window
             new PassengerInfoWindow().setVisible(true);

         }
     }

     public static void main(String[] args) {

         StartingScreen startingScreen = new StartingScreen();
         startingScreen.setVisible(true);


     }
 }

public class PassengerInfoWindow extends JFrame implements ActionListener {
    private JLabel nameLabel;
    private JTextField nameTextField;
    private JButton submitButton;
    private JLabel ageLabel;
    private JTextField ageTextField;

    public PassengerInfoWindow() {
        setTitle("Passenger Information");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 10, 10)); // GridLayout for better alignment and spacing

        nameLabel = new JLabel("Name:");
        nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(nameLabel);

        nameTextField = new JTextField();
        add(nameTextField);

        ageLabel = new JLabel("Age:");
        ageLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(ageLabel);

        ageTextField = new JTextField();
        add(ageTextField);

        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);
        add(submitButton);

        // Set font and size for all components
        Font font = new Font("Arial", Font.PLAIN, 16);
        nameLabel.setFont(font);
        nameTextField.setFont(font);
        ageLabel.setFont(font);
        ageTextField.setFont(font);
        submitButton.setFont(font);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            String passengerName = nameTextField.getText();
            String passengerAge = ageTextField.getText(); // Changed to lowercase 'age'
            JOptionPane.showMessageDialog(this, "Passenger Name: " + passengerName + "\nAge: " + passengerAge);
            dispose();
            // Passing name and age to PassengerWindow
            PassengerWindow window = new PassengerWindow(passengerName, passengerAge);
            window.setVisible(true);
        }
    }

    public static void main(String[] args) {
        PassengerInfoWindow infoWindow = new PassengerInfoWindow();
        infoWindow.setVisible(true);
    }
}

class PassengerWindow extends JFrame implements ActionListener {
    private JComboBox<String> flightOptions;
    private JLabel timeLabel;
    private JCheckBox extraBaggageCheckBox;
    private JCheckBox mealCheckBox;
    private JCheckBox wifiCheckBox;
    private JButton bookButton;
    private JButton cancelButton; // Added cancelButton
    private String[] flightList = {"Flight 1 - 10:00 AM", "Flight 2 - 1:00 PM", "Flight 3 - 4:00 PM"};
    private String passengerName;
    private String passengerAge;
    private BookingHistoryNode bookingHistoryHead;

    public PassengerWindow(String passengerName, String passengerAge) {
        this.passengerName = passengerName;
        this.passengerAge = passengerAge;
        this.bookingHistoryHead = null;

        setTitle("Flight Booking");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1, 10, 10)); // Increased the rows to accommodate cancelButton

        flightOptions = new JComboBox<>(flightList);
        add(flightOptions);

        timeLabel = new JLabel("Selected Time:");
        add(timeLabel);

        extraBaggageCheckBox = new JCheckBox("Extra Baggage");
        add(extraBaggageCheckBox);

        mealCheckBox = new JCheckBox("Meal");
        add(mealCheckBox);

        wifiCheckBox = new JCheckBox("WiFi");
        add(wifiCheckBox);

        bookButton = new JButton("Book Flight");
        bookButton.addActionListener(this);
        add(bookButton);
        
        cancelButton = new JButton("Cancel Booking"); // Added cancelButton
        cancelButton.addActionListener(this);
        add(cancelButton);

        // Set font for all components
        Font font = new Font("Arial", Font.PLAIN, 16);
        flightOptions.setFont(font);
        timeLabel.setFont(font);
        extraBaggageCheckBox.setFont(font);
        mealCheckBox.setFont(font);
        wifiCheckBox.setFont(font);
        bookButton.setFont(font);
        cancelButton.setFont(font);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bookButton) {
            String selectedOption = (String) flightOptions.getSelectedItem();
            StringBuilder extraServices = new StringBuilder();
            if (extraBaggageCheckBox.isSelected()) {
                extraServices.append(" with Extra Baggage");
            }
            if (mealCheckBox.isSelected()) {
                if (extraServices.length() > 0) {
                    extraServices.append(", ");
                }
                extraServices.append(" with Meal");
            }
            if (wifiCheckBox.isSelected()) {
                if (extraServices.length() > 0) {
                    extraServices.append(", ");
                }
                extraServices.append(" with WiFi");
            }
            String bookingInfo = passengerName + ", Age " + passengerAge + ", booked flight for " + selectedOption + extraServices;
            addToBookingHistory(bookingInfo);
            System.out.println("Booking History for " + passengerName + ": " + getLastBookingInfo());
            JOptionPane.showMessageDialog(this, "Flight booked for " + bookingInfo);
        } else if (e.getSource() == cancelButton) { // Handle cancellation
            cancelBooking();
        } else if (e.getSource() == flightOptions) {
            String selectedOption = (String) flightOptions.getSelectedItem();
            timeLabel.setText("Selected Time: " + selectedOption);
        }
    }

    private void addToBookingHistory(String bookingInfo) {
        BookingHistoryNode newNode = new BookingHistoryNode(bookingInfo);
        if (bookingHistoryHead == null) {
            bookingHistoryHead = newNode;
        } else {
            BookingHistoryNode current = bookingHistoryHead;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
    }

    private String getLastBookingInfo() {
        if (bookingHistoryHead == null) {
            return "No booking history";
        }
        BookingHistoryNode current = bookingHistoryHead;
        while (current.next != null) {
            current = current.next;
        }
        return current.bookingInfo;
    }

    private void cancelBooking() {
        if (bookingHistoryHead != null) {
            BookingHistoryNode previous = null;
            BookingHistoryNode current = bookingHistoryHead;
            while (current.next != null) {
                previous = current;
                current = current.next;
            }
            if (previous != null) {
                previous.next = null;
            } else {
                bookingHistoryHead = null;
            }
            System.out.println("Booking Canceled for " + passengerName);
            JOptionPane.showMessageDialog(this, "Booking Canceled for " + passengerName);
            
        } else {
            System.out.println("No booking to cancel for " + passengerName);
            JOptionPane.showMessageDialog(this, "No booking to cancel for " + passengerName);
        }
    }

    private static class BookingHistoryNode {
        String bookingInfo;
        BookingHistoryNode next;

        BookingHistoryNode(String bookingInfo) {
            this.bookingInfo = bookingInfo;
            this.next = null;
        }
    }
}

