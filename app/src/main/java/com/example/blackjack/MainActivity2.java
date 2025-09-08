package com.example.blackjack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class MainActivity2 extends Activity {
    private final Semaphore semaphore = new Semaphore(1);
    //private final Semaphore semaphore2 =new Semaphore(1);
    //统计变量和状态变量初始化
    private final static String[] SHAPES = {"club", "diamond", "heart", "spade"};
    static double W = 0;
    static double E = 0;
    static int win = 0;
    static int lose = 0;
    static int draw = 0;
    static int all = 0;
    int state1 = 0;
    int state2 = 0;
    int state3 = 0;
    int j;//Dealer存牌数组的第一个序号
    int k;//player存牌数组的第一个序号
    int hide;
    //调用类初始化
    Card Cd = new Card();
    Random r = new Random();
    sum1 s1 = new sum1();
    sum2 s2 = new sum2();
    VictoryJudge vic = new VictoryJudge();
    getTVHeight gtvh=new getTVHeight();
    int i,offset;//用于生成随机值和进行选牌的辅助变量
    int dsum1, dsum2, psum1, psum2;

    private TextView txt;
    List<ImageView> DIV = new ArrayList<>();
    List<ImageView> PIV = new ArrayList<>();
    List<Integer> IV = new ArrayList<>();
    private Handler handler = new Handler();
    private final Object object = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        Resources res = getResources();
        txt = findViewById(R.id.GameBox);
        txt.setMovementMethod(ScrollingMovementMethod.getInstance());//使txt框可以滑动

        for (int d = 0; d < 4; d++) {
            for (int c = 1; c <= 13; c++) {
                int id1 = res.getIdentifier(SHAPES[d] + c, "drawable", getPackageName());
                IV.add(id1);
            }
        }//List<Integer>IV中储存了从0-51共52张扑克牌图片的id，从A到K每13张为一组，使用setImageResource(IV.get(i))方法改变imageview框的内容
        for (int c = 1; c <= 11; c++) {
            int id1 = res.getIdentifier("D" + c, "id", getPackageName());//D1和P1是最左边那个view
            int id2 = res.getIdentifier("P" + c, "id", getPackageName());
            ImageView D = findViewById(id1);
            DIV.add(D);
            ImageView P = findViewById(id2);
            PIV.add(P);
        }//List<ImageView>中储存了22个已绑定ImageView控件，从0-10，使用DIV.get(i)访问(D指庄家P指闲家)
        //DIV.get(0).setImageResource(0);使用这个方法清空image框的图片
        //xml布局初始化
        Button btn1 = findViewById(R.id.btn1);
        Button btn2 = findViewById(R.id.btn2);
        Button btn3 = findViewById(R.id.btn3);

        btn1.setOnClickListener(v -> {
            if (state1 == 0 && state2 == 0) {
                state2 = 1;
                new Thread(Game).start();
            } else if (state1 == 0 && state2 == 1) {
                state3 = 1;
                synchronized (object) {
                    object.notify();
                }
            } else if (state1 == 1 && state2 == 0) {
                state3 = 1;
                synchronized (object) {
                    object.notify();
                }
            }
        });
        btn2.setOnClickListener(v -> {
            if (state1 == 0 && state2 == 0) {
                new AlertDialog.Builder(MainActivity2.this)
                        .setTitle("确定退出吗？")
                        .setNegativeButton("取消", (dialogInterface, i) -> {

                        })
                        .setPositiveButton("确定", (dialogInterface, i) -> {
                            finish();
                            System.exit(0);
                        })
                        .show();
            } else if (state1 == 0 && state2 == 1) {
                state3 = 0;
                synchronized (object) {
                    object.notify();
                }
            } else if (state1 == 1 && state2 == 0) {
                state3 = 0;
                synchronized (object) {
                    object.notify();
                }
            }

        });

        btn3.setOnClickListener(v -> handler.post(() -> {
            if(state1==0 && state2==0){
            txt.setText("");
            txt.scrollTo(0,0);
            txt.append("游戏总共进行场数" + all);
            txt.append("\n闲家总共胜利场数：" + win);
            txt.append("\n庄家总共胜利场数：" + lose);
            txt.append("\n闲庄家平局场数：" + draw);
            txt.append("\n收入期望为" + E);
            txt.append("\n胜利期望为" + W);
            }
        }));
    }

    Thread Game = new Thread(new Runnable() {
        @Override
        public void run() {
            while (state1 == 0 && state2 == 1) {
                //D,P为双方抽出牌所存数组
                int[] D = new int[12];
                int[] P = new int[12];
                dsum1=0;dsum2=0;psum1=0;psum2=0;
                j = 1;
                k = 1;
                hide=0;
                all++;
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.post(() -> {
                    txt.setText("");
                    txt.append("开始\n");
                    txt.scrollTo(0,0);
                    for(int i=0;i<11;i++){
                        DIV.get(i).setImageResource(0);
                        PIV.get(i).setImageResource(0);
                    }
                    DIV.get(0).setImageResource(R.drawable.back);
                    semaphore.release();
                });
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }//游戏开始前的准备
                semaphore.release();

                i = r.nextInt(52) + 1;//庄家抽第一张牌
                hide=i;
                D[j] = Cd.Cardget(i);
                dsum1 = s1.sumget(D[j]);
                dsum2 = s2.sumget(D[j]);

                j++;

                i = r.nextInt(52) + 1;//庄家抽第二张牌
                D[j] = Cd.Cardget(i);
                dsum1 += s1.sumget(D[j]);
                dsum2 += s2.sumget(D[j]);
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.post(() -> {
                    DIV.get(j-1).setImageResource(IV.get(i-1));
                    //txt.append("庄家的明牌为"+Cd.Cardname(D[j])+"\n");
                    semaphore.release();
                });
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }semaphore.release();

                i = r.nextInt(52) + 1;//闲家抽第一张牌
                P[k] = Cd.Cardget(i);
                psum1 = s1.sumget(P[k]);
                psum2 = s2.sumget(P[k]);

                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.post(() -> {
                    PIV.get(k-1).setImageResource(IV.get(i-1));
                    //txt.append("闲家的第一张牌为"+Cd.Cardname(P[k])+"\n");
                    semaphore.release();
                });

                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }semaphore.release();

                k++;
                i = r.nextInt(52) + 1;//闲家抽第二张牌
                P[k] = Cd.Cardget(i);
                Cd.Cardname(P[k]);
                psum1 += s1.sumget(P[k]);
                psum2 += s2.sumget(P[k]);
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.post(() -> {
                    PIV.get(k-1).setImageResource(IV.get(i-1));
                    //txt.append("闲家的第二张牌为"+Cd.Cardname(P[k])+"\n");
                    semaphore.release();
                });
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }semaphore.release();


                if (psum2 == 21 && dsum2 == 21) {
                    draw++;
                    state1 = 1;
                    state2 = 0;
                    handler.post(() -> {
                        txt.setText("庄家与闲家同时拿到BlackJack，庄家点数等于闲家点数，平局！\n是否继续游戏，继续请按Confirm，退出请按Exit");
                        txt.scrollTo(0,0);
                    });
                    synchronized (object) {
                        try {
                            object.wait();
                        } catch (InterruptedException e) {
                            handler.post(() -> txt.setText("出现错误"));

                            break;
                        }
                    }//wait函数怎么写
                    if (state3 == 0) {
                        state1 = 0;
                        break;
                    } else if (state3 == 1) {
                        state1 = 0;
                        state2 = 1;
                        continue;
                    }
                }
                if (dsum2 == 21) {
                    lose++;
                    state1 = 1;
                    state2 = 0;
                    handler.post(() -> {
                        txt.setText("庄家拿到BlackJack，庄家胜\n是否继续游戏，继续请按Confirm，退出请按Exit");
                        txt.scrollTo(0,0);

                    });
                    synchronized (object) {
                        try {
                            object.wait();
                        } catch (InterruptedException e) {
                            handler.post(() -> txt.setText("出现错误"));
                            break;
                        }
                    }//wait函数怎么写
                    if (state3 == 0) {
                        state1 = 0;
                        break;
                    } else if (state3 == 1) {
                        state1 = 0;
                        state2 = 1;
                        continue;
                    }
                }
                if (psum2 == 21) {
                    win++;
                    state1 = 1;
                    state2 = 0;
                    handler.post(() -> {
                        txt.setText("闲家拿到BlackJack，闲家胜\n是否继续游戏，继续请按Confirm，退出请按Exit");
                        txt.scrollTo(0,0);

                    });
                    synchronized (object) {
                        try {
                            object.wait();
                        } catch (InterruptedException e) {
                            handler.post(() -> txt.setText("出现错误"));
                            break;
                        }
                    }//wait函数怎么写
                    if (state3 == 0) {
                        state1 = 0;
                        break;
                    } else if (state3 == 1) {
                        state1 = 0;
                        state2 = 1;
                        continue;
                    }
                } else
                    handler.post(() -> {
                        txt.setText("庄家的明牌为" + Cd.Cardname(D[j]) + "\n");
                        txt.scrollTo(0,0);

                    });


                //庄家发牌结束，闲家开始发牌
                if (psum1 == psum2) {
                    handler.post(() -> {
                        txt.append("闲家总点数为" + psum1 + "\n闲家是否继续拿牌（按Confirm继续按Exti退出）\n");
                        offset=gtvh.getTextViewHeight(txt);
                        if (offset > txt.getHeight())
                            txt.scrollTo(0, offset - txt.getHeight());
                    });
                    synchronized (object) {
                        try {
                            object.wait();
                        } catch (InterruptedException e) {
                            handler.post(() -> txt.setText("出现错误"));
                            break;
                        }
                    }//wait函数怎么写
                } else {
                    handler.post(() -> {
                        txt.append("闲家总点数为" + psum1 + "/" + psum2 + "\n闲家是否继续拿牌（按Confirm继续按Exti退出）\n");
                        offset=gtvh.getTextViewHeight(txt);
                        if (offset > txt.getHeight())
                            txt.scrollTo(0, offset - txt.getHeight());
                    });
                    synchronized (object) {
                        try {
                            object.wait();
                        } catch (InterruptedException e) {
                            handler.post(() -> txt.setText("出现错误"));
                            break;
                        }
                    }//wait函数怎么写
                }
                /*try {
                    semaphore2.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                //拿第一张之后的牌
                while (state3 == 1) {
                    k++;
                    i = r.nextInt(52) + 1;
                    P[k] = Cd.Cardget(i);
                    psum1 += s1.sumget(P[k]);
                    psum2 += s2.sumget(P[k]);

                    try {
                        semaphore.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(() -> {
                        PIV.get(k-1).setImageResource(IV.get(i-1));
                        txt.append("闲家的牌为" + Cd.Cardname(P[k]) + "\n");
                        offset=gtvh.getTextViewHeight(txt);
                        if (offset > txt.getHeight())
                            txt.scrollTo(0, offset - txt.getHeight());
                        semaphore.release();
                    });
                    //semaphore2.release();


                    if (psum1 > 21) {
                        handler.post(() -> {
                            txt.append("闲家爆牌，庄家胜利。\n");
                            offset=gtvh.getTextViewHeight(txt);
                            if (offset > txt.getHeight())
                                txt.scrollTo(0, offset - txt.getHeight());
                        });
                        break;
                    } else if (psum1 == psum2 || psum2 > 21) {
                        handler.post(() -> {
                            txt.append("闲家总点数为" + psum1 + "\n闲家是否继续拿牌？（按Confirm继续按Exit退出）\n");
                            offset=gtvh.getTextViewHeight(txt);
                            if (offset > txt.getHeight())
                                txt.scrollTo(0, offset - txt.getHeight());
                        });
                        synchronized (object) {
                            try {
                                object.wait();
                            } catch (InterruptedException e) {
                                handler.post(() -> txt.setText("出现错误"));
                                break;
                            }
                        }//wait函数怎么写
                    }
                    else if(psum1==2 && psum2==22) {
                        handler.post(() -> {
                            txt.append("闲家总点数为2/12\n闲家是否继续拿牌？（按Confirm继续按Exit退出）\n");
                            offset=gtvh.getTextViewHeight(txt);
                            if (offset > txt.getHeight())
                                txt.scrollTo(0, offset - txt.getHeight());
                        });
                        synchronized (object) {
                            try {
                                object.wait();
                            } catch (InterruptedException e) {
                                handler.post(() -> txt.setText("出现错误"));
                                break;
                            }
                        }
                    }
                    else {
                        handler.post(() -> {
                            txt.append("闲家总点数为" + psum1 + "/" + psum2 + "\n闲家是否继续拿牌？（按Confirm继续按Exit退出）\n");
                            offset=gtvh.getTextViewHeight(txt);
                            if (offset > txt.getHeight())
                                txt.scrollTo(0, offset - txt.getHeight());
                        });
                        synchronized (object) {
                            try {
                                object.wait();
                            } catch (InterruptedException e) {
                                handler.post(() -> txt.setText("出现错误"));
                                break;
                            }
                        }//wait函数怎么写
                    }
                }
                if (psum1 > 21) {
                    lose++;
                    state1 = 1;
                    state2 = 0;
                    handler.post(() -> {
                        txt.append("是否继续游戏，继续请按Confirm，退出请按Exit");
                        offset=gtvh.getTextViewHeight(txt);
                        if (offset > txt.getHeight())
                            txt.scrollTo(0, offset - txt.getHeight());
                    });
                    synchronized (object) {
                        try {
                            object.wait();
                        } catch (InterruptedException e) {
                            handler.post(() -> txt.setText("出现错误"));
                            break;
                        }
                    }//wait函数怎么写
                    if (state3 == 0) {
                        state1 = 0;
                        break;
                    } else if (state3 == 1) {
                        state1 = 0;
                        state2 = 1;
                        continue;
                    }
                }

                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.post(() -> {
                    txt.append("庄家的暗牌为" + Cd.Cardname(D[1]) + "\n");
                    DIV.get(0).setImageResource(IV.get(hide-1));
                    offset=gtvh.getTextViewHeight(txt);
                    if (offset > txt.getHeight())
                        txt.scrollTo(0, offset - txt.getHeight());
                    semaphore.release();
                });

                /*try {
                    semaphore2.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                while (dsum2 < 17 || (dsum2 > 21 && dsum1 < 17)) {
                    try {
                        semaphore.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    j++;
                    i = r.nextInt(52) + 1;
                    D[j] = Cd.Cardget(i);
                    dsum1 += s1.sumget(D[j]);
                    dsum2 += s2.sumget(D[j]);
                    semaphore.release();
                    try {
                        semaphore.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(() -> {
                        txt.append("庄家新抽的牌为" + Cd.Cardname(D[j])+"\n");
                        DIV.get(j-1).setImageResource(IV.get(i-1));
                        offset=gtvh.getTextViewHeight(txt);
                        if (offset > txt.getHeight())
                            txt.scrollTo(0, offset - txt.getHeight());
                        semaphore.release();
                    });

                }
                //semaphore2.release();

                if (dsum2 < 21)
                    handler.post(() -> {
                        txt.append("庄家的总点数为" + dsum2 + "\n");
                        offset=gtvh.getTextViewHeight(txt);
                        if (offset > txt.getHeight())
                            txt.scrollTo(0, offset - txt.getHeight());
                    });
                else
                    handler.post(() -> {
                        txt.append("庄家的总点数为" + dsum1 + "\n");
                        offset=gtvh.getTextViewHeight(txt);
                        if (offset > txt.getHeight())
                            txt.scrollTo(0, offset - txt.getHeight());
                    });

                handler.post(() -> {
                    txt.append(vic.Judge(dsum1, dsum2, psum1, psum2));//判定胜负
                    offset=gtvh.getTextViewHeight(txt);
                    if (offset > txt.getHeight())
                        txt.scrollTo(0, offset - txt.getHeight());
                });


                state1 = 1;
                state2 = 0;
                handler.post(() -> {
                    txt.append("\n是否继续游戏，继续请按Confirm，退出请按Exit");
                    offset=gtvh.getTextViewHeight(txt);
                    if (offset > txt.getHeight())
                        txt.scrollTo(0, offset - txt.getHeight());
                });

                synchronized (object) {
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        handler.post(() -> txt.setText("出现错误"));
                        break;
                    }
                }//wait函数怎么写
                if (state3 == 0) {
                    state1 = 0;
                    break;
                } else if (state3 == 1) {
                    state1 = 0;
                    state2 = 1;

                }
            }

            W = ((double) win) / (double) all;
            E = ((double) win - (double) lose) / (double) all;
            handler.post(() -> {
                txt.setText("21点没法帮你实现财富自由，珍爱生命，远离赌博\n按Exit退出游戏");
                txt.scrollTo(0,0);

            });
        }
    });

}