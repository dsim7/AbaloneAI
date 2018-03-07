package abalone;

public class AbaloneCoord {
    int x,y;
    
    public AbaloneCoord(int x, int y) {
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
    
    @Override
    public String toString() {
        return "Coord: " + x + "," + y;
    }

    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + x;
        hash = 31 * hash + y;
        return hash;
    }
}
