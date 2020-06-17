import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

class Main {
  
  static int NBR_INTERFACES;
  static int NBR_APPRENANTS;
  static int NBR_CENTRES_FORMATION;
  static int NBR_SPECIALITES;
  static int NBR_NODES;

  static int COMPETENCE_SIGNES;
  static int COMPETENCE_CODAGE;

  static int[][] competences_interfaces;

  static int SPECIALITE_SANS;
  static int SPECIALITE_MENUISERIE;
  static int SPECIALITE_ELECTRICITE;
  static int SPECIALITE_MECANIQUE;

  static int[][] specialite_interfaces;

  static double[][] coord;

  static int NBR_FORMATION;
  static int NBR_FORMATION_SIGNE;
  static int NBR_FORMATION_CODAGE;

  static int LUNDI;
  static int MARDI;
  static int MERCREDI;
  static int JEUDI;
  static int VENDREDI;
  static int SAMEDI;

  static int[][] formation;
  static int NBR_POPULATION;

  static ArrayList<ArrayList<Double>> distAA;
  static ArrayList<Double> distAC;
  static ArrayList<ArrayList<Double>> distAI;
  
  static ArrayList<Integer> codage_f;
  static ArrayList<Integer> signe_f;

  static ArrayList<Integer> list_s;
  static ArrayList<Integer> list_c;

  public static void set_nbr_form(){
    codage_f = new ArrayList<Integer>();
    signe_f = new ArrayList<Integer>();
    NBR_FORMATION_CODAGE = 0;
    NBR_FORMATION_SIGNE = 0;
    for(int i=0;i<NBR_FORMATION;i++){
      if(formation[i][2]==COMPETENCE_CODAGE){
        NBR_FORMATION_CODAGE++;
        codage_f.add(i);
      }
      else{
        NBR_FORMATION_SIGNE++;
        signe_f.add(i);
      }
    }
  }
  //initialise les populations
  public static void init_agenda(Population p){
    //CREATION D'AGENDA ALEATOIRE EN FONCTION DES COMPETENCES
    AgendaS c = new AgendaS();
    AgendaS s = new AgendaS();

    list_s = new ArrayList<Integer>();
    list_c = new ArrayList<Integer>();
    for (int i=0; i<NBR_INTERFACES; i++) {
      if(competences_interfaces[i][COMPETENCE_SIGNES]==1){
        list_s.add(new Integer(i));
      }else{
        list_c.add(new Integer(i));
      }
    }
    int cpt=0;
    while(cpt<NBR_POPULATION){
      c.agenda.clear();
      s.agenda.clear();
      Collections.shuffle(list_s);
      Collections.shuffle(list_c);
      //MOINS(ou egal) DE FORMATION QUE D'INTERFACE (SIGNES)
      if(NBR_FORMATION_SIGNE<=list_s.size()){//ON MET 1 INTERFACE 1 FOIS
        for (int i=0; i<NBR_FORMATION_SIGNE; i++) {
            s.agenda.add(list_s.get(i));
        }
        p.signe.add(new AgendaS(s));
      }else{//PLUS DE FORMATION QUE D'INTERFACE
        int ts= list_s.size();
        for (int i=0; i<ts; i++) {//ON MET AU MOINS UNE FOIS TOUTES LES INTERFACES
            s.agenda.add(list_s.get(i));
        }
        for(int i=ts;i<NBR_FORMATION_SIGNE;i++){//REMPLI LE REST ALEATOIREMENT
          Random rand = new Random();
          int r1 = rand.nextInt(list_s.size());
          s.agenda.add(list_s.get(r1));
        }
        p.signe.add(new AgendaS(s));
      }

      //MOINS(ou egal) DE FORMATION QUE D'INTERFACE (CODAGE)
      if(NBR_FORMATION_CODAGE<=list_c.size()){//ON MET 1 INTERFACE 1 FOIS
        for (int i=0; i<NBR_FORMATION_CODAGE; i++) {
            c.agenda.add(list_c.get(i));
        }
        p.codage.add(new AgendaS(c));
      }else{//PLUS DE FORMATION QUE D'INTERFACE
        int tc= list_c.size();
        for (int i=0; i<tc; i++) {//ON MET AU MOINS UNE FOIS TOUTES LES INTERFACES
            c.agenda.add(list_c.get(i));
        }
        for(int i=tc;i<NBR_FORMATION_CODAGE;i++){//REMPLI LE REST ALEATOIREMENT
          Random rand = new Random();
          int r1 = rand.nextInt(list_c.size());
          c.agenda.add(list_c.get(r1));
        }
        p.codage.add(new AgendaS(c));
      }
      cpt++;
    }
  }
  //Initialisation des matrices distances
  public static void init_DistAA()
  {
      distAA = new ArrayList<ArrayList<Double>>();
      for(int i = 0; i < NBR_APPRENANTS; i++){
          ArrayList<Double> tmpArrayList = new ArrayList<Double>();
          for(int j = 0; j < NBR_APPRENANTS; j++){
              double distX = Math.abs(coord[27 + i][0] - coord[27 + j][0]);
              double distY = Math.abs(coord[27 + i][1] - coord[27 + j][1]);
              double dist = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
              tmpArrayList.add(dist);
          }
          distAA.add(tmpArrayList);
      }
      
  }
  
