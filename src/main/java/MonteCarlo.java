import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinTask;


public class MonteCarlo
{
    static final long MAX_NUMBER = 100_000_000L;
    static final double RADIUS = 1;
    static final long START_NUMBER = 10_000L;

    public void evaluatePI(int threads)
    {
        ForkJoinPool forkJP = new ForkJoinPool(threads);
        long points_now;
        for (points_now= START_NUMBER; points_now<=MAX_NUMBER; points_now*=2)
        {
            long current_time = System.currentTimeMillis();
            long points_in_area = forkJP.invoke(new ForkJoinCheck(points_now, threads));
            double pi = (double) points_in_area / points_now * 4;

            System.out.println("Test");
            System.out.println("points in area= "+points_in_area+" points now= "+points_now+ ";");
            System.out.println("pi= "+pi + " threads= " + threads +";");
            System.out.println("iterations "+ points_now +";");
            System.out.println("time=" + (System.currentTimeMillis() - current_time) + " micro second;");
        }
    }

    public static class ForkJoinCheck extends RecursiveTask<Long>
    {
        long  points_now;
        int quantity_threads;

        ForkJoinCheck(long points_now, int quantity_threads)
        {
            this.points_now = points_now;
            this.quantity_threads = quantity_threads;
        }

        @Override
        protected Long compute()
        {
            if (quantity_threads > 1)
            {
                return ForkJoinTask.invokeAll(createSubtasks())
                        .stream()
                        .mapToLong(ForkJoinTask::join)
                        .sum();
            }
            else
            {
                return processing(points_now);
            }
        }

        private Long processing(long points_now)
        {
            long points_in_area = 0;
            int i=0;
            while (i<points_now)
            {
                double x = Math.random()*RADIUS;
                double y= Math.random()*RADIUS;
                if (vec(x, y) < RADIUS)
                {
                    points_in_area++;
                }
                else {
                    i++;
                }
            }
            System.out.println(points_now + "/" + points_in_area  + " in thread");
            return points_in_area*2;
        }

        double vec(double x, double y)
        {
            return Math.pow((x * x + y * y), 0.5);
        }

        private Collection<ForkJoinCheck> createSubtasks()
        {
            List<ForkJoinCheck> dividedTasks = new ArrayList<>();
            dividedTasks.add(new ForkJoinCheck(points_now/2, quantity_threads/2));
            dividedTasks.add(new ForkJoinCheck(points_now - points_now/2, quantity_threads - quantity_threads/2));
            return dividedTasks;
        }

    }

}
