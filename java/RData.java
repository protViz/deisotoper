/**
 * @author Lucas Schmidt
 * @since 2017-08-22
 * @url https://www.mkyong.com/java/how-to-convert-java-object-to-from-json-jackson/
 */

public class RData {
    private String filename;
    private double[] mz;
    private double[] intensity;
    private int charge;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public double[] getMz() {
        return mz;
    }

    public void setMz(double[] mz) {
        this.mz = mz;
    }

    public double[] getIntensity() {
        return intensity;
    }

    public void setIntensity(double[] intensity) {
        this.intensity = intensity;
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    // For testing
    public static void main(String[] args) {
        String filename = "TestObject";
        double[] mz = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
        double[] intensity = { 4, 4, 5, 6, 6, 7, 7, 7, 8, 8 };
        int charge = 2;

        Serialize.serializeIt(filename, mz, intensity, charge);

        System.out.println(Deserialize.deserializeIt(filename));

    }

}
