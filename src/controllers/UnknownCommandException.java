package controllers;

/**
 *
 * @author Radu
 */
public class UnknownCommandException extends Exception{
    //Parameterless Constructor

    /**
     *
     */
      public UnknownCommandException() {}

      //Constructor that accepts a message

    /**
     *
     * @param message
     */
      public UnknownCommandException(String message)
      {
         super(message);
      }

}
