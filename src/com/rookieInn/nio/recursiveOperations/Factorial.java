package com.rookieInn.nio.recursiveOperations;

/**
 * Created by gxy on 2016/6/16.
 */
public class Factorial {

    /**
     * Calculate the factorial of n (n! = 1 * 2 * 3 * ... * n)
     *
     * @param n the number to calculate the factorial of.
     * @return n! - the factorial of n.
     */
    public static int factorial(int n) {
        //Base Case
        // If n <= 1 then n！ =1.
        if (n <= 1)
            return 1;
        //Recursive Case:
        //If n > 1 then n! = n * (n-1)!
        else
            return n * factorial(n-1);
    }

    public static void main(String[] args) {
        System.out.println(factorial(5));
    }

}
