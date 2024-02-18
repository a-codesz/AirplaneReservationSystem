import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PassengerInfoWindow extends JFrame implements ActionListener {
    private JLabel nameLabel;
    private JTextField nameTextField;
    private JButton submitButton;

    public PassengerInfoWindow() {
        setTitle("Passenger Information");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        nameLabel = new JLabel("Name: ");
        add(nameLabel);

        nameTextField = new JTextField(20);
        add(nameTextField);

        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);
        add(submitButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            String passengerName = nameTextField.getText();
            JOptionPane.showMessageDialog(this, "Passenger Name: " + passengerName);
            dispose(); 
            PassengerWindow window = new PassengerWindow(passengerName);
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
    private String[] flightList = {"Flight 1 - 10:00 AM", "Flight 2 - 1:00 PM", "Flight 3 - 4:00 PM"};
    private String passengerName;
    private BookingHistoryNode bookingHistoryHead;

    public PassengerWindow(String passengerName) {
        this.passengerName = passengerName;
        this.bookingHistoryHead = null;

        setTitle("Flight Booking");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        flightOptions = new JComboBox<>(flightList);
        flightOptions.addActionListener(this);
        add(flightOptions);

        timeLabel = new JLabel("Selected Time: ");
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
            String bookingInfo = passengerName + " booked flight for " + selectedOption + extraServices;
            addToBookingHistory(bookingInfo); 
            System.out.println("Booking History for " + passengerName + ": " + getLastBookingInfo());
            JOptionPane.showMessageDialog(this, "Flight booked for " + bookingInfo);
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

   
    private static class BookingHistoryNode {
        String bookingInfo;
        BookingHistoryNode next;

        BookingHistoryNode(String bookingInfo) {
            this.bookingInfo = bookingInfo;
            this.next = null;
        }
    }
}
