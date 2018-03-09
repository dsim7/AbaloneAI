package abalone;

public class AbaloneCoord {
    int x,y;
    
    public AbaloneCoord(int x, int y) {
        if (x < 0 || x > 8 || y < 0 || y > 8) {
            this.x = -1;
            this.y = -1;
        } else if (x == 0 && y > 4) {
            this.x = -1;
            this.y = -1;
        } else if (x == 1 && y > 5) {
            this.x = -1;
            this.y = -1;
        } else if (x == 2 && y > 6) {
            this.x = -1;
            this.y = -1;
        } else if (x == 3 && y > 7) {
            this.x = -1;
            this.y = -1;
        } else if (x == 5 && y < 1) {
            this.x = -1;
            this.y = -1;
        } else if (x == 6 && y < 2) {
            this.x = -1;
            this.y = -1;
        } else if (x == 7 && y < 3) {
            this.x = -1;
            this.y = -1;
        } else if (x == 8 && y < 4) {
            this.x = -1;
            this.y = -1;
        } else {
            this.x = x;
            this.y = y;
        }
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
