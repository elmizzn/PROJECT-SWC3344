public class ServiceInfo {
    private String serviceId;
    private String serviceType;
    private double serviceCost;
    private String serviceDate;
    private String estimatedCompletionTime;

    // Constructor
    public ServiceInfo(String serviceId, String serviceType, double serviceCost, String serviceDate, String estimatedCompletionTime) {
        if (serviceId == null || serviceId.trim().isEmpty()) {
            throw new IllegalArgumentException("Service ID cannot be null or empty");
        }
        if (serviceCost < 0) {
            throw new IllegalArgumentException("Service cost cannot be negative");
        }
        
        this.serviceId = serviceId.trim();
        this.serviceType = serviceType != null ? serviceType.trim() : "";
        this.serviceCost = serviceCost;
        this.serviceDate = serviceDate != null ? serviceDate.trim() : "";
        this.estimatedCompletionTime = estimatedCompletionTime != null ? estimatedCompletionTime.trim() : "";
    }

    // Getters
    public String getServiceId() {
        return serviceId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public double getServiceCost() {
        return serviceCost;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public String getEstimatedCompletionTime() {
        return estimatedCompletionTime;
    }

    // Setters (if needed for future modifications)
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public void setServiceCost(double serviceCost) {
        if (serviceCost < 0) {
            throw new IllegalArgumentException("Service cost cannot be negative");
        }
        this.serviceCost = serviceCost;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

    public void setEstimatedCompletionTime(String estimatedCompletionTime) {
        this.estimatedCompletionTime = estimatedCompletionTime;
    }

    // Override toString for better debugging
    @Override
    public String toString() {
        return "ServiceInfo{" +
                "serviceId='" + serviceId + '\'' +
                ", serviceType='" + serviceType + '\'' +
                ", serviceCost=" + serviceCost +
                ", serviceDate='" + serviceDate + '\'' +
                ", estimatedCompletionTime='" + estimatedCompletionTime + '\'' +
                '}';
    }
}
