import java.time.LocalDate;

public class Ship {
  private final LocalDate date;
  private final String size;
  private final String provider;
  private final boolean correctFormat;
  private final String ignore;

  public Ship(boolean correctFormat, LocalDate date, String size, String provider, String ignore) {
    this.correctFormat = correctFormat;
    this.date = date;
    this.size = size;
    this.provider = provider;
    this.ignore = ignore;
  }

  public String getIgnore() {
    return ignore;
  }

  public LocalDate getDate() {
    return date;
  }

  public String getSize() {
    return size;
  }

  public String getProvider() {
    return provider;
  }

  public boolean isCorrectFormat() {
    return correctFormat;
  }
}
