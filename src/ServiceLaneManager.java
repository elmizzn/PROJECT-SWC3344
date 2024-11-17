import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class ServiceLaneManager {
    private static final int MAX_LANE_CAPACITY = 10;
    private static final int MAX_PROCESS_COUNT = 5;

    private Queue<CustomerInfo> lane1;
    private Queue<CustomerInfo> lane2;
    private Queue<CustomerInfo> lane3;
    private Stack<CustomerInfo> completeStack;
    private int laneToggle;

    public ServiceLaneManager() {
        lane1 = new LinkedList<>();
        lane2 = new LinkedList<>();
        lane3 = new LinkedList<>();
        completeStack = new Stack<>();
        laneToggle = 0;
    }

    // Method to add a customer to a lane based on the number of services
    public boolean addCustomerToLane(CustomerInfo customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }

        int serviceCount = customer.getServices().size();
        Queue<CustomerInfo> targetLane;

        if (serviceCount <= 3) {
            targetLane = (laneToggle % 2 == 0) ? lane1 : lane2;
            laneToggle++;
        } else {
            targetLane = lane3;
        }

        if (targetLane.size() >= MAX_LANE_CAPACITY) {
            return false;
        }

        targetLane.add(customer);
        return true;
    }

    // Method to process all lanes
    public void processLanes() {
        System.out.println("\nProcessing Lane 1:");
        processLane(lane1, "Lane 1");

        System.out.println("\nProcessing Lane 2:");
        processLane(lane2, "Lane 2");

        System.out.println("\nProcessing Lane 3:");
        processLane(lane3, "Lane 3");
    }

    // Helper method to process a single lane
    private void processLane(Queue<CustomerInfo> lane, String laneName) {
        int count = 0;
        if (lane.isEmpty()) {
            System.out.println(laneName + " is empty.\n");
            return;
        }

        while (!lane.isEmpty() && count < 5) {
            CustomerInfo customer = lane.poll();
            double totalCost = customer.getServices().stream().mapToDouble(ServiceInfo::getServiceCost).sum();
            System.out.println("Processed Customer: " + customer.getCustomerName() + " | Total Cost: $" + totalCost);
            completeStack.push(customer);
            count++;
        }
    }

    // Method to display completed transactions from the stack
    public void displayCompletedTransactions() {
        System.out.println("\nCompleted Transactions (Stack):");
        while (!completeStack.isEmpty()) {
            CustomerInfo customer = completeStack.pop();
            double totalCost = customer.getServices().stream().mapToDouble(ServiceInfo::getServiceCost).sum();
            System.out.println("Customer: " + customer.getCustomerName() + " | Total Paid: $" + totalCost);
        }
    }

    // Add methods to check lane status
    public int getLane1Size() { return lane1.size(); }
    public int getLane2Size() { return lane2.size(); }
    public int getLane3Size() { return lane3.size(); }
    public int getCompletedCount() { return completeStack.size(); }

    // Add method to clear all lanes
    public void clearAllLanes() {
        lane1.clear();
        lane2.clear();
        lane3.clear();
        completeStack.clear();
        laneToggle = 0;
    }
}
