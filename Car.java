import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Vehicle {
    private String id;
    private String make;
    private String model;
    private double dailyRate;
    private boolean available;

    public Vehicle(String id, String make, String model, double dailyRate) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.dailyRate = dailyRate;
        this.available = true;
    }

    public String getId() {
        return id;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public double computeRentalCost(int days) {
        return dailyRate * days;
    }

    public boolean isAvailable() {
        return available;
    }

    public void markRented() {
        available = false;
    }

    public void markReturned() {
        available = true;
    }
}

class Client {
    private String id;
    private String fullName;

    public Client(String id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }
}

class Booking {
    private Vehicle vehicle;
    private Client client;
    private int rentalDays;

    public Booking(Vehicle vehicle, Client client, int rentalDays) {
        this.vehicle = vehicle;
        this.client = client;
        this.rentalDays = rentalDays;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Client getClient() {
        return client;
    }

    public int getRentalDays() {
        return rentalDays;
    }
}

class RentalService {
    private List<Vehicle> vehicles;
    private List<Client> clients;
    private List<Booking> bookings;

    public RentalService() {
        vehicles = new ArrayList<>();
        clients = new ArrayList<>();
        bookings = new ArrayList<>();
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public void addClient(Client client) {
        clients.add(client);
    }

    public void processRental(Vehicle vehicle, Client client, int days) {
        if (vehicle.isAvailable()) {
            vehicle.markRented();
            bookings.add(new Booking(vehicle, client, days));
        } else {
            System.out.println("Vehicle is not available for rent.");
        }
    }

    public void processReturn(Vehicle vehicle) {
        vehicle.markReturned();
        Booking bookingToRemove = null;
        for (Booking booking : bookings) {
            if (booking.getVehicle() == vehicle) {
                bookingToRemove = booking;
                break;
            }
        }
        if (bookingToRemove != null) {
            bookings.remove(bookingToRemove);
        } else {
            System.out.println("Vehicle was not rented.");
        }
    }

    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("===== Car Rental System =====");
            System.out.println("1. Rent a Vehicle");
            System.out.println("2. Return a Vehicle");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                System.out.println("\n== Rent a Vehicle ==\n");
                System.out.print("Enter your name: ");
                String clientName = scanner.nextLine();

                System.out.println("\nAvailable Vehicles:");
                for (Vehicle vehicle : vehicles) {
                    if (vehicle.isAvailable()) {
                        System.out.println(vehicle.getId() + " - " + vehicle.getMake() + " " + vehicle.getModel());
                    }
                }

                System.out.print("\nEnter the vehicle ID you want to rent: ");
                String vehicleId = scanner.nextLine();

                System.out.print("Enter the number of days for rental: ");
                int rentalDays = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                Client newClient = new Client("CLT" + (clients.size() + 1), clientName);
                addClient(newClient);

                Vehicle selectedVehicle = null;
                for (Vehicle vehicle : vehicles) {
                    if (vehicle.getId().equals(vehicleId) && vehicle.isAvailable()) {
                        selectedVehicle = vehicle;
                        break;
                    }
                }

                if (selectedVehicle != null) {
                    double totalPrice = selectedVehicle.computeRentalCost(rentalDays);
                    System.out.println("\n== Rental Information ==\n");
                    System.out.println("Client ID: " + newClient.getId());
                    System.out.println("Client Name: " + newClient.getFullName());
                    System.out.println("Vehicle: " + selectedVehicle.getMake() + " " + selectedVehicle.getModel());
                    System.out.println("Rental Days: " + rentalDays);
                    System.out.printf("Total Price: ₹%.2f%n", totalPrice);

                    System.out.print("\nConfirm rental (Y/N): ");
                    String confirm = scanner.nextLine();

                    if (confirm.equalsIgnoreCase("Y")) {
                        processRental(selectedVehicle, newClient, rentalDays);
                        System.out.println("\nVehicle rented successfully.");
                    } else {
                        System.out.println("\nRental canceled.");
                    }
                } else {
                    System.out.println("\nInvalid vehicle selection or vehicle not available for rent.");
                }
            } else if (choice == 2) {
                System.out.println("\n== Return a Vehicle ==\n");
                System.out.print("Enter the vehicle ID you want to return: ");
                String vehicleId = scanner.nextLine();

                Vehicle vehicleToReturn = null;
                for (Vehicle vehicle : vehicles) {
                    if (vehicle.getId().equals(vehicleId) && !vehicle.isAvailable()) {
                        vehicleToReturn = vehicle;
                        break;
                    }
                }

                if (vehicleToReturn != null) {
                    Client client = null;
                    for (Booking booking : bookings) {
                        if (booking.getVehicle() == vehicleToReturn) {
                            client = booking.getClient();
                            break;
                        }
                    }

                    if (client != null) {
                        processReturn(vehicleToReturn);
                        System.out.println("Vehicle returned successfully by " + client.getFullName());
                    } else {
                        System.out.println("Vehicle was not rented or rental information is missing.");
                    }
                } else {
                    System.out.println("Invalid vehicle ID or vehicle is not rented.");
                }
            } else if (choice == 3) {
                break;
            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }

        System.out.println("\nThank you for using the Car Rental System!");
    }

}

public class Main {
    public static void main(String[] args) {
        RentalService rentalService = new RentalService();

        Vehicle vehicle1 = new Vehicle("V001", "Toyota", "Innova", 600); // Different daily rate for each vehicle
        Vehicle vehicle2 = new Vehicle("V002", "Honda", "Brio", 700);
        Vehicle vehicle3 = new Vehicle("V003", "Mahindra", "Thar", 1500);
        rentalService.addVehicle(vehicle1);
        rentalService.addVehicle(vehicle2);
        rentalService.addVehicle(vehicle3);

        rentalService.displayMenu();
    }
}
