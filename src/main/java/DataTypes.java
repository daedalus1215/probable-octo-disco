/**
 * Handler for requests to Lambda function.
 */
public class DataTypes {
    public int getNumbers() {
        System.out.println("inc");
        System.out.println(System.getenv("restapiurl"));
        return (int) 1;
    }
}