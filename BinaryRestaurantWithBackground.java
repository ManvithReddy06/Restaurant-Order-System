import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BinaryRestaurantWithBackground {

    private static Map<String, Integer> prices = new HashMap<>();
    private static JLabel displayLabel, orderTotalLabel, orderTransactionLabel, orderIDLabel;
    private static String currentOrder = "";
    private static int totalAmount = 0;

    static {
        prices.put("Fried Calamari", 140);
        prices.put("Beach Burger", 220);
        prices.put("Salmon Wonder", 250);
        prices.put("Shrimp Tacos", 450);
        prices.put("Sushi Platter", 320);
        prices.put("Empanadas", 150);
    }

    public static void main(String[] args) {
        // Load background image
        ImageIcon backgroundImage = new ImageIcon("image rest.png");

        // Create main frame with specific dimensions
        JFrame frame = new JFrame("TTC - Binary Restaurant");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(850, 500);  // Same dimensions as your Python GUI

        // Create a custom panel to set the background image
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this); // Scale image to fit
            }
        };
        backgroundPanel.setLayout(null);  // Use absolute layout for custom positioning

        // Order Section (Right Side)
        JPanel orderPanel = new JPanel();
        orderPanel.setBounds(550, 20, 270, 400); // Panel size and position
        orderPanel.setLayout(null);              // No layout manager
        orderPanel.setOpaque(false);             // Make panel transparent to see the background

        orderIDLabel = new JLabel("ORDER ID: " + generateOrderID());
        orderIDLabel.setBounds(10, 10, 250, 20);
        orderPanel.add(orderIDLabel);

        orderTransactionLabel = new JLabel("<html>Transaction:<br>" + currentOrder + "</html>");
        orderTransactionLabel.setBounds(10, 40, 250, 200);
        orderPanel.add(orderTransactionLabel);

        orderTotalLabel = new JLabel("TOTAL: ₹0");
        orderTotalLabel.setBounds(10, 260, 250, 20);
        orderPanel.add(orderTotalLabel);

        JButton orderButton = new JButton("ORDER");
        orderButton.setBounds(10, 300, 250, 40);
        orderPanel.add(orderButton);
        orderButton.addActionListener(e -> processOrder());

        // Menu Section (Left Side)
        JPanel menuPanel = new JPanel();
        menuPanel.setBounds(20, 20, 250, 400); // Menu panel size
        menuPanel.setLayout(new GridLayout(6, 1, 10, 10)); // Grid of buttons for menu items
        menuPanel.setOpaque(false); // Make panel transparent

        // Menu buttons
        createMenuButton(menuPanel, "Fried Calamari");
        createMenuButton(menuPanel, "Beach Burger");
        createMenuButton(menuPanel, "Salmon Wonder");
        createMenuButton(menuPanel, "Shrimp Tacos");
        createMenuButton(menuPanel, "Sushi Platter");
        createMenuButton(menuPanel, "Empanadas");

        // Display Section (Center)
        JPanel displayPanel = new JPanel();
        displayPanel.setBounds(290, 20, 250, 400); // Display panel size
        displayPanel.setLayout(null);
        displayPanel.setOpaque(false); // Make panel transparent

        displayLabel = new JLabel("Select an item to display");
        displayLabel.setBounds(10, 10, 230, 40);
        displayPanel.add(displayLabel);

        JButton addButton = new JButton("ADD TO ORDER");
        addButton.setBounds(10, 60, 230, 40);
        displayPanel.add(addButton);

        JButton removeButton = new JButton("REMOVE");
        removeButton.setBounds(10, 120, 230, 40);
        displayPanel.add(removeButton);

        addButton.addActionListener(e -> addItem());
        removeButton.addActionListener(e -> removeItem());

        // Add all sections to the background panel
        backgroundPanel.add(orderPanel);
        backgroundPanel.add(menuPanel);
        backgroundPanel.add(displayPanel);

        // Add the background panel to the main frame
        frame.add(backgroundPanel);

        // Display the frame
        frame.setVisible(true);
    }

    private static void createMenuButton(JPanel menuPanel, String itemName) {
        JButton button = new JButton(itemName);
        menuPanel.add(button);
        button.addActionListener(e -> displayItem(itemName));
    }

    private static void displayItem(String itemName) {
        displayLabel.setText(itemName + " - ₹" + prices.get(itemName));
    }

    private static void addItem() {
        String selectedItem = displayLabel.getText().split(" - ")[0];
        if (prices.containsKey(selectedItem)) {
            currentOrder += selectedItem + " - ₹" + prices.get(selectedItem) + "<br>";
            orderTransactionLabel.setText("<html>Transaction:<br>" + currentOrder + "</html>");
            totalAmount += prices.get(selectedItem);
            orderTotalLabel.setText("TOTAL: ₹" + totalAmount);
        }
    }

    private static void removeItem() {
        String selectedItem = displayLabel.getText().split(" - ")[0];
        if (prices.containsKey(selectedItem) && currentOrder.contains(selectedItem)) {
            currentOrder = currentOrder.replaceFirst(selectedItem + " - ₹" + prices.get(selectedItem) + "<br>", "");
            orderTransactionLabel.setText("<html>Transaction:<br>" + currentOrder + "</html>");
            totalAmount -= prices.get(selectedItem);
            orderTotalLabel.setText("TOTAL: ₹" + totalAmount);
        }
    }

    private static void processOrder() {
        String receiptID = orderIDLabel.getText().replace("ORDER ID: ", "");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        try (FileWriter writer = new FileWriter(receiptID + ".txt")) {
            writer.write("Binary Restaurant\n");
            writer.write("Receipt: " + receiptID + "\n");
            writer.write(dateFormat.format(new Date()) + " " + timeFormat.format(new Date()) + "\n\n");
            writer.write(currentOrder.replace("<br>", "\n"));
            writer.write("\nTOTAL: ₹" + totalAmount);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(null, "Order placed successfully!");

        // Reset Order
        currentOrder = "";
        orderTransactionLabel.setText("<html>Transaction:<br>" + currentOrder + "</html>");
        totalAmount = 0;
        orderTotalLabel.setText("TOTAL: ₹0");
        orderIDLabel.setText("ORDER ID: " + generateOrderID());
    }

    private static String generateOrderID() {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuilder id = new StringBuilder("BIN_");

        for (int i = 0; i < 3; i++) {
            id.append(letters.charAt(random.nextInt(letters.length())));
        }

        for (int i = 0; i < 3; i++) {
            id.append(random.nextInt(10));
        }

        return id.toString();
    }
}
