package com.example.blackjack;

public class Card {
    int[] Cd=new int[53];
        public Card(){
            for(int i=1;i<=13;i++)
                Cd[i]=i;
            for(int i=14;i<=26;i++)
                Cd[i]=i-13;
            for(int i=27;i<=39;i++)
                Cd[i]=i-26;
            for(int i=40;i<=52;i++)
                Cd[i]=i-39;
        }
        public int Cardget(int i){
            return Cd[i];
        }

        public String Cardname(int card) {
            switch(card) {
                case 1:
                    return "A";
                case 11:
                    return "J";
                case 12:
                    return "Q";
                case 13:
                    return "K";
                default:
                    return String.valueOf(card);
            }

        }
        }

