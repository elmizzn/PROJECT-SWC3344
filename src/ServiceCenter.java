import java.util.LinkedList;

public class ServiceCenter {
    private final LinkedList<CustomerInfo> customerList;
    private static final int MAX_CUSTOMERS = 100; // Add capacity limit

    public ServiceCenter() {
        customerList = new LinkedList<>();
    }

    // Add validation for adding customers
    public boolean addCustomer(CustomerInfo customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        if (customerList.size() >= MAX_CUSTOMERS) {
            return false;
        }
        if (getCustomerById(customer.getCustomerId()) != null) {
            return false; // Customer ID already exists
        }
        return customerList.add(customer);
    }

    // Add method to update customer
    public boolean updateCustomer(String customerId, CustomerInfo updatedCustomer) {
        for (int i = 0; i < customerList.size(); i++) {
            if (customerList.get(i).getCustomerId().equals(customerId)) {
                customerList.set(i, updatedCustomer);
                return true;
            }
        }
        return false;
    }

    // Add method to get customer count
    public int getCustomerCount() {
        return customerList.size();
    }

    // Display all customers
    public void displayCustomers() {
        if (customerList.isEmpty()) {
            System.out.println("No customers to display.");
            return;
        }
        for (CustomerInfo customer : customerList) {
            System.out.println(customer);
        }
    }

    // Get the list of customers
    public LinkedList<CustomerInfo> getCustomerList() {
        return customerList;
    }

    // Find a customer by customer ID
    public CustomerInfo getCustomerById(String customerId) {
        for (CustomerInfo customer : customerList) {
            if (customer.getCustomerId().equals(customerId)) {
                return customer;
            }
        }
        return null;  // Return null if customer not found
    }

    // Remove a customer by ID
    public boolean removeCustomer(String customerId) {
        return customerList.removeIf(customer -> customer.getCustomerId().equals(customerId));
    }

    // Add a service to a customer's record
    public void addServiceToCustomer(String customerId, ServiceInfo service) {
        CustomerInfo customer = getCustomerById(customerId);
        if (customer != null) {
            customer.addService(service);
        } else {
            System.out.println("Customer not found.");
        }
    }

    // Display a specific customer by ID
    public void displayCustomerById(String customerId) {
        CustomerInfo customer = getCustomerById(customerId);
        if (customer != null) {
            System.out.println(customer);
        } else {
            System.out.println("Customer not found.");
        }
    }
}
