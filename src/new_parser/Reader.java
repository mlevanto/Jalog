// Reader.java
import java.io.*;
import java.util.Stack;
import java.util.Vector;
import java.util.Hashtable;

public class Reader
{
  static Pro_Term exit_value = null;
  static Pro_Term[] Pro_Term_empty = new Pro_Term[0];

  static void run(String FileName)
  {
    String action;
    Stack<Pro_Term> term_stack = new Stack<Pro_Term>();
    Pro_Term T, term = null, term_out;
    Stack<Vector<Pro_Term>> termList_stack = new Stack<Vector<Pro_Term>>();
    Vector<Pro_Term> termList = new Vector<Pro_Term>();
    Hashtable varSymTab = new Hashtable();
    String varname;
    Pro_Term[] Apu = new Pro_Term[10];
    Pro_Term[] operands; 

    exit_value = null;
    JalogParser Pr1 = new JalogParser(JalogSyntax.CLAUSES);
    int ApuCnt = 0;
    String line;
    RandomAccessFile file1;
    int LineNmbr = 0;
    Pro_TermData_Compound tC;

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
  // System.out.println("");
   System.out.println("Line: " + line);
          Pr1.setLine(line);
          
          do
          {
  // System.out.println("   ---");
            Pr1.advance();
            action = Pr1.action;
     System.out.println("action: " + action);
     
            if (action == JalogSyntax.SYM) {
              term = 
                  Pro_Term.m_compound(Pr1.sValue(),Pro_Term_empty);
     System.out.println("term: " + term);
            } else if (action == JalogSyntax.BGN_STRUCT) {
              term = 
                  Pro_Term.m_compound(Pr1.sValue(),Pro_Term_empty);
              termList_stack.push(termList);
              termList = new Vector();
              term_stack.push(term);
            } else if (action == JalogSyntax.END_STRUCT) {
              term = term_stack.pop();
              tC = (Pro_TermData_Compound)term.getData();
              tC.subterm = termList.toArray(Pro_Term_empty);
              tC.arity = (byte)tC.subterm.length;
              termList = termList_stack.pop();
            } else if (action == JalogSyntax.BGN_ARG) {
              
            } else if (action == JalogSyntax.END_ARG) {
              termList.add(term);

            } else if (action == JalogSyntax.BGN_CLAUSE) {
              varSymTab.clear();
            } else if (action == JalogSyntax.END_CLAUSE) {
              operands = new Pro_Term[2];
              operands[0] = term;
              operands[1] = Pro_Term.EMPTY_LIST;
              term_out = Pro_Term.m_compound(":-",operands);
     System.out.println("term: " + term_out);

            } else if (action == JalogSyntax.VARIABLE) {
              varname = Pr1.sValue();
              if (varname.equals("_")) {
                term = Pro_Term.m_open();
              } else {
                term = (Pro_Term)varSymTab.get(varname);
                if (term == null) 
                {
                  term = Pro_Term.m_open();
                  varSymTab.put(varname, term);
                }
                
              }
            } else if (action == JalogSyntax.BGN_BINOP){
              operands = new Pro_Term[2];
              operands[0] = term;
              term = 
                  Pro_Term.m_compound(Pr1.sValue(),operands);
              term_stack.push(term);
              
            } else if (action == JalogSyntax.END_BINOP){
              T = term;
              term = term_stack.pop();
              
              tC = (Pro_TermData_Compound)term.getData();
              tC.subterm[1] = T;
            }

  /*
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
//System.out.println("   Term: " + T);
//System.out.println("");
              process_clause(T);
              if(exit_value != null) {
                T = null;
                line = null;
              }
            }
            if (ApuCnt < 9) {
              Apu[ApuCnt] = T;
              ApuCnt++;
            }
*/            
          } while(action != Syntax.EOL && action != Syntax.COMPLETE);
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
    
System.out.println("\n--Reader: process_clause:" + T );
  }
}
