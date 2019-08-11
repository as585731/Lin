public class DemoI {
    private Integer i;

    public DemoI(Integer i) {
        this.i = i;
    }

    public Integer getI() {
        return i;
    }

    public void setI(Integer i) {
        this.i = i;
    }

    @Override
    public String toString() {
        return "DemoI{" +
                "i=" + i +
                '}';
    }
}
