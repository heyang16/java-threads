package museumvisit;

import java.util.Optional;

public class Turnstile {

  private final MuseumSite originRoom;
  private final MuseumSite destinationRoom;

  public Turnstile(MuseumSite originRoom, MuseumSite destinationRoom) {
    assert !originRoom.equals(destinationRoom);
    this.originRoom = originRoom;
    this.destinationRoom = destinationRoom;
    originRoom.addExitTurnstile(this);
    // complete here if needed
  }

  public Optional<MuseumSite> passToNextRoom() {
    MuseumSite firstLock = originRoom;
    MuseumSite secondLock = destinationRoom;
    if (originRoom.hashCode() > destinationRoom.hashCode()) {
      firstLock = destinationRoom;
      secondLock = originRoom;
    }
    synchronized (firstLock) {
      synchronized (secondLock) {
        if (destinationRoom.hasAvailability()) {
          originRoom.exit();
          destinationRoom.enter();
          return Optional.of(destinationRoom);
        } else {
          return Optional.empty();
        }
      }
    }
  }

  public MuseumSite getOriginRoom() {
    return originRoom;
  }

  public MuseumSite getDestinationRoom() {
    return destinationRoom;
  }
}
