package abalone;

public class AbaloneCoord implements Comparable {
    int x,y;
    
    public AbaloneCoord(int x, int y) {
        this.x = x;
        this.y = y;
        
    }
    
    public void setX(int new_x) {
        x = new_x;
    }
    
    public void setY(int new_y) {
        y = new_y;
    }
    
    public void setCoord(int new_x, int new_y) {
        x = new_x;
        y = new_y;
    }
    
    public boolean isValid() {
        if ((x < 0 || x > 8 || y < 0 || y > 8) || 
                (x == 0 && y > 4) || (x == 1 && y > 5) || (x == 2 && y > 6) || (x == 3 && y > 7) || 
                (x == 5 && y < 1) || (x == 6 && y < 2) || (x == 7 && y < 3) || (x == 8 && y < 4)) {
            return false;
        }
        return true;
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

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof AbaloneCoord)) {
            return -1;
        }
        AbaloneCoord ab = (AbaloneCoord) o;
        int ovalue = ab.y * 8 + ab.x;
        int thisvalue = this.y * 8 + this.x;
        return thisvalue - ovalue;
    }
}
