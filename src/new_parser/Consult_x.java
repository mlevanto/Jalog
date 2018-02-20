// Consult.java
import java.io.*;

public class Consult_x
{
  static Pro_Term exit_value = null;

  static void run(String FileName)
  {
    exit_value = null;
    Parser Pr1 = new Parser(Parser.CLAUSE);
    Pro_Term T;
    Pro_Term[] Apu = new Pro_Term[10];
    int ApuCnt = 0;
    String line;
    RandomAccessFile file1;
    int LineNmbr = 0;

    /*try*/ {
    
//      System.out.println("Consulting " + FileName);
      try {
        file1 = new RandomAccessFile(FileName,"r");
      } catch (Exception e) {
        System.out.println("*** Error: " + e);
        file1 = null;
        exit_value = Pro_Term.m_integer(1); // File not found
      }
      if(file1 != null) {
        do {
          try {
            line = file1.readLine();
            LineNmbr = LineNmbr + 1;
          } catch (Exception e) {
            System.out.println("*** Error: " + e);
            line = null;
          }
          if (line != null) {
  // System.out.println("");
  // System.out.println("Line: " + line);
            Pr1.SetString(line);
          } else {
            Pr1.SetEOF();
  // System.out.println("Consult:EOF");
          }
          do
          {
  // System.out.println("   ---");
            T = Pr1.NextPart();
            if(Pr1.Error != 0)
            {
              exit_value = Pro_Term.m_integer(1); // Syntax error
              System.err.println("*** Error in file " + FileName + " Line: " + LineNmbr + " Pos: " + Pr1.ErrorPos);
              System.err.println("    " + line);
              for(int i = -3; i < Pr1.ErrorPos; i++) {
                System.err.print(" ");
              }
              System.err.println("^");

              T = null;
              line = null;
            } else {
System.out.println("   Term: " + T);
//System.out.println("");
//              process_clause(T);
              if(exit_value != null) {
                T = null;
                line = null;
              }
            }
            if (ApuCnt < 9) {
              Apu[ApuCnt] = T;
              ApuCnt++;
            }
          } while(T != null);
        } while (line != null);
      }
//System.out.println("Consulted");
    
/*    } catch (Exception e) {
      System.out.println(e); */
    }
/*
    int I;
    for (I = 0; I < ApuCnt; I++) {
      System.out.println("Term[" + I + "]:" + Apu[I]);
    }
*/
  }

  private static void process_clause(Pro_Term T){
//System.out.println("\n--Consult: process_clause:" + T );

    Pro_TermData_Compound data = 
        (T != null ? (Pro_TermData_Compound) T.getData() : null);

    if(data != null && data instanceof Pro_TermData_Compound){
      
      if(data.name.equals(":-") && (data.arity == 2)) { // is clause
        if((data.subterm[0] == null) && (data.subterm[1] != null)){
          // Directive to be executed immediately
          // :- <list of predicate calls>.
          Inference I = new Inference();

          Pred.forward = true;
          I.run_body(data.subterm[1]);
          if(I.exit_value == null)
          {
            if(Pred.forward){
              System.out.println("*Yes*");
            } else {
              System.out.println("*No*");
            }
          } else {
            /* Exception! */
            exit_value = I.exit_value;
          }
          Pred.trail.backtrack(I.Mark); // clear variables
        } else {
          if(data.subterm[0] != null) {
            if(data.subterm[1].getData() == Pro_TermData_List.EMPTY) {
              Database.assertz(data.subterm[0]);
            } else {
              Database.assertz(T);
            }
          }

        }
      
      } 

    }
  }
}
