import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class Population {
  //ATTRIBUTS
  ArrayList<AgendaS> codage;
  ArrayList<AgendaS> signe;

  //METHODES
  public Population(){
    codage = new ArrayList<AgendaS>();
    signe = new ArrayList<AgendaS>();
  }
  //croisement 1 p comme vu en cours + appelle des mutations
  public ArrayList<AgendaS> croisement1p(AgendaS p1, AgendaS p2){
    ArrayList<AgendaS> tmp = new ArrayList<AgendaS>();
    Random rand = new Random();
    int s = p1.Size();
    int r1 = rand.nextInt(s);
    
    AgendaS a1 = new AgendaS();
    AgendaS a2 = new AgendaS();

    a1.agenda.addAll(new ArrayList<Integer>(p1.agenda.subList(0, r1)));
    a1.agenda.addAll(new ArrayList<Integer>(p2.agenda.subList(r1, s)));

    a2.agenda.addAll(new ArrayList<Integer>(p2.agenda.subList(0, r1)));
    a2.agenda.addAll(new ArrayList<Integer>(p1.agenda.subList(r1, s)));


    double r2 = rand.nextDouble();
    if(r2< 0.20){
      if(r2<0.05){
          a1.mut_deplacement();
      }else if(r2<0.10){
          a1.mut_insertion();
      }else if(r2<0.15){
          a1.mut_inversion();
      }else{
          a1.mut_permutation();
      }
    }
    r2 = rand.nextDouble();
    if(r2< 0.20){
      if(r2<0.05){
          a2.mut_deplacement();
      }else if(r2<0.10){
          a2.mut_insertion();
      }else if(r2<0.15){
          a2.mut_inversion();
      }else{
          a2.mut_permutation();
      }
    }

    tmp.add(a1);    
    tmp.add(a2);
    return tmp;
  }
  //algorithme roulette population codage
  public void roulette_c(){
    ArrayList<AgendaS> enfant_c = new ArrayList<AgendaS>();

    // CALCUL LES PROBA
    double sum = 0;
    double[] t = new double[codage.size()];
    for(int i = 0; i<codage.size();i++){
        t[i] = (1/ (double)codage.get(i).eval(1));
        sum += t[i];
    }

    t[0] = t[0]/sum;
    for(int i = 1; i<codage.size();i++){
        t[i] = t[i]/sum;
        t[i] += t[i-1];
    }
    for(int k=0; k< Main.NBR_POPULATION/2;k++){
      // CHOISI DES PARENTS
      Random rand = new Random();
      double r1 = rand.nextDouble();
      AgendaS p1 = new AgendaS();
      AgendaS p2= new AgendaS();
      for(int i = 0; i<codage.size();i++){
          if(r1<t[i]){
            p1 = codage.get(i);
            break;
          }
      }
      while(true){
        double r2 = rand.nextDouble();
        for(int i = 0; i<codage.size();i++){
          if(r2<t[i]){
            p2 = codage.get(i);
            break;
          }
        }
        if(p2!=p1){
          break;
        }
      }
      //CREER LES ENFANTS
      ArrayList<AgendaS> tmp = croisement1p(p1,p2);
      enfant_c.addAll(tmp);
    }
    codage = enfant_c;
  }
  //algorithme roulette population signe
  public void roulette_s(){
    ArrayList<AgendaS> enfant_s = new ArrayList<AgendaS>();

    // CALCUL LES PROBA
    double sum = 0;
    double[] t = new double[signe.size()];
    for(int i = 0; i<signe.size();i++){
        t[i] = (1/ (double)signe.get(i).eval(0));
        sum += t[i];
    }

    t[0] = t[0]/sum;
    for(int i = 1; i<signe.size();i++){
        t[i] = t[i]/sum;
        t[i] += t[i-1];
    }

    for(int k=0; k< Main.NBR_POPULATION/2;k++){
      // CHOISI DES PARENTS
      Random rand = new Random();
      double r1 = rand.nextDouble();
      AgendaS p1 = new AgendaS();
      AgendaS p2= new AgendaS();
      for(int i = 0; i<signe.size();i++){
          if(r1<t[i]){
            p1 = signe.get(i);
            break;
          }
      }
      while(true){
        double r2 = rand.nextDouble();
        for(int i = 0; i<signe.size();i++){
          if(r2<t[i]){
            p2 = signe.get(i);
            break;
          }
        }
        if(p2!=p1){
          break;
        }
      }
      //CREER LES ENFANTS
      ArrayList<AgendaS> tmp = croisement1p(p1,p2);
      enfant_s.addAll(tmp);
    }
    signe = enfant_s;
  }
  //trouve le meilleur agenda de la population de codage
  public AgendaS get_min_c(){
    int min = codage.get(0).eval(1);
    AgendaS tmp = new AgendaS();
    tmp = codage.get(0);
    for(int i = 1; i< codage.size();i++){
      if(codage.get(i).eval(1) < min){
        min = codage.get(i).eval(1);
        tmp = codage.get(i);
      }
    }
    return tmp;
  }
  //trouve le meilleur agenda de la population de signe
  public AgendaS get_min_s(){
    int min = signe.get(0).eval(0);
    AgendaS tmp = new AgendaS();
    tmp = signe.get(0);
    for(int i = 1; i< signe.size();i++){
      if(signe.get(i).eval(0) < min){
        min = signe.get(i).eval(0);
        tmp = signe.get(i);
      }
    }
    return tmp;
  }

  //lance l'lgorithme genetique
  public void search(){
    AgendaS global_best_c = get_min_c();
    AgendaS global_best_s = get_min_s();
    
    int it=0;
    while(it<1000){
      roulette_c();
      AgendaS local_best = get_min_c();
      if(local_best.eval(1) < global_best_c.eval(1)){
        global_best_c = local_best;
        System.out.println("nouveaux minimum codage: "+global_best_c.eval(1));
      }else{
        it++;//pas d amelioration
      }
    }
    it=0;
    while(it<1000){
      roulette_s();
      AgendaS local_best = get_min_s();
      if(local_best.eval(0) < global_best_s.eval(0)){
        global_best_s = local_best;
        System.out.println("nouveaux minimum signe: "+ global_best_s.eval(0));
      }else{
        it++;//pas d amelioration
      }
    } 
    System.out.println("");
    System.out.println("eval codage: "+ global_best_c.eval(1));
    System.out.println("Agenda spécialité codage:");
    for(int i =0; i<global_best_c.Size();i++){
      System.out.print(global_best_c.agenda.get(i)+" ");
    }

    System.out.println("");
    System.out.println("eval signe: "+ global_best_s.eval(0));
    System.out.println("Agenda spécialité signe:");

    for(int i =0; i<global_best_s.Size();i++){
      System.out.print(global_best_s.agenda.get(i)+" ");
    }
    System.out.println("");
    System.out.println("Agenda de la semaine : ");
    
    it =0;
    int it2 = 0; 
    for(int i =0;i<Main.NBR_FORMATION;i++){
      if(Main.codage_f.get(it)==i){
        System.out.print(global_best_c.agenda.get(it)+" ");
        it++;
      }else{
        System.out.print(global_best_s.agenda.get(it2)+" ");
        it2++;
      }
    }
  }

}