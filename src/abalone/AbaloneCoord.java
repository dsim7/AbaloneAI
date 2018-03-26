package abalone;

import java.util.HashMap;
import java.util.Map;

public class AbaloneCoord implements Comparable {
    int x,y;
    
    private static final double COORD_WEIGHT = 1;
    private static final double COORD_CONSTANT = 50;
    
    public static Map<AbaloneCoord, Double> coordDistValues = new HashMap<AbaloneCoord, Double>();
    static {
        for (int i = -1; i < 10; ++i) {
            for (int j = -1; j < 10; ++j) {
                AbaloneCoord coord = new AbaloneCoord(i, j);
                coordDistValues.put(coord, coord.getValue());
            }
        }
    }
    
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
    
    public double getValue() {
        int x_dist_from_center = Math.abs(x - 4);
        int y_dist_from_center = Math.abs(y - 4);
        int dist = Math.abs(4 - Math.max(x_dist_from_center, y_dist_from_center));
        double value = Math.pow(dist, 2);
        // double value = Math.pow(2, dist);
        return COORD_WEIGHT * (value + COORD_CONSTANT);
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
