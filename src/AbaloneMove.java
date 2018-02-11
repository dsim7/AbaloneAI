
public class AbaloneMove {
    int x1, x2, y1, y2;
    Abalone.Dir direction;
    
    AbaloneMove(int x1, int y1, int x2, int y2, Abalone.Dir dir) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.direction = dir;
    }
}
