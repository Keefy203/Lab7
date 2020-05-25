public class Main
{
    public static void main(String[] args)
    {
        System.out.println("Start");
        MonteCarlo monteCarlo = new MonteCarlo();
        monteCarlo.evaluatePI(4);
        System.out.println("Finish");
        //result
        //threads=1 time~28000 ms;
        //threads=2 time~40000 ms;
        //threads=3 time~66000 ms;
        //threads=4 time~76000 ms;

    }
}
