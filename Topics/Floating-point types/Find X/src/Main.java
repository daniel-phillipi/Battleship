import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // put your code here

        double a = scanner.nextDouble();
        double b = scanner.nextDouble();
        double c = scanner.nextDouble();

        //equation: a * x + b = c >> b - c = -ax >> -(b - c) / a = x  - a is not 0.

        System.out.println(-(b-c)/a);
    }
}