public class Main {
    public static void main(String[] args) {
        if (args.length > 0) {
            String filepath = args[0];
            AppRunner app = new AppRunner(filepath);
            app.run();
        } else {
            System.out.println("You need to specify the file path as the first argument when running this program.");
        }
    }
}
