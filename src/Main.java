public class Main {
    public static void main(String... args) {
        double intervalBottom = 0;
        double intervalUpper = 1;
        double initialX = 0;
        double initialY = 0;
        int nodeNum = 10;
        double step = (intervalUpper - intervalBottom) / nodeNum;
        double[] nodes = new double[nodeNum + 1];
        double accuracy = Math.pow(10, -3);

        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = intervalBottom + i * step;
        }

        System.out.println("Узлы:");
        printVector(nodes);
        System.out.println("Явный метод Эйлера:");
        System.out.println("Значения искомой функции в заданных узлах:");
        printVector(explicitEulerMethod(nodes, step, initialY));
        System.out.println();

        System.out.println("Неявный метод Эйлера:");
        System.out.println("Значения искомой функции в заданных узлах:");
        printVector(implicitEulerMethod(nodes, step, initialY, accuracy));
        System.out.println();

        System.out.println("Метод последовательныз приближений:");
        System.out.println("Значения искомой функции в заданных узлах:");
        printVector(methodOfSequentialAccuracyImprovement(nodes, step, initialY));
        System.out.println();

        System.out.println("Метод Рунге-Кутта:");
        System.out.println("Значения искомой функции в заданных узлах:");
        printVector(RungeKuttaMethod(nodes, step, initialY));
        System.out.println();

        System.out.println("Метод Адамса:");
        System.out.println("Значения искомой функции в заданных узлах:");
        printVector(AdamsMethod(nodes, step, initialY));
    }

    private static void printVector(double[] vector) {
        for (int i = 0; i < vector.length; i++) {
            System.out.println(vector[i] + " ");
        }
    }

    private static double calcFunction(double x, double y) {
        return 1 / Math.cos(x) - y * Math.tan(x);
    }

    private static double calcFunctionYDerivative(double x, double y) {
        return -Math.tan(x);
    }

    private static double[] explicitEulerMethod(double[] nodes, double step, double initialY) {
        double[] result = new double[nodes.length];
        result[0] = initialY;
        for (int i = 0; i < result.length - 1; i++) {
            result[i + 1] = result[i] + step * calcFunction(nodes[i], result[i]);
        }
        return result;
    }

    private static double calcImplicitEulerFunction(double x, double y, double previousY, double step) {
        return y - previousY - step * calcFunction(x, y);
    }

    private static double calcImplicitEulerFunctionYDeriv(double x, double y, double step) {
        return 1 - step * calcFunctionYDerivative(x, y);
    }

    private static double[] implicitEulerMethod(double[] nodes, double step, double initialY, double accuracy) {
        double[] result = new double[nodes.length];
        double currentY;
        double nextY;

        result[0] = initialY;
        for (int i = 1; i < nodes.length; i++) {
            nextY = result[i - 1];
            do {
                currentY = nextY;
                nextY = currentY - calcImplicitEulerFunction(nodes[i], currentY, result[i - 1], step) /
                        calcImplicitEulerFunctionYDeriv(nodes[i], currentY, step);
            } while (Math.abs(nextY - currentY) >= accuracy);
            result[i] = nextY;
        }
        return result;
    }

    private static double[] methodOfSequentialAccuracyImprovement(double[] nodes, double step, double initialY) {
        double[] result = new double[nodes.length];
        result[0] = initialY;
        for (int i = 0; i < nodes.length - 1; i++) {
            result[i + 1] = result[i] + step * calcFunction(nodes[i] + step / 2, result[i] + step / 2 * calcFunction(nodes[i], result[i]));
        }
        return result;
    }

    private static double[] RungeKuttaMethod(double[] nodes, double step, double initialY) {
        double[] result = new double[nodes.length];
        result[0] = initialY;
        double fi0, fi1, fi2;
        for (int i = 0; i < result.length - 1; i++) {
            fi0 = step * calcFunction(nodes[i], result[i]);
            fi1 = step * calcFunction(nodes[i] + step / 2, result[i] + fi0 / 2);
            fi2 = step * calcFunction(nodes[i + 1], result[i] - fi0 + 2 * fi1);
            result[i + 1] = result[i] + (fi0 + 4 * fi1 + fi2) / 6;
        }
        return result;
    }

    private static double[] AdamsMethod(double[] nodes, double step, double initialY) {
        double[] result = new double[11];
        result[0] = initialY;
        result[1] = 0.09991645751986516;
        result[2] = 0.19908325872557037;
        for (int i = 2; i < result.length - 1; i++) {
            result[i + 1] = result[i] + step / 12 * (23 * calcFunction(nodes[i], result[i]) - 16 * calcFunction(nodes[i - 1], result[i - 1]) +
                    5 * calcFunction(nodes[i - 2], result[i - 2]));
        }
        return result;
    }
}
