/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

/**
 *
 * @author NASA
 */
public class ControllerResponse {

    /**
     *
     */
    public static enum returnTypes{

        /**
         *
         */
        VOID,

        /**
         *
         */
        FILE_LIST,

        /**
         *
         */
        STRING,

        /**
         *
         */
        EXCEPTION
    }
    private final returnTypes returnType;
    private final Object returnValue;

    /**
     *
     * @param returnType
     * @param returnValue
     */
    public ControllerResponse(returnTypes returnType, Object returnValue) {
        this.returnType = returnType;
        this.returnValue = returnValue;
    }

    /**
     *
     * @return
     */
    public returnTypes getReturnType() {
        return returnType;
    }

    /**
     *
     * @return
     */
    public Object getReturnValue() {
        return returnValue;
    }       
}
