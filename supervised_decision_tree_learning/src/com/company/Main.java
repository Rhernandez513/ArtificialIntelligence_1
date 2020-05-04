package com.company;

import com.company.enums.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

  public static void main(String[] args) throws ApplicationRuntimeException {
    final String path = (args.length > 0) ? args[0] : "resources"+ File.separator +"restaurant.csv";
    String contents = Util.readFile(path);
    List<Example> examples = Util.map(contents);
    System.out.println("Data read into a structure: ");
    printAttributes(examples);
    System.out.println();
    Node root =
        DecisionTreeLearning(
            examples, new ArrayList<>(Arrays.asList(Attribute.values())), examples);
    System.out.println(root);
  }

  static Node DecisionTreeLearning(
      List<Example> examples, ArrayList<Attribute> attributes, List<Example> parentExamples) {
    if (examples.isEmpty()) {
      return PluralityValue(parentExamples);
    } else if (allSameClassification(examples)) {
      Node classification = new Node(true);
      classification.setValue(examples.get(0).willWait());
      return classification;
    } else if (attributes.isEmpty()) {
      return PluralityValue(examples);
    }
    Attribute A = argMax(attributes, examples);
    Node tree = new Node(false);
    tree.setAttribute(A);

    List<Example> exs = new ArrayList<>();
    for (Object v_k : V(A)) {
      exs.addAll(EXS(examples, v_k));
      attributes.remove(A);
      Node subTree = DecisionTreeLearning(exs, attributes, examples);
      Map.Entry<Attribute, String> attributePair = determineAttribute(v_k);
      subTree.setAttribute(attributePair.getKey());
      subTree.setAttributeDetail(attributePair.getValue());
      tree.addChild(subTree);
    }

    return tree;
  }

  static Map.Entry<Attribute, String> determineAttribute(Object v) {
    Map<Attribute, String> pair = new HashMap<>();
    if (v instanceof Alternative) {
      pair.put(Attribute.Alt, "Alternative." + ((Alternative) v).toString());
      return pair.entrySet().iterator().next();
    } else if (v instanceof Bar) {
      pair.put(Attribute.Bar, "Bar." + ((Bar) v).toString());
    } else if (v instanceof FridayOrSaturday) {
      pair.put(Attribute.Fri, "FriOrSat." + ((FridayOrSaturday) v).toString());
    } else if (v instanceof Hungry) {
      pair.put(Attribute.Hun, "Hungry." + ((Hungry) v).toString());
    } else if (v instanceof Raining) {
      pair.put(Attribute.Rain, "Raining." + ((Raining) v).toString());
    } else if (v instanceof Reservation) {
      pair.put(Attribute.Res, "Reservation." + ((Reservation) v).toString());
    } else if (v instanceof Patrons) {
      pair.put(Attribute.Pat, "Patrons." + ((Patrons) v).toString());
    } else if (v instanceof Price) {
      pair.put(Attribute.Price, "Price." + ((Price) v).toString());
    } else if (v instanceof Type) {
      pair.put(Attribute.Type, "Type." + ((Type) v).toString());
    } else if (v instanceof Estimate) {
      pair.put(Attribute.Est, "WaitEstimate." + ((Estimate) v).toString());
    }
    return pair.entrySet().iterator().next();
  }

  static List<Example> EXS(List<Example> examples, Object v_k) {
    List<Example> exs = new ArrayList<>();
    if (v_k instanceof Alternative) {
      for (Example e : examples) {
        if (e.getAlternative().equals((Alternative) v_k)) {
          exs.add(e);
        }
      }
    } else if (v_k instanceof Bar) {
      for (Example e : examples) {
        if (e.getBar().equals((Bar) v_k)) {
          exs.add(e);
        }
      }
    } else if (v_k instanceof FridayOrSaturday) {
      for (Example e : examples) {
        if (e.getFridayOrSaturday().equals((FridayOrSaturday) v_k)) {
          exs.add(e);
        }
      }
    } else if (v_k instanceof Hungry) {
      for (Example e : examples) {
        if (e.getHungry().equals((Hungry) v_k)) {
          exs.add(e);
        }
      }
    } else if (v_k instanceof Raining) {
      for (Example e : examples) {
        if (e.getRaining().equals((Raining) v_k)) {
          exs.add(e);
        }
      }
    } else if (v_k instanceof Reservation) {
      for (Example e : examples) {
        if (e.getReservation().equals((Reservation) v_k)) {
          exs.add(e);
        }
      }
    } else if (v_k instanceof Patrons) {
      for (Example e : examples) {
        if (e.getPatrons().equals((Patrons) v_k)) {
          exs.add(e);
        }
      }
    } else if (v_k instanceof Price) {
      for (Example e : examples) {
        if (e.getPrice().equals((Price) v_k)) {
          exs.add(e);
        }
      }
    } else if (v_k instanceof Type) {
      for (Example e : examples) {
        if (e.getType().equals((Type) v_k)) {
          exs.add(e);
        }
      }
    } else if (v_k instanceof Estimate) {
      for (Example e : examples) {
        if (e.getEstimate().equals((Estimate) v_k)) {
          exs.add(e);
        }
      }
    }
    return exs;
  }

  static Object[] V(Attribute a) {
    switch (a) {
      case Alt:
        return new Object[] {Alternative.Yes, Alternative.No};
      case Bar:
        return new Object[] {Bar.Yes, Bar.No};
      case Fri:
        return new Object[] {FridayOrSaturday.Yes, FridayOrSaturday.No};
      case Hun:
        return new Object[] {Hungry.Yes, Hungry.No};
      case Rain:
        return new Object[] {Raining.Yes, Raining.No};
      case Res:
        return new Object[] {Reservation.Yes, Reservation.No};
      case Pat:
        return new Object[] {Patrons.None, Patrons.Some, Patrons.Full};
      case Price:
        return new Object[] {Price.$, Price.$$, Price.$$$};
      case Type:
        return new Object[] {Type.French, Type.Thai, Type.Italian, Type.Burger};
      case Est:
        return new Object[] {Estimate.Short, Estimate.Moderate, Estimate.Long, Estimate.Extreme};
    }
    return null;
  }

  static Node PluralityValue(List<Example> examples) {
    Node classification = new Node(true);

    List<Boolean> Yeses = new ArrayList<>();
    List<Boolean> Nos = new ArrayList<>();

    for (Example e : examples) {
      if (e.willWait()) {
        Yeses.add(e.willWait());
      } else {
        Nos.add(e.willWait());
      }
    }
    if (Yeses.size() > Nos.size()) {
      classification.setValue(Yeses.get(0));
    } else {
      classification.setValue(Nos.get(0));
    }
    return classification;
  }

  static Boolean allSameClassification(List<Example> examples) {
    boolean flag = false;

    List<Boolean> Yeses = new ArrayList<>();
    List<Boolean> Nos = new ArrayList<>();
    for (Example e : examples) {
      if (e.willWait()) {
        Yeses.add(e.willWait());
      } else {
        Nos.add(e.willWait());
      }
    }
    if (Yeses.size() == examples.size() || Nos.size() == examples.size()) {
      return true;
    }
    return false;
  }

  static Attribute argMax(List<Attribute> attributes, List<Example> examples) {
    Double[] Args = new Double[attributes.size()];
    for (int i = 0; i < Args.length; ++i) {
      Args[i] = importance(attributes.get(i), examples);
    }
    Map<Integer, Double> _max = new HashMap<>();
    _max.put(0, -1.0);
    Map.Entry<Integer, Double> max = _max.entrySet().iterator().next();
    for (int i = 0; i < Args.length; ++i) {
      if (Args[i] > max.getValue()) {
        _max = new HashMap<>();
        _max.put(i, Args[i]);
        max = _max.entrySet().iterator().next();
      }
    }
    return attributes.get(max.getKey());
  }

  static void printAttributes(List<Example> atrList) {
    for (Example a : atrList) {
      System.out.println(a);
    }
  }

  static Double importance(Attribute attribute, List<Example> examples) {
    double p, n;
    p = n = 0.000;
    for (Example e : examples) {
      if (e.willWait()) {
        p++;
      } else {
        n++;
      }
    }

    return Gain(attribute, examples, p, n);
  }

  static Double B(Double q) {
    if (q.equals(0) || q.equals(1)) {
      return log2(1.0);
    }
    return -1 * (q * log2(q) + (1 - q) * log2(1 - q));
  }

  private static Double log2(Double x) {
    return (Math.log(x) / Math.log(2) + 1e-10);
  }

  static Double Gain(Attribute attribute, List<Example> examples, Double p, Double n) {
    return B(p / (p + n)) - Remainder(attribute, examples, p, n);
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
        for (int k = 0; k < d; ++k) {
          for (Example e : examples) {
            if (k == 0 && !e.isAlternative()) {
              // looking for No Alternatives
              if (e.willWait()) {
                E[k][0]++;
              } else {
                E[k][1]++;
              }
            }
            if (k == 1 && e.isAlternative()) {
              // looking for Yes Alternative
              if (e.willWait()) {
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
        for (int k = 0; k < d; ++k) {
          for (Example e : examples) {
            if (k == 0 && !e.isBar()) {
              // looking for No Bar
              if (e.willWait()) {
                E[k][0]++;
              } else {
                E[k][1]++;
              }
            }
            if (k == 1 && e.isBar()) {
              // looking for Yes Bar
              if (e.willWait()) {
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
        for (int k = 0; k < d; ++k) {
          for (Example e : examples) {
            if (k == 0 && !e.isFridayOrSaturday()) {
              // looking for Not the Weekend
              if (e.willWait()) {
                E[k][0]++;
              } else {
                E[k][1]++;
              }
            }
            if (k == 1 && e.isFridayOrSaturday()) {
              // looking for Yes the Weekend
              if (e.willWait()) {
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
        for (int k = 0; k < d; ++k) {
          for (Example e : examples) {
            if (k == 0 && !e.isHungry()) {
              // looking for Not Hungry
              if (e.willWait()) {
                E[k][0]++;
              } else {
                E[k][1]++;
              }
            }
            if (k == 1 && e.isHungry()) {
              // looking for Yes Hungry
              if (e.willWait()) {
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
        for (int k = 0; k < d; ++k) {
          for (Example e : examples) {
            if (k == 0 && e.getPatrons() == Patrons.None) {
              if (e.willWait()) {
                E[k][0]++;
              } else {
                E[k][1]++;
              }
            } else if (k == 1 && e.getPatrons() == Patrons.Some) {
              if (e.willWait()) {
                E[k][0]++;
              } else {
                E[k][1]++;
              }
            } else if (k == 2 && e.getPatrons() == Patrons.Full) {
              if (e.willWait()) {
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
        for (int k = 0; k < d; ++k) {
          for (Example e : examples) {
            if (k == 0 && e.getPrice() == Price.$) {
              if (e.willWait()) {
                E[k][0]++;
              } else {
                E[k][1]++;
              }
            } else if (k == 1 && e.getPrice() == Price.$$) {
              if (e.willWait()) {
                E[k][0]++;
              } else {
                E[k][1]++;
              }
            } else if (k == 2 && e.getPrice() == Price.$$$) {
              if (e.willWait()) {
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
        for (int k = 0; k < d; ++k) {
          for (Example e : examples) {
            if (k == 0 && !e.isRaining()) {
              // looking for No Rain
              if (e.willWait()) {
                E[k][0]++;
              } else {
                E[k][1]++;
              }
            }
            if (k == 1 && e.isRaining()) {
              // looking for Yes Rain
              if (e.willWait()) {
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
        for (int k = 0; k < d; ++k) {
          for (Example e : examples) {
            if (k == 0 && !e.isReservation()) {
              // looking for No Reservation
              if (e.willWait()) {
                E[k][0]++;
              } else {
                E[k][1]++;
              }
            }
            if (k == 1 && e.isReservation()) {
              // looking for Yes Reservation
              if (e.willWait()) {
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
        for (int k = 0; k < d; ++k) {
          for (Example e : examples) {
            if (k == 0 && e.getType() == Type.French) {
              if (e.willWait()) {
                E[k][0]++;
              } else {
                E[k][1]++;
              }
            } else if (k == 1 && e.getType() == Type.Thai) {
              if (e.willWait()) {
                E[k][0]++;
              } else {
                E[k][1]++;
              }
            } else if (k == 2 && e.getType() == Type.Burger) {
              if (e.willWait()) {
                E[k][0]++;
              } else {
                E[k][1]++;
              }
            } else if (k == 3 && e.getType() == Type.Italian) {
              if (e.willWait()) {
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
        for (int k = 0; k < d; ++k) {
          for (Example e : examples) {
            if (k == 0 && e.getEstimate() == Estimate.Short) {
              if (e.willWait()) {
                E[k][0]++;
              } else {
                E[k][1]++;
              }
            } else if (k == 1 && e.getEstimate() == Estimate.Moderate) {
              if (e.willWait()) {
                E[k][0]++;
              } else {
                E[k][1]++;
              }
            } else if (k == 2 && e.getEstimate() == Estimate.Long) {
              if (e.willWait()) {
                E[k][0]++;
              } else {
                E[k][1]++;
              }
            } else if (k == 3 && e.getEstimate() == Estimate.Extreme) {
              if (e.willWait()) {
                E[k][0]++;
              } else {
                E[k][1]++;
              }
            }
          }
        }
        break;
    }
    Double sigma_d_k = Double.valueOf(0.000);
    for (int k = 0; k < d; ++k) {
      Double p_k = Double.valueOf(E[k][0]);
      Double n_k = Double.valueOf(E[k][1]);
      sigma_d_k += (p_k + n_k) / (p + n) * B(p_k / (p_k + n_k));
    }

    return sigma_d_k;
  }
}
