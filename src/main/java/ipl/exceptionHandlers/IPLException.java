package ipl.exceptionHandlers;

public class IPLException extends Exception{
    public IPLException(String exceptionMessage) {
        super(exceptionMessage);
        System.out.println(exceptionMessage);
    }
}
