import java.util.ArrayList;
import java.util.List;

public class CustomerInfo {
    private String customerId;
    private String customerName;
    private String vehiclePlateNumber;
    private List<ServiceInfo> services;

    // Constructor
    public CustomerInfo(String customerId, String customerName, String vehiclePlateNumber) {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty");
        }
        
        this.customerId = customerId.trim();
        this.customerName = customerName != null ? customerName.trim() : "";
        this.vehiclePlateNumber = vehiclePlateNumber != null ? vehiclePlateNumber.trim() : "";
        this.services = new ArrayList<>();
    }

    // Getters
    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getVehiclePlateNumber() {
        return vehiclePlateNumber;
    }

    public List<ServiceInfo> getServices() {
        return services;
    }

    // Add a service to the customer
    public void addService(ServiceInfo service) {
        services.add(service);
    }

    // Remove a service (if needed)
    public void removeService(ServiceInfo service) {
        services.remove(service);
    }

    // Calculate the total cost of all services
    public double calculateTotalServiceCost() {
        return services.stream().mapToDouble(ServiceInfo::getServiceCost).sum();
    }

    // Add method to get service by ID
    public ServiceInfo getServiceById(String serviceId) {
        return services.stream()
                      .filter(s -> s.getServiceId().equals(serviceId))
                      .findFirst()
                      .orElse(null);
    }

    // Add method to update service
    public boolean updateService(String serviceId, ServiceInfo updatedService) {
        for (int i = 0; i < services.size(); i++) {
            if (services.get(i).getServiceId().equals(serviceId)) {
                services.set(i, updatedService);
                return true;
            }
        }
        return false;
    }

    // Override toString for better output representation
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Customer Info:\n")
          .append("ID: ").append(customerId).append("\n")
          .append("Name: ").append(customerName).append("\n")
          .append("Vehicle Plate Number: ").append(vehiclePlateNumber).append("\n")
          .append("Services:\n");
        
        if (services.isEmpty()) {
            sb.append("\tNo services recorded.\n");
        } else {
            for (ServiceInfo service : services) {
                sb.append("\t- ").append(service.getServiceType())
                  .append(" ($").append(service.getServiceCost()).append(")\n");
            }
        }
        
        sb.append("Total Service Cost: $").append(calculateTotalServiceCost()).append("\n");
        return sb.toString();
    }
}