  public static void init_DistAC()
  {
      distAC = new ArrayList<Double>();
      ArrayList<Integer> tmp = new ArrayList<Integer>();
      Boolean ok = true;
      for(int i = 0; i < NBR_FORMATION; i++){
          double distX = 0;
          double distY = 0;
          ok = true;
          for(int j = 0; j < tmp.size(); j++){
              if(tmp.get(j) == formation[i][0]){
                  ok = false;
              }
          }
          tmp.add(formation[i][0]);
          if(formation[i][1] == SPECIALITE_MENUISERIE && ok == true){
              distX = Math.abs(coord[27 + formation[i][0]][0] - coord[0][0]);
              distY = Math.abs(coord[27 + formation[i][0]][1] - coord[0][1]);
          }
          else if(formation[i][1] == SPECIALITE_ELECTRICITE && ok == true){
              distX = Math.abs(coord[27 + formation[i][0]][0] - coord[1][0]);
              distY = Math.abs(coord[27 + formation[i][0]][1] - coord[1][1]);
          }
          else if(formation[i][1] == SPECIALITE_MECANIQUE && ok == true){
              distX = Math.abs(coord[27 + formation[i][0]][0] - coord[2][0]);
              distY = Math.abs(coord[27 + formation[i][0]][1] - coord[2][1]);
          }
          else if(formation[i][1] == SPECIALITE_SANS && ok == true){
              distX = Math.abs(coord[27 + formation[i][0]][0] - coord[0][0]);
              distY = Math.abs(coord[27 + formation[i][0]][1] - coord[0][1]);
          }
          if( ok == true){
              double dist = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
              distAC.add(dist);
          }

      }
  }
    
  public static void init_DistAI()
  {
    distAI = new ArrayList<ArrayList<Double>>();
    for(int i = 0; i < NBR_APPRENANTS; i++){
          ArrayList<Double> tmpArrayList = new ArrayList<Double>();
          for(int j = 0; j < NBR_INTERFACES; j++){
              double distX = Math.abs(coord[27 + i][0] - coord[3 + j][0]);
              double distY = Math.abs(coord[27 + i][1] - coord[3 + j][1]);
              double dist = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
              tmpArrayList.add(dist);
          }
          distAI.add(tmpArrayList);
    }
      
  }

  public static void display_DistAA(){
      for(int i = 0; i < NBR_APPRENANTS; i++){
          for(int j = 0; j < NBR_APPRENANTS; j++){
              System.out.print(distAA.get(i).get(j) + " ");
          }
          System.out.println("");
      }
  }
  
  public static void display_DistAC(){
      System.out.println("taille distAC : " + distAC.size());
      for(int i = 0; i < NBR_APPRENANTS; i++){
          System.out.println("apprenant " + i +  " : " + distAC.get(i));
      }
  }
  
  public static void display_DistAI(){
      for(int i = 0; i < NBR_APPRENANTS; i++){
          for(int j = 0; j < NBR_INTERFACES; j++){
              System.out.print(distAI.get(i).get(j) + " ");
          }
          System.out.println("");
      }
  }



