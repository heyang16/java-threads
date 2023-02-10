package museumvisit;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class Museum {

  private final Entrance entrance;
  private final Exit exit;
  private final Set<MuseumSite> sites;

  public Museum(Entrance entrance, Exit exit, Set<MuseumSite> sites) {
    this.entrance = entrance;
    this.exit = exit;
    this.sites = sites;
  }

  public static void main(String[] args) {
    final int numberOfVisitors = 100; // Your solution has to work with any
    // number of visitors
    final Museum museum = buildLoopyMuseum(); // buildLoopyMuseum();

    // create the threads for the visitors and get them moving
    List<Thread> visitors = new ArrayList<>();
    IntStream.range(0, numberOfVisitors)
        .sequential()
        .forEach(
            i -> {
              Thread visitorThread = new Thread(new Visitor("Vis" + i, museum.getEntrance()));
              visitors.add(visitorThread);
              visitorThread.start();
            });

    // Wait for all visitors to complete their visit
    visitors.forEach(
        v -> {
          try {
            v.join();
          } catch (InterruptedException e) {
          }
        });

    // Checking no one is left behind
    if (museum.getExit().getOccupancy() == numberOfVisitors) {
      System.out.println("\nAll the visitors reached the exit\n");
    } else {
      System.out.println(
          "\n"
              + (numberOfVisitors - museum.getExit().getOccupancy())
              + " visitors did not reach the exit. Where are they?\n");
    }

    System.out.println("Occupancy status for each room (should all be zero, but the exit site):");
    museum
        .getSites()
        .forEach(
            s -> {
              System.out.println("Site " + s.getName() + " final occupancy: " + s.getOccupancy());
            });
  }

  public static Museum buildSimpleMuseum() {
    // Build rooms
    Entrance entrance = new Entrance();
    Exit exit = new Exit();
    ExhibitionRoom exhibitionRoom = new ExhibitionRoom("Exhibition Room", 10);

    // Build turnstiles
    Turnstile turnstile1 = new Turnstile(entrance, exhibitionRoom);
    Turnstile turnstile2 = (new Turnstile(exhibitionRoom, exit));

    return new Museum(entrance, exit, Set.of(exhibitionRoom));
  }

  public static Museum buildLoopyMuseum() {
    // Build rooms
    Entrance entrance = new Entrance();
    Exit exit = new Exit();
    ExhibitionRoom venom = new ExhibitionRoom("Venom", 10);
    ExhibitionRoom whales = new ExhibitionRoom("Whales", 10);

    // Build turnstiles
    Turnstile turnstile1 = new Turnstile(entrance, venom);
    Turnstile turnstile2 = new Turnstile(venom, whales);
    Turnstile turnstile3 = new Turnstile(whales, venom);
    Turnstile turnstile4 = new Turnstile(venom, exit);

    return new Museum(entrance, exit, Set.of(venom, whales));
  }

  public Entrance getEntrance() {
    return entrance;
  }

  public Exit getExit() {
    return exit;
  }

  public Set<MuseumSite> getSites() {
    return sites;
  }
}
