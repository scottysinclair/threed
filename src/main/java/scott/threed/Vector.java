package scott.threed;

/**
3d value
*/
class Vector {
    private float x;
    private float y;
    private float z;

    public Vector(Vector v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public Vector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector() {}

    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
    }

    public void addX(float value) {
        this.x += value;
    }

    public void addY(float value) {
        this.y += value;
    }

    public void addZ(float value) {
        this.z += value;
    }

    public float getY() {
        return y;
    }
    public void setY(float y) {
        this.y = y;
    }
    public float getZ() {
        return z;
    }
    public void setZ(float z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "Vector [x=" + x + ", y=" + y + ", z=" + z + "]";
    }
}