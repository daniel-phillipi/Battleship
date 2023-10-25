import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // start coding here

        int input = scanner.nextInt();
        String output;
        switch (input) {
            case 1:
                output = "You have chosen a square";
                break;
            case 2:
                output = "You have chosen a circle";
                break;
            case 3:
                output = "You have chosen a triangle";
                break;
            case 4:
                output = "You have chosen a rhombus";
                break;
            default:
                output = "There is no such shape!";
                break;
        }

        System.out.println(output);
    }
}