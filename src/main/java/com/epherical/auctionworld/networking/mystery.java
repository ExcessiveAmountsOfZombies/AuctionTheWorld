package com.epherical.auctionworld.networking;

public class mystery {

    public static void main(String[] args) {
        System.out.println(mystery(3, 5));
    }


    public static int mystery(int n, int a) {
        if (n == 0) {
            return 0;
        }

        int temp1 = mystery(n/2, a);
        int temp2 = 0;
        for (int i = 0; i < n/2; i++) {
            temp2 += a;
            System.out.println(temp2 + " temp 2");
        }

        if (n%2==1) {
            System.out.println("return mod 1");
            return temp1 + temp2 + a;
        } else {
            System.out.println("return here only");
            return temp1 + temp2;
        }
    }
}
