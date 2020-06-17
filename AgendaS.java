import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Random;

public class AgendaS {
  //ATTRIBUTS
  ArrayList<Integer> agenda;
  ArrayList<ArrayList<Integer>> poidsIA;
  ArrayList<ArrayList<Integer>> tempsT;
  ArrayList<ArrayList<Integer>> distP;

  int[][] formation = Main.formation;
  int[][] specialite_interfaces = Main.specialite_interfaces;
  int[][] competences_interfaces = Main.competences_interfaces;
  static ArrayList<ArrayList<Double>> distAA = Main.distAA;
  static ArrayList<Double> distAC = Main.distAC;
  static ArrayList<ArrayList<Double>> distAI = Main.distAI;
  
  //METHODES
  public AgendaS() {
    agenda = new ArrayList<Integer>();
  }
  public AgendaS(AgendaS a) {
    this.agenda = new ArrayList<Integer>(a.agenda);
  }

  public void Shuffle(){
    Collections.shuffle(this.agenda);
  }
  public int Size(){
    return agenda.size();
  }
  //Fonctions de mutations
  public void mut_permutation(){
    Random rand = new Random();
    int r1 = rand.nextInt(agenda.size());
    int r2 = rand.nextInt(agenda.size());
    while(r1==r2){
      r2 = rand.nextInt(agenda.size());
    }
    int tmp = agenda.get(r1);
    agenda.set(r1,agenda.get(r2));
    agenda.set(r2,tmp);
  }

  public void mut_insertion(){
    Random rand = new Random();
    int r1 = rand.nextInt(agenda.size());
    int r2 = rand.nextInt(agenda.size());
    while(r1==r2){
      r2 = rand.nextInt(agenda.size());
    }
    int tmp = agenda.get(r2);
    agenda.remove(r2);
    agenda.add(r1, tmp);
  }

  public void mut_inversion(){
    Random rand = new Random();
    int r1 = rand.nextInt(agenda.size()-1);
    int r2 = rand.nextInt(agenda.size());
    while(r1>=r2){
      r2 = rand.nextInt(agenda.size());
    }
    int j = r2;
    int l = (int)(r2-((r2-r1)/2));
    for(int i=r1;i<l;i++){
      int tmp = agenda.get(i);
      agenda.set(i,agenda.get(j));
      agenda.set(j,tmp);
      j--;
    }
  }

  public void mut_deplacement(){
    Random rand = new Random();
    int r1 = rand.nextInt(agenda.size()-2)+1;
    int r2 = rand.nextInt(agenda.size());
    while(r1>=r2){
      r2 = rand.nextInt(agenda.size());
    }
    List<Integer> a1 = new ArrayList<Integer>(agenda.subList(0, r1));
    List<Integer> a2 = new ArrayList<Integer>(agenda.subList(r1, r2+1));
    List<Integer> a3 = new ArrayList<Integer>(agenda.subList(r2+1, agenda.size()));
  
    agenda.clear();
    agenda.addAll(a2);
    agenda.addAll(a1);
    agenda.addAll(a3);
  }

  //Fonctions utilisé pour l'évaluation
  public int getTime_Interface_Week(int id, int competence){
    ArrayList<Integer> agendaTmp;
    if(competence == 0){
      agendaTmp = Main.signe_f;
    }
    else{
      agendaTmp = Main.codage_f;
    }
      int time = 0;
      int formationTime = 0; //Temps d'une formation
      
      for(int i = 0; i < agenda.size(); i++){
          if(agenda.get(i) == id){
              formationTime = formation[agendaTmp.get(i)][5] - formation[agendaTmp.get(i)][4]; //Fin de formation - Début de formation
              time += formationTime;
          }    
      }
      return time;
  }
  
  public int getTime_Interface_Day(int id, int day, int competence){
    ArrayList<Integer> agendaTmp;
    if(competence == 0){
      agendaTmp = Main.signe_f;
    }
    else{
      agendaTmp = Main.codage_f;
    }
      int time = 0;
      int formationTime = 0; //Temps d'une formation
      
      for(int i = 0; i < agenda.size(); i++){
          if(agenda.get(i) == id && formation[agendaTmp.get(i)][3] == day){
              formationTime = formation[agendaTmp.get(i)][5] - formation[agendaTmp.get(i)][4]; //Fin de formation - Début de formation
              time += formationTime;
          }    
      } 
      return time;
  }
  
  public double getDist_Interface(int id, int competence){
    ArrayList<Integer> agendaTmp;
    if(competence == 0){
      agendaTmp = Main.signe_f;
    }
    else{
      agendaTmp = Main.codage_f;
    }
      double dist = 0;
      int nbFormation = 0; //Nombre de formation dans la semaine
      int nbFormationD = 0; //Nombre de formation dans la journee
      int day = Main.LUNDI; //Jour de la formation
      int lastFormation = 0; //Id du dernier apprenant accompagné
      
      for(int i = 0 ; i < agenda.size(); i++){
          if(agenda.get(i) == id){
              nbFormation++;
              if(day == formation[agendaTmp.get(i)][3] || nbFormation == 1){
                nbFormationD++;
              }
              else{
                  dist += distAI.get(lastFormation).get(id); //Si dernier voyage de l'interface de la journée, alors ajoute la distance Apprenant/Interface
                  nbFormationD = 1;
              }
              day = formation[i][3];
              if(nbFormationD == 1){
                  dist += distAI.get(formation[agendaTmp.get(i)][0]).get(id); //Si premier voyage de l'interface de la journée, alors ajoute la distance Apprenant/Interface
              }
              else{
                  dist += distAA.get(lastFormation).get(formation[agendaTmp.get(i)][0]); //Si deuxième voyage de la journee, alors ajoute la distance Apprenant/Apprenant
              }
              lastFormation = formation[agendaTmp.get(i)][0];
              
              dist += distAC.get(formation[agendaTmp.get(i)][0]); //Ajoute la distance Apprenant/Centre de formation (aller/reotur)
          }
      }
      return dist;
  }
  
