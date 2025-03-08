import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

  public static void main(String[] args) throws IOException {

    ArrayList<Ship> ships = new ArrayList<>();
    String path = "src/input.txt";
    BufferedReader buffer = new BufferedReader(new FileReader(path));

    String line;
    String[] split;

    // Reading from file
    while ((line = buffer.readLine()) != null) {
      split = line.split(" ");
      if (split.length == 3) {
        ships.add(
          new Ship(
            true,
            LocalDate.parse(split[0], DateTimeFormatter.ISO_LOCAL_DATE),
            split[1],
            split[2],
            null));
      } else {
        ships.add(
          new Ship(
            false,
            null,
            split[1],
            null,
            split[0]));
      }
    }

    // Pricing Table
    Map<String, Double> basePrices = new HashMap<>();
    basePrices.put("LP_S", 1.50);
    basePrices.put("LP_M", 4.90);
    basePrices.put("LP_L", 6.90);
    basePrices.put("MR_S", 2.00);
    basePrices.put("MR_M", 3.00);
    basePrices.put("MR_L", 4.00);

    // Doubles for calculating discounts and price
    double lowestSPrice = Math.min(basePrices.get("LP_S"), basePrices.get("MR_S"));
    double monthlyDiscounts = 0.0;
    double price;
    double discount;

    // Tracks "L" shipments per month
    int lShipmentCount = 0;

    // LocalDate to check the current month
    LocalDate currentMonth = null;

    for (Ship ship : ships) {

      // Checking if the format is correct
      if (!ship.isCorrectFormat()) {
        System.out.println(ship.getIgnore() + " " + ship.getSize() + " Ignored");

        continue;
      }

      // Starting calculation and marking current month
      LocalDate shipmentMonth = ship.getDate().withDayOfMonth(1);
      if (currentMonth == null || !currentMonth.equals(shipmentMonth)) {
        currentMonth = shipmentMonth;

        monthlyDiscounts = 0.0;
      }

      // Assigning base price and clearing discount
      String key = ship.getProvider() + "_" + ship.getSize();
      price = basePrices.getOrDefault(key, 0.0);
      discount = 0.0;

      // "All S shipments should always match the lowest S package price among the providers."
      if (ship.getSize().equals("S")) {
        discount = price - lowestSPrice;

        price = lowestSPrice;
      }

      // "The third L shipment via LP should be free, but only once a calendar month."
      if (ship.getSize().equals("L") && ship.getProvider().equals("LP")) {
        lShipmentCount++;
        // Make only the third "L" shipment free
        if (lShipmentCount == 3) {
          discount = price;

          price = 0.0;
        }
      }

      /*
      Accumulated discounts cannot exceed 10 € in a calendar month.
      If there are not enough funds to fully cover a discount this calendar month,
      it should be covered partially.
      */
      double availableDiscount = Math.min(discount, 10 - monthlyDiscounts);
      price += (discount - availableDiscount);
      monthlyDiscounts += availableDiscount;

      // Printing transaction details
      System.out.println(ship.getDate() + " "
        + ship.getSize() + " "
        + ship.getProvider() + " "
        + String.format("%.2f", price) + " "
        + (availableDiscount > 0 ? String.format("%.2f", availableDiscount) : "-"));
    }
  }
}