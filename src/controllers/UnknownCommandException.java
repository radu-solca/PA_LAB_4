package controllers;


public class UnknownCommandException extends Exception{
    //Parameterless Constructor
      public UnknownCommandException() {}

      //Constructor that accepts a message
      public UnknownCommandException(String message)
      {
         super(message);
      }

}
