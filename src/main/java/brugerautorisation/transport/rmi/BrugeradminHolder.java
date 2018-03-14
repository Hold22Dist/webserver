package brugerautorisation.transport.rmi;

import brugerautorisation.transport.rmi.Brugeradmin;

import java.rmi.Naming;

public class BrugeradminHolder {
    private static Brugeradmin ba = null;
    protected BrugeradminHolder(){ };
    public static Brugeradmin getBrugerAdmin(){
        try {
            if (ba==null) {

                ba = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ba;
    }
}
