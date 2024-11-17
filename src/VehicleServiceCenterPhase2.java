import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class VehicleServiceCenterPhase2 extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextArea textArea;
    private Queue<CustomerInfo> customerQueue;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        EventQueue.invokeLater(() -> {
            try {
                VehicleServiceCenterPhase2 frame = new VehicleServiceCenterPhase2();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public VehicleServiceCenterPhase2() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1024, 768);
        setTitle("Vehicle Service Center");

        // Modern content pane setup with gradient background
        contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(240, 242, 245), 0, h, new Color(225, 228, 232));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setLayout(new BorderLayout(20, 20));
        setContentPane(contentPane);

        // Modern Header Panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setOpaque(false);
        JLabel lblTitle = new JLabel("Vehicle Service Center");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(new Color(51, 51, 51));
        headerPanel.add(lblTitle);
        contentPane.add(headerPanel, BorderLayout.NORTH);

        // Modern Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        buttonPanel.setOpaque(false);
        
        // Modern button styling
        JButton btnLoadData = createStyledButton("Load Customer Data", new Color(52, 152, 219));
        JButton btnAddCustomer = createStyledButton("Add Customer", new Color(46, 204, 113));
        JButton btnRemoveCustomer = createStyledButton("Remove Customer", new Color(231, 76, 60));

        buttonPanel.add(btnLoadData);
        buttonPanel.add(btnAddCustomer);
        buttonPanel.add(btnRemoveCustomer);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        // Modern Text Area
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setBackground(new Color(255, 255, 255));
        textArea.setForeground(new Color(51, 51, 51));
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10),
            BorderFactory.createLineBorder(new Color(200, 200, 200))
        ));
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Action Listeners
        btnLoadData.addActionListener(this::loadCustomerData);
        btnAddCustomer.addActionListener(this::addCustomer);
        btnRemoveCustomer.addActionListener(this::removeCustomer);

        customerQueue = new LinkedList<>();
    }

    // Helper method to create styled buttons
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(color.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(color.brighter());
                } else {
                    g2.setColor(color);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setPreferredSize(new Dimension(180, 40));
        return button;
    }

    private void loadCustomerData(ActionEvent e) {
        StringBuilder result = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Hakim\\Downloads\\customerService.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                CustomerInfo customer = parseCustomerData(line);
                if (customer != null) {
                    customerQueue.add(customer);
                }
            }
            result.append("Customer Data Loaded Successfully!\n\n");
            displayCustomerDetails(result);

        } catch (IOException ex) {
            result.append("Error reading customer data: ").append(ex.getMessage());
        }
        textArea.setText(result.toString());
    }

    private CustomerInfo parseCustomerData(String line) {
        try {
            String[] data = line.split(",");
            if (data.length < 3) return null;

            CustomerInfo customer = new CustomerInfo(data[0], data[1], data[2]);

            for (int i = 3; i + 4 < data.length; i += 5) {
                String serviceId = data[i];
                String serviceType = data[i + 1];
                double cost = Double.parseDouble(data[i + 2]);
                String date = data[i + 3];
                String duration = data[i + 4];

                ServiceInfo service = new ServiceInfo(serviceId, serviceType, cost, date, duration);
                customer.addService(service);
            }
            return customer;
        } catch (Exception ex) {
            System.out.println("Error parsing line: " + line + " | " + ex.getMessage());
            return null;
        }
    }

    private void displayCustomerDetails(StringBuilder result) {
        int count = 1;
        for (CustomerInfo customer : customerQueue) {
            double totalCost = customer.getServices().stream().mapToDouble(ServiceInfo::getServiceCost).sum();

            result.append("Customer #").append(count++).append("\n")
                    .append("ID: ").append(customer.getCustomerId()).append("\n")
                    .append("Name: ").append(customer.getCustomerName()).append("\n")
                    .append("Vehicle Plate: ").append(customer.getVehiclePlateNumber()).append("\n")
                    .append("Services:\n");

            for (ServiceInfo service : customer.getServices()) {
                result.append("\t- Service ID: ").append(service.getServiceId()).append("\n")
                        .append("\t  Type: ").append(service.getServiceType()).append("\n")
                        .append("\t  Cost: RM ").append(service.getServiceCost()).append("\n")
                        .append("\t  Date: ").append(service.getServiceDate()).append("\n")
                        .append("\t  Est. Duration: ").append(service.getEstimatedCompletionTime()).append("\n");
            }

            result.append("Total Cost: RM ").append(String.format("%.2f", totalCost)).append("\n\n");
        }
    }

    private void addCustomer(ActionEvent e) {
        // Create custom dialog
        JDialog dialog = new JDialog(this, "Add New Customer", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create styled text fields
        JTextField idField = new JTextField(20);
        JTextField nameField = new JTextField(20);
        JTextField plateField = new JTextField(20);
        
        // Service fields
        JTextField serviceIdField = new JTextField(20);
        JTextField serviceTypeField = new JTextField(20);
        JTextField serviceCostField = new JTextField(20);
        JTextField serviceDateField = new JTextField(20);
        JTextField serviceDurationField = new JTextField(20);

        // Add customer components to dialog
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Customer ID:"), gbc);
        gbc.gridx = 1;
        dialog.add(idField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Customer Name:"), gbc);
        gbc.gridx = 1;
        dialog.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Vehicle Plate:"), gbc);
        gbc.gridx = 1;
        dialog.add(plateField, gbc);

        // Add service components
        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(new JLabel("Service ID:"), gbc);
        gbc.gridx = 1;
        dialog.add(serviceIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        dialog.add(new JLabel("Service Type:"), gbc);
        gbc.gridx = 1;
        dialog.add(serviceTypeField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        dialog.add(new JLabel("Service Cost:"), gbc);
        gbc.gridx = 1;
        dialog.add(serviceCostField, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        dialog.add(new JLabel("Service Date:"), gbc);
        gbc.gridx = 1;
        dialog.add(serviceDateField, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        dialog.add(new JLabel("Est. Duration:"), gbc);
        gbc.gridx = 1;
        dialog.add(serviceDurationField, gbc);

        // Add buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = createStyledButton("Save", new Color(46, 204, 113));
        JButton cancelButton = createStyledButton("Cancel", new Color(189, 195, 199));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 8;
        gbc.gridwidth = 2;
        dialog.add(buttonPanel, gbc);

        // Button actions
        saveButton.addActionListener(evt -> {
            try {
                String id = idField.getText().trim();
                String name = nameField.getText().trim();
                String plate = plateField.getText().trim();
                String serviceId = serviceIdField.getText().trim();
                String serviceType = serviceTypeField.getText().trim();
                String serviceCost = serviceCostField.getText().trim();
                String serviceDate = serviceDateField.getText().trim();
                String duration = serviceDurationField.getText().trim();
                
                if (!id.isEmpty() && !name.isEmpty() && !plate.isEmpty() && 
                    !serviceId.isEmpty() && !serviceType.isEmpty() && !serviceCost.isEmpty() &&
                    !serviceDate.isEmpty() && !duration.isEmpty()) {
                    
                    // Create new customer
                    CustomerInfo newCustomer = new CustomerInfo(id, name, plate);
                    
                    // Add service to customer
                    ServiceInfo service = new ServiceInfo(
                        serviceId,
                        serviceType,
                        Double.parseDouble(serviceCost),
                        serviceDate,
                        duration
                    );
                    newCustomer.addService(service);
                    
                    // Add to queue and update file
                    customerQueue.add(newCustomer);
                    updateCustomerFile();
                    
                    // Display confirmation with total cost
                    StringBuilder result = new StringBuilder();
                    result.append("New customer added!\n\n");
                    displayCustomerDetails(result);
                    textArea.setText(result.toString());
                    
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid service cost format", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(evt -> dialog.dispose());

        // Show dialog
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void removeCustomer(ActionEvent e) {
        if (customerQueue.isEmpty()) {
            textArea.setText("No customers to remove!\n");
            return;
        }

        String customerId = JOptionPane.showInputDialog("Enter Customer ID to remove:");
        if (customerId != null && !customerId.trim().isEmpty()) {
            Queue<CustomerInfo> tempQueue = new LinkedList<>();
            boolean found = false;

            while (!customerQueue.isEmpty()) {
                CustomerInfo customer = customerQueue.poll();
                if (customer.getCustomerId().equals(customerId)) {
                    found = true;
                } else {
                    tempQueue.offer(customer);
                }
            }

            // Restore remaining customers back to main queue
            customerQueue = tempQueue;

            if (found) {
                updateCustomerFile();
                StringBuilder result = new StringBuilder("Customer with ID " + customerId + " removed!\n\n");
                displayCustomerDetails(result);
                textArea.setText(result.toString());
            } else {
                textArea.setText("Customer with ID " + customerId + " not found!\n");
            }
        }
    }

    private void updateCustomerFile() {
        String filePath = "C:\\Users\\Hakim\\Downloads\\customerService.txt"; // Change as needed to your file path

        try {
            // Create the file and directories if needed
            File file = new File(filePath);
            file.getParentFile().mkdirs();

            // Write customer data to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (CustomerInfo customer : customerQueue) {
                    writer.write(customer.getCustomerId() + "," + customer.getCustomerName() + "," + customer.getVehiclePlateNumber());

                    for (ServiceInfo service : customer.getServices()) {
                        writer.write("," + service.getServiceId() + "," + service.getServiceType() + "," + service.getServiceCost() +
                                "," + service.getServiceDate() + "," + service.getEstimatedCompletionTime());
                    }
                    writer.newLine();
                }
                JOptionPane.showMessageDialog(this, "File updated successfully: " + file.getAbsolutePath());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating customer file: " + ex.getMessage());
        }
    }
}