  public int getPoids(int id, int competence){
    ArrayList<Integer> agendaTmp;
    if(competence == 0){
      agendaTmp = Main.signe_f;
    }
    else{
      agendaTmp = Main.codage_f;
    }
    int poids = 0;
   
      
    for(int i = 0; i < agenda.size(); i++){
        if(agenda.get(i)==id){
          if(specialite_interfaces[id][0] == 0 && specialite_interfaces[id][1] == 0 && specialite_interfaces[id][2] == 0){ //sans spécialité
            poids += 1;
          }    
          else if(specialite_interfaces[id][formation[agendaTmp.get(i)][1]] == 1){ //meme spécialité
              poids += 0;
          }
          else{
            poids += 2;
          }
        }
      
    }
      
      return poids;
  }
  
  public double moyDiffDistance(int competence){ //Retourne la moyenne de la différence des distances parcourues par chaque interface
      double moy = 0;
      double tmp_moy = 0;
      double n = 0;
      ArrayList<Integer> list;
      if(competence == 0){
          list = Main.list_s;
      }
      else{
          list = Main.list_c;
      }
      
      for(int i = 0; i < list.size(); i++){
          for(int j = i + 1; j < list.size(); j++){
              tmp_moy = getDist_Interface(list.get(i), competence);
              tmp_moy -= getDist_Interface(list.get(j), competence);
              tmp_moy = Math.abs(tmp_moy);
              moy += tmp_moy;
              n++;
          }
      }
      
      moy = moy/n;
      
      return moy;
  }
  
  public int getNoPause(int competence){ //Retourne le nombre d'interfaces qui n'ont pas de pause à midi
      int pause = 0;
      ArrayList<Integer> agendaTmp;
      if(competence == 0){
        agendaTmp = Main.signe_f;
      }
      else{
        agendaTmp = Main.codage_f;
      }
      
      for(int i = 0; i < agenda.size(); i++){
          if(formation[agendaTmp.get(i)][4] <= 12 && formation[agendaTmp.get(i)][5] >= 13){ //Si l'interface travaille entre 12h et 13h
              pause++;
          }
      }
      
      return pause;
  }
  
  public int isOutTimeRange(int competence){ //Retourne vrai si une interface travaille en dehors son amplitude horaire (12h)
      int timeRange = 0;
      ArrayList<Integer> list;
      ArrayList<Integer> agendaTmp;
      if(competence == 0){
          list = Main.list_s;
          agendaTmp = Main.signe_f;
      }
      else{
          list = Main.list_c;
          agendaTmp = Main.codage_f;
      }
      int min = 100;
      int max = 0;
      
      for(int i = 0; i < list.size(); i++){
          min = 100;
          max = 0;
          for(int j = 0; j < agenda.size(); j++){ //Parcours la liste d'interface avec la bonne competence
              if(list.get(i) == agenda.get(j)){ //Parcours la liste d'interfaces qui sont attribués aux formations
                  if(formation[agendaTmp.get(j)][4] < min){
                      min = formation[agendaTmp.get(j)][4];
                  }
                  if(formation[agendaTmp.get(j)][5] > max){
                      max = formation[agendaTmp.get(j)][5];
                  }
              }
          }
          if(max - min > 12){ //Si l'interface travaille sur une intervalle plus grande que 12h, alors timeRange = true
              timeRange = 1;
          }
      }
      
      return timeRange;
  }
  
  public int isWorkingTooMuch(int competence){ //Retourne vrai si une interface travaille plus de 8h par jour ou plus de 35h par semaine
      int work = 0;
      ArrayList<Integer> list;
      if(competence == 0){
          list = Main.list_s;
      }
      else{
          list = Main.list_c;
      }
      
      for(int i = 0; i < list.size(); i++){
          if(getTime_Interface_Week(list.get(i), competence) > 35){
              work = 1;
          }
          for(int j = 1; j < 7; j++){
              if(getTime_Interface_Day(i, j, competence) > 8){
                  work = 1;
              }
          }
      }
      return work;
  }
  
  //retourne un int (on cherche a minimiser)
  public int eval(int competence){
    int eval = 0;
    int poids = 0;
    ArrayList<Integer> list;
    if(competence == 0){
        list = Main.list_s;
    }
    else{
        list = Main.list_c;
    }
      
    for(int i = 0; i < list.size(); i++){
        poids += getPoids(list.get(i), competence);
    }
    eval = (int)(moyDiffDistance(competence)/50) + getNoPause(competence)*10 + poids + (isOutTimeRange(competence) + isWorkingTooMuch(competence))*100;
    return eval;
  }

}