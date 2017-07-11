// RegexDemo.java
import java.util.regex.*;

import java.util.ArrayList;
class RegexDemo
{
   
   public static void main (String [] args)
   {
      if (args.length != 2)
      {
          System.err.println ("java RegexDemo regex text");
          return;
      }
      Pattern p;
      try
      {
         p = Pattern.compile (args [0]);
      }
      catch (PatternSyntaxException e)
      {
         System.err.println ("Regex syntax error: " + e.getMessage ());
         System.err.println ("Error description: " + e.getDescription ());
         System.err.println ("Error index: " + e.getIndex ());
         System.err.println ("Erroneous pattern: " + e.getPattern ());
         return;
      }
      //String s = cvtLineTerminators (args [1]);
      String s =  args [1];
      Matcher m = p.matcher (s);
      System.out.println ("Regex = " + args [0]);
      System.out.println ("Text = " + s);
      System.out.println ();
      while (m.find ())
      {
         System.out.println ("Found " + m.group ());
         System.out.println ("  starting at index " + m.start () +
                             " and ending at index " + m.end ());
         System.out.println ();
      }
   }
   // Convert \n and \r character sequences to their single character
   // equivalents
   static String cvtLineTerminators (String s)
   {
      StringBuffer sb = new StringBuffer (80);
      int oldindex = 0, newindex;
      while ((newindex = s.indexOf ("\\n", oldindex)) != -1)
      {
         sb.append (s.substring (oldindex, newindex));
         oldindex = newindex + 2;
         sb.append ('\n');
      }
      sb.append (s.substring (oldindex));
      s = sb.toString ();
      sb = new StringBuffer (80);
      oldindex = 0;
      while ((newindex = s.indexOf ("\\r", oldindex)) != -1)
      {
         sb.append (s.substring (oldindex, newindex));
         oldindex = newindex + 2;
         sb.append ('\r');
      }
      sb.append (s.substring (oldindex));
      return sb.toString ();
   }
}