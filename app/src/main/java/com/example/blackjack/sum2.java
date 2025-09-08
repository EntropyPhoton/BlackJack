package com.example.blackjack;

public class sum2 {
    public int sumget(int card) {
        int sum2=0;
        switch(card) {
            case 1:
                sum2=card+10;break;
            case 11:
                sum2=card-1;break;

            case 12:
                sum2=card-2;break;

            case 13:
                sum2=card-3;break;

            default:
                sum2=card;break;
        }
        return sum2;}
}
