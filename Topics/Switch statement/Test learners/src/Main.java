import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // start coding here

        int input = scanner.nextInt();
        String output;

        switch (input) {
            case 1:
                output = "Yes!";
                break;
            case 2:
                output = "No!";
                break;
            case 3:
                output = "No!";
                break;
            case 4:
                output = "No!";
                break;
            default:
                output = "Unknown number";
                break;
        }

        System.out.println(output);
    }
}