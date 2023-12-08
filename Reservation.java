import java.util.Objects;

public class Reservation {
    private final int idVol;
    private final int idPassager;

    public Reservation(int idVol, int idPassager) {
        this.idVol = idVol;
        this.idPassager = idPassager;
    }

    public int getIdVol() {
        return idVol;
    }

    public int getIdPassager() {
        return idPassager;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return idVol == that.idVol && idPassager == that.idPassager;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVol, idPassager);
    }
}
