
public class AbaloneCoord {
    int x,y;
    
    AbaloneCoord(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AbaloneCoord)) {
            return false;
        }
        AbaloneCoord coord = (AbaloneCoord) obj;
        return x == coord.x && y == coord.y;
    }

    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + x;
        hash = 31 * hash + y;
        return hash;
    }
}
