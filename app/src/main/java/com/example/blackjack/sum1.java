package com.example.blackjack;

public class sum1 {
    public int sumget(int card) {
        int sum1=0;
        switch(card) {
            case 1:
                sum1=card;break;
            case 11:
                sum1=card-1;break;

            case 12:
                sum1=card-2;break;

            case 13:
                sum1=card-3;break;

            default:
                sum1=card;break;

        }
        return sum1;
    }
}
