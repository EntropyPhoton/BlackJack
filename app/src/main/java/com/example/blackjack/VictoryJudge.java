package com.example.blackjack;

public class VictoryJudge {
    public String Judge(int dsum1,int dsum2,int psum1,int psum2) {
        if(dsum1>21) {
            MainActivity2.win++;
            return "庄家爆牌，闲家胜利";

        }
        else if((psum2<=21 && psum2==dsum2)||(psum2>21 && psum1==dsum2)||(psum2<=21 && psum2==dsum1)||(psum2>21 && psum1==dsum1))
        {
            MainActivity2.draw++;
            return "庄家点数等于闲家点数，平局！";
        }
        else if(dsum2<=21) {
            if(psum2<=21 && psum2>dsum2) {
                MainActivity2.win++;
                return "闲家点数大于庄家点数，闲家胜利！";

            }
            else if(psum2>21 && psum1>dsum2) {
                MainActivity2.win++;
                return "闲家点数大于庄家点数，闲家胜利！";
            }
            else {
                MainActivity2.lose++;
                return "庄家点数大于闲家点数，庄家胜利！";
            }
        }
        else {
            if(psum2<=21 && psum2>dsum1) {
                MainActivity2.win++;
                return "闲家点数大于庄家点数，闲家胜利！";
            }
            else if(psum2>21 && psum1>dsum1) {
                MainActivity2.win++;
                return "闲家点数大于庄家点数，闲家胜利！";
            }
            else {
                MainActivity2.lose++;
                return "庄家点数大于闲家点数，庄家胜利！";

            }
        }
    }
}
