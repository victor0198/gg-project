package student.examples.uservice.api.business.game;

public abstract class Item {
    private int id;
    private int x;
    private int y;
    private int size;
    private int ang;
    private int mass;
    private int sx;
    private int sy;
    private int sang;

    public Item(int id, int x, int y, int size, int ang, int mass, int sx, int sy, int sang) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.size = size;
        this.ang = ang;
        this.mass = mass;
        this.sx = sx;
        this.sy = sy;
        this.sang = sang;
    }

    public void update(){
        x +=sx;
        y +=sy;
        ang +=sang;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getAng() {
        return ang;
    }

    public void setAng(int ang) {
        this.ang = ang;
    }

    public int getMass() {
        return mass;
    }

    public void setMass(int mass) {
        this.mass = mass;
    }

    public int getSx() {
        return sx;
    }

    public void setSx(int sx) {
        this.sx = sx;
    }

    public int getSy() {
        return sy;
    }

    public void setSy(int sy) {
        this.sy = sy;
    }

    public int getSang() {
        return sang;
    }

    public void setSang(int sang) {
        this.sang = sang;
    }
}
