package com.company;

import com.company.enums.*;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class Main {

    public static void main(String[] args) throws ApplicationRuntimeException {
        final String path = "resources\\restaurant.csv";
        String contents = Util.readFile(path);
        List<Example> examples = Util.map(contents);
        printAttributes(examples);
        System.out.println(argMax(examples));
    }

    static Attribute argMax(List<Example> examples) {
        Double[] Args = new Double[Attribute.values().length];
        for (int i = 0; i < Args.length; ++i) {
            Args[i] = importance(Attribute.values()[i], examples);
        }
        Pair<Integer, Double> max = new Pair<>(0, -1.0);
        for(int i = 0; i < Args.length; ++i) {
            if(Args[i] > max.getValue()) {
                max = new Pair<>(i, Args[i]);
            }
        }
//        System.out.println(String.format("Argmax i=%s, val=%s", Attribute.values()[max.getKey()], max.getValue()));
        return Attribute.values()[max.getKey()];
    }

    static void printAttributes(List<Example> atrList) {
        for(Example a : atrList) {
            System.out.println(a);
        }
    }

    static Double importance(Attribute attribute, List<Example> examples) {
        double p, n;
        p = n = 0.000;
        for(Example e : examples) {
            if (e.willWait()) {
                p++;
            } else {
                n++;
            }
        }

        return Gain(attribute, examples, p, n);
    }

    static Double B(Double p, Double n) {
        return p / (p + n);
    }

    static Double Gain(Attribute attribute, List<Example> examples, Double p, Double n) {
        return B(p, n) - Remainder(attribute, examples, p, n);
    }

    // Remainder(A) = sigma_d_k=1 {(p_k + n_k / p+n) * B(p_k / p_k + n_k)}
    static Double Remainder(Attribute attribute, List<Example> examples, Double p, Double n) {
        int d = 0;
        int[][] E = null;
        switch (attribute) {
            case Alt:
                // No=0, Yes
                d = 2;
                // 0 -> p_k, 1 -> n_k
                E = new int[d][2];
                for(int k = 0; k < d; ++k) {
                    for(Example e : examples) {
                        if(k==0 && !e.isAlternative()) {
                            // looking for No Alternantive
                            if(e.willWait()) {
                                E[k][0]++;
                            } else {
                                E[k][1]++;
                            }
                        }
                        if(k==1 && e.isAlternative()) {
                            // looking for Yes Alternative
                            if(e.willWait()) {
                                E[k][0]++;
                            } else {
                                E[k][1]++;
                            }
                        }
                    }
                }
                break;
            case Bar:
                // No=0, Yes
                d = 2;
                // 0 -> p_k, 1 -> n_k
                E = new int[d][2];
                for(int k = 0; k < d; ++k) {
                    for(Example e : examples) {
                        if(k==0 && !e.isBar()) {
                            // looking for No Bar
                            if(e.willWait()) {
                                E[k][0]++;
                            } else {
                                E[k][1]++;
                            }
                        }
                        if(k==1 && e.isBar()) {
                            // looking for Yes Bar
                            if(e.willWait()) {
                                E[k][0]++;
                            } else {
                                E[k][1]++;
                            }
                        }
                    }
                }
                break;
            case Fri:
                // No=0, Yes
                d = 2;
                // 0 -> p_k, 1 -> n_k
                E = new int[d][2];
                for(int k = 0; k < d; ++k) {
                    for(Example e : examples) {
                        if(k==0 && !e.isFridayOrSaturday()) {
                            // looking for Not the Weekend
                            if(e.willWait()) {
                                E[k][0]++;
                            } else {
                                E[k][1]++;
                            }
                        }
                        if(k==1 && e.isFridayOrSaturday()) {
                            // looking for Yes the Weekend
                            if(e.willWait()) {
                                E[k][0]++;
                            } else {
                                E[k][1]++;
                            }
                        }
                    }
                }
                break;
            case Hun:
                // No=0, Yes
                d = 2;
                // 0 -> p_k, 1 -> n_k
                E = new int[d][2];
                for(int k = 0; k < d; ++k) {
                    for(Example e : examples) {
                        if(k==0 && !e.isHungry()) {
                            // looking for Not Hungry
                            if(e.willWait()) {
                                E[k][0]++;
                            } else {
                                E[k][1]++;
                            }
                        }
                        if(k==1 && e.isHungry()) {
                            // looking for Yes Hungry
                            if(e.willWait()) {
                                E[k][0]++;
                            } else {
                                E[k][1]++;
                            }
                        }
                    }
                }
                break;
            case Pat:
                // None=0, Some, Full
                d = 3;
                // 0 -> p_k, 1 -> n_k
                E = new int[d][2];
                for(int k = 0; k < d; ++k) {
                    for(Example e : examples) {
                        if(k==0 && e.getPatrons() == Patrons.None) {
                            if(e.willWait()) {
                                E[k][0]++;
                            } else {
                                E[k][1]++;
                            }
                        } else if (k==1 && e.getPatrons() == Patrons.Some) {
                            if(e.willWait()) {
                                E[k][0]++;
                            } else {
                                E[k][1]++;
                            }
                        } else if (k==2 && e.getPatrons() == Patrons.Full) {
                            if(e.willWait()) {
                                E[k][0]++;
                            } else {
                                E[k][1]++;
                            }
                        }
                    }
                }
                break;
            case Price:
                // $=0, $$, $$$
                d = 3;
                // 0 -> p_k, 1 -> n_k
                E = new int[d][2];
                for(int k = 0; k < d; ++k) {
                    for(Example e : examples) {
                        if(k==0 && e.getPrice() == Price.$) {
                            if(e.willWait()) {
                                E[k][0]++;
                            } else {
                                E[k][1]++;
                            }
                        } else if (k==1 && e.getPrice() == Price.$$) {
                            if(e.willWait()) {
                                E[k][0]++;
                            } else {
                                E[k][1]++;
                            }
                        } else if (k==2 && e.getPrice() == Price.$$$) {
                            if(e.willWait()) {
                                E[k][0]++;
                            } else {
                                E[k][1]++;
                            }
                        }
                    }
                }
                break;
            case Rain:
                // No=0, Yes
                d = 2;
                // 0 -> p_k, 1 -> n_k
                E = new int[d][2];
                for(int k = 0; k < d; ++k) {
                    for(Example e : examples) {
                        if(k==0 && !e.isRaining()) {
                            // looking for No Rain
                            if(e.willWait()) {
                                E[k][0]++;
                            } else {
                                E[k][1]++;
                            }
                        }
                        if(k==1 && e.isRaining()) {
                            // looking for Yes Rain
                            if(e.willWait()) {
                                E[k][0]++;
                            } else {
                                E[k][1]++;
                            }
                        }
                    }
                }
                break;
            case Res:
                // No=0, Yes
                d = 2;
                // 0 -> p_k, 1 -> n_k
                E = new int[d][2];
                for(int k = 0; k < d; ++k) {
                    for(Example e : examples) {
                        if(k==0 && !e.isReservation()) {
                            // looking for No Reservation
                            if(e.willWait()) {
                                E[k][0]++;
                            } else {
                                E[k][1]++;
                            }
                        }
                        if(k==1 && e.isReservation()) {
                            // looking for Yes Reservation
                            if(e.willWait()) {
                                E[k][0]++;
                            } else {
                                E[k][1]++;
                            }
                        }
                    }
                }
                break;
            case Type:
                // French=0, Thai, Burger, Italian
                d = 4;
                // 0 -> p_k, 1 -> n_k
                E = new int[d][2];
                for(int k = 0; k < d; ++k) {
                    for(Example e : examples) {
                        if(k==0 && e.getType() == Type.French) {
                            if(e.willWait()) {
                                E[k][0]++;
                            } else {
                                E[k][1]++;
                            }
                        } else if (k==1 && e.getType() == Type.Thai) {
                            if(e.willWait()) {
                                E[k][0]++;
                            } else {
                                E[k][1]++;
                            }
                        } else if (k==2 && e.getType() == Type.Burger) {
                            if(e.willWait()) {
                                E[k][0]++;
                            } else {
                                E[k][1]++;
                            }
                        } else if (k==3 && e.getType() == Type.Italian) {
                            if(e.willWait()) {
                                E[k][0]++;
                            } else {
                                E[k][1]++;
                            }
                        }
                    }
                }
                break;
            case Est:
                // Short = 0-10, Moderate = 10-30, Long = 30-60, Extreme = >60
                // Short=0, Moderate, Long, Extreme
                d = 4;
                // 0 -> p_k, 1 -> n_k
                E = new int[d][2];
                for(int k = 0; k < d; ++k) {
                    for(Example e : examples) {
                        if(k==0 && e.getWaitEstimate() == WaitEstimate.Short) {
                            if(e.willWait()) {
                                E[k][0]++;
                            } else {
                                E[k][1]++;
                            }
                        } else if (k==1 && e.getWaitEstimate() == WaitEstimate.Moderate) {
                            if(e.willWait()) {
                                E[k][0]++;
                            } else {
                                E[k][1]++;
                            }
                        } else if (k==2 && e.getWaitEstimate() == WaitEstimate.Long) {
                            if(e.willWait()) {
                                E[k][0]++;
                            } else {
                                E[k][1]++;
                            }
                        } else if (k==3 && e.getWaitEstimate() == WaitEstimate.Extreme) {
                            if(e.willWait()) {
                                E[k][0]++;
                            } else {
                                E[k][1]++;
                            }
                        }
                    }
                }
                break;
        }

        Double sigma_d_k = new Double(0.000);
        for(int k = 0; k < d; ++k)  {
            Double p_k = new Double(E[k][0]);
            Double n_k = new Double(E[k][1]);
            sigma_d_k += (p_k + n_k) / (p + n) * B(p_k, n_k);
        }

        return sigma_d_k;
    }

}