  public static void main(String[] args) {
      //#######################################################
      //#                     JEU D'ESSAI 1                   #
      //#######################################################
      /*
      NBR_INTERFACES = 10;
      NBR_APPRENANTS = 8;
      NBR_CENTRES_FORMATION = 3;
      NBR_SPECIALITES = 3;
      NBR_NODES = NBR_CENTRES_FORMATION+NBR_INTERFACES+NBR_APPRENANTS;

      // code des compétence en langage des signes et en codage LPC
      COMPETENCE_SIGNES = 0; 
      COMPETENCE_CODAGE = 1; 

      //competences des interfaces en SIGNES et CODAGE taille [NBR_INTERFACES][2]
      competences_interfaces= new int[][]{
      {1,0}, //compétence en langages des SIGNES mais pas en CODAGE LPC 
      {0,1}, // pas de compétence en langages des SIGNES mais compétence en CODAGE LPC
      {0,1},
      {1,0},
      {0,1},
      {1,0},
      {1,0},
      {1,0},
      {0,1},
      {1,0}
    };

    //spécialités des interfaces
    SPECIALITE_SANS = -1;
    SPECIALITE_MENUISERIE = 0;
    SPECIALITE_ELECTRICITE = 1;
    SPECIALITE_MECANIQUE = 2;

    //specialite des interfaces, taille[NBR_INTERFACES][NBR_SPECIALITES]
    specialite_interfaces=new int[][]{
        {1,0,0},
        {0,1,0},
        {0,0,0},
        {0,0,1},
        {0,1,0},
        {0,0,0},
        {0,1,0},
        {1,0,0},
        {1,0,0},
        {0,1,0}
    };

    ///coordonnées du centre, des interfaces et des apprenants, taille [NBR_NODES][2]
    coord=new double[][]
    {
        {565.0,575.0}, // centre formation menuiserie 
        {25.0,185.0},  // centre formation electricite 
        {345.0,750.0}, // centre formation mecanique 
        {945.0,685.0}, // point de départ interfaces 
        {845.0,655.0},
        {880.0,660.0},
        {25.0,230.0},
        {525.0,1000.0},
        {580.0,1175.0},
        {650.0,1130.0},
        {1605.0,620.0},
        {1220.0,580.0}, 
        {1465.0,200.0},
        {1530.0,5.0}, // point de départ apprenants 
        {845.0,680.0},
        {725.0,370.0},
        {145.0,665.0},
        {415.0,635.0},
        {510.0,875.0},
        {560.0,365.0},
        {300.0,465.0}
    } ;

    NBR_FORMATION = 9;
    LUNDI = 1;
    MARDI = 2;
    MERCREDI = 3;
    JEUDI = 4;
    VENDREDI = 5;
    SAMEDI = 6;

    //formation : apprenant, specialite, competence, horaire debut formation, horaire fin formation, taille[NBR_FORMATION][6]
    formation=new int[][]
    {
        {0,SPECIALITE_ELECTRICITE, COMPETENCE_SIGNES, LUNDI, 13, 16},
        {1,SPECIALITE_MENUISERIE, COMPETENCE_CODAGE, MARDI, 8, 11},
        {2,SPECIALITE_ELECTRICITE, COMPETENCE_CODAGE, MERCREDI, 9, 12},
        {3,SPECIALITE_MECANIQUE, COMPETENCE_SIGNES, MARDI, 14, 17},
        {4,SPECIALITE_ELECTRICITE, COMPETENCE_SIGNES, LUNDI, 9, 12},
        {5,SPECIALITE_MENUISERIE, COMPETENCE_SIGNES, JEUDI, 8, 11},
        {0,SPECIALITE_ELECTRICITE, COMPETENCE_SIGNES, JEUDI, 8, 12},
        {6,SPECIALITE_MENUISERIE, COMPETENCE_CODAGE, JEUDI, 14, 17},
        {7,SPECIALITE_ELECTRICITE, COMPETENCE_CODAGE, VENDREDI, 8, 11}
    } ;*/
    
    //#######################################################
    //#                     JEU D ESSAI N2                  #
    //#######################################################

      NBR_INTERFACES = 24;
      NBR_APPRENANTS = 20;
      NBR_CENTRES_FORMATION = 3;
      NBR_SPECIALITES = 3;
      NBR_NODES = NBR_CENTRES_FORMATION+NBR_INTERFACES+NBR_APPRENANTS;

      // code des compétence en langage des signes et en codage LPC
      COMPETENCE_SIGNES = 0; 
      COMPETENCE_CODAGE = 1; 

      //competences des interfaces en SIGNES et CODAGE taille [NBR_INTERFACES][2]
      competences_interfaces= new int[][]{
      {1,0}, /* compétence en langages des SIGNES mais pas en CODAGE LPC */
      {0,1}, /* pas de compétence en langages des SIGNES mais compétence en CODAGE LPC */
      {1,0},
      {0,1},
      {0,1},
      {0,1},
      {1,0},
      {0,1},
      {0,1},
      {0,1},
      {0,1},
      {1,0},
      {1,0},
      {1,0},
      {0,1},
      {1,0},
      {1,0},
      {1,0},
      {0,1},
      {1,0},
      {0,1},
      {1,0},
      {1,0},
      {0,1}
    };

    //spécialités des interfaces
    SPECIALITE_SANS = -1;
    SPECIALITE_MENUISERIE = 0;
    SPECIALITE_ELECTRICITE = 1;
    SPECIALITE_MECANIQUE = 2;

    //specialite des interfaces, taille[NBR_INTERFACES][NBR_SPECIALITES]
    specialite_interfaces=new int[][]{
      {0,0,1},
      {0,0,1},
      {1,0,1},
      {0,0,0},
      {0,0,1},
      {0,0,0},
      {0,0,0},
      {1,0,0},
      {0,0,0},
      {0,0,0},
      {0,1,0},
      {0,0,0},
      {0,0,1},
      {0,1,0},
      {0,0,0},
      {0,0,0},
      {0,0,0},
      {0,1,0},
      {0,0,0},
      {0,0,0},
      {0,1,0},
      {0,0,0},
      {1,0,0},
      {1,0,0}
    };

    ///coordonnées du centre, des interfaces et des apprenants, taille [NBR_NODES][2]
    coord=new double[][]
    {
        /* Centres de formation */
    {82,27}, /* centre formation SPECIALITE_MENUISERIE */
    {31,194}, /* centre formation SPECIALITE_ELECTRICITE */
    {100,60}, /* centre formation SPECIALITE_MECANIQUE */
                  
    /* Les interfaces se rendent directement de leur domicile chez l'apprenant */
    {189,106}, /* interface 0 */
    {148,87}, /* interface 1 */
    {161,130}, /* interface 2 */
    {118,166}, /* interface 3 */
    {44,146}, /* interface 4 */
    {62,144}, /* interface 5 */
    {142,64}, /* interface 6 */
    {74,71}, /* interface 7 */
    {85,183}, /* interface 8 */
    {134,66}, /* interface 9 */
    {3,109}, /* interface 10 */
    {130,80}, /* interface 11 */
    {161,164}, /* interface 12 */
    {149,79}, /* interface 13 */
    {63,64}, /* interface 14 */
    {183,131}, /* interface 15 */
    {73,25}, /* interface 16 */
    {53,39}, /* interface 17 */
    {28,141}, /* interface 18 */
    {151,153}, /* interface 19 */
    {55,170}, /* interface 20 */
    {37,49}, /* interface 21 */
    {46,165}, /* interface 22 */
    {85,187}, /* interface 23 */
                  
    /* Apprenants */
    {122,164}, /* apprenant 0 */
    {146,5}, /* apprenant 1 */
    {7,114}, /* apprenant 2 */
    {171,170}, /* apprenant 3 */
    {113,39}, /* apprenant 4 */
    {0,121}, /* apprenant 5 */
    {44,90}, /* apprenant 6 */
    {132,172}, /* apprenant 7 */
    {100,20}, /* apprenant 8 */
    {64,118}, /* apprenant 9 */
    {28,84}, /* apprenant 10 */
    {185,76}, /* apprenant 11 */
    {82,180}, /* apprenant 12 */
    {148,25}, /* apprenant 13 */
    {109,135}, /* apprenant 14 */
    {173,190}, /* apprenant 15 */
    {142,120}, /* apprenant 16 */
    {82,173}, /* apprenant 17 */
    {194,140}, /* apprenant 18 */
    {119,94}/* apprenant 19 */
    } ;

    NBR_FORMATION = 80;
    LUNDI = 1;
    MARDI = 2;
    MERCREDI = 3;
    JEUDI = 4;
    VENDREDI = 5;
    SAMEDI = 6;

    /* formation : apprenant, specialite, competence, horaire debut formation, horaire fin formation, taille[NBR_FORMATION][6] */
    formation=new int[][]
    {
      {0,SPECIALITE_ELECTRICITE,COMPETENCE_SIGNES,VENDREDI,15,19},
      {0,SPECIALITE_MECANIQUE,COMPETENCE_CODAGE,SAMEDI,13,19},
      {0,SPECIALITE_MENUISERIE,COMPETENCE_CODAGE,JEUDI,13,16},
      {0,SPECIALITE_MECANIQUE,COMPETENCE_SIGNES,MERCREDI,8,12},
      {1,SPECIALITE_ELECTRICITE,COMPETENCE_CODAGE,VENDREDI,10,12},
      {1,SPECIALITE_MENUISERIE,COMPETENCE_SIGNES,LUNDI,10,12},
      {1,SPECIALITE_ELECTRICITE,COMPETENCE_SIGNES,VENDREDI,9,12},
      {1,SPECIALITE_ELECTRICITE,COMPETENCE_CODAGE,LUNDI,9,11},
      {2,SPECIALITE_MECANIQUE,COMPETENCE_CODAGE,SAMEDI,13,15},
      {2,SPECIALITE_ELECTRICITE,COMPETENCE_SIGNES,MERCREDI,14,16},
      {2,SPECIALITE_ELECTRICITE,COMPETENCE_SIGNES,SAMEDI,9,12},
      {2,SPECIALITE_ELECTRICITE,COMPETENCE_CODAGE,SAMEDI,13,19},
      {3,SPECIALITE_MECANIQUE,COMPETENCE_SIGNES,VENDREDI,15,17},
      {3,SPECIALITE_MECANIQUE,COMPETENCE_CODAGE,LUNDI,13,17},
      {3,SPECIALITE_MENUISERIE,COMPETENCE_CODAGE,MARDI,9,11},
      {3,SPECIALITE_MENUISERIE,COMPETENCE_CODAGE,LUNDI,16,19},
      {4,SPECIALITE_MENUISERIE,COMPETENCE_SIGNES,JEUDI,10,12},
      {4,SPECIALITE_ELECTRICITE,COMPETENCE_CODAGE,MERCREDI,10,12},
      {4,SPECIALITE_MENUISERIE,COMPETENCE_SIGNES,MARDI,9,12},
      {4,SPECIALITE_MECANIQUE,COMPETENCE_CODAGE,MERCREDI,13,17},
      {5,SPECIALITE_ELECTRICITE,COMPETENCE_SIGNES,MERCREDI,13,19},
      {5,SPECIALITE_ELECTRICITE,COMPETENCE_SIGNES,SAMEDI,8,12},
      {5,SPECIALITE_MECANIQUE,COMPETENCE_CODAGE,MERCREDI,10,12},
      {5,SPECIALITE_MENUISERIE,COMPETENCE_CODAGE,SAMEDI,16,19},
      {6,SPECIALITE_ELECTRICITE,COMPETENCE_SIGNES,MERCREDI,16,18},
      {6,SPECIALITE_ELECTRICITE,COMPETENCE_SIGNES,VENDREDI,10,12},
      {6,SPECIALITE_MENUISERIE,COMPETENCE_SIGNES,MERCREDI,10,12},
      {6,SPECIALITE_MECANIQUE,COMPETENCE_SIGNES,JEUDI,16,18},
      {7,SPECIALITE_MENUISERIE,COMPETENCE_CODAGE,JEUDI,8,12},
      {7,SPECIALITE_ELECTRICITE,COMPETENCE_CODAGE,JEUDI,8,12},
      {7,SPECIALITE_MENUISERIE,COMPETENCE_CODAGE,MARDI,9,11},
      {7,SPECIALITE_ELECTRICITE,COMPETENCE_SIGNES,JEUDI,10,12},
      {8,SPECIALITE_MECANIQUE,COMPETENCE_CODAGE,MARDI,14,18},
      {8,SPECIALITE_ELECTRICITE,COMPETENCE_CODAGE,LUNDI,8,12},
      {8,SPECIALITE_MECANIQUE,COMPETENCE_SIGNES,LUNDI,16,18},
      {8,SPECIALITE_ELECTRICITE,COMPETENCE_SIGNES,VENDREDI,13,15},
      {9,SPECIALITE_ELECTRICITE,COMPETENCE_SIGNES,JEUDI,13,16},
      {9,SPECIALITE_ELECTRICITE,COMPETENCE_SIGNES,MARDI,8,11},
      {9,SPECIALITE_MECANIQUE,COMPETENCE_CODAGE,VENDREDI,13,17},
      {9,SPECIALITE_ELECTRICITE,COMPETENCE_CODAGE,VENDREDI,14,17},
      {10,SPECIALITE_MENUISERIE,COMPETENCE_SIGNES,JEUDI,10,12},
      {10,SPECIALITE_MENUISERIE,COMPETENCE_CODAGE,JEUDI,9,12},
      {10,SPECIALITE_MENUISERIE,COMPETENCE_CODAGE,SAMEDI,14,16},
      {10,SPECIALITE_MENUISERIE,COMPETENCE_CODAGE,MERCREDI,14,18},
      {11,SPECIALITE_MENUISERIE,COMPETENCE_SIGNES,LUNDI,8,12},
      {11,SPECIALITE_MENUISERIE,COMPETENCE_SIGNES,VENDREDI,8,12},
      {11,SPECIALITE_MECANIQUE,COMPETENCE_CODAGE,JEUDI,10,12},
      {11,SPECIALITE_ELECTRICITE,COMPETENCE_CODAGE,MARDI,15,19},
      {12,SPECIALITE_MECANIQUE,COMPETENCE_SIGNES,LUNDI,10,12},
      {12,SPECIALITE_ELECTRICITE,COMPETENCE_CODAGE,SAMEDI,15,17},
      {12,SPECIALITE_MENUISERIE,COMPETENCE_SIGNES,LUNDI,14,19},
      {12,SPECIALITE_ELECTRICITE,COMPETENCE_SIGNES,SAMEDI,10,12},
      {13,SPECIALITE_ELECTRICITE,COMPETENCE_CODAGE,VENDREDI,9,12},
      {13,SPECIALITE_MECANIQUE,COMPETENCE_CODAGE,LUNDI,10,12},
      {13,SPECIALITE_MECANIQUE,COMPETENCE_CODAGE,VENDREDI,13,16},
      {13,SPECIALITE_ELECTRICITE,COMPETENCE_SIGNES,LUNDI,14,17},
      {14,SPECIALITE_ELECTRICITE,COMPETENCE_CODAGE,LUNDI,16,18},
      {14,SPECIALITE_ELECTRICITE,COMPETENCE_CODAGE,MERCREDI,14,18},
      {14,SPECIALITE_MECANIQUE,COMPETENCE_CODAGE,MERCREDI,10,12},
      {14,SPECIALITE_MECANIQUE,COMPETENCE_CODAGE,VENDREDI,10,12},
      {15,SPECIALITE_MENUISERIE,COMPETENCE_SIGNES,SAMEDI,10,12},
      {15,SPECIALITE_MECANIQUE,COMPETENCE_CODAGE,SAMEDI,8,12},
      {15,SPECIALITE_MENUISERIE,COMPETENCE_SIGNES,LUNDI,16,18},
      {15,SPECIALITE_ELECTRICITE,COMPETENCE_CODAGE,LUNDI,14,19},
      {16,SPECIALITE_MECANIQUE,COMPETENCE_CODAGE,VENDREDI,10,12},
      {16,SPECIALITE_MENUISERIE,COMPETENCE_SIGNES,MARDI,10,12},
      {16,SPECIALITE_ELECTRICITE,COMPETENCE_SIGNES,JEUDI,8,10},
      {16,SPECIALITE_MECANIQUE,COMPETENCE_CODAGE,LUNDI,10,12},
      {17,SPECIALITE_MENUISERIE,COMPETENCE_SIGNES,MARDI,9,12},
      {17,SPECIALITE_ELECTRICITE,COMPETENCE_SIGNES,LUNDI,16,19},
      {17,SPECIALITE_MENUISERIE,COMPETENCE_CODAGE,VENDREDI,16,18},
      {17,SPECIALITE_MENUISERIE,COMPETENCE_CODAGE,LUNDI,15,17},
      {18,SPECIALITE_MECANIQUE,COMPETENCE_CODAGE,JEUDI,9,12},
      {18,SPECIALITE_ELECTRICITE,COMPETENCE_SIGNES,MARDI,14,18},
      {18,SPECIALITE_MENUISERIE,COMPETENCE_SIGNES,LUNDI,16,19},
      {18,SPECIALITE_MECANIQUE,COMPETENCE_CODAGE,MERCREDI,16,19},
      {19,SPECIALITE_MECANIQUE,COMPETENCE_CODAGE,MERCREDI,16,18},
      {19,SPECIALITE_MENUISERIE,COMPETENCE_CODAGE,VENDREDI,14,17},
      {19,SPECIALITE_MENUISERIE,COMPETENCE_CODAGE,SAMEDI,10,12},
      {19,SPECIALITE_MENUISERIE,COMPETENCE_CODAGE,JEUDI,8,11}
};
   

    //#######################################################
    //#                     INIT ALGO                       #
    //#######################################################

    init_DistAA();
    init_DistAC();
    init_DistAI();

    NBR_POPULATION = 50;
    Population p = new Population();
    set_nbr_form();
    init_agenda(p);
    p.search();

  }
}


